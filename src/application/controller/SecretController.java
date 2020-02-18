package application.controller;

import application.model.AES;
import application.model.Decode;
import application.model.Encode;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextArea;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import com.jfoenix.controls.JFXTextField;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.application.Platform;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import javax.imageio.ImageIO;

public class SecretController {
    @FXML
    private AnchorPane root;

    @FXML
    private JFXButton encodeButton;

    @FXML
    private JFXButton decodeButton;

    @FXML
    private Pane encodePane;

    @FXML
    private JFXTextArea encodeArea;

    @FXML
    private Button encodeChoose;

    @FXML
    private Label encodeDir;

    @FXML
    private Button encodeConfirm;

    @FXML
    private Label encodeStatus;

    @FXML
    private Label decodeStatus;

    @FXML
    private Pane decodePane;

    @FXML
    private JFXTextArea decodeArea;

    @FXML
    private Button decodeChoose;

    @FXML
    private Label decodeDir;

    @FXML
    private Button decodeConfirm;

    @FXML
    private FontAwesomeIconView close1;

    @FXML
    private FontAwesomeIconView close2;

    @FXML
    private JFXTextField encodeKey;

    @FXML
    private JFXTextField decodeKey;

    private Encode encode = new Encode();
    private Decode decode = new Decode();
    private File encodeFile;
    private File decodeFile;

    @FXML
    void initialize() {
        encodePane.toFront();
        encodeButton.setOnAction(event -> encodePane.toFront());
        decodeButton.setOnAction(event -> decodePane.toFront());

        encodeChoose.setOnAction(event -> {
            encodeDir.setText("");
            Stage stage = (Stage) root.getScene().getWindow();
            FileChooser fileChooser = new FileChooser();
            configuringFileChooser(fileChooser);
            encodeFile = fileChooser.showOpenDialog(stage);
            try {
                encodeDir.setText(encodeFile.getAbsolutePath());
            } catch (NullPointerException ignored) {
                encodeStatus.setText("Зураг сонгогдсонгүй");
                encodeStatus.setStyle("-fx-text-fill: red");
            }
        });

        decodeChoose.setOnAction(event -> {
            decodeDir.setText("");
            Stage stage = (Stage) root.getScene().getWindow();
            FileChooser fileChooser = new FileChooser();
            configuringFileChooser(fileChooser);
            decodeFile = fileChooser.showOpenDialog(stage);
            try {
                decodeDir.setText(decodeFile.getAbsolutePath());
            } catch (NullPointerException ignored) {
                decodeStatus.setText("Зураг сонгогдсонгүй");
                decodeStatus.setStyle("-fx-text-fill: red");
            }
        });

        encodeConfirm.setOnAction(e -> {
            encodeStatus.setText("");
            if (encodeFile != null && !encodeArea.getText().isEmpty()) {
                Stage stage = (Stage) root.getScene().getWindow();
                FileChooser fileChooser = new FileChooser();
                fileChooser.setInitialFileName(encodeFile.getName());
                File file = fileChooser.showSaveDialog(stage);
                if (file == null) {
                    encodeStatus.setText("Нууцлагдсан шинэ зураг хадгалах замаа сонгоно уу");
                    encodeStatus.setStyle("-fx-text-fill: red");
                } else {
                    Image oldImage = new Image(encodeFile.toURI().toString());
                    String key = AES.getKey();
                    assert key != null;
                    Image newImage = encode.encode(oldImage, AES.encode(encodeArea.getText(), key));
                    BufferedImage bImage = SwingFXUtils.fromFXImage(newImage, null);
                    try {
                        ImageIO.write(bImage, "png", file);
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                    encodeKey.setText(key);
                    encodeStatus.setText("Амжилттай. Та доорх түлхүүрийг хадгалж авна уу!");
                    encodeStatus.setStyle("-fx-text-fill: green");
                }
            } else {
                encodeStatus.setText("Зураг эсвэл текст талбар хоосон байна");
                encodeStatus.setStyle("-fx-text-fill: red");
            }
        });

        decodeConfirm.setOnAction(e -> {
            decodeArea.setText("");
            if (decodeFile != null) {
                if (!decodeKey.getText().isEmpty()) {
                    Image image = new Image(decodeFile.toURI().toString());
                    try {
                        String data = decode.decode(image);
                        if (data.isEmpty()) throw new Exception("error");
                        String secretText = AES.decode(data, decodeKey.getText());
                        decodeArea.setText(secretText);
                        decodeStatus.setText("Амжилттай");
                        decodeStatus.setStyle("-fx-text-fill: green");
                    } catch (Exception ex) {
                        decodeStatus.setText("Нууцлагдсан мэдээлэл байхгүй байна");
                        decodeStatus.setStyle("-fx-text-fill: red");
                    }
                } else {
                    decodeStatus.setText("Нууц түлхүүр оруулаагүй байна");
                    decodeStatus.setStyle("-fx-text-fill: red");
                }
            } else {
                decodeStatus.setText("Тайлах зураг сонгоогүй байна");
                decodeStatus.setStyle("-fx-text-fill: red");
            }
        });
        close1.setOnMouseClicked(e -> Platform.exit());
        close2.setOnMouseClicked(e -> Platform.exit());
    }

    private void configuringFileChooser(FileChooser fileChooser) {
        fileChooser.setTitle("Select Pictures");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("JPG", "*.jpg"),
                new FileChooser.ExtensionFilter("PNG", "*.png")
        );
    }
}
