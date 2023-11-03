import io.reactivex.rxjava3.core.BackpressureStrategy;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.observables.ConnectableObservable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import org.junit.jupiter.api.Test;
import util.Color;

import java.io.FileNotFoundException;
import java.util.concurrent.TimeUnit;

import static util.ColorUtil.print;
import static util.ColorUtil.printThread;

public class RxTests {

    private static final String MOVIES1_DB = "movies1";

    private static final String MOVIES2_DB = "movies2";

    /**
     * Example 1: Creating and subscribing observable from iterable.
     */
    @Test
    public void loadMoviesAsList() throws FileNotFoundException {
        var movieReader = new MovieReader();

        movieReader.getMoviesFromList(MOVIES1_DB)
                .subscribe(movie -> print(movie, Color.GREEN));
    }

    /**
     * Example 2: Creating and subscribing observable from custom emitter.
     */
    @Test
    public void loadMoviesAsStream() {
        MovieReader movieReader = new MovieReader();

        movieReader.getMoviesAsStream(MOVIES1_DB)
                .subscribe(movie -> print(movie, Color.GREEN));
    }

    /**
     * Example 3: Handling errors.
     */
    @Test
    public void loadMoviesAsStreamAndHandleError() {
        MovieReader movieReader = new MovieReader();

        movieReader.getMoviesAsStream("incorrectPath")
                .subscribe(movie -> print(movie, Color.GREEN),
                        error -> print("Nie pyklo: " + error, Color.RED));
    }

    /**
     * Example 4: Signaling end of a stream.
     */
    @Test
    public void loadMoviesAsStreamAndFinishWithMessage() {
        MovieReader movieReader = new MovieReader();

        movieReader.getMoviesAsStream(MOVIES1_DB)
                .subscribe(movie -> print(movie, Color.GREEN),
                        error -> print("Nie pyklo: " + error, Color.RED),
                        () -> print("To juz jest koniec, nie ma juz nic", Color.BLUE));
    }

    /**
     * Example 5: Filtering stream data.
     */
    @Test
    public void displayLongMovies() {
        MovieReader movieReader = new MovieReader();

        movieReader.getMoviesAsStream(MOVIES1_DB)
                .filter(movie -> movie.getLength() > 150)
                .subscribe(movieLength -> print(movieLength, Color.GREEN));
    }

    /**
     * Example 6: Transforming stream data.
     */
    @Test
    public void displaySortedMoviesDescriptions() {
        MovieReader movieReader = new MovieReader();

        movieReader.getMoviesAsStream(MOVIES1_DB)
                .map(Movie::getDescription)
                .subscribe(description -> print(description, Color.GREEN));
    }

    /**
     * Example 7: Monads are like burritos.
     */
    @Test
    public void displayActorsForMovies() {
        MovieReader movieReader = new MovieReader();

        movieReader.getMoviesAsStream(MOVIES1_DB)
                .map(movie -> movieReader.readActors(movie))
                .flatMap(actorList -> Observable.fromIterable(actorList))
                .distinct()
                .sorted()
                .subscribe(actor -> print(actor, Color.GREEN));
    }

    /**
     * Example 8: Combining observables.
     */
    @Test
    public void loadMoviesFromManySources() {
        MovieReader movieReader = new MovieReader();

        Observable<Movie> movies1Observable = movieReader.getMoviesAsStream(MOVIES1_DB).doOnNext(movie -> print(movie, Color.GREEN));
        Observable<Movie> movies2Observable = movieReader.getMoviesAsStream(MOVIES2_DB).doOnNext(movie -> print(movie, Color.BLUE));

        Observable.merge(movies1Observable, movies2Observable)
                .subscribe(movie -> print(movie, Color.MAGENTA));
    }

