package br.com.design.reactive.repeatretry;

import java.time.Duration;
import java.util.concurrent.atomic.AtomicInteger;

import org.junit.jupiter.api.Test;

import com.github.javafaker.Faker;

import br.com.design.reactive.util.Util;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

@Slf4j
class RepeatRetryTest {
	
	private AtomicInteger atomicInteger = new AtomicInteger(1);

	@Test
	void testingRepeat() {

		getIntegers()
		.repeat(() -> Faker.instance().random().nextBoolean())
		.subscribe(Util.subscriber());
		
	}
	
	@Test
	void testingRetry() {
		getIntegers()
		.retry(2)
		.subscribe(Util.subscriber());
	}
	
	@Test
	void testingRetryWhen() {
		getIntegers()
		.retryWhen(Retry.fixedDelay(2, Duration.ofSeconds(3)))
		.subscribe(Util.subscriber());
		Util.sleepSeconds(60);
	}
	
	@Test
	void testingRetryAdvanced() {
		orderService(Util.faker().business().creditCardNumber())
		.retryWhen(Retry.from(flux -> flux.doOnNext(rs -> {
			log.info("{} -> {}", rs.totalRetries(), rs.failure());
		}).handle((rs, synchronousSink) -> {
			if (rs.failure().getMessage().equals("500")) {
				synchronousSink.next(1);
			} else {
				synchronousSink.error(rs.failure());
			}
		}).delayElements(Duration.ofSeconds(1)))).subscribe(Util.subscriber());
		
		Util.sleepSeconds(60);
	}
	
	private Mono<String> orderService(String ccNumber) {
		return Mono.fromSupplier(() -> {
			processPayment(ccNumber);
			return Util.faker().idNumber().valid();
		});
	}
	
	private void processPayment(String ccNumber) {
		int random = Util.faker().random().nextInt(1, 10);
		if (random < 8) {
			throw new RuntimeException("500");
		} else if (random < 10) {
			throw new RuntimeException("404");
		}
	}
	
	private Flux<Integer> getIntegers() {
		return Flux.range(1,  3)
				.doOnSubscribe(s -> log.info("Subscribed"))
				.doOnComplete(() -> log.info("--Completed"))
				.map(i -> atomicInteger.getAndIncrement())
				.map(i -> i / (Util.faker().random().nextInt(1, 5) > 3 ? 0 : 1))
				.doOnError(err -> log.error("Error -> {}", err.getMessage()));
	}
}
