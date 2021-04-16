package br.com.design.reactive.hotcoldpublisher;

import java.time.Duration;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;

import br.com.design.reactive.util.Util;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;

@Slf4j
class HotColdPublisherTest {

	@Test
	void testingColdPublisher() {
		Flux<String> movieStream = Flux.fromStream(() -> getNetflix()).delayElements(Duration.ofSeconds(2));

		movieStream.subscribe(Util.subscriber("sam"));

		Util.sleepSeconds(5);

		movieStream.subscribe(Util.subscriber("mike"));

		Util.sleepSeconds(60);

	}

	@Test
	void testingHotShare() {
		Flux<String> movieStream = Flux.fromStream(() -> getMovieTheatre()).delayElements(Duration.ofSeconds(2))
				.share();

		movieStream.subscribe(Util.subscriber("sam"));

		Util.sleepSeconds(5);

		movieStream.subscribe(Util.subscriber("mike"));

		Util.sleepSeconds(60);
	}

	@Test
	void testingHotPublisher() {
		Flux<String> movieStream = Flux.fromStream(() -> getMovieTheatre()).delayElements(Duration.ofSeconds(1))
				.publish().refCount(1);

		movieStream.subscribe(Util.subscriber("sam"));

		Util.sleepSeconds(10);

		movieStream.subscribe(Util.subscriber("mike"));

		Util.sleepSeconds(60);
	}
	
	@Test
	void testingHotPublisherAutoConnect() {
		Flux<String> movieStream = Flux.fromStream(() -> getMovieTheatre()).delayElements(Duration.ofSeconds(1))
				.publish().autoConnect(0);

		Util.sleepSeconds(3);
		
		movieStream.subscribe(Util.subscriber("sam"));

		Util.sleepSeconds(10);
		
		log.info("Mike is about to join");

		movieStream.subscribe(Util.subscriber("mike"));

		Util.sleepSeconds(60);
	}
	
	@Test
	void testingHotPublisherCache() {
		Flux<String> movieStream = Flux.fromStream(() -> getMovieTheatre()).delayElements(Duration.ofSeconds(1))
				.cache(); // replay with history

		Util.sleepSeconds(2);
		
		movieStream.subscribe(Util.subscriber("sam"));

		Util.sleepSeconds(10);
		
		log.info("Mike is about to join");

		movieStream.subscribe(Util.subscriber("mike"));

		Util.sleepSeconds(60);
	}

	private Stream<String> getNetflix() {
		log.info("Got the netflix streaming req");
		return Stream.of("Scene 1", "Scene 2", "Scene 3", "Scene 4", "Scene 5", "Scene 6", "Scene 7");
	}

	private Stream<String> getMovieTheatre() {
		log.info("Got the movie theatre streaming req");
		return Stream.of("Scene 1", "Scene 2", "Scene 3", "Scene 4", "Scene 5", "Scene 6", "Scene 7");
	}

}
