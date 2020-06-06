package library.ui.addBook;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import library.database.BookDB;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class Controller implements Initializable {
    BookDB bdb;
    @FXML
    private JFXTextField author;

    @FXML
    private JFXTextField title;

    @FXML
    private JFXTextField publisher;

    @FXML
    private JFXTextField yearofpublication;

    @FXML
    private JFXTextField isbn;

    @FXML
    private JFXTextField genre;

    @FXML
    private JFXTextField quantity;

    @FXML
    private JFXButton saveButton;

    @FXML
    private JFXButton cancelButton;

    @FXML
    void cancel(ActionEvent event) {

    }

    @FXML
    void save(ActionEvent event) throws SQLException {
        String bookAuthor = author.getText();
        String bookTitle = title.getText();
        String bookPublication = yearofpublication.getText();
        String bookIsbn = isbn.getText();
        int bookQuantity = Integer.parseInt(quantity.getText());
        String bookPublisher = publisher.getText();

        if(bookAuthor.isEmpty() || bookTitle.isEmpty() || bookPublication.isEmpty() || bookIsbn.isEmpty() ||
         bookQuantity == 0){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(null);
            alert.setContentText("All fields must be entered!");
            alert.showAndWait();
            return;
        }
        bdb.insertBook(bookTitle,bookAuthor,bookPublication,bookIsbn,bookQuantity,bookPublisher);

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
    }
}

