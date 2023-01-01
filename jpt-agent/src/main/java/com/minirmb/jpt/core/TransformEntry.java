package com.minirmb.jpt.core;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.Instrumentation;
import java.security.ProtectionDomain;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.UUID;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;

/**
 * agent 的入口。
 *
 */
public class TransformEntry implements ClassFileTransformer {

	private InjectConfig injectConfig;
	private String tracerId;

	/**
	 * Agent Entry
	 * 
	 * @param agentArgs String
	 * @param inst      Instrumentation
	 */
	public static void premain(String agentArgs, Instrumentation inst) {
		System.out.println(TransformEntry.class.getName() + ".premain() args : " + agentArgs + "\n");
		JPTLogger.log(TransformEntry.class.getName() + ".premain() args : " + agentArgs + "\n");

		try {
			/*
			 * ClassFileTransformer : An agent provides an implementation of this interface
			 * in order to transform class files.
			 */
			TransformEntry trans = new TransformEntry();
			{ // Inject Config
				Map<String, String> env = System.getenv();
				String server = env.getOrDefault("jpt_nio_server_ip", "localhost");
				int port = Integer.parseInt(env.getOrDefault("jpt_nio_server_port", "8877"));
				trans.injectConfig = InjectConfig.GetInjectConfig(server, port);
			}
			trans.tracerId = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date()) + "-"
					+ UUID.randomUUID().toString().substring(0, 8);

			/* Registers the supplied transformer. */
			inst.addTransformer(trans);

			JPTLogger.log("load adviser ok.", TransformEntry.class.getName(), ".premain() was called. args:",
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

	public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined,
			ProtectionDomain protectionDomain, byte[] classfileBuffer) {
		byte[] transformed = null;
		if (injectConfig.shouldInject(className)) {
			try {
				ClassReader cr = new ClassReader(classfileBuffer);
				ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_FRAMES);
				ClassAdapter ca = new ClassAdapter(tracerId, cw);
				cr.accept(ca, ClassReader.EXPAND_FRAMES);
				transformed = cw.toByteArray();
				JPTLogger.log("advised class ok:", className, ", ClassLoader:", String.valueOf(loader) + "\n");
			} catch (RuntimeException re) {
				re.printStackTrace();
				JPTLogger.log("advised class failed:", className, ", ClassLoader:", String.valueOf(loader) + "\n");
				JPTLogger.log(JPTLogger.ExceptionStackTraceToString(re));
			}
		}

		return transformed;
	}
}
