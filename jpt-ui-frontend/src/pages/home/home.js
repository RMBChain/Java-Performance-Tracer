import React from 'react';
import { Divider, Typography } from 'antd';

const { Title, Paragraph, Text, Link } = Typography;

const Home: React.FC = () => (
    <Typography style={{paddingLeft:72}}>
        <Title>JAVA Performance Tracer(JPT)</Title>
        <Divider />
        <Paragraph>
            <Text style={{fontSize:24}}>JPT是WEB版的，易于使用。</Text>
        </Paragraph>
        <Paragraph>
            <Text style={{fontSize:24}}>能够记录执行了哪些方法，这些方法执行了多长时间</Text>
        </Paragraph>
        <Paragraph>
            <Text style={{fontSize:24}}>可以看到方法的调用链路。</Text>
        </Paragraph>
        <Paragraph>
            <Text style={{fontSize:24}}>对代码没有侵入性,无需修改现有代码即可使用。</Text>
        </Paragraph>
        <br/>
        <Paragraph>
            <Text style={{fontSize:24}}>
                更多信息请关注 <Link style={{fontSize:24}} href="https://github.com/RMBChain/Java-Performance-Tracer.git">https://github.com/RMBChain/Java-Performance-Tracer.git</Link>
            </Text>
        </Paragraph>
    </Typography>
);

export default Home;
