package ca.jrvs.apps.jdbc;

import ca.jrvs.apps.stockquote.dao.Position;
import ca.jrvs.apps.stockquote.dao.PositionDao;
import ca.jrvs.apps.stockquote.services.PositionService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class PositionService_UnitTest {
    private PositionService positionService;
    private PositionDao dao;
    @Before
    public void setup() {
        PositionDao dao = new PositionDao();
        positionService = new PositionService();
    }

    @Test
    public void testBuyValid() {
        String ticker = "AAPL";
        int numOfShares = 10;
        double price = 100.0;

        Position mockSavedPosition = new Position();


        Position result = positionService.buy(ticker, numOfShares, price);

        assertNotNull(result);

    }

    @Test
    public void testBuyNotValid() {
        String ticker = "AAPLe33e";
        int numOfShares = 10;
        double price = 100.0;

        Position mockSavedPosition = new Position();


        Position result = positionService.buy(ticker, numOfShares, price);

        assertNull(result);

    }

    @Test
    public void testSell() {
        String ticker = "AAPL";

        Position mockPosition = new Position();


        positionService.sell(ticker);

        assertNull(dao.findById(ticker));

    }
}

