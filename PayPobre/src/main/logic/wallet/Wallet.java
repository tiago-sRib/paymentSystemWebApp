package wallet;

import account.*;
import db.User_db;

public class Wallet{
    public String card;
    public double money;

    //public Wallet(){ this.card = new Card(); }

    public boolean deposit(double input, User user) {
        User_db user_db = new User_db();
        user.wallet.money = user.wallet.money + input;
        String sql = "UPDATE \"PayPobre\".users SET money = '" + user.wallet.money + "' WHERE email = '" + user.email + "'";
        return user_db.updateSQL(sql);
    }

    public boolean withdraw(double input, User user) {
        User_db user_db = new User_db();
        if(user.wallet.money < input) return false;

        user.wallet.money = user.wallet.money - input;
        String sql = "UPDATE \"PayPobre\".users SET money = '" + user.wallet.money + "' WHERE email = '" + user.email + "'";
        return user_db.updateSQL(sql);
    }

    public String formatCard(long cardNumber) {
        String cardString;
        try {
            cardString = Long.toString(cardNumber);
        } catch (NumberFormatException e) {
            return null;
        }

        char delimiter = ' ';
        return cardString.replaceAll(".{4}(?!$)", "$0" + delimiter);
    }
}
