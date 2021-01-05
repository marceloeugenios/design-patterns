package br.com.design.patterns.strategy.behavior;

import java.util.List;

public interface IReadable {

	List<String[]> readFile(String filePath);
	
}
