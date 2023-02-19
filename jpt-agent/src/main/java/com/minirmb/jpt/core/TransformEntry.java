package com.minirmb.jpt.core;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.Instrumentation;
import java.net.InetAddress;
import java.security.ProtectionDomain;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;
import java.util.stream.Stream;

import com.minirmb.jpt.common.AnalysisRange;
import com.minirmb.jpt.common.HeartBeat;
import com.minirmb.jpt.tools.JPTLogger;
import com.minirmb.jpt.tools.Transmitter;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;

/**
 * agent 的入口。
 *
 */
public class TransformEntry implements ClassFileTransformer {

	private AnalysisRange analysisRange;
	private String tracerId;
	private String[] systemPackages= new String[]{"java/","jdk/","sun/","javax/"};

	/**
	 * Agent Entry
	 * 
	 * @param agentArgs String
	 * @param inst      Instrumentation
	 */
	public static void premain(String agentArgs, Instrumentation inst) {
		JPTLogger.log(TransformEntry.class.getName() + ".premain() args : " + agentArgs );

		try {
			/*
			 * ClassFileTransformer : An agent provides an implementation of this interface
			 * in order to transform class files.
			 */
			TransformEntry trans = new TransformEntry();
			trans.analysisRange = AnalysisRangeHelper.GetAnalysisRange();
			trans.tracerId = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date()) + "-"
					+ UUID.randomUUID().toString().substring(0, 8);

			/* Registers the supplied transformer. */
			inst.addTransformer(trans);

			// Setup HeartBeat
			InetAddress address = InetAddress.getLocalHost();
			String hostName = address.getHostName();
			String ip = address.getHostAddress();
			new Timer(true).schedule(new TimerTask() {
				public void run() {
					HeartBeat heartBeat = new HeartBeat(trans.tracerId, System.currentTimeMillis(), hostName, ip);
					Transmitter.sendData(heartBeat);
				}
			}, 1000, 1000);

			JPTLogger.log("premain exec success.", TransformEntry.class.getName(), ".premain() was called. args:",
					agentArgs);

		} catch (Exception e) {
			e.printStackTrace();
			JPTLogger.log("Error when premain : " + agentArgs, e);
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e1) {
			}
			throw new IllegalArgumentException("Error when premain : " + agentArgs, e);
		}
	}

	@Override
	public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined,
			ProtectionDomain protectionDomain, byte[] classfileBuffer) {
		byte[] transformed = null;
		boolean isSystemPackage = Stream.of(systemPackages).parallel().anyMatch( sp -> className.startsWith(sp));
		if ( !isSystemPackage && this.analysisRange.isInIncludeRange(className)) {
			try {
				ClassReader cr = new ClassReader(classfileBuffer);
				ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_FRAMES);
				ClassAdapter ca = new ClassAdapter(tracerId, cw, analysisRange);
				cr.accept(ca, ClassReader.EXPAND_FRAMES);
				transformed = cw.toByteArray();
			} catch (RuntimeException re) {
				re.printStackTrace();
				JPTLogger.log("advised class failed:", className, ", ClassLoader:", String.valueOf(loader) );
				JPTLogger.log(JPTLogger.ExceptionStackTraceToString(re));
			}
		}

		return transformed;
	}
}
