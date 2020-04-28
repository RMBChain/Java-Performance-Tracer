package com.minirmb.jds.core.zbackup.transfer;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;

import com.minirmb.jds.common.Utils;

/**
 * 
 * //		// Regular transfer to remote server
//		final HttpTransfer transfer = new HttpTransfer();
//		new Timer(true).schedule(new TimerTask() {
//			public void run() {
//				transfer.upload();
//			}
//		}, 10000, 5000);
 * 
 * @author WeiHuaXu
 *
 */
public class HttpTransfer {
	HttpClientUploadFile client = new HttpClientUploadFile();

	public long upload() {
		System.out.println("-----------upload------------2 " + Utils.GetFolderInUsehome());
		String basePath = Utils.GetFolderInUsehome();
		String[] dataFiles = new File(basePath).list();

		System.out.println("-----------upload------------4");
		for (String filePath : dataFiles) {
			System.out.println("-----------upload------------5 " + filePath );
			File file = new File(basePath + filePath);
			if( !file.isFile()) {
				continue;
			}
			try (FileOutputStream currentFOS = new FileOutputStream(file, true);
					FileChannel fileChannel = currentFOS.getChannel();
					FileLock lock = fileChannel.tryLock(0L, Long.MAX_VALUE, true);) {
				if(lock.isValid()) {
					System.out.println("-----------upload------------6 " + file.getAbsolutePath() );
					client.uploadFiles("http://127.0.0.1:8080/doUploadFiles1", file);
					file.delete();
				}
			} catch (IOException e) {
				System.out.println("-----------upload------------7 " + filePath );
				e.printStackTrace();
			}
			System.out.println("-----------upload------------8 " + filePath );
		}
		System.out.println("-----------upload------------9 " );
		return dataFiles.length;
	}
}
