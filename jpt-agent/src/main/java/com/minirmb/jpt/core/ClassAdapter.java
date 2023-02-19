package com.minirmb.jpt.core;

import com.minirmb.jpt.common.AnalysisLog;
import com.minirmb.jpt.common.AnalysisRange;
import com.minirmb.jpt.tools.JPTLogger;
import com.minirmb.jpt.tools.Transmitter;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

public class ClassAdapter extends ClassVisitor implements Opcodes {

	private String className;
	private boolean isInterface;
	private final String tracerId;
	private final AnalysisRange analysisRange;

	public ClassAdapter(String tracerId, final ClassVisitor cv, AnalysisRange analysisRange) {
		super(ASM9, cv);
		this.tracerId = tracerId;
		this.analysisRange = analysisRange;
	}

	@Override
	public void visit(int version, int access, String className, String signature, String superName,
			String[] interfaces) {
		cv.visit(version, access, className, signature, superName, interfaces);
		this.className = className;
		isInterface = (access & Opcodes.ACC_INTERFACE) != 0;
	}

	@Override
	public MethodVisitor visitMethod(final int access, final String methodName, final String desc,
			final String signature, final String[] exceptions) {
		MethodVisitor mv = super.visitMethod(access, methodName, desc, signature, exceptions);
		if (!isInterface && mv != null && !"<init>".equals(methodName) && !"<clinit>".equals(methodName)) {
			if( analysisRange.shouldAnalysisMethod( this.className , methodName ) ){
				JPTLogger.log("  visitMethod:", className + "." + methodName, desc);
				mv = new RoundAdviceAdapter(tracerId, mv, access, className, methodName, desc);

				AnalysisLog analysisLog = new AnalysisLog();
				analysisLog.setTracerId(tracerId);
				analysisLog.setClassName( className );
				analysisLog.setMethodName( methodName );
				analysisLog.setMethodDesc( desc );
				analysisLog.setSignature( signature );
				analysisLog.setTimestamp(System.currentTimeMillis());
				Transmitter.sendData(analysisLog);
			}
		}
		return mv;
	}
}
