package br.com.design.patterns.strategy;

import java.util.List;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class DataPersist {

	private final List<String[]> data;

	public void printData() {
		data.stream().forEach(strArray -> {
			StringBuilder sb = new StringBuilder();
			for (int i = 0; i < strArray.length; i++) {
				sb.append(" [").append(i).append("]").append(" = ").append(strArray[i]);
			}
			log.info(sb.toString());
		});
	}
}
