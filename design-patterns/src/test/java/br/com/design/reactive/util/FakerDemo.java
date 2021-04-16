package br.com.design.reactive.util;

import java.util.stream.IntStream;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class FakerDemo {
	
	public static void main(String[] args) {
		
		IntStream.range(0, 10).forEach(i -> {
			log.info("{}", Util.faker().name().fullName());
		});
	}
	
}
