package com.minirmb.jds;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.minirmb.jds.receiver.NIOServer;

@SpringBootApplication
public class JDS_Receiver_Application {

	@Autowired
	private NIOServer server;
	
	@PreDestroy
	public void preDestroy() throws Exception {
	}

	@PostConstruct
	public void postConstruct() throws Exception {
		server.start();
	}
	
    public static void main(String[] args) {
        SpringApplication.run(JDS_Receiver_Application.class, args);
    }
}
