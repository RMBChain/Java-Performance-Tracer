package com.minirmb.jds;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

import com.minirmb.jds.client.collect.DataTransferStation;
import com.minirmb.jds.client.config.RootConfig;
import com.minirmb.jds.common.SnapshotFlag;


public class ClassAdapter extends ClassVisitor implements Opcodes {

	private String className;
	private boolean isInterface;

	public ClassAdapter(final ClassVisitor cv) {
		super(ASM5, cv);
	}

	@Override
	public void visit(int version, int access, String className, String signature, String superName,
			String[] interfaces) {
		if (RootConfig.GetInstance().isLogVisitClass()) {
			DataTransferStation.sendData(SnapshotFlag.PrefixForRoundClass, "visitClass:", className);
		}
		cv.visit(version, access, className, signature, superName, interfaces);
		this.className = className;
		isInterface = (access & Opcodes.ACC_INTERFACE) != 0;
	}
	
	@Override
	public MethodVisitor visitMethod(final int access, final String methodName, final String desc,
			final String signature, final String[] exceptions) {
		MethodVisitor mv = super.visitMethod(access, methodName, desc, signature, exceptions);
		if (!isInterface && mv != null && !"<init>".equals(methodName) && !"<clinit>".equals(methodName)) {
			if (RootConfig.GetInstance().isLogVisitMethod()) {
				DataTransferStation.sendData(SnapshotFlag.PrefixForRoundClass, "  visitMethod:", className, ".", methodName, desc);
			}
			mv = new RoundAdviceAdapter(mv, access, className, methodName, desc);
		}
		return mv;
	}
}
