import { Col, Form, Row, Select } from 'antd';
import React, {useEffect, useState} from 'react';
import { Table } from 'antd';
import {getStatistics, listHosts, listTracers} from "../../api/tracerAPI";
import { FloatButton } from 'antd';

const Statistics = () => {

    const [form] = Form.useForm();
    const [hostList, setHostList] = useState([]);
    const [tracerList, setTracerList] = useState([]);
    const [metricList, setMetricList] = useState([]);
    const [tableLoading, setTableLoading] = useState(false);

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
        },
        {
            title: '方法',
            dataIndex: 'methodName',
            width: '20%',
            key: 'methodName',
            ellipsis: true,
        },
        {
            title: '消耗时间(ms)',
            dataIndex: 'usedTime',
            width: '10%',
            key: 'usedTime',
            align:'right'
        },
        {
            title: '调用次数',
            dataIndex: 'invokedCount',
            width: '10%',
            key: 'invokedCount',
            align:'right'
        },
        {
            title: '首次调用',
            dataIndex: 'firstInvokeTimeString',
            key: 'firstInvokeTimeString',
            align:'right'
        },
        {
            title: '最后结束',
            dataIndex: 'lastInvokeTimeString',
            key: 'lastInvokeTimeString',
            align:'right'
        }
    ];

    useEffect(() => {
        listHosts().then( ( data ) => {
            setHostList( data );
        })
    }, [])

    const onHostChanged = ( val ) => {
        listTracers( { "hostName": val }).then( ( data ) => {
            setTracerList( data );
        })
    };

    const onTracerChanged= ( val ) => {
        setTableLoading(true);
        getStatistics( { "tracerId": val }).then( ( data ) => {
            console.log( data);
            setMetricList( data );
            setTableLoading( false );
        })
    };

    return (
        <>
            <Form
                form={form}
                className="ant-advanced-search-form"
                onFinish={onFinish}>
                <Row gutter={24}>
                    <Col span={8}>
                        <Form.Item
                            name={`Host`}
                            label={`Host`}
                            >
                            <Select onChange={onHostChanged} options={(hostList || []).map((d) => ({
                                value: d.value,
                                label: d.text,
                            }))}/>
                        </Form.Item>
                    </Col>
                    <Col span={8}>
                        <Form.Item
                            name={`Tracer`}
                            label={`Tracer`}>
                            <Select onChange={onTracerChanged} options={(tracerList || []).map((d) => ({
                                value: d.value,
                                label: d.text,
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
