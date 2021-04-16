package br.com.design.reactive.batching;

import java.time.Duration;
import java.util.concurrent.atomic.AtomicInteger;

import org.junit.jupiter.api.Test;

import br.com.design.reactive.util.Util;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
class BatchingTest {

	private AtomicInteger atomicInteger = new AtomicInteger();
	
	@Test
	void testingWithBuffer() {

		eventStream()
		.bufferTimeout(5, Duration.ofSeconds(2))
		.subscribe(Util.subscriber());
		
		Util.sleepSeconds(60);
		
	}
	
	@Test
	void testingOverlapAndDrop() {
		eventStream()
		.buffer(3, 2)
		.subscribe(Util.subscriber());
		
		Util.sleepSeconds(60);
	}
	
	@Test
	void testingWindow() {
		eventStream()
		.window(Duration.ofSeconds(2))
		.flatMap(a -> saveEvents(a))
		.subscribe(Util.subscriber());
		
		Util.sleepSeconds(60);
	}
	
	@Test
	void testingGroupBy() {
		
		Flux.range(1, 30)
		.delayElements(Duration.ofSeconds(1))
		.groupBy(i -> i % 2)
		.subscribe(gf -> process(gf, gf.key()));
		
		Util.sleepSeconds(60);
		
	}
	
	private void process(Flux<Integer> flux, int key) {
		log.info("Called");
		flux.subscribe(i -> log.info("Key: {} -> {}", key, i));
	}
	
	private Mono<Integer> saveEvents(Flux<String> flux) {
		return flux.doOnNext(e -> log.info("saving -> {}", e))
				.doOnComplete(() -> {
					log.info("Saved this batch");
					log.info("-----------------");
				}).then(Mono.just(atomicInteger.getAndIncrement()));
	}

	private Flux<String> eventStream() {
		return Flux.interval(Duration.ofMillis(800)).map(i -> "event" + i);
	}

}
