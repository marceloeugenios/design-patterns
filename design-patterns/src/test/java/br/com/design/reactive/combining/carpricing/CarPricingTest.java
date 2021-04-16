package br.com.design.reactive.combining.carpricing;

import java.time.Duration;

import org.junit.jupiter.api.Test;

import br.com.design.reactive.util.Util;
import reactor.core.publisher.Flux;

class CarPricingTest {

	@Test
	void testingCarPricing() {
		
		final int carPrice = 10000;
		
		Flux.combineLatest(monthStream(), demandStream(), (month, demand) -> {
			return (carPrice - (month * 100)) * demand;
		}).subscribe(Util.subscriber());
		
		Util.sleepSeconds(10);
	}

	private Flux<Long> monthStream() {
		return Flux.interval(Duration.ZERO, Duration.ofSeconds(1));
	}
	
	private Flux<Double> demandStream() {
		return Flux.interval(Duration.ofSeconds(3))
				.map(i -> Util.faker().random().nextInt(80, 120) / 100d)
				.startWith(1d);
	}
	
}
