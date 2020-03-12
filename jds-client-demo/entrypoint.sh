#!/bin/bash

while true                                               
  do                                                     
    /usr/local/openjdk-8/bin/java -javaagent:/home/jds-client-0.0.1.jar=/home/config -cp jds-client-demo-0.0.1.jar com.thirdpart.jds.test.JDS_ClientDemo_Application
    sleep 20                                 
  done                                        
