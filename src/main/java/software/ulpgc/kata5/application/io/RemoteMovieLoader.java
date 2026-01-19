package software.ulpgc.kata5.application.io;

import software.ulpgc.kata5.architecture.model.Movie;
import software.ulpgc.kata5.architecture.io.MovieLoader;

import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLConnection;
import java.util.function.Function;
import java.util.stream.Stream;
import java.util.zip.GZIPInputStream;

public class RemoteMovieLoader implements MovieLoader {
    private static final String url = "https://datasets.imdbws.com/title.basics.tsv.gz";
    private static final int bufferSize = 1024;
    private final Function<String, Movie> deserializer;

    public RemoteMovieLoader(Function<String, Movie> fromTsp) {
        deserializer = fromTsp;
    }

    @Override
    public Stream<Movie> loadAll() {
        try {
            return loadFrom(new URI(url).toURL().openConnection());
        } catch (IOException | URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    private Stream<Movie> loadFrom(URLConnection urlConnection) throws IOException {
        return loadUsing(unzipped(urlConnection.getInputStream()));
    }

    private Stream<Movie> loadUsing(InputStream is) {
        return readFrom(readerFrom(is)).onClose(() -> close(is));
    }

    private Stream<Movie> readFrom(BufferedReader reader) {
        return reader.lines()
                .skip(1)
                .map(deserializer);
    }

    private static BufferedReader readerFrom(InputStream is) {
        return new BufferedReader(new InputStreamReader(is), bufferSize);
    }

    private static InputStream unzipped(InputStream inputStream) throws IOException {
        return new GZIPInputStream(new BufferedInputStream(inputStream, bufferSize));
    }

    private static void close(InputStream is) {
        try {
            is.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
