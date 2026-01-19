package software.ulpgc.kata5.application.io;

import software.ulpgc.kata5.architecture.io.Storer;
import software.ulpgc.kata5.architecture.model.Movie;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;
import java.util.stream.Stream;

public class DatabaseStorer extends Storer {
    private final Connection connection;
    private final PreparedStatement statement;

    public DatabaseStorer(Connection connection) throws SQLException {
        this.connection = connection;
        createTableIfNotExists();
        statement = connection.prepareStatement("INSERT INTO movies (title, year, duration) VALUES (?, ?, ?)");
    }

    private void createTableIfNotExists() throws SQLException {
        connection.createStatement().execute("CREATE TABLE IF NOT EXISTS movies (title TEXT, year INTEGER, duration INTEGER)");
    }

    @Override
    public Stream<Movie> loadAll() {
        try {
            return moviesIn(query());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private Stream<Movie> moviesIn(ResultSet query) {
        return Stream.generate(()->movieIn(query)).takeWhile(Objects::nonNull);
    }

    private Movie movieIn(ResultSet query) {
        try {
            return query.next() ? readMovieFrom(query) : null;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private Movie readMovieFrom(ResultSet query) throws SQLException {
        return new Movie(
                query.getString(1),
                query.getInt(2),
                query.getInt(3)
        );
    }

    private ResultSet query() throws SQLException {
        return connection.createStatement().executeQuery("SELECT title, year, duration FROM movies");
    }

    @Override
    public void recordAll(Stream<Movie> movies) {
        try {
            movies.forEach(this::record);
            connection.commit();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void record(Movie movie) {
        try {
            write(movie);
            executeBatchIfRequired();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void write(Movie movie) throws SQLException {
        statement.setString(1, movie.title());
        statement.setInt(2, movie.year());
        statement.setInt(3, movie.duration());
        statement.addBatch();
    }

    private int count = 0;
    private void executeBatchIfRequired() throws SQLException {
        if (++count % 10_000 == 0) statement.executeBatch();
    }

    @Override
    public boolean hasData() {
        try {
            return connection.createStatement().executeQuery("SELECT 1 FROM movies LIMIT 1").next();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
