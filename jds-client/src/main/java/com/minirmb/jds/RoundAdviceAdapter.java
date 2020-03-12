package com.minirmb.jds;


import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.commons.AdviceAdapter;

import com.minirmb.jds.client.collect.Collector;
import com.minirmb.jds.client.collect.DataTransferStation;
import com.minirmb.jds.client.config.RootConfig;
import com.minirmb.jds.common.SnapshotFlag;

public class RoundAdviceAdapter extends AdviceAdapter {
	private String className;
	private String methodName;
	private String descriptor;
	private Label startFinally = new Label();
	private String methodSign;
	private String metricClassName;
	private RootConfig rootConfig;

	public RoundAdviceAdapter(MethodVisitor mv, int access, String className, String methodName, String descriptor) {
		super(ASM5, mv, access, methodName, descriptor);
		this.className = className.replace("/", ".");
		this.methodName = methodName;
		this.descriptor = descriptor;
		this.methodSign = className.replace("/", ".") + "." + methodName + descriptor;
		this.metricClassName = Collector.class.getName().replace(".", "/");
		rootConfig = RootConfig.GetInstance();
	}

	public void visitCode() {
		super.visitCode();
		mv.visitLabel(startFinally);
	}

	@Override
	public void onMethodEnter() {		
		if (rootConfig.isLogOnMethodEnter()) {
			DataTransferStation.sendData(SnapshotFlag.PrefixForRoundClass,"    onMethodEnter:" , methodSign);
		}
		mv.visitLdcInsn(className);
		mv.visitLdcInsn(methodName + descriptor);
		mv.visitMethodInsn(INVOKESTATIC, "java/lang/System", "currentTimeMillis", "()J", false);
		mv.visitMethodInsn(INVOKESTATIC, metricClassName, "LogEnter", "(Ljava/lang/String;Ljava/lang/String;J)V", false);
	}

	@Override
	public void onMethodExit(int opcode) {
		if (opcode != ATHROW) {
			if (rootConfig.isLogOnMethodExit()) {
				DataTransferStation.sendData(SnapshotFlag.PrefixForRoundClass,"    onMethodExit:", methodSign, ", opcode:" + opcode);
			}
			onFinally(opcode);
		}
	}

	private void onFinally(int opcode) {
		if (rootConfig.isLogOnFinally()) {
			DataTransferStation.sendData(SnapshotFlag.PrefixForRoundClass,"    onFinally:", methodSign, "opcode:" + opcode);
		}
		mv.visitLdcInsn(className);
		mv.visitLdcInsn(methodName + descriptor);
		mv.visitMethodInsn(INVOKESTATIC, "java/lang/System", "currentTimeMillis", "()J", false);
		mv.visitMethodInsn(INVOKESTATIC, metricClassName, "LogExit", "(Ljava/lang/String;Ljava/lang/String;J)V", false);
	}

	@Override
	public void visitMaxs(int maxStack, int maxLocals) {
		if (rootConfig.isLogVisitMaxs()) {
			DataTransferStation.sendData(SnapshotFlag.PrefixForRoundClass, "    visitMaxs:", " maxStack:" +maxStack, ",  maxLocals:" + maxLocals);
		}
		Label endFinally = new Label();
		mv.visitTryCatchBlock(startFinally, endFinally, endFinally, null);
		mv.visitLabel(endFinally);
		onFinally(ATHROW);
		mv.visitInsn(ATHROW);
		mv.visitMaxs(maxStack, maxLocals);
	}
}
