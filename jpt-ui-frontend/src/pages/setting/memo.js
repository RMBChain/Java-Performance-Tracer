import React, {useEffect, useState} from 'react';
import "./memo.css"

const Memo = ( props ) => {
    return (
        <>
            <div>
                <h4 style={{marginBottom:0}}>$ 表示后面接的是类名。 # 表示后面接的是方法名。 * 通配符，代表0个，1个或多个字符。 </h4>
                <h4 style={{marginBottom:0}}>Example:</h4>
                <div style={{paddingLeft:10}}>
                    <h5>a.b.c</h5>
                    <div className={"memoContent"}><b>包</b>a.b.c下的类的所有方法都将被分析，<b>不包括</b>子包中的类及方法。</div>

                    <h5>a.b.*</h5>
                    <div className={"memoContent"}>包a.b下的类的所有方法都将被分析，<b>包括</b>子包中的类及方法。</div>

                    <h5>a.b.$C</h5>
                    <div className={"memoContent"}><b>类</b>a.b.c中的所有方法都将被分析。</div>

                    <h5>a.b.$*c*</h5>
                    <div className={"memoContent"}><b>类</b>包a.b下包含字母c的类中的所有方法都将被分析，<b>不包括</b>a.b子包中的类及方法。。</div>

                    <h5>a.b.$C#d</h5>
                    <div className={"memoContent"}>类a.b.C类中的方法d将被分析。</div>

                    <h5>a.b.$C#*d*</h5>
                    <div className={"memoContent"}>类a.b.C类中的方法名包含字母d的都将被分析。</div>

                    <h5>a.b.$C#d*</h5>
                    <div className={"memoContent"}>类a.b.C类中以字母d开头的方法都将被分析。</div>

                    <h5>a.b.*.*c.$d*#*ef*</h5>
                    <div className={"memoContent"}>
                        限制为以下的全部条件：<br/>
                        1.包a.b及其子包中的类<br/>
                        2.包名的最后一层以字母c结尾<br/>
                        3.类名以字母d开头<br/>
                        4.方法名中包含ef<br/>
                    </div>
                </div>
            </div>
        </>
    );
};
export default Memo;
