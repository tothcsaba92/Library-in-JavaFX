package library.ui.listUsersAndTheBooks;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import library.database.BookDB;
import library.database.BookInfo;
import library.exceptions.InvalidUserException;


import java.net.URL;
import java.util.ResourceBundle;

public class ListUsersAndBooksController implements Initializable {
    ObservableList<BookInfo>  list = FXCollections.observableArrayList();

    @FXML
    private StackPane rootPane;

    @FXML
    private AnchorPane contentPane;

    @FXML
    private TableView<BookInfo> tableView;

    @FXML
    private TableColumn<BookInfo,String> userNameCol;

    @FXML
    private TableColumn<BookInfo,String> authorCol;

    @FXML
    private TableColumn<BookInfo,String> titleCol;

    @FXML
    private TableColumn<BookInfo,String> isbnCol;

    @FXML
    private TableColumn<BookInfo,Integer> publicationCol;

    @FXML
    private TableColumn<BookInfo,String> copyIdCol;



    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initCol();
        loadData();
    }

    private void initCol() {
    userNameCol.setCellValueFactory(new PropertyValueFactory<>("userName"));
    copyIdCol.setCellValueFactory(new PropertyValueFactory<>("copyId"));
    authorCol.setCellValueFactory(new PropertyValueFactory<>("bookAuthor"));
    titleCol.setCellValueFactory(new PropertyValueFactory<>("bookTitle"));
    isbnCol.setCellValueFactory(new PropertyValueFactory<>("bookISBN"));
    publicationCol.setCellValueFactory(new PropertyValueFactory<>("publishYear"));
    }
    private void loadData() {
    list.clear();
        BookDB bookDB = new BookDB();
        try {
            list.addAll(bookDB.listUsersAndTheBooksTheyHave());
            tableView.setItems(list);
        } catch (InvalidUserException e) {
            System.out.println(e.getMessage());
        }
    }
}
