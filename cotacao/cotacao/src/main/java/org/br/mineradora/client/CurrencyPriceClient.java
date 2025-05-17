package org.br.mineradora.client;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import org.br.mineradora.dto.CurrencyPriceDTO;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

@Path("/last") //url raiz que vamos buscar infos fora da nossa aplicação
@RegisterRestClient
@ApplicationScoped
public interface CurrencyPriceClient {
    @GET
    @Path("/{pair}") // /last/pair(USD-BRL)
    CurrencyPriceDTO getCurrencyPrice(@PathParam("pair") String pair);

    //Quando esse método for chamado ele sera conectado a uma api externa, vai buscar resultados
    //vai devolver resultados e vai devolver no tipo de estrutura CurrencyPriceDTO
}
