package com.minirmb.jpt.orm.entity;

import com.minirmb.jpt.common.MetricBase;
import lombok.Data;
import org.springframework.beans.BeanUtils;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * 进入方法 和 离开方法 是两条记录。
 * 它们的 tracerId, bundleId, threadId, methodId 相同。这些确定同一个方法调用的出入记录。
 * 它们的parentId也相同。
 *
 *
 */
@Document(collection="MetricEntity")
@Data
@CompoundIndexes({
		@CompoundIndex(name = "unMergedOutRow",def = "{'inOrOut': 1,'merged': 1}"),
		@CompoundIndex(name = "findInRow",def = "{'tracerId': 1,'bundleId': 1,'threadId': 1,'methodId': 1,'inOrOut': 1}"),
})
public class MetricEntity extends MetricBase {
	@Id
	private String id;

	private Boolean merged;
	private String packageName;
	private Long usedTime;// Long

	public static MetricEntity parse(String data) {
		MetricEntity metricEntity = null;
		if( data.trim().length() > 0 ) {
			metricEntity = new MetricEntity();
			MetricBase mb = MetricBase.parse( data );
			BeanUtils.copyProperties(mb, metricEntity);
			metricEntity.setMerged(false);
		}
		return metricEntity;
	}
}
