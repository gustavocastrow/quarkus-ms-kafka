package org.br.mineradora.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.br.mineradora.client.CurrencyPriceClient;
import org.br.mineradora.dto.CurrencyPriceDTO;
import org.br.mineradora.dto.QuotationDTO;
import org.br.mineradora.entity.QuotationEntity;
import org.br.mineradora.message.KafkaEvents;
import org.br.mineradora.repository.QuotationRepository;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.slf4j.Logger; // Import SLF4J Logger
import org.slf4j.LoggerFactory; // Import SLF4J LoggerFactory

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
// Removido AtomicBoolean não utilizado

@ApplicationScoped
public class QuotationService {

    private static final Logger LOG = LoggerFactory.getLogger(QuotationService.class); // Crie um Logger

    @Inject
    @RestClient
    CurrencyPriceClient currencyPriceClient;

    @Inject
    QuotationRepository quotationRepository;

    @Inject
    KafkaEvents kafkaEvents;

    public void getCurrencyPrice() {
        LOG.info("Buscando cotação para USD-BRL...");
        CurrencyPriceDTO currencyPriceInfo = currencyPriceClient.getCurrencyPrice("USD-BRL");

        // Log crucial aqui!
        LOG.info("CurrencyPriceDTO recebido do client: {}", currencyPriceInfo);
        if (currencyPriceInfo != null) {
            LOG.info("Campo USDBRL dentro de currencyPriceInfo: {}", currencyPriceInfo.getUsdBRL()); // Ajuste para getUsdBRL() se usar a Opção 1
        } else {
            LOG.error("currencyPriceInfo retornado pelo client é NULO!");
            return; // Não continue se currencyPriceInfo for nulo
        }

        // Verificação adicional antes de chamar updateCurrentInfoPrice
        if (currencyPriceInfo.getUsdBRL() == null) { // Ajuste para getUsdBRL() se usar a Opção 1
            LOG.error("O campo USDBRL dentro de currencyPriceInfo é NULO, mesmo que currencyPriceInfo não seja. Payload: {}", currencyPriceInfo);
            // Você pode querer lançar uma exceção aqui ou tratar de outra forma
            return;
        }

        if (updateCurrentInfoPrice(currencyPriceInfo)) {
            // Se você mudou para private USDBRL usdBRL; e usa getUsdBRL(), ajuste aqui também:
            kafkaEvents.sendNewKafkaEvent(QuotationDTO
                    .builder()
                    .currencyPrice(new BigDecimal(currencyPriceInfo.getUsdBRL().getBid()))
                    .date(new Date())
                    .build());
        }
    }

    private boolean updateCurrentInfoPrice(CurrencyPriceDTO currencyPriceInfo) {
        // A verificação primária de currencyPriceInfo.getUSDBRL() != null deve ser feita antes de chamar este método,
        // ou no início deste método.
        if (currencyPriceInfo == null || currencyPriceInfo.getUsdBRL() == null) { // Ajuste para getUsdBRL() se usar a Opção 1
            LOG.error("updateCurrentInfoPrice chamado com currencyPriceInfo ou seu campo USDBRL nulo.");
            return false;
        }

        // Se você mudou para private USDBRL usdBRL; e usa getUsdBRL(), ajuste aqui também:
        BigDecimal currentPrice = new BigDecimal(currencyPriceInfo.getUsdBRL().getBid());
        boolean updatePrice = false;

        List<QuotationEntity> quotationList = quotationRepository.findAll().list();

        if (quotationList.isEmpty()) {
            saveQuotation(currencyPriceInfo);
            updatePrice = true;
        } else {
            QuotationEntity lastDollarPrice = quotationList.get(quotationList.size() - 1);
            if (currentPrice.compareTo(lastDollarPrice.getCurrencyPrice()) > 0) { // Use compareTo para BigDecimal
                updatePrice = true;
                saveQuotation(currencyPriceInfo);
            }
        }
        return updatePrice;
    }

    private void saveQuotation(CurrencyPriceDTO currencyInfo) {
        // A verificação primária de currencyInfo.getUSDBRL() != null deve ser feita antes.
        if (currencyInfo == null || currencyInfo.getUsdBRL() == null) { // Ajuste para getUsdBRL() se usar a Opção 1
            LOG.error("saveQuotation chamado com currencyInfo ou seu campo USDBRL nulo.");
            return;
        }

        QuotationEntity quotation = new QuotationEntity();
        quotation.setDate(new Date());
        // Se você mudou para private USDBRL usdBRL; e usa getUsdBRL(), ajuste aqui também:
        quotation.setCurrencyPrice(new BigDecimal(currencyInfo.getUsdBRL().getBid()));
        quotation.setPctChange(currencyInfo.getUsdBRL().getPctChange());
        quotation.setPair("USD-BRL");

        quotationRepository.persist(quotation);
    }
}