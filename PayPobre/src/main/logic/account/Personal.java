package account;
import db.Transfers_db;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static util.Const.*;

public class Personal extends User{
    public Personal(User user){
        super(user);
    }

    public boolean acceptTransaction(int trans_id){
        String state = DONE;
        Transfers_db transfer = new Transfers_db();
        return transfer.updateTransactionSQL(trans_id, state);
    }

    public boolean cancelTransaction(int trans_id){
        String state = CANCELED;
        Transfers_db transfer = new Transfers_db();
        return transfer.updateTransactionSQL(trans_id, state);
    }

    public boolean sendMoney(int seller_id, int buyer_id, Double amount){
        LocalDateTime oldDate = LocalDateTime.now();
        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        String date = oldDate.format(dateFormat);
        Transfers_db transfer = new Transfers_db();
        return transfer.executeTransactionSQL(seller_id, buyer_id, amount, date, INSTANTANEOUS);
    }

}
