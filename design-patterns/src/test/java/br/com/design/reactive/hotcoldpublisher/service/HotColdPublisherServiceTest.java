package br.com.design.reactive.hotcoldpublisher.service;

import org.junit.jupiter.api.Test;

import br.com.design.reactive.util.Util;

class HotColdPublisherServiceTest {
	
	@Test
	void testingServices() {
		
		OrderService orderService = new OrderService();
		RevenueService revenueService = new RevenueService();
		InventoryService inventoryService = new InventoryService();
		
		
		// revenue and inventory - obserce the order stream
		orderService.orderStream().subscribe(revenueService.subscribeOrderStream());
		orderService.orderStream().subscribe(inventoryService.subscribeOrderStream());
		
		
		inventoryService.inventoryStream()
						.subscribe(Util.subscriber("inventory"));
		
		revenueService.revenueStream()
					  .subscribe(Util.subscriber("revenue"));
		
		Util.sleepSeconds(60);
		
	}
	
}
