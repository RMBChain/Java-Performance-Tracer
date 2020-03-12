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

import com.minirmb.jds.client.collect.DataTransferStation;
import com.minirmb.jds.client.config.CommonSuper;
import com.minirmb.jds.client.config.InjectRangeConfig;
import com.minirmb.jds.client.config.RootConfig;
import com.minirmb.jds.client.utils.LogUtil;
import com.minirmb.jds.common.SnapshotFlag;
import com.minirmb.jds.common.Utils;

public class TransformEntry  implements ClassFileTransformer {

	private static InjectRangeConfig injectRange;
	private static Set<String> ShouldIgnorePackagePrefixs;//Those packages will no be record
	private static RootConfig rootConfig;

	/**
	 * Agent Entry 
	 * 
	 * @param agentArgs String
	 * @param inst Instrumentation
	 */
    public static void premain(String agentArgs, Instrumentation inst) {
    	System.out.println("---------------jds-client------------------");
    	LogUtil.log( SnapshotFlag.PrefixForSysInfo, TransformEntry.class.getName(),  ".premain() was called.");
    	LogUtil.log( SnapshotFlag.PrefixForSysInfo, "args:", agentArgs);
    	try {
    		RootConfig.Init(agentArgs);
    		rootConfig = RootConfig.GetInstance();
    		//InjectRange
    		InjectRangeConfig.Init( rootConfig.getInjectRangeConfigFile());
    		injectRange = InjectRangeConfig.GetInstance();
    		InjectRangeConfig.GetInstance().print();
    		//CommonSuper
    		CommonSuper.Init(rootConfig.getCommonSuperConfigFile());
    		//LogSerial
    		DataTransferStation.Init(rootConfig);
		} catch (Exception e) {
			e.printStackTrace();
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e1) {
			}
			throw new IllegalArgumentException("Error with folder:" + agentArgs, e);
		}

    	LogUtil.log( SnapshotFlag.PrefixForSysInfo, "config loaded.");
    	
    	ShouldIgnorePackagePrefixs = new HashSet<>(Arrays.asList(new String[] {"java","jdk","com/minirmb/jds"}));
    	ShouldIgnorePackagePrefixs.add(TransformEntry.class.getPackage().getName().replace(".", "/"));
    	
        /* ClassFileTransformer : An agent provides an implementation of this interface in order to transform class files.*/
        ClassFileTransformer trans = new TransformEntry();

        /*Registers the supplied transformer.*/
        inst.addTransformer(trans);
        DataTransferStation.sendData( SnapshotFlag.PrefixForSysInfo, "load adviser ok.", TransformEntry.class.getName(),  ".premain() was called. args:", agentArgs);
        LogUtil.log( SnapshotFlag.PrefixForSysInfo, "load adviser ok.");
    }

    public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined,
			ProtectionDomain protectionDomain, byte[] classfileBuffer) throws IllegalClassFormatException {
   		
    	if(ShouldIgnorePackagePrefixs.stream().filter(str->className.startsWith(str)).findAny().isPresent()) {
    		LogUtil.log(SnapshotFlag.PrefixForSysInfo,"ignore class when advise:", className, ", ClassLoader:", loader);
    		return null;
    	}

		byte[] transformed = classfileBuffer;
		if (injectRange.shouldInject(className)) {
			LogUtil.log(SnapshotFlag.PrefixForSysInfo, "advising class:", className, ", ClassLoader:", loader);
			try {
				ClassReader cr = new ClassReader(classfileBuffer);
				ClassWriter cw = new ClassWriterEx(ClassWriter.COMPUTE_FRAMES);
				ClassAdapter ca = new ClassAdapter(cw);
				cr.accept(ca, ClassReader.EXPAND_FRAMES);
				transformed = cw.toByteArray();
				LogUtil.log(SnapshotFlag.PrefixForSysInfo, "advised class ok:", className, ", ClassLoader:", loader);				
				if (rootConfig.isLogVisitClass()) {
					DataTransferStation.sendData(SnapshotFlag.PrefixForSysInfo, "advised class ok:", className, ", ClassLoader:", loader.toString());
				}
			} catch (RuntimeException re) {
				LogUtil.log(SnapshotFlag.PrefixForSysInfo, "advised class failed:", className, ", ClassLoader:", loader);
				if (rootConfig.isLogVisitClass()) {
					DataTransferStation.sendData(SnapshotFlag.PrefixForSysInfo, "advised class failed:", className, ", ClassLoader:", loader.toString());
					DataTransferStation.sendData(SnapshotFlag.PrefixForSysInfo, Utils.ExceptionStackTraceToString(re));
				}							
				re.printStackTrace();
			} 
		}else {
			LogUtil.log(SnapshotFlag.PrefixForSysInfo,"ignore class when transform:", className, ", ClassLoader:", loader);
		}
		
		return transformed;
	}
}
