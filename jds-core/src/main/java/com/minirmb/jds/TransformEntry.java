package com.minirmb.jds;
import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.lang.instrument.Instrumentation;
import java.security.ProtectionDomain;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;

import com.minirmb.jds.common.Utils;
import com.minirmb.jds.core.collect.DataTransferStation;
import com.minirmb.jds.core.config.InjectRangeConfig;
import com.minirmb.jds.core.config.RootConfig;
import com.minirmb.jds.core.utils.LogUtil;

public class TransformEntry  implements ClassFileTransformer {

	private static Set<String> ShouldIgnorePackagePrefixs;//Those packages will not be record
	private static RootConfig rootConfig;

	/**
	 * Agent Entry 
	 * 
	 * @param agentArgs String
	 * @param inst Instrumentation
	 */
    public static void premain(String agentArgs, Instrumentation inst) {
    	System.out.println( TransformEntry.class.getName() + ".premain() args : " + agentArgs);
    	LogUtil.log( TransformEntry.class.getName() + ".premain() args : " + agentArgs);

    	try {
    		RootConfig.Init(agentArgs);
    		rootConfig = RootConfig.GetInstance();
    		LogUtil.log(rootConfig.getLogConfig());
    		LogUtil.log(rootConfig.getInjectRangeConfig());
    		DataTransferStation.Init(rootConfig);
		} catch (Exception e) {
			e.printStackTrace();
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e1) {
			}
			throw new IllegalArgumentException("Error with folder:" + agentArgs, e);
		}

    	LogUtil.log( "config loaded.");
    	    	
    	ShouldIgnorePackagePrefixs = new HashSet<>(Arrays.asList(new String[] {"java","jdk"}));
    	ShouldIgnorePackagePrefixs.add(TransformEntry.class.getPackage().getName().replace(".", "/"));
    	
        /* ClassFileTransformer : An agent provides an implementation of this interface in order to transform class files.*/
        ClassFileTransformer trans = new TransformEntry();

        /*Registers the supplied transformer.*/
        inst.addTransformer(trans);

        LogUtil.log( "load adviser ok.", TransformEntry.class.getName(),  ".premain() was called. args:", agentArgs);
    }

    public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined,
			ProtectionDomain protectionDomain, byte[] classfileBuffer) throws IllegalClassFormatException {
   		
    	if(ShouldIgnorePackagePrefixs.stream().filter(str->className.startsWith(str)).findAny().isPresent()) {
    		LogUtil.log("ignore class when advise:", className, ", ClassLoader:", String.valueOf(loader));
    		return null;
    	}

		byte[] transformed = classfileBuffer;
		
		if (shouldInject(className)) {
			LogUtil.log("advising class:", className, ", ClassLoader:", String.valueOf(loader));
			try {
				ClassReader cr = new ClassReader(classfileBuffer);
				ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_FRAMES);
				ClassAdapter ca = new ClassAdapter(cw);
				cr.accept(ca, ClassReader.EXPAND_FRAMES);
				transformed = cw.toByteArray();				
				LogUtil.log("advised class ok:", className, ", ClassLoader:", String.valueOf(loader));
			} catch (RuntimeException re) {
				re.printStackTrace();
				LogUtil.log("advised class failed:", className, ", ClassLoader:", String.valueOf(loader));
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
		InjectRangeConfig injectRangeConfig = rootConfig.getInjectRangeConfig();
		if (!injectRangeConfig.getExcludedClasses().contains(className)) {
			if (!isSubPackage(injectRangeConfig.getExcludedPackages(), className)) {
				if (injectRangeConfig.getIncludedClasses().contains(className)) {
					result = true;
				} else if (isSubPackage(injectRangeConfig.getIncludedPackages(), className)) {
					result = true;
				}
			}
		}
		LogUtil.log("className:", className, ", shouldInject:", String.valueOf(result));
		return result;
	}

	private boolean isSubPackage(Set<String> packages, String package2) {
		boolean result = false;
		for (String pk : packages) {
			if (package2.startsWith(pk)) {
				result = true;
				break;
			}
		}
		return result;
	}
}
