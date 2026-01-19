package software.ulpgc.kata5.architecture.io;

import software.ulpgc.kata5.architecture.model.Movie;

import java.util.stream.Stream;

public interface MovieRecorder {
    void recordAll(Stream<Movie> movies);
    boolean hasData();
}
