package br.com.caelum.camel;

import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.impl.DefaultCamelContext;

public class RotaRedmine {

	public static void main(String[] args) throws Exception {

		CamelContext context = new DefaultCamelContext();
		context.addRoutes(new RouteBuilder() {
			@Override
			public void configure() throws Exception {
				/* inicia com timer */
				from("timer://negociacoes?fixedRate=true&delay=1s&period=360s").
				/* lendo do servidor */
				to("http4://www.redmine.org/projects.xml").
				/* converte */
				convertBodyTo(String.class).
				/* loga o que foi lido */
				log("Algo - ${body}").
				/* Muda o nome */
				setHeader(Exchange.FILE_NAME, constant("projects.xml")).
				/* finaliza */
				to("file:saida");
			}
		});

		context.start(); // aqui camel realmente começa a trabalhar
		Thread.sleep(10000); // esperando um pouco para dar um tempo para camel
	}
}
