package br.com.design.reactive.batching.grouping;

import java.time.Duration;

import reactor.core.publisher.Flux;

class OrderService {

	public static Flux<PurchaseOrder> orderStream() {
		return Flux.interval(Duration.ofMillis(100))
				.map(i -> new PurchaseOrder());
	}
}
