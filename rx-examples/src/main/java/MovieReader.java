import io.reactivex.rxjava3.core.Emitter;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.ObservableEmitter;
import util.Actor;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class MovieReader {
    private static final String FILM_ACTOR_DB = "film_actor";
    private static final String ACTORS_DB = "actors";

    public Observable<Movie> getMoviesFromList(String moviesDb) throws FileNotFoundException {
        return Observable.fromIterable(readMovies(moviesDb));
    }

    public Observable<Movie> getMoviesAsStream(String moviesDb) {
        return Observable.create(emitter -> readMovies(moviesDb, emitter));
    }

    private List<Movie> readMovies(String moviesDb) throws FileNotFoundException {
        List<Movie> movies = new ArrayList<>();
        try (Scanner reader = new Scanner(getDbFile(moviesDb))) {
            while (reader.hasNextLine()) {
                String filmLine = reader.nextLine();
                String[] lineSplit = filmLine.split("\t");

                Movie movie = new Movie(Integer.parseInt(lineSplit[0]), lineSplit[1], lineSplit[2], Integer.parseInt(lineSplit[3]),
                        Integer.parseInt(lineSplit[4]), lineSplit[5]);

                System.out.println("MOVIE LOADED: " + movie);
                movies.add(movie);
            }
            return movies;
        }
    }

    private void readMovies(String moviesDb, ObservableEmitter<Movie> emitter) throws FileNotFoundException {
        try (Scanner reader = new Scanner(getDbFile(moviesDb))) {
            while (reader.hasNextLine() && !emitter.isDisposed()) {
                String filmLine = reader.nextLine();
                String[] lineSplit = filmLine.split("\t");

                Movie movie = new Movie(Integer.parseInt(lineSplit[0]), lineSplit[1], lineSplit[2], Integer.parseInt(lineSplit[3]),
                        Integer.parseInt(lineSplit[4]), lineSplit[5]);

                System.out.println("MOVIE LOADED: " + movie);
                emitter.onNext(movie);
            }
        } catch (FileNotFoundException e) {
            emitter.onError(e);
        } finally {
            emitter.onComplete();
        }
    }

    public List<Actor> readActors(Movie movie) throws FileNotFoundException {

        Map<Integer, Actor> actorsMap = new HashMap<>();

        try (Scanner reader = new Scanner(getDbFile(ACTORS_DB))) {
            while (reader.hasNextLine()) {
                String line = reader.nextLine();
                String[] lineSplit = line.split("\t");

                int actorId = Integer.parseInt(lineSplit[0]);
                actorsMap.put(actorId, new Actor(lineSplit[1], lineSplit[2]));
            }
        }

        List<Actor> actors = new ArrayList<>();
        try (Scanner reader = new Scanner(getDbFile(FILM_ACTOR_DB))) {
            while (reader.hasNextLine()) {
                String line = reader.nextLine();
                String[] lineSplit = line.split("\t");
                int actorId = Integer.parseInt(lineSplit[0]);
                int movieId = Integer.parseInt(lineSplit[1]);

                if (movieId == movie.getIndex()) {
                    actors.add(actorsMap.get(actorId));
                }
            }
        }
        return actors;
    }

    private File getDbFile(String dbName) {
        return new File("src/main/resources/" + dbName + ".tsv");
    }
}
