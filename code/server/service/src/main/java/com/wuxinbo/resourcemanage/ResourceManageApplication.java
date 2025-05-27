package com.wuxinbo.resourcemanage;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ResourceManageApplication {

	static {
		System.loadLibrary("xbwuc_core");
	}
	public static void main(String[] args) {
		SpringApplication.run(ResourceManageApplication.class, args);
	}

}
