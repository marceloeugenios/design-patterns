package br.com.design.patterns;

import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.assertj.core.util.Arrays;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import lombok.AllArgsConstructor;
import lombok.Getter;
import reactor.core.publisher.Flux;

//@SpringBootTest
class DesignPatternsApplicationTests {

	private List<Person> people = List.of(new Person("MARK", 10, List.of(new Task("Task 1"), new Task("Task 2"))),
			new Person("JOHN", 2, List.of(new Task("Task 1"))), new Person("JIM", 1, List.of(new Task("Task 2"))),
			new Person("MARY", 15, List.of(new Task("Task 3"))));

	@Test
	void pessoaMaisVelha() {
		/**
		 * Inicia na pessoa de posicao 1 p1 = pessoa sendo passada da interacao p2 =
		 * pessoa da interacao corrente
		 */
		Person person = people.parallelStream().reduce(people.get(0), (p1, p2) -> p1.getAge() > p2.getAge() ? p1 : p2);
		Assertions.assertEquals("MARY", person.getName());
	}

	@Test
	void pessoaMaisNova() {
		/**
		 * Inicia na pessoa de posicao 1 p1 = pessoa sendo passada da interacao p2 =
		 * pessoa da interacao corrente
		 */
		Person person = people.parallelStream().reduce(people.get(0), (p1, p2) -> p1.getAge() < p2.getAge() ? p1 : p2);
		Assertions.assertEquals("JIM", person.getName());
	}

	@Test
	void contadorDeTasks() {
		List<String> tasks = people.stream().flatMap(a -> {
			return a.getTasks().stream().map(Task::getName);

		}).collect(Collectors.toList());
		Assertions.assertEquals(5, tasks.size());
	}

	@Test
	void contadorDeTasksFluxFlatMap() {

		List<Task> tasks = Flux.fromIterable(people).flatMap(p -> {
			return Flux.fromIterable(p.getTasks());
		}).collect(Collectors.toList()).block();

		Assertions.assertEquals(5, tasks.size());
	}

	@Test
	void somaDasIdadesReduce() {
		Integer somaIdade = people.parallelStream().mapToInt(Person::getAge).reduce(0,
				(somaAtual, idade) -> somaAtual + idade);
		Assertions.assertEquals(28, somaIdade);
	}

	@Test
	void somaDasIdadesSum() {
		Integer somaIdade = people.parallelStream().mapToInt(Person::getAge).sum();
		Assertions.assertEquals(28, somaIdade);
	}

	@Test
	void maiorTextoNaListaReduce() {
		List<String> textos = List.of("MY NAME IS JOHN", "HEY JOHN, HOW ARE YOU MY FELLOW?");

		String text = textos.parallelStream().reduce(textos.get(0),
				(current, n) -> current.length() > n.length() ? current : n);

		Assertions.assertEquals(textos.get(1), text);
	}

	@Test
	void maiorTextoNaListaMax() {
		List<String> textos = List.of("MY NAME IS JOHN", "HEY JOHN, HOW ARE YOU MY FELLOW?");
		String text = textos.parallelStream().max(Comparator.comparingInt(String::length)).get();
		Assertions.assertEquals(textos.get(1), text);

	}

	@Test
	void separarAsPalavras() {
		List<String> textos = List.of("MY NAME IS JOHN", "HEY JOHN, HOW ARE YOU MY FELLOW?");
		List<Object> collect = textos.parallelStream()
				.flatMap(a -> Arrays.asList(a.replaceAll("^[\\s]", "").split(" ")).stream())
				.collect(Collectors.toList());
		Assertions.assertEquals(11, collect.size());

	}

	@Getter
	@AllArgsConstructor
	class Person {
		private String name;
		private Integer age;
		private List<Task> tasks;
	}

	@Getter
	@AllArgsConstructor
	class Task {
		private String name;
	}

	public static void main(String[] args) {

//		Scanner sc = new Scanner(System.in);
//		String A = sc.next();
//		/* Enter your code here. Print output to STDOUT. */
		
//		Arrays.asList(A.split("")).stream().sorted(Comparator.reverseOrder());
		
		
		String dataErrada = "09/30/2020 00:00:00";
		
		DateTimeFormatter formatters = DateTimeFormatter.ofPattern("MM/dd/yyyy HH:MM:ss");
		
		

	}

}
