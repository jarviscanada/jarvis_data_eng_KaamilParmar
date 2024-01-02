package ca.jrvs.apps.stockquote.services;

import ca.jrvs.apps.stockquote.dao.Position;
import ca.jrvs.apps.stockquote.dao.Quote;
import ca.jrvs.apps.stockquote.dao.QuoteDao;
import ca.jrvs.apps.stockquote.dao.QuoteHttpHelper;

import java.util.Optional;

public class QuoteService {
    private QuoteDao dao;
    private QuoteHttpHelper httpHelper;



    public QuoteService(QuoteDao qRepo, QuoteHttpHelper rcon) {
        dao = qRepo;
        httpHelper = rcon;
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

    public void save(Quote quote) {
        //TO DO
        dao.save(quote);
    }

    public Optional<Quote> find(String symbol) {
        //TO DO
        return dao.findById(symbol);
    }

    public void delete(String symbol) {
        //TO DO
        dao.deleteById(symbol);
    }

    public Iterable<Quote> findAll() {
        //TO DO
        return dao.findAll();
    }

    public void deleteAll() {
        //TO DO
        dao.deleteAll();
    }
}
