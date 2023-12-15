package ca.jrvs.apps.stockquote.services;

import ca.jrvs.apps.stockquote.dao.Position;
import ca.jrvs.apps.stockquote.dao.PositionDao;

import java.util.Optional;

public class PositionService {

    private PositionDao dao;

    public PositionService(PositionDao pRepo){
        this.dao = pRepo;
    }

    /**
     * Processes a buy order and updates the database accordingly
     * @param ticker
     * @param numberOfShares
     * @param price
     * @return The position in our database after processing the buy
     */
    public Position buy(String ticker, int numberOfShares, double price) {
        //TO DO
        Position pos = new Position();

        pos.setTicker(ticker);
        pos.setNumOfShares(numberOfShares);
        pos.setValuePaid(price);

        return dao.save(pos);
    }

    /**
     * Sells all shares of the given ticker symbol
     * @param ticker
     */
    public void sell(String ticker) {
        //TO DO
        Optional<Position> posOp = dao.findById(ticker);
        if(posOp.isEmpty()) return;

        Position pos = posOp.get();

        dao.deleteById(pos.getTicker());
    }
}
