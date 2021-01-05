package br.com.design.patterns.strategy;

import br.com.design.patterns.strategy.behavior.IReadable;
import br.com.design.patterns.strategy.impl.CsvReader;
import br.com.design.patterns.strategy.impl.ExcelReader;
import br.com.design.patterns.strategy.impl.TextReader;
import br.com.design.patterns.strategy.impl.XmlReader;

public class Main {

	/**
	 * O foco principal deste pattern é permitir uma variação de implementação baseado na classe concreta que implementa a interface comum.
	 */
	
	public static void main(String[] args) {

		IReadable readable      = new ExcelReader();
		DataPersist dataPersist = new DataPersist(new FileReader(readable).read("/opt/my-file.xls"));
		dataPersist.printData();

		readable    = new TextReader();
		dataPersist = new DataPersist(new FileReader(readable).read("/opt/my-file.txt"));
		dataPersist.printData();

		readable    = new CsvReader();
		dataPersist = new DataPersist(new FileReader(readable).read("/opt/my-file.csv"));
		dataPersist.printData();

		readable    = new XmlReader();
		dataPersist = new DataPersist(new FileReader(readable).read("/opt/my-file.xml"));
		dataPersist.printData();

	}
}
