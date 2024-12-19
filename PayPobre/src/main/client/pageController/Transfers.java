package pageController;

import account.*;
import db.User_db;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import transactions.Transaction;
import util.Macros;
import static util.Const.*;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import db.*;

public class Transfers extends GenericSubPage{
    // General
    private Transaction t;
    private User_db rx_db = new User_db();
    private Transfers_db t_db = new Transfers_db();
    private Connection c;
    private Statement stmt;
    private String pendingTransfersQuery = null;
    private String allTransfersQuery = null;
    @FXML private Label header;

    // Every Transaction table
    ObservableList<Transaction> EveryTransaction = FXCollections.observableArrayList();
    @FXML private TableView<Transaction> mainTable;
    @FXML private TableColumn<Transaction, String> everyId;
    @FXML private TableColumn<Transaction, String> everyAmount;
    @FXML private TableColumn<Transaction, String> everyFrom;
    @FXML private TableColumn<Transaction, String> everyTo;
    @FXML private TableColumn<Transaction, String> everyDate;
    @FXML private TableColumn<Transaction, String> everyState;


    // Issue PopUp
    ObservableList<String> transferTypeList  = FXCollections.observableArrayList("Instantaneous", "Commercial Transaction");
    @FXML private HBox issueTransactionPopup;
    @FXML private TextField issueAmountField;
    @FXML private TextField issueRxField;
    @FXML private Label logMessage_issue;
    @FXML private ComboBox<String> transferType;
    @FXML private Label issueRxLabel;

    // Pending
    ObservableList<Transaction> PendingTransactions = FXCollections.observableArrayList();
    @FXML private HBox pendingTransfersPopup;
    @FXML private Label logMessage_pending;
    @FXML private RadioButton acceptTransfer;
    @FXML private RadioButton cancelTransfer;
    @FXML private ToggleGroup Respond2Transfer;
    @FXML private TableView<Transaction> pendingTable;
    @FXML private TableColumn<Transaction, String> penDate;
    @FXML private TableColumn<Transaction, String> penFrom;
    @FXML private TableColumn<Transaction, String> penTo;
    @FXML private TableColumn<Transaction, String> penAmount;
    @FXML private TableColumn<Transaction, String> penId;


    @FXML private void backHome(ActionEvent actionEvent) throws IOException {
        goToHome(actionEvent);
    }

    @FXML private void initialize() throws SQLException {
        pendingTransfersPopup.setVisible(false);
        issueTransactionPopup.setVisible(false);
        c = t_db.connect();
        stmt = c.createStatement();

        logMessage_issue.setText("");
        logMessage_pending.setText("");
        issueAmountField.addEventFilter(KeyEvent.KEY_TYPED, Macros.numeric_Validation(10));
        issueRxLabel.setText("Email:");

        //Main Table
        everyId.setCellValueFactory(new PropertyValueFactory<Transaction, String>("Id"));
        everyAmount.setCellValueFactory(new PropertyValueFactory<Transaction, String>("Amount"));
        everyFrom.setCellValueFactory(new PropertyValueFactory<Transaction, String>("From"));
        everyTo.setCellValueFactory(new PropertyValueFactory<Transaction, String>("To"));
        everyDate.setCellValueFactory(new PropertyValueFactory<Transaction, String>("Date"));
        everyState.setCellValueFactory(new PropertyValueFactory<Transaction, String>("State"));

        //Pending
        penId.setCellValueFactory(new PropertyValueFactory<Transaction, String>("Id"));
        penAmount.setCellValueFactory(new PropertyValueFactory<Transaction, String>("Amount"));
        penFrom.setCellValueFactory(new PropertyValueFactory<Transaction, String>("From"));
        penTo.setCellValueFactory(new PropertyValueFactory<Transaction, String>("To"));
        penDate.setCellValueFactory(new PropertyValueFactory<Transaction, String>("Date"));
    }

    public void setPage(){
        header.setText(home.user.name);

        if(home.user.type.equals(COMMERCIAL))
            transferType.setItems(FXCollections.observableArrayList("Send Money", "Commercial Transaction"));

        else if(home.user.type.equals(PERSONAL))
            transferType.setItems(FXCollections.observableArrayList("Send Money"));

        pendingTransfersQuery = "SELECT *  FROM \"PayPobre\".transfers WHERE state = '" + PENDING + "' AND buyer_id = '" + home.user.user_id + "'";
        allTransfersQuery = "SELECT *  FROM \"PayPobre\".transfers WHERE buyer_id = '" + home.user.user_id + "' OR seller_id = '" + home.user.user_id + "'";
        refreshMainTable();
    }

    // Open PopUps
    @FXML private void pendingTransfersOnAction(ActionEvent actionEvent) {
        refreshPending();

        pendingTransfersPopup.setVisible(true);
    }

    @FXML private void issueTransactionOnAction(ActionEvent actionEvent) {
        logMessage_issue.setText("");
        issueRxField.clear();
        issueAmountField.clear();
        issueTransactionPopup.setVisible(true);
    }

    // close commands
    @FXML private void closePendingTransfers(ActionEvent actionEvent) {

        pendingTransfersPopup.setVisible(false);
    }

