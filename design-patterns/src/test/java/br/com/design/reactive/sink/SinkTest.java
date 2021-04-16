package br.com.design.reactive.sink;

import org.junit.jupiter.api.Test;

import br.com.design.reactive.util.Util;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Sinks;
import reactor.core.publisher.Sinks.One;

class SinkTest {
	
	@Test
	void testingTryEmitValue() {
		One<Object> sink = Sinks.one();
		
		Mono<Object> mono = sink.asMono();
		
		mono.subscribe(Util.subscriber("sam"));
		
//		sink.tryEmitValue("hi");
//		sink.tryEmitEmpty();
		sink.tryEmitError(new RuntimeException("err"));
	}
}
