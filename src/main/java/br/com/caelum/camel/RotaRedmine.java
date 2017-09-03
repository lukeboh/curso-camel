package br.com.caelum.camel;

import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.impl.DefaultCamelContext;

public class RotaRedmine {

	public static void main(String[] args) throws Exception {
		String password = args[0];

		CamelContext context = new DefaultCamelContext();
		context.setTracing(true);
		context.addRoutes(new RouteBuilder() {
			@Override
			public void configure() throws Exception {
				/* inicia com timer */
				from("timer://timer-qualquer?fixedRate=true&delay=1s&period=360s").
				/* lendo do servidor */
				to("http4://redmine.tse.jus.br/projects/mrc/issues.xml?authUsername=luciano.bohnert&authPassword=" + password).
				/* converte */
				convertBodyTo(String.class).
				/* loga o que foi lido */
				log("${body}").
				/* Muda o nome */
				setHeader(Exchange.FILE_NAME, constant("issues.xml")).
				/* finaliza */
				to("file:saida");
			}
		});

		context.start(); // aqui camel realmente começa a trabalhar
		Thread.sleep(20000); // esperando um pouco para dar um tempo para camel
	}
}
