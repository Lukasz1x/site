package org.example;

import org.example.site.auth.AccountManager;
import org.example.site.database.DatabaseConnection;


public class Main {
    public static void main(String[] args) {
        DatabaseConnection db = new DatabaseConnection();
        db.connect("accounts.db");
        AccountManager am = new AccountManager(db);
        am.init();
        am.register("test", "test");
        am.register("test2", "test2");
        am.register("test3", "test3");
        System.out.println(am.getAccount("test"));
        System.out.println(am.authenticate("test", "test"));
        System.out.println(am.authenticate("test2", "test"));

        db.disconnect();
    }
}