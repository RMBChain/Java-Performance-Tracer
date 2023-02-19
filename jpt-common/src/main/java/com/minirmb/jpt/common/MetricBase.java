package com.minirmb.jpt.common;

import static com.minirmb.jpt.common.TracerFlag.*;

/**
 * 进入方法 和 离开方法 是两条记录。
 * 它们的 hostName, tracerId, threadId, hierarchy, methodId 相同。这些确定同一次调用的出入记录。
 * 它们的parentId也相同。
 *
 * 这个类的field不要赋初始值
 */
public class MetricBase implements IData{
    private String id; // = tracerId + "_" + threadId + "_" + serial
    private String tracerId;
    private String bundleId; // 标注每个 OSGI 的 bundle。
    private Long threadId;//当前线程id
    private Integer hierarchy;//当前被调用的方法所在的层级。in 和 out 是相同的。
    private Long serial; // logEnter和logExit被调用的序列数。

    private Long parentId;//调用 当前方法 的 方法id
    private Long methodId; // 一次调用的in和out的methodId是相同的。
    private String inOrOut; // true-方法开始，false-方法结束。
    private String className;
    private String methodName;
    private Long inTime; // isMethodIn==true时，表示方法开始的时间，否则表示方法结束的时间。
    private Long outTime; // isMethodIn==true时，表示方法开始的时间，否则表示方法结束的时间。

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTracerId() {
        return tracerId;
    }

    public void setTracerId(String tracerId) {
        this.tracerId = tracerId;
    }

    public long getThreadId() {
        return threadId;
    }

    public String getBundleId() {
        return bundleId;
    }

    public void setBundleId(String bundleId) {
        this.bundleId = bundleId;
    }
    public void setThreadId(long threadId) {
        this.threadId = threadId;
    }

    public Integer getHierarchy() {
        return hierarchy;
    }

    public void setHierarchy(int hierarchy) {
        this.hierarchy = hierarchy;
    }

    public long getSerial() {
        return serial;
    }

    public void setSerial(long serial) {
        this.serial = serial;
    }

    public long getParentId() {
        return parentId;
    }

    public void setParentId(long parentId) {
        this.parentId = parentId;
    }

    public long getMethodId() {
        return methodId;
    }

    public void setMethodId(long methodId) {
        this.methodId = methodId;
    }

    public String getInOrOut() {
        return inOrOut;
    }

    public void setInOrOut(String inOrOut) {
        this.inOrOut = inOrOut;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public long getInTime() {
        return inTime;
    }

    public void setInTime(long inTime) {
        this.inTime = inTime;
    }

    public long getOutTime() {
        return outTime;
    }

    public void setOutTime(long outTime) {
        this.outTime = outTime;
    }

    public byte[] toBytes(){
        //id, MetricId, threadId,hierarchy,enter or exit,serial,className,methodName,time,methodId,parentId
        StringBuilder builder = new StringBuilder();
        builder.append( MeasureDataPrefix );
        builder.append( ItemSplitter).append( id );
//        builder.append( ItemSplitter).append( hostName );
        builder.append( ItemSplitter).append( tracerId );
        builder.append( ItemSplitter).append( bundleId );
        builder.append( ItemSplitter).append( String.format("%4d",  threadId ));
        builder.append( ItemSplitter).append( String.format("%4d",  hierarchy ));
        builder.append( ItemSplitter).append( String.format("%6d",  serial ));
        builder.append( ItemSplitter).append( inOrOut );
        builder.append( ItemSplitter).append( String.format("%" + (hierarchy * 2) + "s",  " " )).append( className);
        builder.append( ItemSplitter).append( methodName);
        builder.append( ItemSplitter).append( inTime );
        builder.append( ItemSplitter).append( outTime );
        builder.append( ItemSplitter).append( methodId );
        builder.append( ItemSplitter).append( parentId);
        builder.append( LineBreaker );
        return builder.toString().getBytes();
    }

    //++ 20200509_013010_915, 14,   1,    12,out,    com.thirdpart.jds.test.xxx.tp3.AAAA2,main([Ljava/lang/String;)V,1588959013041,0,1,0
    public static MetricBase parse(String data) {
        MetricBase metric = null;
        if( data.trim().length() > 0 ) {
            metric = new MetricBase();
            String[] items = data.split(TracerFlag.ItemSplitter);
            // id
            String id = items[1].trim();
            metric.setId( id );
            //tracerId
            metric.setTracerId(items[2].trim());
            //bundleId
            metric.setBundleId(items[3].trim());
            //threadId
            metric.setThreadId(Long.parseLong(items[4].trim()));
            //hierarchy
            metric.setHierarchy(Integer.parseInt(items[5].trim()));
            //serial
            metric.setSerial(Long.parseLong(items[6].trim()));
            //in or out
            metric.setInOrOut(items[7].trim());
            metric.setClassName(items[8].trim());
            metric.setMethodName(items[9].trim());
            //start or end time
            metric.setInTime(Long.parseLong(items[10].trim()));
            metric.setOutTime(Long.parseLong(items[11].trim()));
            //method id
            metric.setMethodId(Long.parseLong(items[12]));
            //parent serial id
            metric.setParentId(Long.parseLong(items[13]));
        }
        return metric;
    }
}
