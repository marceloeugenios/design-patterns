package br.com.design.reactive.overflow;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

import org.junit.jupiter.api.Test;

import br.com.design.reactive.util.Util;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

@Slf4j
class OverflowTest {

	@Test
	void testingDemo() {
		
		Flux.create(fluxSink -> {
			IntStream.range(0, 501).forEach(i -> {
				fluxSink.next(i);
				log.info("Pushed -> {}", i);
			});
			fluxSink.complete();
		})
		.publishOn(Schedulers.boundedElastic())
		.doOnNext(i -> {
			Util.sleepMiliseconds(10);
		}).subscribe(Util.subscriber());
		
		Util.sleepSeconds(60);
	}
	
	@Test
	void testingDrop() {
		
		System.setProperty("reactor.bufferSize.small", "16");
		
		List<Object> list = new ArrayList<>();
		
		Flux.create(fluxSink -> {
			IntStream.range(1, 201).forEach(i -> {
				fluxSink.next(i);
				log.info("Pushed -> {}", i);
				Util.sleepMiliseconds(1);
			});
			fluxSink.complete();
		})
		.onBackpressureDrop(list::add)
		.publishOn(Schedulers.boundedElastic())
		.doOnNext(i -> {
			Util.sleepMiliseconds(10);
		}).subscribe(Util.subscriber());
		
		Util.sleepSeconds(10);
		
		log.info("List size -> {}", list.size());
	}
	
	@Test
	void testingLatest() {
		
		System.setProperty("reactor.bufferSize.small", "16");
		
		Flux.create(fluxSink -> {
			IntStream.range(1,201).forEach(i -> {
				fluxSink.next(i);
				log.info("Pushed -> {}", i);
				Util.sleepMiliseconds(1);
			});
			fluxSink.complete();
		})
		.onBackpressureLatest()
		.publishOn(Schedulers.boundedElastic())
		.doOnNext(i -> {
			Util.sleepMiliseconds(10);
		}).subscribe(Util.subscriber());
		Util.sleepSeconds(10);
		
	}
	
	@Test
	void testingError() {
		
		System.setProperty("reactor.bufferSize.small", "16");
		
		Flux.create(fluxSink -> {
			IntStream.range(1, 201).forEach(i -> {
				if (!fluxSink.isCancelled()) {
					fluxSink.next(i);
					log.info("Pushed -> {}", i);
					Util.sleepMiliseconds(1);
				}
			});
			fluxSink.complete();
		}).onBackpressureError()
		.publishOn(Schedulers.boundedElastic())
		.doOnNext(i -> {
			Util.sleepMiliseconds(10);
		}).subscribe(Util.subscriber());
		Util.sleepSeconds(10);
		
	}
	
	@Test
	void testingBufferWithSize() {
		
		System.setProperty("reactor.bufferSize.small", "16");
		
		Flux.create(fluxSink -> {
			IntStream.range(1, 201).forEach(i -> {
				if (!fluxSink.isCancelled()) {
					fluxSink.next(i);
					log.info("Pushed -> {}", i);
					Util.sleepMiliseconds(1);
				}
			});
			fluxSink.complete();
		}).onBackpressureBuffer(50, e -> log.info("Dropped -> {}", e))
		.publishOn(Schedulers.boundedElastic())
		.doOnNext(i -> {
			Util.sleepMiliseconds(10);
		}).subscribe(Util.subscriber());
		Util.sleepSeconds(10);
		
	}
}
