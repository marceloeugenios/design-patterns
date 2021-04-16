package br.com.design.patterns.strategy;

import java.math.BigInteger;
import java.util.stream.Stream;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MainFibonacci {

	public static void main(String[] args) {

		int mySequence = 5000;
//		log.info("Sequence: {} -> Value: {}", mySequence, fibonacci(mySequence));
		fibonacci2(mySequence);

	}

	private static BigInteger fibonacci(Integer num) {
		return Stream.iterate(new BigInteger[] {BigInteger.ZERO, BigInteger.ONE}, 
				t -> new BigInteger[] {t[1], t[0].add(t[1])}).limit(num)
				.map(n -> n[1]).reduce((a, b) -> b)
				.orElse(BigInteger.ZERO);
	}
	
	private static void fibonacci2(Integer num) {
//		return Stream.iterate(new BigInteger[] {BigInteger.ZERO, BigInteger.ONE}, 
//				t -> new BigInteger[] {t[1], t[0].add(t[1])}).limit(num)
//				.map(n -> n[1]).reduce((a, b) -> b)
//				.orElse(BigInteger.ZERO);
		Stream.iterate(new BigInteger[] {BigInteger.ZERO, BigInteger.ONE}, n -> new BigInteger[]{n[1], n[0].add(n[1])}).limit(100).forEach(t -> log.info("Value: {}", t));
		
	}
}
