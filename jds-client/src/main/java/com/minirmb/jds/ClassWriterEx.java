package com.minirmb.jds;

import org.objectweb.asm.ClassWriter;

import com.minirmb.jds.client.config.CommonSuper;
import com.minirmb.jds.client.utils.LogUtil;
import com.minirmb.jds.common.SnapshotFlag;

public class ClassWriterEx extends ClassWriter {
	private CommonSuper cs = CommonSuper.GetInstance();
	public ClassWriterEx(int flags) {
		super(flags);
	}

	protected String getCommonSuperClass(final String type1, final String type2) {
		LogUtil.log(SnapshotFlag.PrefixForRoundClass,  "type1:", type1, ", type2:" , type2);
		String result = "java/lang/Object";
		if(!type1.equals("java/lang/Object") || !type2.equals("java/lang/Object")  ) {
			String custSupper = cs.getCommonSuper(type1, type2);
			if( null != custSupper) {
				result = custSupper;
				LogUtil.log(SnapshotFlag.PrefixForRoundClass,  "type1:", type1, ", type2:" , type2, ", get result from cust:", custSupper);
			}else {
				result = super.getCommonSuperClass(type1, type2);
				LogUtil.log(SnapshotFlag.PrefixForRoundClass,  "type1:", type1, ", type2:" , type2, ", get result from super:", custSupper);
			}
		}
		LogUtil.log(SnapshotFlag.PrefixForRoundClass,  "type1:", type1, ", type2:" , type2, ", result:", result);
		return result;
	}

}
