import {Button, Checkbox, Divider, Form, message, Modal, Table, Tooltip} from 'antd';
import React, {useEffect, useState} from 'react';
import {getAnalysisRange, saveAnalysisRange } from "../../api/jptAPI";
import { FloatButton } from 'antd';
import {
    CopyTwoTone,
    DeleteTwoTone, EditTwoTone,
    InfoCircleOutlined
} from "@ant-design/icons";
import {nanoid} from "nanoid";
import Input from "antd/es/input/Input";

const Setting = () => {
    const [form] = Form.useForm();
    const [confirmLoading, setConfirmLoading] = useState(false);
    const [disableSubmit, setDisableSubmit] = useState(true);
    const [analysisRange, setAnalysisRange] = useState({});
    const [showInputAnalysis, setShowInputAnalysis] = useState(false);
    const [messageApi, contextHolder] = message.useMessage();

    const fetchData = ()=>{
        getAnalysisRange().then( ( data ) => {
            const addKey = ( range ) =>{
                if( range ){
                    range.map( item =>{
                        item.key = nanoid();
                        return item;
                    });
                }
                return range;
            };
            data.include = addKey( data.include );
            data.exclude = addKey( data.exclude );
            setAnalysisRange( data );
        })
    };

    useEffect(() => {
        fetchData();
    }, [])

    const reCreateAnalysisRange = ( analysisRange )=>{
        const data = {...analysisRange};
        data.include = [...analysisRange.include];
        // data.exclude = [...analysisRange.exclude];
        return data;
    };

    const saveRange = ( msg )=>{
        saveAnalysisRange( analysisRange ).then( ( data ) => {
            messageApi.open({type: 'success', content: msg,}).then();
        }).catch(()=>{
            messageApi.open({ type: 'error', content: 'Save Management failed!',}).then();
        });
    };

    const findDuplicate = ( recorde )=>{
        let result = false;
        let data = analysisRange.include;
        for(let i=0; i<data.length;i++){
            if( data[i].packageName === recorde.packageName &&
                data[i].className === recorde.className &&
                data[i].methodName === recorde.methodName ){
                result = true;
                break;
            }
        }
        return result;
    };

    const isSameRange = ( range1, range2 )=>{
        return range1.key === range2.key;
    }

    const addAnalysisRange = ( range )=>{
        if( findDuplicate(range) ) {
            messageApi.open({ type: 'error', content: '相同的配置已经存在!',}).then();
        }else{
            if ( range ) {
                range.key = nanoid();
                analysisRange.include.push(range);
                setAnalysisRange(reCreateAnalysisRange(analysisRange));
                saveRange("添加成功！");
            }
        }
    };

    const deleteAnalysisRange = ( recorde )=>{
        let data = analysisRange.include;
        for(let i=0; i<data.length;i++){
            if( isSameRange( data[i], recorde) ){
                data.splice(i,1);
                setAnalysisRange( reCreateAnalysisRange(analysisRange) );
                break;
            }
        }
        saveRange("删除成功!");
    };

    const updateAnalysisRange = ( recorde )=>{
        if( findDuplicate(recorde) ) {
            messageApi.open({ type: 'error', content: '相同的配置已经存在!',}).then();
        }else {
            let data = analysisRange.include;
            for (let i = 0; i < data.length; i++) {
                if (isSameRange(data[i], recorde)) {
                    data[i] = recorde;
                    setAnalysisRange(reCreateAnalysisRange(analysisRange));
                    break;
                }
            }
            saveRange("修改成功!");
        }
    };

    const enableAnalysisRange = ( recorde )=>{
        let data = analysisRange.include;
        for (let i = 0; i < data.length; i++) {
            if (isSameRange(data[i], recorde)) {
                data[i] = recorde;
                setAnalysisRange(reCreateAnalysisRange(analysisRange));
                break;
            }
        }
        saveRange(recorde.enabled ? '启用成功!' : "关闭成功!");
    };

    const columns = [
        {
            title: '包名',
            dataIndex: 'packageName',
            width: '40%',
            key: 'packageName',
            ellipsis: true,
            sorter: (a, b) => { return a.packageName.localeCompare(b.packageName);},
            sortDirections: ["ascend",'descend'],
        },
        {
            title: '类名',
            dataIndex: 'className',
            width: '20%',
            key: 'className',
            ellipsis: true,
            sorter: (a, b) => { return a.className.localeCompare(b.className);},
            sortDirections: ["ascend",'descend'],
        },
        {
            title: '方法名',
            dataIndex: 'methodName',
            width: '20%',
            key: 'methodName',
            ellipsis: true,
            sorter: (a, b) => { return a.methodName.localeCompare(b.methodName);},
            sortDirections: ["ascend",'descend'],
        },
        {
            title: '启用',
            dataIndex: 'enabled',
            key: 'enabled',
            ellipsis: true,
            align:'center',
            sorter: (a, b) => {
                if( a.enabled == b.enabled ){
                    return 0;
                }else if( a.enabled == true && b.enabled == false){
                    return 1;
                }else{
                    return -1;
                }
                },
            sortDirections: ["ascend",'descend'],
            render: (_, record) => {
                return <Checkbox checked={record.enabled} onChange={(e)=>{
                    record.enabled = e.target.checked;
                    enableAnalysisRange(record);
                }} />
            },
        },
        {
            title: '',
            dataIndex: 'hierarchy',
            key: 'hierarchy',
            align:'center',
            render: (_, record) => (
                <>
                    <DeleteTwoTone onClick={()=> deleteAnalysisRange( record)}/>
                    &nbsp;&nbsp;
                    <EditTwoTone onClick={()=> showEditAnalysisRangeDialog( record)}/>
                    &nbsp;&nbsp;
                    <CopyTwoTone onClick={()=> showCopyAnalysisRangeDialog( record)}/>
                </>
            ),
        },
    ];

    const showEditAnalysisRangeDialog = ( record ) => {
        form.setFieldsValue( record );
        setShowInputAnalysis(true);
    };

    const showCopyAnalysisRangeDialog = ( record ) => {
        const r = {...record};
        r.key = undefined;
        form.setFieldsValue( r );
        setShowInputAnalysis(true);
    };

    const showAddAnalysisRangeDialog = () => {
        form.resetFields();
        setShowInputAnalysis(true);
    };

    const handleOk = () => {
        setConfirmLoading(true);
        let fieldValues = form.getFieldsValue();
        if( fieldValues.key ){
            updateAnalysisRange( fieldValues );
        }else{
            addAnalysisRange( fieldValues );
        }
        setConfirmLoading(false);
        setShowInputAnalysis(false);
    };

    const handleCancel = () => {
        setShowInputAnalysis(false);
    };

    const layout = {labelCol: {span: 4,}};

    const onValuesChange = (changedValues, allValues)=>{
        const isBad = ( value )=> typeof value != 'string' || value.length === 0;
        let bad = isBad( allValues["packageName"] ) || isBad( allValues["className"] ) || isBad( allValues["methodName"] );
        setDisableSubmit( bad );
    };

    return (
        <>{contextHolder}
            <Button type="primary" onClick={ showAddAnalysisRangeDialog }>添加分析范围</Button>
            <Divider />
            <Modal
                title={ '添加分析范围' }
                open={showInputAnalysis}
                confirmLoading={confirmLoading}
                centered={true}
                footer={[
                    <Button key="back" onClick={handleCancel}>
                        关闭
                    </Button>,
                    <Button key="submit" type="primary" disabled={disableSubmit} loading={confirmLoading} onClick={handleOk}>
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
                    onValuesChange={onValuesChange}
                    scrollToFirstError
                >
                    <Form.Item
                        name="enabled"
                        label={"enabled"}
                        hidden={true}
                    />
                    <Form.Item
                        name="key"
                        label={"key"}
                        hidden={true}
                    >
                        <Input/>
                    </Form.Item>
                    <Form.Item
                        name="packageName"
                        label={"包名"}
                        rules={[{required: true, message: '请输入包名!',},]}
                    >
                        <Input placeholder="请输入包名" suffix={
                            <Tooltip title={<div>支持JAVA正则表达式。<br/>*表示一个或多个包。<br/>Example：a.b.*, a.*.c, a.b*.c*。 </div>}>
                                <InfoCircleOutlined style={{ color: 'rgba(0,0,0,.45)' }} />
                            </Tooltip>
                        }/>
                    </Form.Item>
                    <Form.Item
                        name="className"
                        label={"类名"}
                        rules={[{required: true, message: '请输入类名!',},]}
                    >
                        <Input placeholder="请输入类名" suffix={
                            <Tooltip title={<div>支持JAVA正则表达式。<br/>*表示一个或多个字符。<br/>Example：<br/>C*表示以字母C开头的类。 <br/>*C表示以字母C结尾的类。<br/> *C*表示包含C的类。 </div>}>
                                <InfoCircleOutlined style={{ color: 'rgba(0,0,0,.45)' }} />
                            </Tooltip>
                        }/>
                    </Form.Item>
                    <Form.Item
                        name="methodName"
                        label={"方法名"}
                        rules={[{required: true, message: '请输入方法名!',},]}
                    >
                        <Input placeholder="请输入方法名" suffix={
                            <Tooltip title={<div>支持JAVA正则表达式。<br/>*表示一个或多个字符.<br/>Example：<br/>C*表示以字母C开头的方法名。 <br/>*C表示以字母C结尾的方法名。 <br/>*C*表示包含C的方法名。 </div>}>
                                <InfoCircleOutlined style={{ color: 'rgba(0,0,0,.45)' }} />
                            </Tooltip>
                        }/>
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
                dataSource={ analysisRange.include }
            />
            <FloatButton.BackTop />
        </>
    );
};
export default Setting;
