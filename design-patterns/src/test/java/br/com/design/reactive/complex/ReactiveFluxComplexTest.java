package br.com.design.reactive.complex;

import java.util.stream.IntStream;

import org.junit.jupiter.api.Test;

import br.com.design.reactive.util.Util;
import reactor.core.publisher.Flux;

class ReactiveFluxComplexTest {

	@Test
	void testingFluxCreate() {
		Flux.create(fluxSink -> {
			String country;
			do {
				country = Util.faker().country().name();
				fluxSink.next(country);
			} while (!country.toLowerCase().equals("canada"));
			fluxSink.complete();

		}).subscribe(Util.subscriber());
	}

	@Test
	void testingFluxCreateRefactoring() {
		NameProducer nameProducer = new NameProducer();
		Flux.create(nameProducer).subscribe(Util.subscriber());
		Runnable runnable = () -> nameProducer.produce();

		IntStream.range(0, 10).forEach(i -> {
			new Thread(runnable).start();
		});

		Util.sleepSeconds(2);
	}

	@Test
	void testingFluxTake() {

		Flux.range(1, 10).log().take(3).log().subscribe(Util.subscriber());

	}

	@Test
	void testingFluxCreateIssueFix() {
		Flux.create(fluxSink -> {
			String country;
			do {
				country = Util.faker().country().name();
				System.out.println("emitting: " + country);
				fluxSink.next(country);
			} while (!country.toLowerCase().equals("canada") && !fluxSink.isCancelled());
			fluxSink.complete();

		}).take(3).subscribe(Util.subscriber());
	}

	@Test
	void testingFluxGenerate() {

		Flux.generate(synchronousSink -> {
			System.out.println("Emitting");
			synchronousSink.next(Util.faker().country().name());
		}).take(2).subscribe(Util.subscriber());
	}

	@Test
	void testingFluxGenerateUtil() {
		Flux.generate(synchronousSink -> {
			String country = Util.faker().country().name();
			System.out.println("Emitting: " + country);
			synchronousSink.next(country);
			if (country.toLowerCase().equals("canada")) {
				synchronousSink.complete();
			}
		}).subscribe(Util.subscriber());
	}

	@Test
	void testingFluxGenerateCounter() {
		Flux.generate(() -> 1, (counter, sink) -> {
			String country = Util.faker().country().name();
			sink.next(country);
			if (counter >= 10 || country.toLowerCase().equals("canada")) {
				sink.complete();
			}
			return counter + 1;
		}).take(4).subscribe(Util.subscriber());
	}
	
	/**
	 * Single thread
	 */
	@Test
	void testingFluxPush() {
		NameProducer nameProducer = new NameProducer();
		Flux.push(nameProducer).subscribe(Util.subscriber());
		Runnable runnable = () -> nameProducer.produce();

		IntStream.range(0, 10).forEach(i -> {
			new Thread(runnable).start();
		});

		Util.sleepSeconds(2);
	}
}
