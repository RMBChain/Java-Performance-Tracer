package com.minirmb.jpt.core;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

public class ClassAdapter extends ClassVisitor implements Opcodes {

	private String className;
	private boolean isInterface;
	private String tracerId;

	public ClassAdapter(String tracerId, final ClassVisitor cv) {
		super(ASM9, cv);
		this.tracerId = tracerId;
	}

	@Override
	public void visit(int version, int access, String className, String signature, String superName,
			String[] interfaces) {
		JPTLogger.log("visitClass:", className);
		cv.visit(version, access, className, signature, superName, interfaces);
		this.className = className;
		isInterface = (access & Opcodes.ACC_INTERFACE) != 0;
	}

	@Override
	public MethodVisitor visitMethod(final int access, final String methodName, final String desc,
			final String signature, final String[] exceptions) {
		MethodVisitor mv = super.visitMethod(access, methodName, desc, signature, exceptions);
		if (!isInterface && mv != null && !"<init>".equals(methodName) && !"<clinit>".equals(methodName)) {
			JPTLogger.log("  visitMethod:", className, ".", methodName, desc);
			mv = new RoundAdviceAdapter(tracerId, mv, access, className, methodName, desc);
		}
		return mv;
	}
}
