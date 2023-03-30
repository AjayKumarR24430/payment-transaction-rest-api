package com.restapi.demo.transaction;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;


@SpringBootApplication
@EntityScan("com.restapi.demo.transaction.model")
public class TransactionApplication  {

	public static void main(String[] args) {
		SpringApplication.run(TransactionApplication.class, args);
	}

}
