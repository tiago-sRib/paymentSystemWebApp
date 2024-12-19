package transactions;

import db.Transfers_db;
import db.User_db;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import static util.Const.*;

import java.text.SimpleDateFormat;

public class Transaction {
    private SimpleStringProperty id;
    private SimpleStringProperty amount;
    private SimpleStringProperty to;
    private SimpleStringProperty from;
    private SimpleStringProperty date;
    private SimpleStringProperty state;

    public int seller_id;
    public int buyer_id;
    public double amountOld;
    public int trans_id;
    public String stateOld;
    public String dateOld;
    Transfers_db t_db = new Transfers_db();
    User_db u_db = new User_db();

    public Transaction (int seller_id, int buyer_id, double amount, int trans_id, String state, String date){
        this.id = new SimpleStringProperty(Integer.toString(trans_id));
        this.amount = new SimpleStringProperty(Double.toString(amount));
        this.to = new SimpleStringProperty(getNameFromID(seller_id));
        this.from = new SimpleStringProperty(getNameFromID(buyer_id));
        this.state = new SimpleStringProperty(state);
        this.date = new SimpleStringProperty(date);
    }

    public Transaction(){

    }

    public String getNameFromID(int id){
        return u_db.queryNameFromID(id);
    }

    public String getId(){
        return this.id.get();
    }

    public void setId(String id){
        this.id.set(id);
    }

    public String getAmount(){
        return amount.get();
    }

    public void setAmount(Double amount){
        this.amount.set(Double.toString(amount));
    }

    public String getTo(){
        return this.to.get();
    }

    public void setTo(int id){
        this.to.set(getNameFromID(id));
    }

    public String getFrom(){
        return this.from.get();
    }

    public void setFrom(int id){
        this.to.set(getNameFromID(id));
    }

    public String getDate(){
        return this.date.get();
    }

    public void setDate(String date){
        this.date.set(date);
    }

    public String getState(){
        return state.get();
    }

    public void setState(String state){
        if(state.equals(PENDING) || state.equals(DONE) || state.equals(CANCELED))
            this.state.set(state);
        else this.state.set(null);
    }
}
