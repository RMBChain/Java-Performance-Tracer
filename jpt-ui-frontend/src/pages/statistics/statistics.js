import {Button, Col, Form, Row, Select} from 'antd';
import React, {useEffect, useState} from 'react';
import { Table } from 'antd';
import {getStatistics, listHosts, findTracers} from "../../api/jptAPI";
import { nanoid } from 'nanoid'
import {ReloadOutlined} from "@ant-design/icons";

const Statistics = () => {

    const [form] = Form.useForm();
    const [hostList, setHostList] = useState([]);
    const [tracerList, setTracerList] = useState([]);
    const [metricList, setMetricList] = useState([]);
    const [tableLoading, setTableLoading] = useState(false);


    useEffect(() => {
        refreshHosts();
    }, [])

    const onFinish = (values) => {
        console.log('Received values of form: ', values);
    };

    const columns = [
        {
            title: '类',
            dataIndex: 'className',
            width: '25%',
            key: 'className',
            ellipsis: true,
            sorter: (a, b) => { return a.className.localeCompare(b.className);},
            sortDirections: ["ascend",'descend'],
        },
        {
            title: '方法',
            dataIndex: 'methodName',
            width: '20%',
            key: 'methodName',
            ellipsis: true,
            sorter: (a, b) => { return a.methodName.localeCompare(b.methodName);},
            sortDirections: ["ascend",'descend'],
        },
        {
            title: '消耗时间(ms)',
            dataIndex: 'usedTime',
            width: '10%',
            key: 'usedTime',
            align:'right',
            sorter: (a, b, c) => a.usedTime - b.usedTime,
            sortDirections: ["ascend",'descend'],
        },
        {
            title: '调用次数',
            dataIndex: 'invokedCount',
            width: '10%',
            key: 'invokedCount',
            align:'right',
            sorter: (a, b) => a.invokedCount - b.invokedCount,
            sortDirections: ["ascend",'descend'],
        },
        {
            title: '首次调用',
            dataIndex: 'firstInvokeTimeString',
            key: 'firstInvokeTimeString',
            align:'right',
            sorter: (a, b, c) => a.firstInvokeTime.localeCompare(b.firstInvokeTime),
            sortDirections: ["ascend",'descend'],
        },
        {
            title: '最后结束',
            dataIndex: 'lastInvokeTimeString',
            key: 'lastInvokeTimeString',
            align:'right',
            sorter: (a, b, c) => a.lastInvokeTime.localeCompare(b.lastInvokeTime),
            sortDirections: ["ascend",'descend'],
        }
    ];

    useEffect(() => {
        listHosts().then( ( data ) => {
            setHostList( data );
        })
    }, [])

    const onHostChanged = ( val ) => {
        findTracers( { "hostName": val }).then( ( data ) => {
            setTracerList( data );
            let defaultTracer = ( data.length > 0  ) ? data[0].tracerId : "";
            form.setFieldValue("tracer", defaultTracer);
            onTracerChanged( defaultTracer );
        })
    };

    const onTracerChanged= ( val ) => {
        setTableLoading(true);
        getStatistics( { "tracerId": val }).then( ( data ) => {
            data.map( (item) =>{
                item.key = nanoid();
                return item;
            });
            setMetricList( data );
            setTableLoading( false );
        })
    };

    const refreshHosts = ()=>{
        listHosts().then( ( data ) => {
            setHostList( data );
            let defaultHost = ( data.length > 0  ) ? data[0].text : "";
            form.setFieldValue("host", defaultHost);
            onHostChanged(defaultHost);
        })
    };

    return (
        <>
            <Form
                form={form}
                name="advanced_search"
                className="ant-advanced-search-form"
                onFinish={onFinish}>
                <Row gutter={24}>
                    <Col span={1}>
                        <Button
                            type="primary"
                            icon={<ReloadOutlined />}
                            onClick={() => refreshHosts()}
                        />
                    </Col>
                    <Col span={1} />
                    <Col span={6}>
                        <Form.Item
                            name={`host`}
                            label={`Host`}
                        >
                            <Select onChange={onHostChanged} options={(hostList || []).map((d) => ({
                                value: d.value,
                                label: d.text,
                            }))}/>
                        </Form.Item>
                    </Col>
                    <Col span={7}>
                        <Form.Item
                            name={`tracer`}
                            label={`Metric`}>
                            <Select onChange={onTracerChanged} options={(tracerList || []).map((d) => ({
                                value: d.tracerId,
                                label: d.tracerName,
                            }))}/>
                        </Form.Item>
                    </Col>
                </Row>
            </Form>

            <Table
                columns={columns}
                bordered={true}
                pagination={false}
                loading={tableLoading}
                size={"small"}
                onRow={(record) => {
                    return {
                        onClick: (event) => { }, // 点击行
                        onDoubleClick: (event) => {},
                        onContextMenu: (event) => {},
                        onMouseEnter: (event) => {}, // 鼠标移入行
                        onMouseLeave: (event) => {},
                    };
                }}
                dataSource={metricList}
            />
        </>
    );
};
export default Statistics;
