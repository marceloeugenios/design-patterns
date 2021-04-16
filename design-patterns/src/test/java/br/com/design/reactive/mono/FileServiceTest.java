package br.com.design.reactive.mono;

import static br.com.design.reactive.util.Util.onComplete;
import static br.com.design.reactive.util.Util.onError;
import static br.com.design.reactive.util.Util.onNext;

import org.junit.jupiter.api.Test;

class FileServiceTest {

	@Test
	void loadingFileService() {

		String fileName = "file03.txt";
		FileUtil.read(fileName).subscribe(onNext(), onError(), onComplete());
		
		FileUtil.delete(fileName).subscribe(onNext(), onError(), onComplete());

//		FileUtil.write("file03.txt", "This is file3")
//		.subscribe(onNext(), onError(), onComplete());
		
	}

}
