package com.carolinachang.test;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.carolinachang.test.services.S3Service;

@SpringBootApplication
public class TestApplication implements CommandLineRunner{

	
	private Logger LOG = LoggerFactory.getLogger(S3Service.class);
	
//	@Autowired
//	private S3Service s3Service;
	
	
	public static void main(String[] args) {
		SpringApplication.run(TestApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
//		try {	
//			s3Service.uploadFile("C:\\lagoa.jpg");
//		}catch (Exception e) {
//			LOG.info("Erro ao carregar img" +e.getMessage()) ;
//		}
	}
}
