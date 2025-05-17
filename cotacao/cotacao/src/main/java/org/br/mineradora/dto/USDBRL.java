package org.br.mineradora.dto;

import com.fasterxml.jackson.annotation.JsonProperty; // Adicione esta importação
import lombok.Data;
import lombok.NoArgsConstructor; // Adicione esta importação

@Data
@NoArgsConstructor
public class USDBRL {

    // Usando campos privados e @JsonProperty para clareza e robustez
    @JsonProperty("code")
    private String code;
    @JsonProperty("codein")
    private String codein;
    @JsonProperty("name")
    private String name;
    @JsonProperty("high")
    private String high;
    @JsonProperty("low")
    private String low;
    @JsonProperty("varBid")
    private String varBid;
    @JsonProperty("pctChange")
    private String pctChange;
    @JsonProperty("bid")
    private String bid;
    @JsonProperty("ask")
    private String ask;
    @JsonProperty("timestamp")
    private String timestamp;
    @JsonProperty("create_date") // O Jackson normalmente mapeia snake_case para camelCase (createDate),
    // mas com @JsonProperty("create_date") fica explícito.
    // Se o nome do campo for exatamente "create_date", a anotação é opcional mas boa prática.
    private String create_date;
}