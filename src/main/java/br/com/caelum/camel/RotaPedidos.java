package br.com.caelum.camel;

import org.apache.camel.CamelContext;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.impl.DefaultCamelContext;

public class RotaPedidos {

	public static void main(String[] args) throws Exception {

		CamelContext context = new DefaultCamelContext();
		context.addRoutes(new RouteBuilder() {
			@Override
			public void configure() throws Exception {
				/*
				 * lendo arquivos da pasta pedidos de 5 em 5 segundos, sem apaga
				 * os arquivos
				 */
				from("file:pedidos?delay=5s&noop=true").
				/* Quebrar xml por item */
				split().xpath("/pedido/itens/item").
				/*
				 * fitrar por formato de item
				 */
				filter().xpath("/item/formato[text()='EBOOK']").
				/*
				 * queremos transformar a mensagem em outro formatode xml para
				 * json
				 */
				marshal().xmljson().
				/* log de execução */
				log("${id} - ${body}").
				/* renomeando o arquivo para json */
				setHeader("CamelFileName", simple("${file:name.noext}-${header.CamelSplitIndex}.json")).
				/* fim */
				to("file:saida");
			}
		});

		context.start(); // aqui camel realmente começa a trabalhar
		Thread.sleep(10000); // esperando um pouco para dar um tempo para camel
	}
}
