package br.com.design.reactive.complex;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.jupiter.api.Test;

import br.com.design.reactive.util.Util;

public class FileReaderServiceTest {

	@Test
	void testingFileReadService() {
		FileUtil util = new FileUtil();

		Path path = Paths.get("src/test/resources/files/file01-Complex.txt");
		util.read(path)
			.map(s -> {
				Integer nextInt = Util.faker().random().nextInt(0, 10);
				if (nextInt > 8) {
					throw new RuntimeException("Ooops");
				}
				return s;
			}).take(20)
			.subscribe(Util.subscriber());
	}

}
