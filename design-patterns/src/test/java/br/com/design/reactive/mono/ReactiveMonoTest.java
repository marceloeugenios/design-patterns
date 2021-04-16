package br.com.design.reactive.mono;

import static br.com.design.reactive.util.Util.onComplete;
import static br.com.design.reactive.util.Util.onError;
import static br.com.design.reactive.util.Util.onNext;

import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

import org.junit.jupiter.api.Test;

import br.com.design.reactive.util.Util;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@Slf4j
class ReactiveMonoTest {

	/**
	 * Just: MUST be used ONLY when there is no computation - Data is ready to be
	 * used!
	 * 
	 * If you need to calculate something we MUST use SUPPLIER/CONSUMER instead of
	 * JUST
	 * 
	 */

	@Test
	void testingJust() {
		Mono<Integer> mono1 = Mono.just("My Fist Mono").map(String::length).map(l -> l / 1);

		mono1.subscribe(onNext(), onError(), onComplete());
	}

	@Test
	void testingEmptyOrError() {
		userRepository(1).subscribe(onNext(), onError(), onComplete());
		userRepository(2).subscribe(onNext(), onError(), onComplete());
		userRepository(3).subscribe(onNext(), onError(), onComplete());
	}

	@Test
	void testingMonoSupplierAndConsumer() {
		// Bad way to use just method
		@SuppressWarnings("unused")
		Mono<String> badUseOfJust = Mono.just(getName());

		Supplier<String> stringSupplier = () -> getName();
		Mono<String> monoWithSupplier = Mono.fromSupplier(stringSupplier);

		monoWithSupplier.subscribe(onNext());

		Callable<String> stringCallable = () -> getName();

		Mono.fromCallable(stringCallable).subscribe(onNext());

	}

	@Test
	void testingSupplierRefactoring() {
		getNamePublisher();
		getNamePublisher().subscribeOn(Schedulers.boundedElastic()).subscribe(onNext());
		String blockName = getNamePublisher().block();
		log.info("Block Name: {}", blockName);

		Util.sleepSeconds(4);
	}

	@Test
	void testingMonoFromFuture() {
		Mono.fromFuture(getNameFuture()).subscribe(onNext());
		Util.sleepSeconds(1);
	}

	@Test
	void testingMonoFromRunnable() {
		Mono.fromRunnable(timeConsumingProcess())
		.subscribe(onNext(), 
				onError(), () -> {
					log.info("Process is done. Sending emails");
				});

	}
	
	private static Runnable timeConsumingProcess() {
		return () -> {
			Util.sleepSeconds(3);
			log.info("Some batch process is completed");
		};
	}

	private static CompletableFuture<String> getNameFuture() {
		return CompletableFuture.supplyAsync(() -> Util.faker().name().fullName());
	}

	private static Mono<String> getNamePublisher() {
		log.info("Calling getNamePublisher method...");
		return Mono.fromSupplier(() -> {
			// Only when somebody subscribes to this it will be fired
			log.info("Getting the name publisher...");
			Util.sleepSeconds(3);
			return Util.faker().name().fullName();
		}).map(String::toUpperCase);
	}

	private static String getName() {
		log.info("Getting the name...");
		return Util.faker().name().fullName();
	}

	private static Mono<String> userRepository(int userId) {
		if (userId == 1) {
			return Mono.just(Util.faker().name().firstName());
		} else if (userId == 2) {
			return Mono.empty();
		} else {
			return Mono.error(new RuntimeException("Not in the allowed range"));
		}
	}
}
