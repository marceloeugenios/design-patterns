package br.com.design.reactive.threads;

import java.time.Duration;
import java.util.stream.IntStream;

import org.junit.jupiter.api.Test;

import br.com.design.reactive.util.Util;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

@Slf4j
class ThreadSchedulersDemoTest {

	@Test
	void testingThreadDefault() {
		
		Flux<Object> flux = Flux.create(fluxSink -> {
			printThreadName("create");
			fluxSink.next(1);
		})
		.doOnNext(i -> printThreadName("next " + i));
		
		Runnable runnable = () -> flux.subscribe(v -> printThreadName("sub " + v));
		
		IntStream.range(1, 3).forEach(i -> {
			new Thread(runnable).start();
		});
		Util.sleepSeconds(5);
	}
	
	@Test
	void testingSubscribeOn() {
		Flux<Object> flux = Flux.create(fluxSink -> {
			printThreadName("create");
			fluxSink.next(1);
		})
		.subscribeOn(Schedulers.newParallel("vins"))
		.doOnNext(i -> printThreadName("next " + i));
		
		Runnable runnable = () -> flux
		.doFirst(() -> printThreadName("first2"))
		.subscribeOn(Schedulers.boundedElastic())
		.doFirst(() -> printThreadName("first1"))
		.subscribe(v -> printThreadName("sub " + v));
		
		IntStream.range(1, 3).forEach(i -> {
			new Thread(runnable).start();
		});
		
		Util.sleepSeconds(5);
	}
	
	@Test
	void testingSubscriberOnMultipleItems() {
		Flux<Object> flux = Flux.create(fluxSink -> {
			printThreadName("create");
			IntStream.range(1, 4).forEach(i -> {
				fluxSink.next(i);
				Util.sleepSeconds(1);
			});
			fluxSink.complete();
			
		}).doOnNext(i -> printThreadName("next " + i));
		
		flux.subscribeOn(Schedulers.boundedElastic())
			.subscribe(v -> printThreadName("sub " + v));
		
		flux.subscribeOn(Schedulers.parallel())
		.subscribe(v -> printThreadName("sub " + v));
		
		
		Util.sleepSeconds(5);
	}
	
	@Test
	void testingPublisherOn() {
		Flux<Object> flux = Flux.create(fluxSink -> {
			printThreadName("create");
			IntStream.range(0, 4).forEach(i -> {
				fluxSink.next(i);
			});
			fluxSink.complete();
			
		}).doOnNext(i -> printThreadName("next " + i));
		
		flux.publishOn(Schedulers.boundedElastic())
			.doOnNext(i -> printThreadName("next " + i))
			.publishOn(Schedulers.parallel())
			.subscribe(v -> printThreadName("sub " + v));
		
		
		Util.sleepSeconds(5);
	}
	
	@Test
	void testingPublisherSubscriberOn() {
		Flux<Object> flux = Flux.create(fluxSink -> {
			printThreadName("create");
			IntStream.range(0, 4).forEach(i -> {
				fluxSink.next(i);
			});
			fluxSink.complete();
			
		}).doOnNext(i -> printThreadName("next " + i));
		
		flux.publishOn(Schedulers.parallel()) // used for downstream - from here to down
			.doOnNext(i -> printThreadName("next " + i))
			.subscribeOn(Schedulers.boundedElastic()) // used for upstream
			.subscribe(v -> printThreadName("sub " + v));
		
		
		Util.sleepSeconds(5);
	}
	
	@Test
	void testingParalel() {
		
		Flux.range(1, 10)
			.parallel(10)
			.runOn(Schedulers.boundedElastic())
			.doOnNext(i -> printThreadName("next " + i))
			.subscribe(v -> printThreadName("sub " + v));
		
		
		Util.sleepSeconds(5);
	}
	
	@Test
	void testingFluxInterval() {
		Flux.interval(Duration.ofSeconds(1))
		.subscribeOn(Schedulers.boundedElastic())
		.subscribe(Util.subscriber());
		
		Util.sleepSeconds(60);
	}
	
	
	private void printThreadName(String msg) {
		log.info("{} -> {}", msg, Thread.currentThread().getName());
	}
}
