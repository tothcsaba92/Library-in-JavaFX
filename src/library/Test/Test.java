package library.Test;

import library.database.BookDB;

public class Test {
    public static void main(String[] args) {
        Console console = new Console(new BookDB());
        console.listUsers();
        System.out.println(System.getProperties());
    }
}
