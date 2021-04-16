package br.com.design.reactive.batching.book;

import com.github.javafaker.Book;

import br.com.design.reactive.util.Util;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
class BookOrder {

	private String title;
	private String author;
	private String category;
	private double price;
	
	public BookOrder() {
		Book book = Util.faker().book();
		this.title = book.title();
		this.author = book.author();
		this.category = book.genre();
		this.price = Double.parseDouble(Util.faker().commerce().price());
	}
}
