package br.com.design.reactive.flux;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import br.com.design.reactive.util.Util;
import reactor.core.publisher.Flux;

class NameGenerator {
	
	public static List<String> getNames(int count) {
		return IntStream.range(0, count)
				 .mapToObj(i -> getName())
				 .collect(Collectors.toList());
	}
	
	public static Flux<String> getFluxNames(int count) {
		return Flux.range(0, count)
				   .map(i -> getName());
	}
	
	
	private static String getName() {
		Util.sleepSeconds(1);
		return Util.faker().name().fullName();
	}
}
