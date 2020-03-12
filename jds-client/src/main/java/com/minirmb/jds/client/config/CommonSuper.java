package com.minirmb.jds.client.config;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.minirmb.jds.client.utils.LogUtil;

public class CommonSuper {
	private static CommonSuper instance;

	private List<String[]> cs = new ArrayList<>();

	public static CommonSuper GetInstance() {
		return instance;
	}

	private static String convert(String str) {
		return str.trim().replace(".", "/");
	}

	public static synchronized void Init(String cfgFile) {		 
		LogUtil.log("Read CommonSuper file:" + cfgFile);

		CommonSuper tf = new CommonSuper();
		List<String> allLine = new ArrayList<String>();
		try (FileReader fr = new FileReader(cfgFile); BufferedReader bf = new BufferedReader(fr);) {
			String str;
			while ((str = bf.readLine()) != null) {
				allLine.add(str.trim());
			}
		} catch (IOException e) {
			throw new IllegalArgumentException(e);
		}

		for (String line : allLine) {
			String[] lineEle = line.split("=");
			if (lineEle.length >= 2) {
				String[] types = lineEle[0].split("&");
				if (types.length == 2) {
					tf.cs.add(new String[] { convert(types[0]), convert(types[1]), convert(lineEle[1]) });
				}
			}
		}
		instance = tf;
	}

	/**
	 * 
	 * @param type1 String
	 * @param type2 String
	 * 
	 * @return result result 
	 */
	public String getCommonSuper(String type1, String type2) {
		String result = null;
		for (String[] line : cs) {
			if (type1.equals(line[0]) && type2.equals(line[1])) {
				result = line[2];
				break;
			}
		}

		return result;
	}
}
