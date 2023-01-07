
import {Button, Col, Form, message, Row} from 'antd';
import React, {useEffect, useState} from 'react';
import {getInjectConfig, saveInjectConfig } from "../../api/tracerAPI";
import { FloatButton } from 'antd';
import TextArea from "antd/es/input/TextArea";
import { Divider } from 'antd';

const Setting = () => {

    const [form] = Form.useForm();
    const [injectConfig, setInjectConfig] = useState({});

    const [messageApi, contextHolder] = message.useMessage();
    const success = () => {
        messageApi.open({
            type: 'success',
            content: 'Setting saved!',
        }).then();
    };

    const onFinish = (values) => {
        saveInjectConfig( values ).then( ( data ) => {
            success();
        });
    };

    useEffect(() => {
        getInjectConfig().then( ( data ) => {
            form.setFieldsValue({
                id:data.id,
                exclude:data.exclude,
                include:data.include
            })
        })
    }, [])

    return (
        <>{contextHolder}
            <Form
                form={form}
                layout="vertical"
                name="advanced_search"
                className="ant-advanced-search-form"
                onFinish={onFinish}>
                <Form.Item
                    style={{display:'none'}}
                    name={`id`}
                    label={`id`}
                >
                    <input value={injectConfig.id} />
                </Form.Item>
                <Row gutter={24}>
                    <Col span={8}>
                        <Form.Item
                            name={`exclude`}
                            label={<div style={{fontSize:"32px"}}>排除的包和类(Exclude)</div>}
                            >
                            <TextArea rows={24} placeholder="Exclude package,classes,method" value={injectConfig.include} />
                        </Form.Item>
                    </Col>
                    <Col span={8}>
                        <Form.Item
                            name={`include`}
                            label={<div style={{fontSize:"32px"}}>需要处理的包和类(Include)</div>}
                            >
                            <TextArea rows={24} placeholder="Include package,classes,method" value={injectConfig.exclude}/>
                        </Form.Item>
                    </Col>
                    <Col span={8}>
                        <div style={{fontSize:"32px"}}>说明:</div>
                        <div>Exclude 的优先级高于 Include</div>
                        <div>指定包名时也即包含子包及包中的类</div>
                    </Col>
                </Row>
                <Divider/>
                <Form.Item
                    wrapperCol={{
                        offset: 8,
                        span: 16,
                    }}
                >
                    <Button type="primary" htmlType="submit">
                        保存
                    </Button>
                </Form.Item>
            </Form>

            <FloatButton.BackTop />
        </>
    );
};
export default Setting;