    /**
     * Example 9: Playing with threads (subscribeOn).
     */
    @Test
    public void loadMoviesInBackground() {
        MovieReader movieReader = new MovieReader();

        movieReader.getMoviesAsStream(MOVIES1_DB)
                .doOnNext(movie -> printThread(movie, Color.BLUE))
                .subscribeOn(Schedulers.io())   // it doesn't matter where we place this operator; whole stream will run on this scheduler
                .subscribe(movie -> printThread(movie, Color.GREEN));

        printThread("koniec testu", Color.RED);

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Example 10: Playing with threads (observeOn).
     */
    @Test
    public void switchThreadsDuringMoviesProcessing() {
        MovieReader movieReader = new MovieReader();

        movieReader.getMoviesAsStream(MOVIES1_DB)
                .subscribeOn(Schedulers.computation())
                .doOnNext(movie -> printThread(movie, Color.BLUE))
                .observeOn(Schedulers.io()) // it DOES matter where this operator is placed, because from this place the stream starts working on new thread from the scheduler
                .doOnNext(movie -> printThread(movie, Color.RED))
                .blockingSubscribe(movie -> printThread(movie, Color.GREEN));

        printThread("koniec testu", Color.RED);


    }

    /**
     * Example 11: Combining parallel streams.
     */
    @Test
    public void loadMoviesFromManySourcesParallel() {
        MovieReader movieReader = new MovieReader();

        // Static merge solution
//
//        Observable<Movie> movies1Observable = movieReader.getMoviesAsStream(MOVIES1_DB)
//                .subscribeOn(Schedulers.io())
//                .doOnNext(movie -> print(movie, Color.GREEN));
//
//        Observable<Movie> movies2Observable = movieReader.getMoviesAsStream(MOVIES2_DB)
//                .subscribeOn(Schedulers.io())
//                .doOnNext(movie -> print(movie, Color.BLUE));
//
//        Observable.merge(movies1Observable, movies2Observable)
//                .blockingSubscribe(movie -> print(movie, Color.MAGENTA));


        // FlatMap solution:
        final MovieDescriptor movie1Descriptor = new MovieDescriptor(MOVIES1_DB, Color.GREEN);
        final MovieDescriptor movie2Descriptor = new MovieDescriptor(MOVIES2_DB, Color.BLUE);

        Observable.just(movie1Descriptor, movie2Descriptor)
                .flatMap(descriptor -> movieReader.getMoviesAsStream(descriptor.movieDbFilename)
                                .subscribeOn(Schedulers.io())
                                .doOnNext(movie -> print(movie, descriptor.debugColor)))
                .blockingSubscribe(movie -> print(movie, Color.GREEN));
    }

    /**
     * Example 12: Zip operator.
     */
    @Test
    public void loadMoviesWithDelay() {
        MovieReader movieReader = new MovieReader();

        Observable<Movie> moviesObservable = movieReader.getMoviesAsStream(MOVIES1_DB)
                .take(10)
                .subscribeOn(Schedulers.io())
                .doOnNext(movie -> print(movie, Color.BLUE));

        Observable<Long> intervalObservable = Observable.interval(1, TimeUnit.SECONDS);

        Observable.zip(moviesObservable, intervalObservable, (movie, tick) -> movie)
                .blockingSubscribe(movie -> print(movie, Color.GREEN));
    }

    /**
     * Example 13: Backpressure.
     */
    @Test
    public void trackMoviesLoadingWithBackpressure() {
        MovieReader movieReader = new MovieReader();

        movieReader.getMoviesAsStream(MOVIES1_DB)
                .doOnNext(movie -> printThread(movie, Color.BLUE))
                .subscribeOn(Schedulers.io())
                .doOnNext(movie -> Thread.sleep(10))
                .toFlowable(BackpressureStrategy.LATEST)
                .observeOn(Schedulers.io(), true, 1)
                .doOnNext(this::displayProgress)
                .blockingSubscribe();

    }

    /**
     * Example 14: Cold and hot observables.
     */
    @Test
    public void oneMovieStreamManyDifferentSubscribers() {
        MovieReader movieReader = new MovieReader();

        Observable<Movie> coldObservable = movieReader.getMoviesAsStream(MOVIES1_DB);

        ConnectableObservable<Movie> hotObservable = coldObservable.publish();

        hotObservable.take(10)
                .subscribe(movie -> print(movie, Color.BLUE));

        hotObservable
                .filter(movie -> movie.getRating().equals("R"))
                .subscribe(movie -> print(movie, Color.RED));

        hotObservable.connect();

        hotObservable
                .subscribe(movie -> print(movie, Color.YELLOW));
    }

    /**
     * Example 15: Caching observables (hot-cold hybrid).
     */
    @Test
    public void cacheMoviesInfo() {
        MovieReader movieReader = new MovieReader();

        Observable<Movie> movieObservable = movieReader.getMoviesAsStream(MOVIES1_DB).cache();


        movieObservable.subscribe(movie -> print(movie, Color.YELLOW));

        System.out.println(movieObservable.count().blockingGet());
    }

    private void displayProgress(Movie movie) throws InterruptedException {
        print((movie.getIndex() / 500.0 * 100) + "%", Color.GREEN);
        Thread.sleep(50);
    }

    private class MovieDescriptor {
        private final String movieDbFilename;

        private final Color debugColor;

        private MovieDescriptor(String movieDbFilename, Color debugColor) {
            this.movieDbFilename = movieDbFilename;
            this.debugColor = debugColor;
        }

        public Color getDebugColor() {
            return debugColor;
        }

        public String getMovieDbFilename() {
            return movieDbFilename;
        }
    }
}
