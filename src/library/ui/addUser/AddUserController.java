package library.ui.addUser;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import javafx.fxml.FXML;

import java.awt.event.ActionEvent;

import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import library.database.BookDB;

import java.net.URL;
import java.util.ResourceBundle;

public class AddUserController implements Initializable {
    private BookDB bdb = new BookDB();

    @FXML
    private JFXTextField userName;

    @FXML
    private JFXTextField address;

    @FXML
    private JFXTextField email;

    @FXML
    private JFXTextField phoneNumber;

    @FXML
    private JFXTextField identityCard;

    @FXML
    private JFXTextField birthDate;

    @FXML
    private JFXButton addButton;

    @FXML
    private JFXButton backButton;

    @FXML
    void addUser(ActionEvent event) {

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }


    public void addUser(javafx.event.ActionEvent actionEvent) {
        String userName1 = userName.getText();
        String userAddress = address.getText();
        String userEmail = email.getText();
        String userPhoneNumber = phoneNumber.getText();
        String userIdentityCard = identityCard.getText();
        String userBirthDate = birthDate.getText();

        if (userName1 == null || userAddress == null || userEmail == null || userPhoneNumber == null ||
                userIdentityCard == null || userBirthDate == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(null);
            alert.setContentText("All fields must be entered!");
            alert.showAndWait();
            return;
        }
        bdb.insertNewUserToDatabase(userName1, userAddress, userEmail, Long.parseLong(userPhoneNumber), userIdentityCard, userBirthDate);
    }

    public void backToMain(javafx.event.ActionEvent actionEvent) {
    }
}

