//package com.minirmb.jpt.test;
//
//import com.minirmb.jpt.core.collect.DataTransferStation;
//import com.minirmb.jpt.core.InjectConfig;
//
//import java.io.*;
//import java.util.ArrayList;
//import java.util.List;
//
//public class PerformanceTest {
//
//    private final static String ConfigPath = "C:\\project\\Java-Dynamic-Snapshot\\jds-core-tester\\workspace";
//    private final static String TestDataFile = "C:\\project\\Java-Dynamic-Snapshot\\jds-core\\PerformacneTestData.txt";
//    public static void main(String[] args) throws Exception {
//        DataTransferStation.Init( InjectConfig.Init(ConfigPath) );
//        final List<String> dataBlocks = ReadFileContent(TestDataFile);
//
//        long startTime = System.currentTimeMillis();
//        int sendDataBlockCount = 10 * 10000;
//        for(int i=1;i<=sendDataBlockCount;i++){
//            for(String data : dataBlocks){
//                StringBuilder builder = new StringBuilder();
//                DataTransferStation.sendData( builder.append(data) );
//            }
//            if( i % 1000 == 0 ){
//                System.out.println(i);
//            }
//        }
//        long endTime = System.currentTimeMillis();
//        System.out.println(sendDataBlockCount);
//
//        int perDataBlockSize = 0;
//        for(String data : dataBlocks){
//            perDataBlockSize += data.getBytes().length;
//        }
//
//        double usedTime = 1.0 * (endTime - startTime) / 1000 ;//second
//        long sendDataRowCount = sendDataBlockCount * dataBlocks.size();
//        double sendDataSize = 1.0 * sendDataBlockCount * perDataBlockSize/ (1024 * 1024);//M
//        double sendRowCountPerMinutes = 1.0 * sendDataRowCount / usedTime;//
//        double sendBytePerMinutes =  sendDataSize / usedTime;//PerMinutes
//
//        StringBuilder result = new StringBuilder();
//        result.append("\n");
//        result.append("\n      PerDataBlock Size (byte) : " + perDataBlockSize );
//        result.append("\n  PerDataBlock Row Count (Row) : " + dataBlocks.size() );
//        result.append("\n               Row size (byte) : " + (perDataBlockSize/dataBlocks.size()) );
//        result.append("\n         Send Data Block Count : " + sendDataBlockCount );
//        result.append("\n     Send Data Row Count (Row) : " + sendDataRowCount );
//        result.append("\n           used time (Seconds) : " + usedTime);
//        result.append("\n            Send Data Size (M) : " + (long)sendDataSize + " M");
//        result.append("\nSend Row Speed (K Row/Seconds) : " + (long)(sendRowCountPerMinutes/1000) );
//        result.append("\n   Send Byte Speed (M/Seconds) : " + (long)sendBytePerMinutes );
//
//        System.out.println( result );
//    }
//
//    private static List<String> ReadFileContent(String fileName) throws IOException {
//        List<String> dataList = new ArrayList<>();
//        File file = new File(fileName);
//        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
//            String tempStr;
//            while ((tempStr = reader.readLine()) != null) {
//                dataList.add( tempStr );
//            }
//        }
//        return dataList;
//    }
//}
