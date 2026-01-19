package software.ulpgc.kata5.application;

import software.ulpgc.kata5.application.io.DatabaseStorer;
import software.ulpgc.kata5.application.io.RemoteMovieLoader;
import software.ulpgc.kata5.application.io.Store;
import software.ulpgc.kata5.architecture.io.MovieLoader;
import software.ulpgc.kata5.architecture.io.Storer;
import software.ulpgc.kata5.architecture.viewmodel.Histogram;
import software.ulpgc.kata5.architecture.viewmodel.HistogramBuilder;
import software.ulpgc.kata5.architecture.model.Movie;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.stream.Stream;

public class Main {
    private static final File database = new File("movies.db");

    static void main() {
        try (Connection connection = openConection()) {
            MovieLoader remoteLoader = new RemoteMovieLoader(Movie::fromTsp);
            Storer database = new DatabaseStorer(connection);

            Stream<Movie> movies = new Store(remoteLoader, database).loadAll()
                    .filter(m -> m.year() > 1900)
                    .filter(m -> m.year() < 2025)
                    .limit(10_000);

            Histogram histogram = HistogramBuilder.with(movies)
                    .title("Movies per year")
                    .x("Year")
                    .y("Frequency")
                    .legend("Movies")
                    .build(Movie::year);

            Desktop.create()
                    .display(histogram)
                    .setVisible(true);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private static Connection openConection() throws SQLException {
        Connection connection = DriverManager.getConnection("jdbc:sqlite:" + database.getAbsolutePath());
        connection.setAutoCommit(false);
        return connection;
    }
}
