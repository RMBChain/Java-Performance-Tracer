package com.minirmb.jpt.common;

public class ClientConfig {

    public static String GetServerIp(){
        return System.getenv().getOrDefault("jpt_nio_server_ip", "localhost");
    }

    public static int GetServerPort(){
        return Integer.parseInt(System.getenv().getOrDefault("jpt_nio_server_port", "8877"));
    }

    public static boolean ShouldLog(){
        return "true".equalsIgnoreCase(System.getenv().getOrDefault("jpt_client_log", "true"));
    }
}
