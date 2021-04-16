package br.com.design.reactive.batching.book;

import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;

import br.com.design.reactive.util.Util;
import reactor.core.publisher.Flux;

class BookTest {

	@Test
	void testingBook() {

		Set<String> allowedCategories = Set.of("Science fiction", "Fantasy", "Suspense/Thriller");

		bookStream().filter(book -> allowedCategories.contains(book.getCategory()))
		.buffer(Duration.ofSeconds(5))
		.map(list -> revenueCalculator(list))
		.subscribe(Util.subscriber());
		
		Util.sleepSeconds(60);

	}
	
	private RevenueReport revenueCalculator(List<BookOrder> books) {
		Map<String, Double> map = books.stream().collect(
				Collectors.groupingBy(BookOrder::getCategory, 
									  Collectors.summingDouble(BookOrder::getPrice)));
		return new RevenueReport(map);
	}

	private Flux<BookOrder> bookStream() {
		return Flux.interval(Duration.ofMillis(200)).map(i -> new BookOrder());
	}
}
