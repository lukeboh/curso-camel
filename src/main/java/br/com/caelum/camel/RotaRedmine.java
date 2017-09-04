package br.com.caelum.camel;

import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class RotaRedmine {
	private static final Logger log = LogManager.getLogger(RotaRedmine.class);

	public static void main(String[] args) throws Exception {
		String password = args[0];

		log.info("Iniciando RotaRedmine");
		
		CamelContext context = new DefaultCamelContext();
		context.addRoutes(new RouteBuilder() {
			
			@Override
			public void configure() throws Exception {
				log.info("Início da configuração da rota");
				/* inicia com timer */
				from("timer://timer-qualquer?fixedRate=true&delay=1s&period=360s").id("from-timer").
				/* dar nome a rota */
				routeId("Salvar-issues").
				/* lendo do servidor */
				to("http4://redmine.tse.jus.br/projects/mrc/issues.xml?authUsername=luciano.bohnert&authPassword=" + password).id("to-redmine").
				/* converte */
				convertBodyTo(String.class).
				/* loga o que foi lido */
				log("${body}").
				/* Muda o nome */
				setHeader(Exchange.FILE_NAME, constant("issues.xml")).
				/* finaliza */
				to("file:saida");
				log.info("Fim da Configuração da Rota");
			}
		});
		log.info("Iniciando Contexto Camel");
		context.start(); // aqui camel realmente começa a trabalhar
		long sleepTime = 20000;
		log.info("Pausando Thread Main por {} segundos", sleepTime);
		Thread.sleep(20000); // esperando um pouco para dar um tempo para camel
		log.info("Fim do Rota Redmine");
	}
}
