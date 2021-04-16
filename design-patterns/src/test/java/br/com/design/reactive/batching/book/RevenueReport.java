package br.com.design.reactive.batching.book;

import java.time.LocalDateTime;
import java.util.Map;

import lombok.ToString;

@ToString
class RevenueReport {

	private LocalDateTime localDateTime = LocalDateTime.now();
	private Map<String, Double> revenue;
	
	public RevenueReport(Map<String, Double> revenue) {
		this.revenue = revenue;
	}
}
