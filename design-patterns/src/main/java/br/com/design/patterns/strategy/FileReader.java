package br.com.design.patterns.strategy;

import java.util.List;

import br.com.design.patterns.strategy.behavior.IReadable;

public class FileReader {

	private final IReadable readable;

	public FileReader(IReadable readable) {
		this.readable = readable;
	}

	public List<String[]> read(String filePath) {
		return this.readable.readFile(filePath);
	}
}
