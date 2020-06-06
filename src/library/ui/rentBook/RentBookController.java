package library.ui.rentBook;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import library.database.BookDB;
import library.exceptions.InvalidBookException;

import java.net.URL;
import java.util.ResourceBundle;

public class RentBookController implements Initializable {
    BookDB bdb =new BookDB();

    @FXML
    private StackPane rootPane;

    @FXML
    private AnchorPane mainContainer;

    @FXML
    private JFXTextField userID;

    @FXML
    private JFXTextField title;

    @FXML
    private JFXTextField author;

    @FXML
    private JFXButton applyButton;

    @FXML
    private JFXButton cancelButton;

    @FXML
    private JFXTextArea alert;

    @FXML
    void cancel(ActionEvent event) {
        Stage stage = (Stage) rootPane.getScene().getWindow();
        stage.close();

    }

    @FXML
    void rentBook(ActionEvent event) throws InvalidBookException {
        String userId = userID.getText();
        String bookTitle = title.getText();
        String bookAuthor = author.getText();
        System.out.println(bdb.rentBook(bookTitle,bookAuthor,Integer.parseInt(userId)).size());
        if (bdb.rentBook(bookTitle,bookAuthor,Integer.parseInt(userId)).isEmpty()) {
            alert.setVisible(true);
        }

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
    alert.setVisible(false);
    }
}
