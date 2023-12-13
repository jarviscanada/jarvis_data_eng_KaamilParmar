package ca.jrvs.apps.stockquote.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PositionDao implements CrudDao<Position, String>{
    private Connection c;

    /**
     * Saves a given entity. Used for create and update
     *
     * @param entity - must not be null
     * @return The saved entity. Will never be null
     * @throws IllegalArgumentException - if id is null
     */
    @Override
    public Position save(Position entity) throws IllegalArgumentException {
        if (entity == null) {
            throw new IllegalArgumentException("Id cannot be null");
        }
        else if(findById(entity.getTicker()).isEmpty()){
            String sqlQuery = "INSERT INTO position (ticker, number_of_shares, value_paid) VALUES (?, ?, ?)";
            try (PreparedStatement ps = c.prepareStatement(sqlQuery)) {
                ps.setString(1, entity.getTicker());
                ps.setInt(2, entity.getNumOfShares());
                ps.setDouble(3, entity.getValuePaid());

                ps.executeUpdate();

                return entity;
            } catch (SQLException e) {
                throw new RuntimeException("Error saving entity: " + e.getMessage());
            }
        }
        else {
            String sqlQuery = "UPDATE position SET ticker = ?, number_of_shares = ?, value_paid = ? WHERE ticker = ?";
            try (PreparedStatement ps = c.prepareStatement(sqlQuery)) {
                ps.setString(1, entity.getTicker());
                ps.setInt(2, entity.getNumOfShares());
                ps.setDouble(3, entity.getValuePaid());

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
    public Optional<Position> findById(String s) throws IllegalArgumentException {
        if (s == null) {
            throw new IllegalArgumentException("Id cannot be null");
        }

        String sqlQuery = "SELECT * FROM position WHERE ticker = ?";
        try (PreparedStatement ps = c.prepareStatement(sqlQuery)) {
            ps.setString(1, s);

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return Optional.of(mapPosition(rs));
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
    public Iterable<Position> findAll() {
        List<Position> positions = new ArrayList<>();

        String sqlQuery = "SELECT * FROM position";
        try (PreparedStatement ps = c.prepareStatement(sqlQuery)) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                positions.add(mapPosition(rs));
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error saving entity: " + e.getMessage());
        }

        return positions;
    }


    /**
     * Deletes the entity with the given id. If the entity is not found, it is silently ignored
     *
     * @param s - must not be null
     * @throws IllegalArgumentException - if id is null
     */
    @Override
    public void deleteById(String s) throws IllegalArgumentException {
        if (s == null) {
            throw new IllegalArgumentException("Id cannot be null");
        }

        try {
            PreparedStatement ps = c.prepareStatement("DELETE FROM position WHERE ticker = ?");
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
            PreparedStatement ps = c.prepareStatement("DELETE FROM position");
            ps.executeUpdate();
        }
        catch (SQLException e) {
            throw new RuntimeException("Error saving entity: " + e.getMessage());
        }
    }

    //implement all inherited methods
    //you are not limited to methods defined in CrudDao

    private Position mapPosition(ResultSet rs) throws SQLException{
        if (rs.next()) {
            Position position = new Position();
            position.setTicker(rs.getString("ticker"));
            position.setNumOfShares(rs.getInt("number_of_shares"));
            position.setValuePaid(rs.getDouble("value_paid"));

            return position;
        } else {
            return null;
        }
    }
}
