package software.ulpgc.kata5.application;

import software.ulpgc.kata5.architecture.viewmodel.Histogram;
import software.ulpgc.kata5.architecture.viewmodel.HistogramBuilder;
import software.ulpgc.kata5.architecture.model.Movie;

import java.util.stream.Stream;

public class Main {
    static void main() {
        Stream<Movie> movies = new RemoteMovieLoader(Movie::fromTsp).loadAll()
                .filter(m -> m.year()>1900)
                .filter(m -> m.year()<2025)
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
    }
}
