package com.minirmb.jpt.core;

import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.commons.AdviceAdapter;

public class RoundAdviceAdapter extends AdviceAdapter {
	private final String className;
	private final String methodName;
	private final String descriptor;
	private final Label startFinally = new Label();
	private final String methodSign;
	private final String metricClassName;
	private final String tracerId;

	public RoundAdviceAdapter(String tracerId, MethodVisitor mv, int access, String className, String methodName,
			String descriptor) {
		super(ASM9, mv, access, methodName, descriptor);
		this.className = className.replace("/", ".");
		this.methodName = methodName;
		this.descriptor = descriptor;
		this.methodSign = className.replace("/", ".") + "." + methodName + descriptor;
		this.metricClassName = "com/minirmb/jpt/collect/Collector";
		this.tracerId = tracerId;
	}

	public void visitCode() {
		super.visitCode();
		mv.visitLabel(startFinally);
	}

	@Override
	public void onMethodEnter() {
		mv.visitLdcInsn(tracerId);
		mv.visitLdcInsn(className);
		mv.visitLdcInsn(methodName + descriptor);
		mv.visitMethodInsn(INVOKESTATIC, "java/lang/System", "currentTimeMillis", "()J", false);
		mv.visitMethodInsn(INVOKESTATIC, metricClassName, "RecordMethodIn",
				"(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;J)V", false);
	}

	@Override
	public void onMethodExit(int opcode) {
		if (opcode != ATHROW) {
			onFinally(opcode);
		}
	}

	private void onFinally(int opcode) {
		mv.visitLdcInsn(tracerId);
		mv.visitLdcInsn(className);
		mv.visitLdcInsn(methodName + descriptor);
		mv.visitMethodInsn(INVOKESTATIC, "java/lang/System", "currentTimeMillis", "()J", false);
		mv.visitMethodInsn(INVOKESTATIC, metricClassName, "RecordMethodOut",
				"(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;J)V", false);
	}

	@Override
	public void visitMaxs(int maxStack, int maxLocals) {
		Label endFinally = new Label();
		mv.visitTryCatchBlock(startFinally, endFinally, endFinally, null);
		mv.visitLabel(endFinally);
		onFinally(ATHROW);
		mv.visitInsn(ATHROW);
		mv.visitMaxs(maxStack, maxLocals);
	}
}
