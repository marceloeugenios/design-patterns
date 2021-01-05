package br.com.design.patterns.strategy.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.IntStream;

import br.com.design.patterns.strategy.behavior.IReadable;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class XmlReader implements IReadable {

	@Override
	public List<String[]> readFile(String filePath) {
		log.info("Reading in Xml: {}", filePath);
		List<String[]> list = new ArrayList<>();
		IntStream.range(0, 10).forEach(i -> {
			String[] a = new String[2];
			IntStream.range(0, 2).forEach(j -> a[j] = String.valueOf(new Random().nextInt(1000)));
			list.add(a);
		});
		return list;
	}
}
