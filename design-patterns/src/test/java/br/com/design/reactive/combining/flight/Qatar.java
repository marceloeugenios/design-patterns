package br.com.design.reactive.combining.flight;

import java.time.Duration;

import br.com.design.reactive.util.Util;
import reactor.core.publisher.Flux;

class Qatar {

	public static Flux<String> getFlights() {
		return Flux.range(1, Util.faker().random().nextInt(1, 5))
				.delayElements(Duration.ofSeconds(1))
				.map(i -> "Qatar " + Util.faker().random().nextInt(100, 999))
				.filter(i -> Util.faker().random().nextBoolean());
	}
}
