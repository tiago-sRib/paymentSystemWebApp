package db;
import account.Commercial;
import account.User;
import transactions.Transaction;

import java.sql.*;
import java.util.Random;

import static util.Const.*;

public class Transfers_db {
    private Connection c;
    private String db_UserName = "pswt0203";
    private String db_PassWord = "plataoplomo";
    private String db_URL = "jdbc:postgresql://db.fe.up.pt:5432/";
    private String output_msg = null;

    public Connection connect() {
        try {
            Class.forName("org.postgresql.Driver");
            c = DriverManager.getConnection(db_URL, db_UserName, db_PassWord);
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
            return null;
        }
        return c;
    }

    public String CreateTable(){
        try {
            //Class.forName("org.postgresql.Driver");
            c = DriverManager.getConnection(db_URL, db_UserName, db_PassWord);
            Statement stmt = c.createStatement();
            String sql = "CREATE TABLE IF NOT EXISTS \"PayPobre\".transfers (" +
                    "seller_id integer NOT NULL," +
                    "buyer_id text NOT NULL," +
                    "amount numeric NOT NULL," +
                    "trans_id text UNIQUE NOT NULL PRIMARY KEY," +
                    "state text NOT NULL," +
                    "date text NOT NULL);";

            stmt.executeUpdate(sql);
            stmt.close();
            c.close();
            output_msg = output_msg + "\n" + "Table created successfully";
        } catch (Exception e) {
            e.printStackTrace();
            return e.getMessage();
        }
        return output_msg;
    }
    public boolean executeTransactionSQL(int seller_id, int buyer_id, Double amount, String date, String state){
        var user_db = new User_db();
        if((user_db.querySQLfromID(seller_id) == null) || (user_db.querySQLfromID(buyer_id) == null)) return false;
        try {
            c = DriverManager.getConnection(db_URL, db_UserName, db_PassWord);
            Statement stmt = c.createStatement();
            Random rand = new Random();
            int upperbound = 999999;
            int trans_id = rand.nextInt(upperbound);
            if(state == PENDING) {
                String sql = "INSERT into \"PayPobre\".transfers (seller_id, buyer_id, amount, trans_id, state, date)" +
                        "VALUES ('" + seller_id + "', '" + buyer_id + "', '" + amount + "', '" + trans_id + "', '" + state + "', '" + date + "')";
                while (stmt.executeUpdate(sql) == -1) {
                    trans_id = rand.nextInt(upperbound);
                    sql = "INSERT into \"PayPobre\".transfers (seller_id, buyer_id, amount, trans_id, state, date)" +
                            "VALUES ('" + seller_id + "', '" + buyer_id + "', '" + amount + "', '" + trans_id + "', '" + state + "', '" + date + "')";
                }
                stmt.close();
                c.close();
                return true;
            }
            else if(state == INSTANTANEOUS){
                System.out.println("1");
                Transaction trans = new Transaction();
                User seller = user_db.querySQLfromID(seller_id);
                User buyer = user_db.querySQLfromID(buyer_id);
                System.out.println("money " + buyer.wallet.money);
                if (buyer.wallet.money < amount) return false;


                String sql = "INSERT into \"PayPobre\".transfers (seller_id, buyer_id, amount, trans_id, state, date)" +
                        "VALUES ('" + seller_id + "', '" + buyer_id + "', '" + amount + "', '" + trans_id + "', '" + state + "', '" + date + "')";
                user_db.updateSQL(sql);
                System.out.println("3");


                double moneySeller = seller.wallet.money + amount;
                System.out.println( "money seller  " + moneySeller);
                System.out.println( "ID seller  " + seller.user_id);
                String sql_update_money_seller = "UPDATE \"PayPobre\".users SET money = '" + moneySeller + "' WHERE user_id = '" + seller_id + "'";
                user_db.updateSQL(sql_update_money_seller);

                System.out.println("4");
                double moneyBuyer = buyer.wallet.money - amount;
                String sql_update_money_buyer = "UPDATE \"PayPobre\".users SET money = '" + moneyBuyer + "' WHERE user_id = '" + buyer_id + "'";
                user_db.updateSQL(sql_update_money_buyer);
                stmt.close();
                c.close();
                System.out.println("5");
                return true;
            }
            stmt.close();
            c.close();
            return true;

        }catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    public boolean updateTransactionSQL(int trans_id, String state){
        User_db user_db = new User_db();
        Transfers_db trans_db = new Transfers_db();
        Transaction trans;

        try {
            var userDB = new User_db();
            var user = new User();
            trans = trans_db.querySQL(trans_id);
            if(trans == null) return false;

            user = userDB.querySQLfromID(trans.buyer_id);

            System.out.println("money buyer: " + user.wallet.money);

            c = DriverManager.getConnection(db_URL, db_UserName, db_PassWord);
            Statement stmt = c.createStatement();
            trans = trans_db.querySQL(trans_id);
            if (trans.stateOld.equals(PENDING)){
                if (state.equals(DONE)) {
                    if (user.wallet.money < trans.amountOld) return false;
                    Commercial seller = new Commercial(user_db.querySQLfromID(trans.seller_id));
                    double moneySeller = seller.wallet.money + trans.amountOld;
                    String sql_update_money_seller = "UPDATE \"PayPobre\".users SET money = '" + moneySeller + "' WHERE user_id = '" + trans.seller_id + "'";
                    user_db.updateSQL(sql_update_money_seller);

                    User buyer = user_db.querySQLfromID(trans.buyer_id);
                    int moneyBuyer = (int) (buyer.wallet.money - trans.amountOld);
                    String sql_update_money_buyer = "UPDATE \"PayPobre\".users SET money = '" + moneyBuyer + "' WHERE user_id = '" + trans.buyer_id + "'";
                    user_db.updateSQL(sql_update_money_buyer);

                    String sql_update_transaction = "UPDATE \"PayPobre\".transfers SET state = '" + state + "' WHERE trans_id = '" + trans_id + "'";
                    trans_db.updateSQL(sql_update_transaction);
                } else {
                    String sql_update_transaction = "UPDATE \"PayPobre\".transfers SET state = '" + CANCELED + "' WHERE trans_id = '" + trans_id + "'";
                    trans_db.updateSQL(sql_update_transaction);
                }
                stmt.close();
                c.close();
                return true;
            } else{
                stmt.close();
                c.close();
                return false;
            }
        }catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public Transaction querySQL(int ID){
        Transaction trans = new Transaction();
        try {
            c = DriverManager.getConnection(db_URL, db_UserName, db_PassWord);
            Statement stmt = c.createStatement();
            String query = "SELECT *  FROM \"PayPobre\".transfers WHERE trans_id = '" + ID + "'";
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                trans.seller_id = rs.getInt(1);
                trans.buyer_id = rs.getInt(2);
                trans.amountOld = rs.getDouble(3);
                trans.trans_id = rs.getInt(4);
                trans.stateOld = rs.getString(5);
                trans.dateOld = rs.getString(6);
            }
            if(trans.trans_id == 0) return null;
            stmt.close();
            c.close();
            return trans;

        }catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    public Transaction[] queryStateSQL(String state, int userID){
        try {
            c = DriverManager.getConnection(db_URL, db_UserName, db_PassWord);
            Statement stmt = c.createStatement();
            String query = "SELECT *  FROM \"PayPobre\".transfers WHERE state = '" + state + "' AND (buyer_id = '" + userID + "' OR seller_id = '" + userID + "')";
            ResultSet rs = stmt.executeQuery(query);
            int length = queryLengthUserStateSQL(state, userID);
            Transaction[] trans = new Transaction[length];

            int i = 0;
            while (rs.next()) {
                trans[i] = new Transaction();
                trans[i].seller_id = rs.getInt(1);
                trans[i].buyer_id = rs.getInt(2);
                trans[i].amountOld = rs.getDouble(3);
                trans[i].trans_id = rs.getInt(4);
                trans[i].stateOld = rs.getString(5);
                trans[i].dateOld = rs.getString(6);
                i++;
            }
            stmt.close();
            c.close();
            return trans;

        }catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    public Transaction[] queryAllTransfersSQL(int userID){
        try {
            c = DriverManager.getConnection(db_URL, db_UserName, db_PassWord);
            Statement stmt = c.createStatement();
            String query = "SELECT *  FROM \"PayPobre\".transfers WHERE buyer_id = '" + userID + "' OR seller_id = '" + userID + "'";
            ResultSet rs = stmt.executeQuery(query);
            int length = queryLengthUserSQL(userID);
            Transaction[] trans = new Transaction[length];
            int i = 0;
            while (rs.next()) {
                trans[i] = new Transaction();
                trans[i].seller_id = rs.getInt(1);
                trans[i].buyer_id = rs.getInt(2);
                trans[i].amountOld = rs.getDouble(3);
                trans[i].trans_id = rs.getInt(4);
                trans[i].stateOld = rs.getString(5);
                trans[i].dateOld = rs.getString(6);
                i++;
            }
            stmt.close();
            c.close();
            return trans;

        }catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    public int queryLengthUserStateSQL(String state, int userID){
        try {
            c = DriverManager.getConnection(db_URL, db_UserName, db_PassWord);
            Statement stmt = c.createStatement();
            String query = "SELECT *  FROM \"PayPobre\".transfers WHERE state = '" + state + "' AND (buyer_id = '" + userID + "' OR seller_id = '" + userID + "')";
            ResultSet rs = stmt.executeQuery(query);
            int i = 0;
            while (rs.next()) {
                rs.getInt(1);
                i++;
            }
            stmt.close();
            c.close();
            return i;

        }catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }
    public int queryLengthUserSQL(int userID){
        try {
            c = DriverManager.getConnection(db_URL, db_UserName, db_PassWord);
            Statement stmt = c.createStatement();
            String query = "SELECT *  FROM \"PayPobre\".transfers WHERE buyer_id = '" + userID + "' OR seller_id = '" + userID + "'";
            ResultSet rs = stmt.executeQuery(query);
            int i = 0;
            while (rs.next()) {
                rs.getInt(1);
                i++;
            }
            stmt.close();
            c.close();
            return i;

        }catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }
    public boolean updateSQL(String sql){
        try {
            c = DriverManager.getConnection(db_URL, db_UserName, db_PassWord);
            Statement stmt = c.createStatement();
            stmt.executeUpdate(sql);
            stmt.close();
            c.close();
            return true;

        }catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


}