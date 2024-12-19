package account;

import db.Transfers_db;
import java.sql.Date;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static util.Const.*;

public class Commercial extends Personal{
    public String company;
    public Commercial(User user){
        super(user);
    }
    public Commercial(User user, String company){
        super(user);
        this.company = company;
    }

    public boolean issueTransaction(int seller_id, int buyer_id, Double amount){
        LocalDateTime oldDate = LocalDateTime.now();
        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        String date = oldDate.format(dateFormat);
        Transfers_db transfer = new Transfers_db();
        return transfer.executeTransactionSQL(seller_id, buyer_id, amount, date, PENDING);
    }
}