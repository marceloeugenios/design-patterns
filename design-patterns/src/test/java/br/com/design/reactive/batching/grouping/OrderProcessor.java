package br.com.design.reactive.batching.grouping;

import java.util.function.Function;

import reactor.core.publisher.Flux;

class OrderProcessor {

	public static Function<Flux<PurchaseOrder>, Flux<PurchaseOrder>> automotiveProcessing() {
		return flux -> flux.doOnNext(p -> p.setPrice(1.1 * p.getPrice())).doOnNext(p -> p.setItem("{{" + p.getItem() + "}}"));
	}
	
	public static Function<Flux<PurchaseOrder>, Flux<PurchaseOrder>> kidsProcessing() {
		return flux -> flux
				.doOnNext(p -> p.setPrice(.5 * p.getPrice()))
				.flatMap(p -> Flux.just(getFreeKidsOrder()));
	}
	
	private static PurchaseOrder getFreeKidsOrder() {
		PurchaseOrder p = new PurchaseOrder();
		p.setItem("FREE - " + p.getItem());
		p.setPrice(0);
		p.setCategory("Kids");
		return p;
	}
}
