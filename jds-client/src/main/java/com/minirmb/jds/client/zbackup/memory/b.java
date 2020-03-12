package com.minirmb.jds.client.zbackup.memory;

import java.lang.management.ManagementFactory;
import java.util.ArrayList;
import java.util.List;

import com.sun.management.OperatingSystemMXBean;

public class b {
	public static void main(String[] args) {
		List<byte[]> caches = new ArrayList<>();
		for (int i = 0; i < 100; i++) {
			showJVMInfo(i);
			caches.add(new byte[10 * 1024*1024]);
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public static void showJVMInfo(int i) {
		System.out.println("======================================" + i);
		// ������������ӳ�Ķ���java������̵��ڴ����:totalMemory freeMemory maxMemory
		// ��������ڴ������ѯ
		int byteToMb = 1024 * 1024;
		Runtime rt = Runtime.getRuntime();
		long vmTotal = rt.totalMemory() / byteToMb;
		System.out.println("totalMemory:" + vmTotal + " MB");

		long vmMax = rt.maxMemory() / byteToMb;
		System.out.println("maxMemory:" + vmMax + " MB");

		long vmFree = rt.freeMemory() / byteToMb;
		System.out.println("freeMemory:" + vmFree + " MB");

		long vmUse = vmTotal - vmFree;
		System.out.println("usedMemory:" + vmUse + " MB");

		// ����ϵͳ���ڴ������ѯ
		OperatingSystemMXBean osmxb = (OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();
		String os = System.getProperty("os.name");
		long physicalFree = osmxb.getFreePhysicalMemorySize() / byteToMb;
		long physicalTotal = osmxb.getTotalPhysicalMemorySize() / byteToMb;
		long physicalUse = physicalTotal - physicalFree;
		System.out.println("����ϵͳ�İ汾��" + os);
		System.out.println("AvailableProcessors��" + osmxb.getAvailableProcessors());
		System.out.println("Arch��" + osmxb.getArch());
		System.out.println("SystemLoadAverage��" + osmxb.getSystemLoadAverage());
		System.out.println("����ϵͳ�����ڴ����õĿռ�Ϊ��" + physicalFree + " MB");
		System.out.println("����ϵͳ�����ڴ�Ŀ��пռ�Ϊ��" + physicalUse + " MB");
		System.out.println("����ϵͳ�������ڴ棺" + physicalTotal + " MB");

		// ����߳�����
		ThreadGroup parentThread;
		int totalThread = 0;
		for (parentThread = Thread.currentThread().getThreadGroup(); parentThread
				.getParent() != null; parentThread = parentThread.getParent()) {
			totalThread = parentThread.activeCount();
		}
		System.out.println("����߳�����:" + totalThread);
	}
}
