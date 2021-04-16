package br.com.design.reactive.operators.flatmap;

import java.time.Duration;
import java.util.function.Function;
import java.util.stream.IntStream;

import org.junit.jupiter.api.Test;

import br.com.design.reactive.operators.util.Person;
import br.com.design.reactive.util.Util;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
class OperatorsTest {

	@Test
	void testingHandle() {

		Flux.range(1, 20).handle((integer, synchronousSink) -> {

			if (integer == 7) {
				synchronousSink.complete();
			} else {
				if (integer % 2 == 0)
					synchronousSink.next(integer); // filter
				else
					synchronousSink.next(integer + "a"); // map
			}
		}).subscribe(Util.subscriber());
	}

	@Test
	void testingGenerate() {
		Flux.generate(synchronousSink -> synchronousSink.next(Util.faker().country().name())).map(Object::toString)
				.handle((string, synchronousSink) -> {
					synchronousSink.next(string);
					if (string.toLowerCase().equals("canada")) {
						synchronousSink.complete();
					}
				}).subscribe(Util.subscriber());
	}

	@Test
	void testingDoCallbacks() {
		Flux.create(fluxSink -> {
			log.info("Inside create");
			IntStream.range(0, 5).forEach(i -> {
				fluxSink.next(i);
			});
			fluxSink.complete();
			log.info("Completed");
		}).doOnComplete(() -> System.out.println("doOnComplete")).doFirst(() -> System.out.println("doFirst"))
				.doOnNext(o -> System.out.println("doOnNext: " + o))
				.doOnSubscribe(s -> System.out.println("doOnSubscribe: " + s))
				.doOnRequest(l -> System.out.println("doOnRequest: " + l))
				.doOnError(err -> System.out.println("doOnError: " + err.getMessage()))
				.doOnTerminate(() -> System.out.println("doOnTerminate"))
				.doOnCancel(() -> System.out.println("doOnCancel"))
				.doFinally(signal -> System.out.println("doFinally 1: " + signal))
				.doOnDiscard(Object.class, o -> System.out.println("doOnDiscard: " + o)).take(2)
				.doFinally(signal -> System.out.println("doFinally 2: " + signal)).subscribe(Util.onNext());
	}

	@Test
	void testingLimitRate() {
		Flux.range(1, 1000)
			.log()
			.limitRate(100, 0) // 75%
			.subscribe(Util.subscriber());
	}
	
	@Test
	void testingDelay() {
		Flux.range(1, 100)
		.log()
		.delayElements(Duration.ofSeconds(1))
		.subscribe(Util.subscriber());
		
		Util.sleepSeconds(32);
	}

	@Test
	void testingOnError() {
		Flux.range(1, 10)
		.log()
		.map(i -> 10 / (5 - i))
//		.onErrorReturn(-1)
//		.onErrorResume(e -> fallback())
		.onErrorContinue((err, obj) -> {
			
		})
		.subscribe(Util.subscriber());
	}
	
	@Test
	void testingTimeout() {
		
		getOrderNumbers()
		.timeout(Duration.ofSeconds(2), fallbackTimeout())
		.subscribe(Util.subscriber());
		
		Util.sleepSeconds(60);
	}
	
	@Test
	void testingDefaultIfEmpty() {
		getOrderNumbers()
		.filter(i -> i > 10)
		.defaultIfEmpty(-100)
		.subscribe(Util.subscriber());
	}
	
	@Test
	void testingSwitchIfEmpty() {
		getOrderNumbers()
		.filter(i -> i > 10)
		.switchIfEmpty(fallback())
		.subscribe(Util.subscriber());
	}

	/**
	 * Used when you have multiple transform used across the pipelines - Avoid duplication
	 */
	@Test
	void testingTransform() {
		getPerson()
		.transform(applyFilterMap())
		.subscribe(Util.subscriber());
	}
	
	@Test
	void testingSwitchOnFirst() {
		getPerson()
		.switchOnFirst((signal, personFlux) -> {
			log.info("inside switch-on-first");
			return signal.isOnNext() && signal.get().getAge() > 10 ? personFlux : applyFilterMap().apply(personFlux);
		})
		.subscribe(Util.subscriber());
	}
	
	@Test
	void testingFlatMap() {
		UserService.getUsers()
		.flatMap(user -> OrderService.getOrders(user.getUserId()))
		.subscribe(Util.subscriber());
		
		Util.sleepSeconds(60);
	}
	
	@Test
	void testingConcatMap() {
		
	}
	
	private Flux<Person> getPerson() {
		return Flux.range(1, 10)
				.map(i -> new Person());
	}
	
	private Function<Flux<Person>, Flux<Person>> applyFilterMap() {
		return flux -> flux.filter(p -> p.getAge() > 10)
						   .doOnNext(p -> p.setName(p.getName().toUpperCase()))
						   .doOnDiscard(Person.class, p -> System.out.println("Not Allowing: " + p));
	}
	
	private Flux<Integer> getOrderNumbers() {
		return Flux.range(1, 10);
	}
	
	private Flux<Integer> fallbackTimeout() {
		return Flux.range(100, 10)
				   .delayElements(Duration.ofMillis(200));
	}
	
	private Mono<Integer> fallback() {
		return Mono.fromSupplier(() -> Util.faker().random().nextInt(100, 200));
	}
	
}