    @FXML private void closeIssueTransaction(ActionEvent actionEvent) {
        logMessage_issue.setText("");
        issueRxField.clear();
        issueAmountField.clear();
        issueTransactionPopup.setVisible(false);
    }

    // ok commands
    @FXML private void issueOkTrans(ActionEvent actionEvent) {
        String email = issueRxField.getText();
        String type = transferType.getValue();
        Commercial com = new Commercial(home.user);
        User rxUser;

        if(issueRxField.getText().isEmpty() || (transferType.getValue() == null) || issueAmountField.getText().isEmpty()){
            logMessage_issue.setText("Please fulfill every field");
            logMessage_issue.setTextFill(Color.RED);
            return;
        }

        double amount = 0.0;
        if(type.isEmpty()) {
            logMessage_issue.setText("Please select transaction type");
            logMessage_issue.setTextFill(Color.RED);
            return;
        }

        if(!Macros.emailValidator(email)){
            logMessage_issue.setText("Please insert a valid email:\n      example@test.com");
            logMessage_issue.setTextFill(Color.RED);
            return;
        }

        rxUser = rx_db.querySQLfromEmail(email);
        if( rxUser == null){
            logMessage_issue.setText("This user does not exist");
            logMessage_issue.setTextFill(Color.RED);
            return;
        }

        amount = Double.parseDouble(issueAmountField.getText());
        if(amount == 0){
            logMessage_issue.setText("Specify amount");
            logMessage_issue.setTextFill(Color.RED);
            return;
        }
        System.out.println("rx ID: " + rxUser.user_id + " tx ID: " + home.user.user_id);
        issueAmountField.clear();
        issueRxField.clear();
        switch (type){
            case ("Send Money"):
                if(!com.sendMoney(rxUser.user_id, home.user.user_id, amount)) {
                    logMessage_issue.setText("Something went wrong");
                    logMessage_issue.setTextFill(Color.RED);
                }
                else {
                    logMessage_issue.setText("Money sent successfully");
                    logMessage_issue.setTextFill(Color.GREEN);
                }
                break;

            case("Commercial Transaction"):
                if(!com.issueTransaction(home.user.user_id, rxUser.user_id, amount)) {
                    logMessage_issue.setText("Something went wrong");
                    logMessage_issue.setTextFill(Color.RED);
                }
                else {
                    logMessage_issue.setText("Transaction issued successfully\n  Wait for buyer confirmation");
                    logMessage_issue.setTextFill(Color.GREEN);
                }
                break;
        }
        refreshMainTable();
        refreshPending();
    }

    @FXML private void issueUpdateEmailField(ActionEvent actionEvent) {
        String value = transferType.getValue();
        if (value.equals("Send Money")) {
            issueRxLabel.setText("Receiver:");
        } else if (value.equals("Commercial Transaction")) {
            issueRxLabel.setText("Buyer:");
        }
    }

    @FXML private void pendingOk(ActionEvent actionEvent) {
        logMessage_pending.setText("");
        Transaction t = pendingTable.getSelectionModel().getSelectedItem();

        if(acceptTransfer.isSelected()){
            //to do - accept selected transfer in table
            if(t_db.updateTransactionSQL(Integer.parseInt(t.getId()), DONE)){
                logMessage_pending.setText("Transaction accepted");
                logMessage_pending.setTextFill(Color.GREEN);
                refreshPending();
                refreshMainTable();
            }
            else {
                logMessage_pending.setText("Not enough money\n in your PayPobre account");
                logMessage_pending.setTextFill(Color.RED);
            }
        }

        if(cancelTransfer.isSelected()){
            //to do - cancel selected transfer in table
            if(t_db.updateTransactionSQL(Integer.parseInt(t.getId()), CANCELED)){
                logMessage_pending.setText("Transaction canceled");
                logMessage_pending.setTextFill(Color.GREEN);
                refreshPending();
                refreshMainTable();
            }
            else {
                logMessage_pending.setText("Something went wrong");
                logMessage_pending.setTextFill(Color.RED);
            }
        }
    }

    private void refreshPending(){
        try {
            PendingTransactions.clear();

            ResultSet rs = stmt.executeQuery(pendingTransfersQuery);
            while (rs.next()) {
                PendingTransactions.add(new Transaction(
                        rs.getInt(1),
                        rs.getInt(2),
                        rs.getDouble(3),
                        rs.getInt(4),
                        rs.getString(5),
                        rs.getString(6)));
            }
                pendingTable.setItems(PendingTransactions);
        }catch (Exception e) {
            //e.printStackTrace();
            logMessage_pending.setText("Couldn't find any");
            logMessage_pending.setTextFill(Color.RED);
        }
    }

    private void refreshMainTable(){
        try {
            EveryTransaction.clear();

            ResultSet rs = stmt.executeQuery(allTransfersQuery);
            while (rs.next()) {
                EveryTransaction.add(new Transaction(
                        rs.getInt(1),
                        rs.getInt(2),
                        rs.getDouble(3),
                        rs.getInt(4),
                        rs.getString(5),
                        rs.getString(6)));
            }
            mainTable.setItems(EveryTransaction);

        }catch (Exception e) {
            e.printStackTrace();
            //logMessage_pending.setText("Couldn't find any");
        }
    }
}
