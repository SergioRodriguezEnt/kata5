package software.ulpgc.kata5.application;

import software.ulpgc.kata5.architecture.io.MovieLoader;
import software.ulpgc.kata5.architecture.io.Storer;
import software.ulpgc.kata5.architecture.model.Movie;

import java.util.stream.Stream;

public class Store implements MovieLoader {
    private final MovieLoader remoteLoader;
    private final Storer database;

    public Store(MovieLoader remoteLoader, Storer database) {
        this.remoteLoader = remoteLoader;
        this.database = database;
    }

    @Override
    public Stream<Movie> loadAll() {
        if (!database.hasData()) database.recordAll(remoteLoader.loadAll());
        return database.loadAll();
    }
}
