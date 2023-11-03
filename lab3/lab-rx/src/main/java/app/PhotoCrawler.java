package app;

import io.reactivex.rxjava3.core.Observable;
import model.Photo;
import util.PhotoDownloader;
import util.PhotoProcessor;
import util.PhotoSerializer;

import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PhotoCrawler {

    private static final Logger log = Logger.getLogger(PhotoCrawler.class.getName());

    private final PhotoDownloader photoDownloader;

    private final PhotoSerializer photoSerializer;

    private final PhotoProcessor photoProcessor;

    public PhotoCrawler() throws IOException {
        this.photoDownloader = new PhotoDownloader();
        this.photoSerializer = new PhotoSerializer("./photos");
        this.photoProcessor = new PhotoProcessor();
    }

    public void resetLibrary() throws IOException {
        photoSerializer.deleteLibraryContents();
    }

    public void downloadPhotoExamples() {
        try {
            photoDownloader.getPhotoExamples()
                    .compose(photoProcessor::processPhotos)
                    .subscribe(photo -> photoSerializer.savePhoto(photo));
        } catch (IOException e) {
            log.log(Level.SEVERE, "Downloading photo examples error", e);
        }
    }

    public void downloadPhotosForQuery(String query) {
        photoDownloader.searchForPhotos(query)
                .take(5)
                .compose(photoProcessor::processPhotos)
                .subscribe(
                    photo -> photoSerializer.savePhoto(photo),
                    error -> log.log(Level.SEVERE, "Something went wrong" + error),
                        () -> log.log(Level.INFO, "All photos downloaded")
                );
    }

    public void downloadPhotosForMultipleQueries(List<String> queries) {
        photoDownloader.searchForPhotos(queries)
                .compose(photoProcessor::processPhotos)
                .subscribe(
                        photo -> photoSerializer.savePhoto(photo),
                        error -> log.log(Level.SEVERE, "Something went wrong" + error),
                        () -> log.log(Level.INFO, "All photos downloaded")
                );
    }

    public void downloadPhotosForMultipleQueriesAndGroupThem(List<String> queries) {

    }
}
