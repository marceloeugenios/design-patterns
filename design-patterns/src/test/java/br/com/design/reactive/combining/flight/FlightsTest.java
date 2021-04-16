package br.com.design.reactive.combining.flight;

import org.junit.jupiter.api.Test;

import br.com.design.reactive.util.Util;
import reactor.core.publisher.Flux;

class FlightsTest {

	@Test
	void testingFlights() {

		Flux<String> merge = Flux.merge(Qatar.getFlights(), Emirates.getFlights(), AmericanAirlines.getFlights());
		
		merge.subscribe(Util.subscriber());
		
		Util.sleepSeconds(10);

	}

}
