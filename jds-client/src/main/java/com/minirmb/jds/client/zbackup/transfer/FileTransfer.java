package com.minirmb.jds.client.zbackup.transfer;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;

import com.minirmb.jds.client.collect.Collector;
import com.minirmb.jds.client.utils.CoreConst;
import com.minirmb.jds.common.Utils;

public class FileTransfer {

	private int fileCount = 0;
	private long currentFileSize = 0;
	private final long fileSizeLimit = 10 * 1024 * 1024;// 10M
	private String dataFolder;
	private File currentFile;
	public FileTransfer() {
		this.dataFolder = Utils.GetFolderInUsehome();
		new File(dataFolder).mkdirs();
		currentFile = new File(getFilename(0));
	}

	private String getFilename(int fileOrder) {
		StringBuilder filePath = new StringBuilder();
		filePath.append(dataFolder).append(Collector.SnapshotId).append("-");
		filePath.append(Utils.padding(fileOrder, 3)).append(CoreConst.DataFilePostFix);
		return filePath.toString();
	}

	public long transfer(String data) throws IOException {
		System.out.print(".");
		
		if (currentFileSize > fileSizeLimit) {// M
			fileCount++;
			currentFileSize = 0;		
			currentFile = new File(getFilename(fileCount));
		}
		currentFileSize += data.getBytes().length;
		// store data to file
		try (FileOutputStream currentFOS = new FileOutputStream(currentFile,true);
				FileChannel fileChannel = currentFOS.getChannel();
				PrintWriter pr = new PrintWriter(currentFOS);
				FileLock lock = fileChannel.lock();
				) {
			pr.print(data);
		}
		return data.length();
	}
}
