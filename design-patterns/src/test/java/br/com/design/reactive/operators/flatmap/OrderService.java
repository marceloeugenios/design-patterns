package br.com.design.reactive.operators.flatmap;

import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxSink;

class OrderService {

	private static Map<Integer, List<PurchaseOrder>> db = new HashMap<>();

	static {
		db.put(1, List.of(new PurchaseOrder(1), new PurchaseOrder(1), new PurchaseOrder(1)));
		db.put(2, List.of(new PurchaseOrder(2), new PurchaseOrder(2)));
	}

	public static Flux<PurchaseOrder> getOrders(int userId) {
		return Flux.create((FluxSink<PurchaseOrder> purchaseOrderFluxSink) -> {
			if (db.containsKey(userId)) {
				db.get(userId).forEach(purchaseOrderFluxSink::next);
			}
			purchaseOrderFluxSink.complete();
		}).delayElements(Duration.ofSeconds(2));
	}
}
