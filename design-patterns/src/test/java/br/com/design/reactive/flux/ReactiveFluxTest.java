package br.com.design.reactive.flux;

import static br.com.design.reactive.util.Util.onComplete;
import static br.com.design.reactive.util.Util.onError;
import static br.com.design.reactive.util.Util.onNext;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import org.junit.jupiter.api.Test;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import br.com.design.reactive.util.Util;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
class ReactiveFluxTest {

	@Test
	void testingJust() {

		Flux<Object> flux = Flux.just(1, 2, 3, "a", Util.faker().name().fullName());

		flux.subscribe(onNext(), onError(), onComplete());
	}

	@Test
	void testingMultipleSubscribers() {

		Flux<Integer> flux = Flux.just(1, 2, 3, 4);

		Flux<Integer> evenFlux = flux.filter(i -> i % 2 == 0);

		flux.subscribe(i -> log.info("Sub1: {}", i));

		evenFlux.subscribe(i -> log.info("Sub2: {}", i));
	}

	@Test
	void testingFromArrayOrList() {

		List<String> strings = Arrays.asList("a", "b", "c");

		Integer[] array = { 2, 5, 7, 8 };

		Flux.fromIterable(strings).subscribe(onNext());

		Flux.fromArray(array).subscribe(onNext());
	}

	@Test
	void testingFromStream() {

		List<Integer> list = List.of(1, 2, 3, 4, 5);

		Flux<Integer> integerFlux = Flux.fromStream(() -> list.stream());

		integerFlux.subscribe(onNext(), onError(), onComplete());
		integerFlux.subscribe(onNext(), onError(), onComplete());

	}

	@Test
	void testingRange() {
		Flux.range(3, 10).log().map(i -> Util.faker().name().fullName()).log().subscribe(onNext());
	}

	@Test
	void testingSubscription() {
		
		AtomicReference<Subscription> atomicReference = new AtomicReference<>();
		
		Flux.range(1, 20)
			.log()
			.subscribeWith(new Subscriber<Integer>() {

			@Override
			public void onSubscribe(Subscription subscription) {
				System.out.println("Received Sub: " + subscription);
				atomicReference.set(subscription);
			}

			@Override
			public void onNext(Integer integer) {
				System.out.println("onNext: " + integer);
			}

			@Override
			public void onError(Throwable throwable) {
				System.out.println("onError: " + throwable.getMessage());
			}

			@Override
			public void onComplete() {
				System.out.println("onComplete");
			}
		});
		
		Util.sleepSeconds(3);
		atomicReference.get().request(3);
		
		Util.sleepSeconds(5);
		atomicReference.get().request(3);
		
		Util.sleepSeconds(5);
		log.info("Subscription going to get canceled");
		atomicReference.get().cancel();
		
		Util.sleepSeconds(3);
		atomicReference.get().request(4);
		Util.sleepSeconds(3);
	}

	@Test
	void testingFluxVsList() {
		
		List<String> names = NameGenerator.getNames(5);
		names.forEach(System.out::println);
		
		NameGenerator.getFluxNames(5)
					 .subscribe(onNext());
	}
	
	@Test
	void testingInterval() {
		
		Flux.interval(Duration.ofSeconds(1))
			.subscribe(onNext());
		
		Util.sleepSeconds(5);
	}
	
	@Test
	void testingFluxFromMono() {
		Mono<String> mono = Mono.just("a");
		Flux<String> flux = Flux.from(mono);
		
		flux.subscribe(onNext());
	}

	@Test
	void testingFluxToMono() {
		
		Flux.range(1, 10)
			.filter(i -> i > 3)
			.next()
			.subscribe(onNext(), onError(), onComplete());
	}
	
}
