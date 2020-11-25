package com.dili.trace.service;

import com.dili.trace.service.CodeGenerateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class InitCodeCommandLineRunner implements CommandLineRunner {
	@Autowired
	CodeGenerateService codeGenerateService;

	@Override
	public void run(String... args) throws Exception {
		codeGenerateService.init();
	}
}
