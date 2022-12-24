package com.minirmb.jpt.core;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.lang.instrument.Instrumentation;
import java.security.ProtectionDomain;
import java.util.Set;

import com.minirmb.jpt.core.collect.DataTransferStation;
import com.minirmb.jpt.core.utils.InjectConfig;
import com.minirmb.jpt.core.utils.LogUtil;
import com.minirmb.jpt.core.utils.Utils;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;

/**
 * 注入的入口。
 *
 */
public class TransformEntry implements ClassFileTransformer {

	private static InjectConfig injectConfig;

	/**
	 * Agent Entry 
	 * 
	 * @param agentArgs String
	 * @param inst Instrumentation
	 */
    public static void premain(String agentArgs, Instrumentation inst) {
    	System.out.println( TransformEntry.class.getName() + ".premain() args : " + agentArgs + "\n");
    	LogUtil.log( TransformEntry.class.getName() + ".premain() args : " + agentArgs + "\n");

		try {


			String injectConfigStr = new String(DataTransferStation.getInjectConfig());
			LogUtil.log( "*****injectConfigStr : " + injectConfigStr + "\n");
			injectConfig = InjectConfig.Parse(injectConfigStr);
		} catch (Exception e) {
			e.printStackTrace();
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e1) {
			}
			throw new IllegalArgumentException("Error with folder:" + agentArgs, e);
		}

    	LogUtil.log( "config loaded.");

        /* ClassFileTransformer : An agent provides an implementation of this interface in order to transform class files.*/
        ClassFileTransformer trans = new TransformEntry();

        /*Registers the supplied transformer.*/
        inst.addTransformer(trans);

        LogUtil.log( "load adviser ok.", TransformEntry.class.getName(),  ".premain() was called. args:", agentArgs);
    }

    public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined,
			ProtectionDomain protectionDomain, byte[] classfileBuffer) throws IllegalClassFormatException {

		// 忽略包
		if (isInPackages(className, injectConfig.getExcludedPackages())) {
			LogUtil.log("ignore class when advise:", className, ", ClassLoader:", String.valueOf(loader) + "\n");
			return null;
		}

		// 忽略class
		if (injectConfig.getExcludedClasses().contains(className)) {
			LogUtil.log("ignore class when advise:", className, ", ClassLoader:", String.valueOf(loader) + "\n");
			return null;
		}

		byte[] transformed = null;
		if (shouldInject(className)) {
			LogUtil.log("advising class:", className, ", ClassLoader:", String.valueOf(loader) + "\n");
			try {
				ClassReader cr = new ClassReader(classfileBuffer);
				ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_FRAMES);
				ClassAdapter ca = new ClassAdapter(cw);
				cr.accept(ca, ClassReader.EXPAND_FRAMES);
				transformed = cw.toByteArray();
				LogUtil.log("advised class ok:", className, ", ClassLoader:", String.valueOf(loader) + "\n");
			} catch (RuntimeException re) {
				re.printStackTrace();
				LogUtil.log("advised class failed:", className, ", ClassLoader:", String.valueOf(loader) + "\n");
				LogUtil.log(Utils.ExceptionStackTraceToString(re));
			} 
		}

		return transformed;
	}
    
	/**
	 * 
	 * @param className className
	 * @return boolean boolean
	 */
    private boolean shouldInject(String className) {    	
		boolean result = false;
		if (injectConfig.getIncludedClasses().contains(className)) {
			result = true;
		} else if (isInPackages(className, injectConfig.getIncludedPackages())) {
			result = true;
		}
		LogUtil.log("className:", className, ", shouldInject:", String.valueOf(result) + "\n");
		return result;
	}

	private boolean isInPackages(String className, Set<String> packages) {
		return packages.stream().filter(p -> className.startsWith(p)).findAny().isPresent();
	}


}
