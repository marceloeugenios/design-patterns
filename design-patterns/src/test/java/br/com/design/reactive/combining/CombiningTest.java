package br.com.design.reactive.combining;

import java.time.Duration;

import org.junit.jupiter.api.Test;

import br.com.design.reactive.util.Util;
import reactor.core.publisher.Flux;

class CombiningTest {

	@Test
	void testingStartWith() {
		NameGenerator nameGenerator = new NameGenerator();

		nameGenerator.generateNames().take(2).subscribe(Util.subscriber("sam"));
		nameGenerator.generateNames().take(2).subscribe(Util.subscriber("mike"));
		nameGenerator.generateNames().take(3).subscribe(Util.subscriber("jake"));
		nameGenerator.generateNames().filter(n -> n.startsWith("A")).take(1).subscribe(Util.subscriber("marshal"));
	}
	
	@Test
	void testingConcat() {

		Flux<String> flux1 = Flux.just("a", "b");
		Flux<String> flux2 = Flux.error(new RuntimeException("Ooops"));
		Flux<String> flux3 = Flux.just("c", "d", "e");
		
		Flux<String> flux = Flux.concatDelayError(flux1, flux2, flux3);
		
		flux.subscribe(Util.subscriber());
		
	}
	
	@Test
	void testingZip() {
		
		Flux.zip(getBody(), getTires(), getEngine())
			.subscribe(Util.subscriber());
		
	}
	
	@Test
	void testingCombineLatest() {
		
		Flux.combineLatest(getString(), getNumber(), (string, number) -> string + number).subscribe(Util.subscriber());
		Util.sleepSeconds(10);
		
	}
	
	private Flux<String> getString() {
		return Flux.just("A", "B", "C", "D")
				.delayElements(Duration.ofSeconds(1));
	}
	
	private Flux<Integer> getNumber() {
		return Flux.just(1, 2, 3)
				.delayElements(Duration.ofSeconds(3));
	}
	
	private Flux<String> getBody() {
		return Flux.range(1, 5)
				.map(i -> "body " + i);
	}
	
	private Flux<String> getEngine() {
		return Flux.range(1, 2)
				.map(i -> "engine " + i);
	}
	
	private Flux<String> getTires() {
		return Flux.range(1, 6)
				.map(i -> "tires " + i);
	}
}
