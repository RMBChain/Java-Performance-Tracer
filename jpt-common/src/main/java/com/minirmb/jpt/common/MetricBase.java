package com.minirmb.jpt.common;


import static com.minirmb.jpt.common.TracerFlag.*;

/**
 * 进入方法 和 离开方法 是两条记录。
 * 它们的 hostName, tracerId, threadId, hierarchy, methodId 相同。这些确定同一次调用的出入记录。
 * 它们的parentId也相同。
 *
 * 这个类的field不要赋初始值
 */
public class MetricBase {
    private String hostName;
    private String ip;
    private String tracerId;
    private Long threadId;//当前线程id
    private Integer hierarchy;//当前被调用的方法所在的层级。in 和 out 是相同的。
    private Long serial; // logEnter和logExit被调用的次数。

    private Long parentId;//调用 当前方法 的 方法id
    private Long methodId;
    private String inOrOut; // true-方法开始，false-方法结束。
    private String className;
    private String methodName;
    private Long inTime; // isMethodIn==true时，表示方法开始的时间，否则表示方法结束的时间。
    private Long outTime; // isMethodIn==true时，表示方法开始的时间，否则表示方法结束的时间。

    public String getHostName() {
        return hostName;
    }

    public void setHostName(String hostName) {
        this.hostName = hostName;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
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
        //MetricId, threadId,hierarchy,enter or exit,serial,className,methodName,time,methodId,parentId
        StringBuilder builder = new StringBuilder();
        builder.append( MeasureDataPrefix );
        builder.append( hostName );
        builder.append( ItemSplitter);
        builder.append( ip );
        builder.append( ItemSplitter);
        builder.append( tracerId );
        builder.append( ItemSplitter);
        builder.append( String.format("%4d",  threadId ));
        builder.append( ItemSplitter);
        builder.append( String.format("%4d",  hierarchy ));
        builder.append( ItemSplitter);
        builder.append( String.format("%6d",  serial ));
        builder.append( ItemSplitter);
        builder.append( inOrOut );
        builder.append( ItemSplitter);
        builder.append( String.format("%" + (hierarchy * 2) + "s",  " " ) );
        builder.append( className);
        builder.append( ItemSplitter);
        builder.append( methodName);
        builder.append( ItemSplitter);
        builder.append( inTime );
        builder.append( ItemSplitter);
        builder.append( outTime );
        builder.append( ItemSplitter);
        builder.append( methodId );
        builder.append( ItemSplitter);
        builder.append( parentId);
        builder.append( LineBreaker );
        return builder.toString().getBytes();
    }

    //++ 20200509_013010_915, 14,   1,    12,out,    com.thirdpart.jds.test.xxx.tp3.AAAA2,main([Ljava/lang/String;)V,1588959013041,0,1,0
    public static MetricBase parse(String data) {
        MetricBase metric = null;
        if( data.trim().length() > 0 ) {
            metric = new MetricBase();
            String[] items = data.split(TracerFlag.ItemSplitter);
            //tracerId
            metric.setHostName(items[0].substring(TracerFlag.MeasureDataPrefix.length()));
            //tracerId
            metric.setIp(items[1].trim());
            //tracerId
            metric.setTracerId(items[2].trim());
            //threadId
            metric.setThreadId(Long.parseLong(items[3].trim()));
            //hierarchy
            metric.setHierarchy(Integer.parseInt(items[4].trim()));
            //serial
            metric.setSerial(Long.parseLong(items[5].trim()));
            //in or out
            String type = items[6].trim();
            metric.setInOrOut(type);
            //start or end time
            metric.setInTime(Long.parseLong(items[9].trim()));
            metric.setOutTime(Long.parseLong(items[10].trim()));
            metric.setClassName(items[7].trim());
            metric.setMethodName(items[8].trim());
            //method id
            metric.setMethodId(Long.parseLong(items[11]));
            //parent serial id
            metric.setParentId(Long.parseLong(items[12]));
        }
        return metric;
    }
}
