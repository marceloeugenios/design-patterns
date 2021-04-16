package br.com.design.reactive.operators.flatmap;

import br.com.design.reactive.util.Util;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class User {

	private int userId;
	private String name;
	
	public User(int userId) {
		this.userId = userId;
		this.name = Util.faker().name().fullName();
	}
}
