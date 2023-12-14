package ca.jrvs.apps.jdbc;

import ca.jrvs.apps.stockquote.dao.Quote;
import ca.jrvs.apps.stockquote.dao.QuoteDao;
import ca.jrvs.apps.stockquote.dao.QuoteHttpHelper;
import ca.jrvs.apps.stockquote.services.QuoteService;
import org.junit.Before;
import org.junit.Test;

import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class QuoteService_UnitTest {
    private QuoteService quoteService;

    @Before
    public void setup() {
        quoteService = new QuoteService("");
    }

    @Test
    public void testCorrectAPI() {
        Optional<Quote> result = quoteService.fetchQuoteDataFromAPI("MFST");

        assertFalse(result.isEmpty());

    }
    @Test
    public void testIncorrectAPI() {
        Optional<Quote> result = quoteService.fetchQuoteDataFromAPI("mdek");

        assertFalse(result.isPresent());

    }


}
