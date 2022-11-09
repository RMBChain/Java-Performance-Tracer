package com.minirmb.jps.core.zbackup.transfer;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Set;

public class HttpClientUploadFile {

	private HttpURLConnection getConnection(String serviceURL, final String BOUNDARY) throws IOException {
		URL url = new URL(serviceURL);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setConnectTimeout(5000);// 30秒连接
		conn.setReadTimeout(5 * 60 * 1000);// 5分钟读数据
		conn.setDoOutput(true);
		conn.setDoInput(true);
		conn.setUseCaches(false);
		conn.setRequestMethod("POST");
		conn.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + BOUNDARY);
		return conn;
	}

	private String getTextParameter(Map<String, String> textMap, final String BOUNDARY) {
		StringBuffer strBuf = new StringBuffer();
		for (Entry<String, String> entry : textMap.entrySet()) {
			strBuf.append("\r\n").append("--").append(BOUNDARY).append("\r\n");
			strBuf.append("Content-Disposition: form-data; name=\"" + entry.getKey() + "\"\r\n\r\n");
			strBuf.append(entry.getValue());
		}
		return strBuf.toString();
	}

	private void writeFileToStream(File file, final String BOUNDARY, OutputStream out) throws IOException {
		String filename = file.getName();
		Path path = Paths.get(file.getAbsolutePath());

		String contentType = Files.probeContentType(path);
		StringBuffer strBuf = new StringBuffer();
		strBuf.append("\r\n").append("--").append(BOUNDARY).append("\r\n");
		strBuf.append("Content-Disposition: form-data; name=\"files\"; filename=\"" + filename + "\"\r\n");
		strBuf.append("Content-Type:" + contentType + "\r\n\r\n");
		System.out.println(String.format("filename:%s,contentType:%s", filename, contentType));
		out.write(strBuf.toString().getBytes());
		try (DataInputStream in = new DataInputStream(new FileInputStream(file))) {
			int bytes = 0;
			byte[] bufferOut = new byte[1024];
			while ((bytes = in.read(bufferOut)) != -1) {
				out.write(bufferOut, 0, bytes);
			}
		}
	}
	
	private String getResponse(HttpURLConnection conn) throws IOException {
		// 读取返回数据
		System.out.println(String.format("http 返回状态:ResponseCode=%s,ResponseMessage=%s", conn.getResponseCode(),
				conn.getResponseMessage()));
		StringBuffer strBuf = new StringBuffer();

		InputStream is = conn.getInputStream();
		try (BufferedReader reader = new BufferedReader(new InputStreamReader(is))) {
			String line = null;
			while ((line = reader.readLine()) != null) {
				strBuf.append(line).append("\n");
			}
			System.out.println(String.format("http 返回数据:%s", strBuf.toString()));
		}
		return strBuf.toString();
	}
	public String uploadFiles(String serviceURL, File filePath) throws IOException {
		Set<File> fileMap = new HashSet<>();
		fileMap.add(filePath);
		return uploadFiles(serviceURL, null, fileMap);
	}
	
	/**
	 * 上传文件,允许同一个属性上传多个文件
	 * 
	 * @param serviceURL String
	 * @param textMap    表单参数
	 * @param fileMap    文件参数
	 * @return result 
	 * @throws IOException IOException
	 */
	public String uploadFiles(String serviceURL, Map<String, String> textMap, Set<File> fileMap)
			throws IOException {
		System.out.println(
				String.format("调用文件上传，传入参数:serviceURL=%s,textMap=%s,fileMap=%s", serviceURL, textMap, fileMap));
		String res = "";
		HttpURLConnection conn = null;
		OutputStream out = null;
		final String BOUNDARY = "---------------------------" + System.currentTimeMillis(); // boundary就是request头和上传文件内容的分隔符
		try {
			conn = getConnection(serviceURL, BOUNDARY);
			out = new DataOutputStream(conn.getOutputStream());
			// text
			if (!Objects.isNull(textMap) && !textMap.isEmpty()) {
				out.write(getTextParameter(textMap, BOUNDARY).getBytes());
			}

			// file
			if (!Objects.isNull(fileMap) && !fileMap.isEmpty()) {
				for (File filePath : fileMap) {
					writeFileToStream(filePath, BOUNDARY, out);
				}
			}

			byte[] endData = ("\r\n--" + BOUNDARY + "--\r\n").getBytes();
			out.write(endData);
			out.flush();

			// 读取返回数据
			res = getResponse(conn);
		} finally {
			if (!Objects.isNull(out)) {
				out.close();
			}
			if (!Objects.isNull(conn)) {
				conn.disconnect();
			}
		}
		return res;
	}

	public static void main(String[] args) throws IOException {
		Map<String, String> textMap = new HashMap<>();
		textMap.put("metricId", "111111");
		Set<File> fileMap = new HashSet<>();
		fileMap.add(new File("C:\\_minirmb_\\JavaDynamicSnapshot_workspace\\jds\\jds-test\\config\\log\\20200212_231842_637-00.log"));
		
		HttpClientUploadFile client = new HttpClientUploadFile();
		client.uploadFiles("http://127.0.0.1:8080/doUploadFiles1", textMap, fileMap);
	}
}