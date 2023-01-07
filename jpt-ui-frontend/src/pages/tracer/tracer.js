
import {Button, Col, Form, Row, Select} from 'antd';
import React, {useEffect, useState} from 'react';
import { Table } from 'antd';
import { listHosts, listMetrics, listTracers} from "../../api/tracerAPI";
import { FloatButton } from 'antd';

const Tracer = () => {

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
            title: '方法',
            dataIndex: 'methodName',
            width: '40%',
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
            title: '所在类',
            dataIndex: 'className',
            width: '20%',
            key: 'className',
            ellipsis: true,
        },
        {
            title: '层级',
            dataIndex: 'hierarchy',
            key: 'hierarchy',
            width: '5%',
            align:'right'
        },
        {
            title: '序列',
            dataIndex: 'serial',
            key: 'serial',
            width: '5%',
            align:'right'
        },
        {
            title: 'Bundle&Thread',
            dataIndex: 'bundleThread',
            key: 'bundleThread',
        },

    ];

    // rowSelection objects indicates the need for row selection
    const rowSelection = {
        onSelect: (record, selected, selectedRows) => {
            console.log(record, selected, selectedRows);
        },
    };

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

    const postMetricList= ( metricList ) => {
        metricList.map( (item) =>{
            item.key = item.id;
            item.bundleThread = item.bundleId + "_" + item.threadId;
            return item;
        });
        return metricList;
    };

    const onTracerChanged= ( val ) => {
        setTableLoading(true);
        listMetrics( { "tracerId": val }).then( ( data ) => {
            setMetricList( postMetricList( data ) );
            setTableLoading( false );
        })
    };

    const onMetricExpand= ( expanded, record ) => {
        if( !record.childrenLoaded ) {
            setTableLoading(true);
            const parameters = {"tracerId": record.tracerId, "threadId": record.threadId, "serial": record.serial};
            listMetrics(parameters).then((data) => {
                if (data.length === 0) {
                    delete record.children;
                } else {
                    record.children = postMetricList(data);
                    record.childrenLoaded = true;
                }
                setMetricList([...metricList]);
                setTableLoading(false);
            })
        }
    };

    return (
        <>
            <Form
                form={form}
                name="advanced_search"
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
                onExpand={onMetricExpand}
                size={"small"}
                onRow={(record) => {
                    return {
                        onClick: (event) => { console.log("click on row")}, // 点击行
                        onDoubleClick: (event) => {},
                        onContextMenu: (event) => {},
                        onMouseEnter: (event) => {}, // 鼠标移入行
                        onMouseLeave: (event) => {},
                    };
                }}
                rowSelection={{
                    ...rowSelection
                }}
                dataSource={metricList}
            />
            <FloatButton.BackTop />
        </>
    );
};
export default Tracer;
