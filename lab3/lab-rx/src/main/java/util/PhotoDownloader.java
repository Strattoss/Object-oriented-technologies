package util;

import driver.DuckDuckGoDriver;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Scheduler;
import io.reactivex.rxjava3.schedulers.Schedulers;
import model.Photo;
import org.apache.tika.Tika;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PhotoDownloader {

    private static final Logger log = Logger.getLogger(PhotoDownloader.class.getName());

    public Observable<Photo> getPhotoExamples() throws IOException {
        return Observable.just(
                "https://i.ytimg.com/vi/7uxQjydfBOU/hqdefault.jpg",
                "http://digitalspyuk.cdnds.net/16/51/1280x640/landscape-1482419524-12382542-low-res-sherlock.jpg",
                "http://image.pbs.org/video-assets/pbs/masterpiece/132733/images/mezzanine_172.jpg",
                "https://classicmystery.files.wordpress.com/2016/04/miss-marple-2.jpg",
                "https://i.pinimg.com/736x/7c/14/c9/7c14c97839940a09f987fbadbd47eb89--detective-monk-adrian-monk.jpg"
        ).map(url -> getPhoto(url));
    }

    public Observable<Photo> searchForPhotos(String searchQuery) {
        return Observable.create(observer -> {
            List<String> photoUrls = null;
            try {
                photoUrls = DuckDuckGoDriver.searchForImages(searchQuery);
            } catch (IOException | InterruptedException e) {
                observer.onError(e);
            }

            for (String photoUrl : photoUrls) {
                if (!observer.isDisposed()) {
                    try {
                        observer.onNext(getPhoto(photoUrl));
                    } catch (IOException e) {
                        log.log(Level.WARNING, "Could not download a photo", e);
                    }
                }

            }
            observer.onComplete();
        });
    }

    public Observable<Photo> searchForPhotos(List<String> queries) {
        List<Observable<Photo>> searchObservables = new ArrayList<>();

        for (String query : queries) {
            searchObservables.add(
                    searchForPhotos(query)
                            .subscribeOn(Schedulers.io())
                            .take(5));
        }

        return Observable.merge(searchObservables);
    }

    private Photo getPhoto(String photoUrl) throws IOException {
        log.info("Downloading... " + photoUrl);
        byte[] photoData = downloadPhoto(photoUrl);
        return createPhoto(photoData);
    }

    private Photo createPhoto(byte[] photoData) throws IOException {
        Tika tika = new Tika();
        String fileType = tika.detect(photoData);
        if (fileType.startsWith("image")) {
            return new Photo(LocalDate.now(), fileType.substring(fileType.indexOf("/") + 1), photoData);
        }
        throw new IOException("Unsupported media type: " + fileType);
    }


    private byte[] downloadPhoto(String url) throws IOException {
        URL photoUrl = new URL(url);
        URLConnection yc = photoUrl.openConnection();
        yc.setRequestProperty("User-Agent", DuckDuckGoDriver.USER_AGENT);
        try (InputStream inputStream = yc.getInputStream()) {
            ByteArrayOutputStream buffer = new ByteArrayOutputStream();
            int nRead;
            byte[] data = new byte[16384];

            while ((nRead = inputStream.read(data, 0, data.length)) != -1) {
                buffer.write(data, 0, nRead);
            }
            buffer.flush();
            return buffer.toByteArray();
        }
    }
}
