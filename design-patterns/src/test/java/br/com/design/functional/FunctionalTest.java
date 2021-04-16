package br.com.design.functional;

import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

import org.junit.jupiter.api.Test;

class FunctionalTest {

	@Test
	void testingConsumer() {
		Consumer<String> consumer = (t) -> System.out.println(t);
		consumer.accept("Hello my consumer");
	}
	
	@Test
	void testingSupplier() {
		Supplier<String> supplier = () -> "Hello my supplier"; 
		System.out.println(supplier.get());
	}
	
	@Test
	void testingFunction() {
		Function<String, Integer> supplier = (t) -> t.length(); 
		System.out.println(supplier.apply("My Function test"));
		
	}
	
	@Test
	void testingBiFunction() {
		BiFunction<Integer, Integer, Integer> biFunction = (a, b) -> a + b;
//		System.out.println(doCalc(biFunction));
	}
	
	public Integer doCalc(Function<Integer, Integer> func) {
		return func.apply(10);
	}
}
