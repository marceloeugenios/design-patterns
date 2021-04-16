package br.com.design.reactive.operators.flatmap;

import br.com.design.reactive.util.Util;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
class PurchaseOrder {

	private String item;
	private String price;
	private int userId;
	
	public PurchaseOrder(int userId) {
		this.userId = userId;
		this.item = Util.faker().commerce().productName();
		this.price = Util.faker().commerce().price();
	}
}
