package br.com.design.reactive.batching.grouping;

import java.util.Map;
import java.util.Set;
import java.util.function.Function;

import org.junit.jupiter.api.Test;

import br.com.design.reactive.util.Util;
import reactor.core.publisher.Flux;

class GroupingTest {

	@Test
	void testingGroup() {

		Map<String, Function<Flux<PurchaseOrder>, Flux<PurchaseOrder>>> map = Map.of("Kids",
				OrderProcessor.kidsProcessing(), "Automotive", OrderProcessor.automotiveProcessing());

		Set<String> set = map.keySet();

		OrderService.orderStream().filter(p -> set.contains(p.getCategory())).groupBy(PurchaseOrder::getCategory)
		.flatMap(gf -> map.get(gf.key()).apply(gf)).subscribe(Util.subscriber());
		
		Util.sleepSeconds(60);

	}
}
