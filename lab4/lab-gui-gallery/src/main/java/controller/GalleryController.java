package controller;


import io.reactivex.rxjava3.schedulers.Schedulers;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import model.Gallery;
import model.Photo;
import org.pdfsam.rxjavafx.schedulers.JavaFxScheduler;
import util.PhotoDownloader;

public class GalleryController {

    @FXML
    private TextField searchTextField;
    @FXML
    private ListView<Photo> imagesListView;

    @FXML
    private ImageView imageView;

    @FXML
    private TextField imageNameField;

    private Gallery galleryModel;

    @FXML
    public void initialize() {
        imagesListView.setCellFactory(param -> new ListCell<>() {
            @Override
            protected void updateItem(Photo item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setText(null);
                    setGraphic(null);
                } else {
                    ImageView photoIcon = new ImageView(item.getPhotoData());
                    photoIcon.setPreserveRatio(true);
                    photoIcon.setFitHeight(50);
                    setGraphic(photoIcon);
                }
            }
        });

        imagesListView.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> {
                    unbindOldSelectedPhoto(oldValue);
                    bindSelectedPhoto(newValue);
                }
        );
    }

    public void setModel(Gallery gallery) {
        this.galleryModel = gallery;

        imagesListView.setItems(gallery.getPhotos());
        imagesListView.getSelectionModel().select(0);
    }

    private void bindSelectedPhoto(Photo selectedPhoto) {
        if (selectedPhoto == null) {
            return;
        }

        this.imageNameField.textProperty().bindBidirectional(selectedPhoto.nameProperty());
        this.imageView.imageProperty().bind(selectedPhoto.photoDataProperty());
    }

    private void unbindOldSelectedPhoto(Photo oldSelectedPhoto) {
        if (oldSelectedPhoto == null) {
            return;
        }
        this.imageNameField.textProperty().unbindBidirectional(oldSelectedPhoto.nameProperty());
        this.imageView.imageProperty().unbind();
    }

    public void searchButtonClicked(ActionEvent event) {
        PhotoDownloader photoDownloader = new PhotoDownloader();

        galleryModel.clear();

        photoDownloader.searchForPhotos(this.searchTextField.getText())
                .subscribeOn(Schedulers.io())
                .observeOn(JavaFxScheduler.platform())
                .subscribe(photo -> galleryModel.addPhoto(photo));
    }
}

