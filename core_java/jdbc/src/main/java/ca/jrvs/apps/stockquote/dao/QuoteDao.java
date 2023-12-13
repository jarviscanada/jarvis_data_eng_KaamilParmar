package ca.jrvs.apps.stockquote.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class QuoteDao implements CrudDao<Quote, String> {

    private Connection c;

    /**
     * Saves a given entity. Used for create and update
     *
     * @param entity - must not be null
     * @return The saved entity. Will never be null
     * @throws IllegalArgumentException - if id is null
     */
    @Override
    public Quote save(Quote entity) throws IllegalArgumentException {

        if (entity == null) {
            throw new IllegalArgumentException("Id cannot be null");
        }
        else if(findById(entity.getSymbol()).isEmpty()){
            String sqlQuery = "INSERT INTO quote (symbol, open, high, low, price, volume, latest_trading_day, previous_close, change, change_percent, timestamp) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            try (PreparedStatement ps = c.prepareStatement(sqlQuery)) {
                ps.setString(1, entity.getSymbol());
                ps.setDouble(2, entity.getOpen());
                ps.setDouble(3, entity.getHigh());
                ps.setDouble(4, entity.getLow());
                ps.setDouble(5, entity.getPrice());
                ps.setInt(6, entity.getVolume());
                ps.setDate(7, entity.getLatestTradingDay());
                ps.setDouble(8, entity.getPreviousClose());
                ps.setDouble(9, entity.getChange());
                ps.setString(10, entity.getChangePercent());
                ps.setTimestamp(11, entity.getTimestamp());

                ps.executeUpdate();

                return entity;
            } catch (SQLException e) {
                throw new RuntimeException("Error saving entity: " + e.getMessage());
            }
        }
        else {
            String sqlQuery = "UPDATE quote SET symbol = ?, open = ?, high = ?, low = ?, price = ?, volume = ?, " +
                    "latest_trading_day = ?, previous_close = ?, change = ?, change_percent = ?, timestamp = ? WHERE symbol = ?";
            try (PreparedStatement ps = c.prepareStatement(sqlQuery)) {
                ps.setString(1, entity.getSymbol());
                ps.setDouble(2, entity.getOpen());
                ps.setDouble(3, entity.getHigh());
                ps.setDouble(4, entity.getLow());
                ps.setDouble(5, entity.getPrice());
                ps.setInt(6, entity.getVolume());
                ps.setDate(7, entity.getLatestTradingDay());
                ps.setDouble(8, entity.getPreviousClose());
                ps.setDouble(9, entity.getChange());
                ps.setString(10, entity.getChangePercent());
                ps.setTimestamp(11, entity.getTimestamp());
                ps.setString(12, entity.getSymbol());

                ps.executeUpdate();

                return entity;
            } catch (SQLException e) {
                throw new RuntimeException("Error saving entity: " + e.getMessage());
            }
        }




    }

    /**
     * Retrieves an entity by its id
     *
     * @param s - must not be null
     * @return Entity with the given id or empty optional if none found
     * @throws IllegalArgumentException - if id is null
     */
    @Override
    public Optional<Quote> findById(String s) throws IllegalArgumentException {
        if (s == null) {
            throw new IllegalArgumentException("Id cannot be null");
        }

        String sqlQuery = "SELECT * FROM quote WHERE symbol = ?";
        try (PreparedStatement ps = c.prepareStatement(sqlQuery)) {
            ps.setString(1, s);

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return Optional.of(mapQuote(rs));
            } else {
                return Optional.empty();
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error saving entity: " + e.getMessage());
        }

    }

    /**
     * Retrieves all entities
     *
     * @return All entities
     */
    @Override
    public Iterable<Quote> findAll() {
        List<Quote> quotes = new ArrayList<>();

        String sqlQuery = "SELECT * FROM quote";
        try (PreparedStatement ps = c.prepareStatement(sqlQuery)) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                quotes.add(mapQuote(rs));
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error saving entity: " + e.getMessage());
        }

        return null;
    }

    /**
     * Deletes the entity with the given id. If the entity is not found, it is silently ignored
     *
     * @param s - must not be null
     * @throws IllegalArgumentException - if id is null
     */
    @Override
    public void deleteById(String s) throws IllegalArgumentException {
        try {
            PreparedStatement ps = c.prepareStatement("DELETE FROM quote WHERE symbol = ?");
            ps.setString(1, s);
            ps.executeUpdate();
        }
        catch (SQLException e) {
            throw new RuntimeException("Error deleting entity: " + e.getMessage());
        }
    }

    /**
     * Deletes all entities managed by the repository
     */
    @Override
    public void deleteAll() {
        try {
            PreparedStatement ps = c.prepareStatement("DELETE FROM quote");
            ps.executeUpdate();
        }
        catch (SQLException e) {
            throw new RuntimeException("Error saving entity: " + e.getMessage());
        }
    }

    //implement all inherited methods
    //you are not limited to methods defined in CrudDao

    private Quote mapQuote(ResultSet rs) throws SQLException {
        if (rs.next()) {
            Quote quote = new Quote();
            quote.setSymbol(rs.getString("symbol"));
            quote.setOpen(rs.getDouble("open"));
            quote.setHigh(rs.getDouble("high"));
            quote.setLow(rs.getDouble("low"));
            quote.setPrice(rs.getDouble("price"));
            quote.setVolume(rs.getInt("volume"));
            quote.setLatestTradingDay(rs.getDate("latest_trading_day"));
            quote.setPreviousClose(rs.getDouble("previous_close"));
            quote.setChange(rs.getDouble("change"));
            quote.setChangePercent(rs.getString("change_percent"));

            return quote;
        } else {
            return null;
        }
    }
}