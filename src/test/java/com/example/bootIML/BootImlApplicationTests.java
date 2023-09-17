package com.example.bootIML;

import com.example.bootIML.interpretator.SourceProgram;
import com.example.bootIML.service.InterpretatorService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Slf4j
@SpringBootTest
class BootImlApplicationTests {

	@Autowired
	private InterpretatorService interpretatorService;
	@Test
	void contextLoads() {
		log.info("TEST: contexLoads");
	}

	@Test
	void testInterpretator() {
  	String srcCode = System.getProperty("user.dir") + System.getProperty("file.separator") + "m-ext-gcd.txt";
		log.info("TEST: testInterpretator");
		log.info(srcCode);
		String sourceText = "";
		Path path = Paths.get(srcCode);
		try {
			List<String> lines = Files.readAllLines(path, StandardCharsets.UTF_8);
			for (String line : lines) {
				sourceText += line + System.lineSeparator();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		SourceProgram sourceProgram = interpretatorService.invokeInterpretator(sourceText);
		sourceProgram.resultList.forEach(s -> System.out.println(s));
		List<Integer> tstFiles = List.of(5, 3, 1, -1, 2);
		Assertions.assertArrayEquals(sourceProgram.resultList.toArray(), tstFiles.toArray());
	}
}
