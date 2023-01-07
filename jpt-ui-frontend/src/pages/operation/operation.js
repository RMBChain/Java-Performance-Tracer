
import {Button, Col, Form, Row } from 'antd';
import React, {useEffect, useState} from 'react';
import {clearAllData, getInjectConfig} from "../../api/tracerAPI";
import { FloatButton,message } from 'antd';
import Modal from "antd/es/modal/Modal";

const Operation = () => {

    const [form] = Form.useForm();

    const onClearAllData = () => {
        setOpen(true);
    };

    const [open, setOpen] = useState(false);

    const onOk = () => {
        clearAllData( ).then( ( data ) => {
            setOpen(false);
            success();
        });
    };

    const [messageApi, contextHolder] = message.useMessage();
    const success = () => {
        messageApi.open({
            type: 'success',
            content: 'All Data has been cleared',
        }).then();
    };
    const onCancel = () => {
        setOpen(false);
    };
    return (
        <>
            {contextHolder}
            <Modal
                title="Modal"
                open={open}
                onOk={onOk}
                onCancel={onCancel}
                okText="Clear"
                cancelText="Cancel"
            >
                <p>Are you sure CLEAR ALL DATA ??</p>
            </Modal>
            <Form
                form={form}
                name="advanced_search"
                className="ant-advanced-search-form">
                    <Button type="primary" onClick={onClearAllData}>
                        Clear All Data
                    </Button>
            </Form>
            <FloatButton.BackTop />
        </>
    );
};
export default Operation;
