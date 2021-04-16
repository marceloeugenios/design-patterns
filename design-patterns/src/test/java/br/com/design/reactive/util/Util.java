package br.com.design.reactive.util;

import java.util.function.Consumer;

import org.reactivestreams.Subscriber;

import com.github.javafaker.Faker;

import br.com.design.reactive.complex.DefaultSubscriber;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Util {

	private static Faker FAKER = Faker.instance();

	public static Consumer<Object> onNext() {
		return o -> log.info("Received: {}", o);
	}

	public static Consumer<Throwable> onError() {
		return e -> log.error("ERROR: {}", e.getMessage());
	}

	public static Runnable onComplete() {
		return () -> log.info("Completed");
	}

	public static Faker faker() {
		return FAKER;
	}
	
	public static void sleepSeconds(int seconds) {
		sleepMiliseconds(seconds * 1000);
	}
	
	public static void sleepMiliseconds(int miliseconds) {
		try {
			Thread.sleep(miliseconds);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static Subscriber<Object> subscriber() {
		return new DefaultSubscriber();
	}
	
	public static Subscriber<Object> subscriber(String name) {
		return new DefaultSubscriber(name);
	}
}
