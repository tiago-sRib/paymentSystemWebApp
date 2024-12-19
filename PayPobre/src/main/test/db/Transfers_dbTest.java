package db;

import account.Commercial;
import org.junit.jupiter.api.Test;
import transactions.Transaction;

import static org.junit.jupiter.api.Assertions.*;
import static util.Const.*;

class Transfers_dbTest {

    @Test
    void connect() {
        var trans = new Transfers_db();
        System.out.println(trans.connect());

    }

    @Test
    void createTable() {
        var trans = new Transfers_db();
        System.out.println(trans.CreateTable());
    }

    @Test
    void executeTransactionSQL() {
        var trans = new Transfers_db();
        System.out.println(trans.executeTransactionSQL(68, 70, 1000.0, "1", PENDING));
    }

    @Test
    void updateTransactionSQL() {
        var trans = new Transfers_db();
        System.out.println(trans.updateTransactionSQL(72386, DONE));
    }

    @Test
    void querySQL() {
        var transDB = new Transfers_db();
        var trans = new Transaction();
        trans = transDB.querySQL(138312);
        System.out.println("trans ID = "+ trans.trans_id + " seller ID = " + trans.seller_id);
    }

    @Test
    void queryStateSQL() {
        var transDB = new Transfers_db();
        int i = 0;
        var trans = transDB.queryStateSQL(PENDING, 70);
        while (i < trans.length){
            System.out.println("trans ID = " + trans[i].trans_id + " seller ID = " + trans[i].seller_id);
            i++;
        }
    }

    @Test
    void queryAllTransfersSQL() {
        var transDB = new Transfers_db();
        int i = 0;
        var trans = transDB.queryAllTransfersSQL(70);
        while (i < trans.length){
            System.out.println("trans ID = " + trans[i].trans_id + " seller ID = " + trans[i].seller_id);
            i++;
        }
    }
}