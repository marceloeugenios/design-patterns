package br.com.design.patterns.strategy;

import br.com.design.patterns.strategy.behavior.IReadable;
import br.com.design.patterns.strategy.impl.CsvReader;
import br.com.design.patterns.strategy.impl.ExcelReader;
import br.com.design.patterns.strategy.impl.TextReader;
import br.com.design.patterns.strategy.impl.XmlReader;

public class Main {

	public static void main(String[] args) {

		IReadable readable = new ExcelReader();
		FileReader r = new FileReader(readable);
		new DataPersist(r.read("/opt/my-file.xls")).printData();

		readable = new TextReader();
		r = new FileReader(readable);
		new DataPersist(r.read("/opt/my-file.txt")).printData();

		readable = new CsvReader();
		r = new FileReader(readable);
		new DataPersist(r.read("/opt/my-file.csv")).printData();
		
		readable = new XmlReader();
		r = new FileReader(readable);
		new DataPersist(r.read("/opt/my-file.xml")).printData();

	}
}
