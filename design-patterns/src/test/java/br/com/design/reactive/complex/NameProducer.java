package br.com.design.reactive.complex;

import java.util.function.Consumer;

import br.com.design.reactive.util.Util;
import reactor.core.publisher.FluxSink;

public class NameProducer implements Consumer<FluxSink<String>> {

	private FluxSink<String> fluxSink;
	
	@Override
	public void accept(FluxSink<String> fluxSink) {
		this.fluxSink = fluxSink;
	}
	
	public void produce() {
		String name = Util.faker().name().fullName();
		String thread = Thread.currentThread().getName();
		this.fluxSink.next(thread + " : " + name);
	}
}
