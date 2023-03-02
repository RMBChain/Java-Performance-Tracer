#!/bin/bash
export jpt_client_log=true
java -javaagent:/jpt-agent-0.2.jar -cp /jpt-collector-0.2.jar:/jpt-tester-0.2.jar com.thirdpart.jpt.test.JPT_Tester_Application
