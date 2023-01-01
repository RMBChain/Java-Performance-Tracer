import {
    DesktopOutlined,
    PieChartOutlined,
} from '@ant-design/icons';
import {  Layout, Menu } from 'antd';
import React, { useState } from 'react';
import Tracer from "./tracer/tracer";
import Setting from "./setting/setting";
import Operation from "./operation/operation";
const { Header, Content, Footer, Sider } = Layout;
function getItem(label, key, icon, children) {
    return {
        key,
        icon,
        children,
        label,
    };
}
const items = [
    getItem('Tracer', '1', <PieChartOutlined />),
    getItem('Setting', '2', <DesktopOutlined />),
    getItem('Operation', '3', <DesktopOutlined />),
];
const App = () => {
    const [collapsed, setCollapsed] = useState(false);
    const [currentMenu, setCurrentMenu] = useState(1);

    return (
        <Layout
            style={{
                minHeight: '100vh',
            }}
        >
            <Sider collapsible collapsed={collapsed} onCollapse={(value) => setCollapsed(value)}>
                <div className="logo" />
                <Menu theme="dark" defaultSelectedKeys={['1']} mode="inline" items={items} onSelect={( val )=>{
                    console.log(val)
                    setCurrentMenu( val.key );
                }}/>
            </Sider>
            <Layout className="site-layout">
                <Content
                    style={{
                        margin: '0 8px',
                    }}
                >
                    <div
                        className="site-layout-background"
                        style={{
                            padding: 12,
                            minHeight: 360,
                        }}
                    >
                        { currentMenu == 1 &&  <Tracer />}
                        { currentMenu == 2 &&  <Setting />}
                        { currentMenu == 3 &&  <Operation />}
                    </div>
                </Content>
                <Footer
                    style={{
                        textAlign: 'center',
                    }}
                >
                    Java Performance Tracer
                </Footer>
            </Layout>
        </Layout>
    );
};
export default App;
