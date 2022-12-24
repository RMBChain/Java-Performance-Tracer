package com.minirmb.jpt.core;

import com.minirmb.jpt.core.collect.Collector;
import com.minirmb.jpt.core.utils.LogUtil;
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
	
	public RoundAdviceAdapter(MethodVisitor mv, int access, String className, String methodName, String descriptor) {
		super(ASM9, mv, access, methodName, descriptor);
		this.className = className.replace("/", ".");
		this.methodName = methodName;
		this.descriptor = descriptor;
		this.methodSign = className.replace("/", ".") + "." + methodName + descriptor;
		this.metricClassName = Collector.class.getName().replace(".", "/");
	}

	public void visitCode() {
		super.visitCode();
		mv.visitLabel(startFinally);
	}

	@Override
	public void onMethodEnter() {		
		LogUtil.log(className, methodName, descriptor);
		mv.visitLdcInsn(className);
		mv.visitLdcInsn(methodName + descriptor);
		mv.visitMethodInsn(INVOKESTATIC, "java/lang/System", "currentTimeMillis", "()J", false);
		mv.visitMethodInsn(INVOKESTATIC, metricClassName, "RecordMethodIn", "(Ljava/lang/String;Ljava/lang/String;J)V", false);
	}

	@Override
	public void onMethodExit(int opcode) {
		if (opcode != ATHROW) {
			LogUtil.log(methodSign, ", opcode:" + opcode);
			onFinally(opcode);
		}
	}

	private void onFinally(int opcode) {
		LogUtil.log(methodSign, ", opcode:" + opcode);
		mv.visitLdcInsn(className);
		mv.visitLdcInsn(methodName + descriptor);
		mv.visitMethodInsn(INVOKESTATIC, "java/lang/System", "currentTimeMillis", "()J", false);
		mv.visitMethodInsn(INVOKESTATIC, metricClassName, "RecordMethodOut", "(Ljava/lang/String;Ljava/lang/String;J)V", false);
	}

	@Override
	public void visitMaxs(int maxStack, int maxLocals) {
		LogUtil.log(methodSign, "    visitMaxs:", " maxStack:" +maxStack, ",  maxLocals:" + maxLocals);
		Label endFinally = new Label();
		mv.visitTryCatchBlock(startFinally, endFinally, endFinally, null);
		mv.visitLabel(endFinally);
		onFinally(ATHROW);
		mv.visitInsn(ATHROW);
		mv.visitMaxs(maxStack, maxLocals);
	}
}
