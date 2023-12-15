package ca.jrvs.apps.stockquote.services;

import ca.jrvs.apps.stockquote.dao.Quote;
import ca.jrvs.apps.stockquote.dao.QuoteDao;
import ca.jrvs.apps.stockquote.dao.QuoteHttpHelper;

import java.util.Optional;

public class QuoteService {
    private QuoteDao dao;
    private QuoteHttpHelper httpHelper;



    public QuoteService(QuoteDao qRepo, QuoteHttpHelper rcon) {
        dao = qRepo;
        httpHelper= rcon;
    }

    /**
     * Fetches latest quote data from endpoint
     * @param symbol
     * @return Latest quote information or empty optional if ticker symbol not found
     */
    public Optional<Quote> fetchQuoteDataFromAPI(String symbol) {
        //TO DO
        return Optional.ofNullable(httpHelper.fetchQuoteInfo(symbol));

    }
}
