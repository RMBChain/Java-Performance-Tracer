import {Button, Col, Divider, Form, List, message, Modal, Skeleton, Table, Tooltip} from 'antd';
import React, {useEffect, useState} from 'react';
import {deleteTracer, saveTracer, findTracers, findLogs} from "../../api/jptAPI";
import { FloatButton } from 'antd';
import {
    DeleteTwoTone, EditTwoTone, ReloadOutlined
} from "@ant-design/icons";
import Input from "antd/es/input/Input";
import Row from "antd/es/descriptions/Row";
import Avatar from "antd/es/avatar/avatar";
import InfiniteScroll from 'react-infinite-scroll-component';

const Management = () => {
    const [form] = Form.useForm();
    const [open, setOpen] = useState(false);
    const [confirmLoading, setConfirmLoading] = useState(false);
    const [tracers, setTracers ] = useState([]);
    const [logs, setLogs ] = useState([]);
    const [showInputAnalysis, setShowInputAnalysis] = useState(false);
    const [showLog, setShowLog] = useState(false);
    const [messageApi, contextHolder] = message.useMessage();
    const [shouldDeleteRecorde, setShouldDeleteRecorde] = useState({});

    const fetchData = ()=>{
        findTracers().then( ( data ) => {
            console.log( data );
            data.map( item =>{
                item.key = item.tracerId;
                return item;
            });
            setTracers( data );
        })
    };

    useEffect(() => {
        fetchData();
    }, [])

    const columns = [
        {
            title: '主机名',
            dataIndex: 'hostName',
            width: '20%',
            key: 'hostName',
            ellipsis: true,
            sorter: (a, b) => { return a.hostName.localeCompare(b.hostName);},
            sortDirections: ["ascend",'descend'],
        },
        {
            title: 'IP',
            dataIndex: 'ip',
            width: '10%',
            key: 'ip',
            ellipsis: true,
            sorter: (a, b) => { return a.ip.localeCompare(b.ip);},
            sortDirections: ["ascend",'descend'],
        },
        {
            title: 'tracerName',
            dataIndex: 'tracerName',
            width: '20%',
            key: 'tracerName',
            ellipsis: true,
            sorter: (a, b) => { return a.tracerName.localeCompare(b.tracerName);},
            sortDirections: ["ascend",'descend'],
        },
        {
            title: 'lastUpdate',
            dataIndex: 'lastUpdateStr',
            width: '20%',
            key: 'lastUpdateStr',
            ellipsis: true,
            sorter: (a, b) => { return a.lastUpdateStr.localeCompare(b.lastUpdateStr);},
            sortDirections: ["ascend",'descend'],
        },
        {
            title: 'live',
            dataIndex: 'live',
            key: 'live',
            ellipsis: true,
            align:'center',
            sorter: (a, b) => {
                if( a.live === b.live ){
                    return 0;
                }else if( a.live === true && b.live === false){
                    return 1;
                }else{
                    return -1;
                }
                },
            sortDirections: ["ascend",'descend'],
            render: (_, record) => {
                return record.live ? "live":"-";
            },
        },
        {
            title: '',
            align:'center',
            render: (_, record) => (
                <>
                    <Tooltip title="删除">
                        <DeleteTwoTone onClick={()=> onDeleteTracer( record)}/>
                    </Tooltip>
                    &nbsp;&nbsp;
                    <Tooltip title="修改Tracer名称">
                        <EditTwoTone onClick={()=> showEditAnalysisRangeDialog( record)}/>
                    </Tooltip>
                    &nbsp;&nbsp;
                    <Tooltip title="日志">
                        <EditTwoTone onClick={()=> showLogDialog( record)}/>
                    </Tooltip>
                </>
            ),
        },
    ];

    const showLogDialog = ( record ) => {
        findLogs().then( ( data ) => {
            console.log( "================" );
            console.log( data );
            data.map( item =>{
                item.key = item.timestamp;
                return item;
            });
            setLogs( data );
        })
        setShowLog(true);
    };

    const closeDialog = ( record ) => {
        setShowLog(false);
    };

    const showEditAnalysisRangeDialog = ( record ) => {
        form.setFieldsValue( record );
        setShowInputAnalysis(true);
    };

    const handleOk = () => {
        setConfirmLoading(true);
        let fieldValues = form.getFieldsValue();
        console.log( fieldValues );

        saveTracer( fieldValues ).then( ( data ) => {
            messageApi.open({type: 'success', content: "保存Trace名称成功!",}).then();
        }).catch(()=>{
            messageApi.open({ type: 'error', content: 'Save Management failed!',}).then();
        }).finally(()=>{
            fetchData();
            setConfirmLoading(false);
            setShowInputAnalysis(false);
        });
    };

    const handleCancel = () => {
        setShowInputAnalysis(false);
    };

    const layout = {labelCol: {span: 6,}};

    const onDeleteTracer = ( recorde ) => {
        setShouldDeleteRecorde( recorde );
        setOpen(true);
    };

    const onOk = () => {
        deleteTracer( { "tracerId": shouldDeleteRecorde.tracerId }  ).then( ( ) => {
            setOpen(false);
            success();
            fetchData();
        });
    };

    const success  = () => { messageApi.open({ type: 'success', content: 'All Data has been cleared',}).then(); };
    const onCancel = () => { setOpen(false); };

    return (
        <>{contextHolder}
            <Modal
                title={ '拦截日志' }
                open={showLog}
                centered={true}
                width={1000}
                footer={[
                    <Button key="back" onClick={closeDialog}>
                        关闭
                    </Button>,
                ]}
            >
                <div
                    id="scrollableDiv"
                    style={{
                        height: 600,
                        overflow: 'auto',
                        padding: '0 16px',
                        border: '1px solid rgba(140, 140, 140, 0.35)',
                    }}
                >
                    <InfiniteScroll
                        dataLength={logs.length}
                        loader={
                            <Skeleton
                                avatar
                                paragraph={{
                                    rows: 1,
                                }}
                                active
                            />
                        }
                        endMessage={<Divider plain>🤐</Divider>}
                        scrollableTarget="scrollableDiv"
                    >
                        <List
                            dataSource={logs}
                            renderItem={(item) => (
                                <List.Item key={item.key}>
                                    <div>{ item.timestampStr + " -> " + item.className + "." + item.methodName + item.methodDesc}</div>
                                </List.Item>
                            )}
                        />
                    </InfiniteScroll>
                </div>
            </Modal>

            <Modal
                title="确认"
                open={open}
                onOk={onOk}
                onCancel={onCancel}
                okText="Clear"
                cancelText="Cancel"
            >
                <p>确认删除 { shouldDeleteRecorde.tracerName } 数据么？</p>
            </Modal>

            <Button type="primary"  icon={<ReloadOutlined />} style={{marginBottom:20}} onClick={fetchData}></Button>

            <Modal
                title={ '添加分析范围' }
                open={showInputAnalysis}
                confirmLoading={confirmLoading}
                centered={true}
                footer={[
                    <Button key="back" onClick={handleCancel}>
                        关闭
                    </Button>,
                    <Button key="submit" type="primary" loading={confirmLoading} onClick={handleOk}>
                        OK
                    </Button>,
                ]}
            >
                <Form
                    name="basic"
                    {...layout}
                    form={form}
                    wrapperCol={{ span: 24 }}
                    autoComplete="off"
                    validateTrigger={['onChange', 'onBlur']}
                    scrollToFirstError
                >
                    <Form.Item name="tracerId" label={"tracerId"} hidden={true}><Input/></Form.Item>
                    <Form.Item name="hostName" label={"hostName"} hidden={true}><Input/></Form.Item>
                    <Form.Item name="ip" label={"ip"} hidden={true}><Input/></Form.Item>
                    <Form.Item name="lastUpdateStr" label={"lastUpdateStr"} hidden={true}><Input/></Form.Item>
                    <Form.Item name="live" label={"live"} hidden={true}><Input/></Form.Item>
                    <Form.Item name="tracerName" label={"Metric Name"} rules={[{required: true, message: '请输入Tracer Name!',},]}>
                        <Input placeholder="请输入Tracer Name!"/>
                    </Form.Item>
                </Form>
            </Modal>
            <Table
                columns={columns}
                bordered={true}
                pagination={false}
                size={"small"}
                onRow={(record) => {
                    return {
                        onClick: (event) => {}, // 点击行
                        onDoubleClick: (event) => {},
                        onContextMenu: (event) => {},
                        onMouseEnter: (event) => {}, // 鼠标移入行
                        onMouseLeave: (event) => {},
                    };
                }}
                dataSource={ tracers }
            />
            <FloatButton.BackTop />
        </>
    );
};
export default Management;
