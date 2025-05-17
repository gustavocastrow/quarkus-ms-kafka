package org.br.mineradora.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.jackson.Jacksonized;

@Jacksonized
@Builder
@AllArgsConstructor
@Data // Fornece getters, setters, toString, equals, hashCode e construtor padrão (no-args)
@NoArgsConstructor // Importante para o Jackson em muitos casos
public class CurrencyPriceDTO {

    @JsonProperty("USDBRL") // Garante o mapeamento explícito do JSON para este campo
    private USDBRL usdBRL; // Convenção Java: nome do campo em camelCase, privado
    // @Data irá gerar getUsdBRL() e setUsdBRL()
}