package br.com.design.reactive.combining;

import java.util.ArrayList;
import java.util.List;

import br.com.design.reactive.util.Util;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;

@Slf4j
class NameGenerator {
	
	private List<String> list = new ArrayList<>();
	
	public Flux<String> generateNames() {
		return Flux.generate(sink -> {
			log.info("generated fresh");
			Util.sleepSeconds(1);
			String name = Util.faker().name().firstName();
			list.add(name);
			sink.next(name);
		}).cast(String.class)
		.startWith(getFromCache());
	}
	
	private Flux<String> getFromCache() {
		return Flux.fromIterable(list);
	}
}
