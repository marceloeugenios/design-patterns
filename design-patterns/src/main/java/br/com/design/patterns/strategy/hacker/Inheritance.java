package br.com.design.patterns.strategy.hacker;

import java.util.stream.IntStream;

public class Inheritance {

	interface AdvancedArithmetic {
		int divisor_sum(int n);
	}

	class MyCalculator implements AdvancedArithmetic {

		@Override
		public int divisor_sum(int n) {
			return IntStream.range(1, n + 1).filter(i -> n % i == 0)
					.reduce(0, (current, num) -> current + num);
		}
	}

	public static void main(String[] args) {
		Inheritance in = new Inheritance();

		Inheritance.MyCalculator myCalculator = in.new MyCalculator();

		System.out.println(myCalculator.divisor_sum(6));
	}

}
