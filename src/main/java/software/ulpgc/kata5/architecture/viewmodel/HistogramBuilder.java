package software.ulpgc.kata5.architecture.viewmodel;

import software.ulpgc.kata5.architecture.model.Movie;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Stream;

public class HistogramBuilder {
    private final Stream<Movie> movies;
    private final Map<String, String> labels;

    public static HistogramBuilder with(Stream<Movie> movies) {
        return new HistogramBuilder(movies);
    }

    private HistogramBuilder(Stream<Movie> movies) {
        this.movies = movies;
        this.labels = new HashMap<>();
    }

    public Histogram build(Function<Movie, Integer> binarize) {
        Histogram histogram = new Histogram(labels);
        movies.mapToInt(binarize::apply).forEach(histogram::put);
        return histogram;
    }

    public HistogramBuilder title(String title) {
        labels.put("title", title);
        return this;
    }

    public HistogramBuilder x(String x) {
        labels.put("x", x);
        return this;
    }

    public HistogramBuilder y(String y) {
        labels.put("y", y);
        return this;
    }

    public HistogramBuilder legend(String legend) {
        labels.put("legend", legend);
        return this;
    }
}
