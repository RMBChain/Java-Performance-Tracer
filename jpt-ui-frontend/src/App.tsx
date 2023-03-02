import {
  BarChartOutlined, BugOutlined,
  DesktopOutlined, HomeOutlined,
  SettingOutlined,
} from '@ant-design/icons';
import {FloatButton, Layout, Menu} from 'antd';
import React, { useState } from 'react';
import Metric from "./pages/metric/metric";
import Setting from "./pages/setting/setting";
import Management from "./pages/management/management";
import Statistics from "./pages/statistics/statistics";
import Home from "./pages/home/home";
import Link from "antd/es/typography/Link";

const { Content, Footer, Sider } = Layout;
function getItem(label:string, key:string, icon:any, children:any) {
  return {
    key,
    icon,
    children,
    label,
  };
}
const items = [
  getItem('Home', "1",     <HomeOutlined />, null),
  getItem('Metric', "2",     <BugOutlined />, null),
  getItem('统计', "3", <BarChartOutlined />, null),
  getItem('设置', "4",    <SettingOutlined />, null),
  getItem('清理数据', "5",  <DesktopOutlined />, null),
];
const App = ( props:any ) => {
  const [collapsed, setCollapsed] = useState(true);
  const [currentMenu, setCurrentMenu] = useState("1");

  return (
      <Layout
          style={{
            minHeight: '100vh',
          }}
      >
        <Sider collapsible collapsed={collapsed} onCollapse={(value) => setCollapsed(value)}>
          <div className="logo" style={{
            minHeight: 20,
            color:"white",
            padding:6,
          }}>
            <Link style={{fontSize:36}} target={"_blank"} href="https://github.com/RMBChain/Java-Performance-Tracer.git">JPT</Link>
          </div>
          <Menu theme="dark" defaultSelectedKeys={['1']} mode="inline" items={items} onSelect={( val )=>{
            setCurrentMenu( val.key );
          }}/>
        </Sider>
        <Layout className="site-layout">
          <Content
              style={{
                margin: '0 8px',
              }}>
            <div
                className="site-layout-background"
                style={{
                  padding: 12,
                  minHeight: 360,
                }}>
              <div style={{display: currentMenu === "1" ? '': 'none'}}> <Home /></div>
              <div style={{display: currentMenu === "2" ? '': 'none'}}> <Metric /></div>
              <div style={{display: currentMenu === "3" ? '': 'none'}}> <Statistics /></div>
              <div style={{display: currentMenu === "4" ? '': 'none'}}> <Setting /></div>
              <div style={{display: currentMenu === "5" ? '': 'none'}}> <Management /></div>
            </div>
            <FloatButton.BackTop />
          </Content>
          <Footer
              style={{
                textAlign: 'center',
              }}>
            Java Performance Tracer
          </Footer>
        </Layout>
      </Layout>
  );
};
export default App;
