package library.Test;

import library.database.BookCopyInfo;
import library.database.BookInfo;
import library.exceptions.InvalidBookException;
import library.exceptions.InvalidUserException;
import library.database.BookDB;

import java.sql.Date;
import java.sql.SQLException;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class Console {
    BookDB bookDB;
    Scanner sc = new Scanner(System.in);

    public Console(BookDB bdb) {
        bookDB = bdb;
    }

    public void printInfo() {
        System.out.println("Ezen lehetosegek kozul tudsz valasztani:");
        System.out.println("1. uj felhasznalo regisztralasa");
        System.out.println("2. uj konyv regisztralasa");
        System.out.println("3. kereses a konyvek kozott(szerzo, cim, kiadiev, mufaj, isbn)");
        System.out.println("4. listazza ki a felhasznalokat es a naluk levo konyveket");
        System.out.println("5. listazzon ki egy adott felhasznalo es a nala levo konyveket");
        System.out.println("6. konyv berlese");
        System.out.println("7. konyv visszavetele");
        System.out.println("8. adott konyv osszes korabbi kolcsonzese");


    }

    public void welcomeMessage() {
        System.out.println("Udv a konyvtarkezelo programunkban!");

    }

    public void backToMain() {
        System.out.println("nyomj le bármilyen karaktert, ha vissza akarsz lepni a menube, X ha kilepni akarsz");
        String option = sc.nextLine();
        switch (option.toUpperCase()) {
            case "B":
                mainMenu();
                break;
            case "X":
                System.out.println("viszlat");
                System.exit(1);

        }
        mainMenu();
    }

    public void mainMenu() {
        printInfo();
        try {
            int option = sc.nextInt();
            sc.nextLine();
            switch (option) {
                case 1:
                    userRegistration();
                    backToMain();
                    break;
                case 2:
                    insertBook();
                    backToMain();
                    break;
                case 3:
                    searchForBook();
                    backToMain();
                    break;
                case 4:
                    listUsers();
                    backToMain();
                    break;
                case 5:
                    listAParticularUser();
                    backToMain();
                    break;
                case 6:
                    rentABook();
                    backToMain();
                    break;
                case 7:
                    returnABook();
                    backToMain();
                    break;
                case 8:
                    prevBorrows();
                    backToMain();
                    break;

            }

        } catch (InputMismatchException e) {
            System.out.println(e.getMessage());
        }


    }

    public void userRegistration() {
        System.out.println("Add meg a neved");
        String userName = sc.nextLine();
        System.out.println("Add meg a címed:");
        String userAddress = sc.nextLine();
        System.out.println("Add meg az e-mail címed:");
        String userEmail = sc.nextLine();
        System.out.println("Add meg a telefonszámod:");
        long userPhone = sc.nextLong();
        sc.nextLine();
        System.out.println("Add meg a személyig. számod:");
        String userId = sc.nextLine();
        System.out.println("Add meg a születési éved:");
        String userDate = sc.nextLine();
        bookDB.insertNewUserToDatabase(userName, userAddress, userEmail, userPhone, userId, userDate);
    }

    public void listUsers() {
        try {
            for (BookInfo bookInfo : bookDB.listUsersAndTheBooksTheyHave()) {
                System.out.println(bookInfo);
            }
        } catch (InvalidUserException e) {
            System.out.println(e.getMessage());
        }
    }

    public void listAParticularUser() {
        System.out.println("Add meg, hogy melyik felhasználóra vagy kíváncsi: ");
        Scanner sc = new Scanner(System.in);
        String userName = sc.nextLine();
        try {
            System.out.println(bookDB.listParticularUser(userName));
        } catch (InvalidUserException e) {
            System.out.println(e.getMessage());
            listAParticularUser();
        }
    }

    public void nrOfBorrows(){
        System.out.println("Add meg, melyik felhasználó kölcsönzéseire vagy kíváncsi: ");
        String userName = sc.nextLine();
        try {
            for (BookInfo bookInfo : bookDB.nrOfBorrowsForAParticularUser(userName)) {
                System.out.println("Felhaszáló: "+bookInfo.getUserName()+ ", kikölcsönzött könyvek száma: "+bookInfo.getNumberOfBooks());
            }
        } catch (InvalidUserException e) {
            System.out.println(e.getMessage());
        }
    }

    public void rentABook() {
        System.out.println("Konyv cime:");
        String title = sc.nextLine();
        System.out.println("Konyv szerzoje:");
        String author = sc.nextLine();
        System.out.println("Add meg a regisztrációs számodat: ");
        int userId = sc.nextInt();
        sc.nextLine();

        try {
            bookDB.rentBook(title, author, userId);
        } catch (InvalidBookException e) {
            System.out.println(e.getMessage());
        }
    }

    public void returnABook() {
        System.out.println("regisztracios szamod");
        String userID = sc.nextLine();
        System.out.println("konyv cime");
        String title = sc.nextLine();
        System.out.println("konyv szerzoje");
        String author = sc.nextLine();

        try {
            System.out.println(bookDB.returnBook(userID, title, author));
        } catch (InvalidBookException e) {
            System.out.println(e.getMessage());
            returnABook();
        }
    }

    public void searchForBook() {
        System.out.println("author");
        String author = sc.nextLine();
        System.out.println("title");
        String title = sc.nextLine();
        System.out.println("genre");
        String genre = sc.nextLine();
        System.out.println("year");
        String year = sc.nextLine();
        System.out.println("isbn");
        String isbn = sc.nextLine();
        try {
            for (BookCopyInfo bookCopyInfo : bookDB.searchForBook(author, title, genre, year, isbn)) {
                System.out.println(bookCopyInfo);
            }
        } catch (InvalidUserException e) {
            System.out.println(e.getMessage());
        }
    }


    public void trafficOnSelectedDate() {
        System.out.println("add meg a datumot");
        String dateString = sc.nextLine();
        Date date = Date.valueOf(dateString);
        System.out.println(bookDB.trafficOnSelectedDate(date));
    }

    public void insertBook() {
        System.out.println("cim");
        String title = sc.nextLine();
        System.out.println("szerzo");
        String author = sc.nextLine();
        System.out.println("kiadasi datum");
        String publicationYear = sc.nextLine();
        System.out.println("borito(1/0)");
        int cover = sc.nextInt();
        sc.nextLine();
        System.out.println("korhatar");
        String age = sc.nextLine();
        System.out.println("isbn");
        String isbn = sc.nextLine();
        System.out.println("masolatok szama");
        int numberOfCopies = sc.nextInt();
        System.out.println("kiado");
        String publisher = sc.nextLine();
        sc.nextLine();

        try {
            bookDB.insertBook(title, author, publicationYear,  isbn, numberOfCopies,publisher);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void prevBorrows() {
        System.out.println("Add meg, hogy melyik könyv korábbi kölcsönzéseire vagy kíváncsi: ");
        String title = sc.nextLine();
        try {
            List<BookInfo> bookInfoList = bookDB.previousBorrowsOfABook(title);
            if (bookInfoList.isEmpty()) {
                System.out.println("Ez a könyv még nem lett kikölcsönözve!");
            } else {
                for (BookInfo bookInfo : bookInfoList) {
                    System.out.println("Könyv címe: " + bookInfo.getBookTitle()
                            + ", ISBN száma" + bookInfo.getBookISBN()
                            + ", bérlési dátuma: " + bookInfo.getRentDate()
                            + ", visszahozatal dátuma: " + bookInfo.getReturnDate()
                            + ", bérlő neve: " + bookInfo.getUserName());
                }
            }

        } catch (InvalidBookException e) {
            System.out.println(e.getMessage());
            prevBorrows();
        }
    }

    public void deleteBook(){
        System.out.println(" isbn ");
        String isbn = sc.nextLine();
        try {
            bookDB.deleteBook(isbn);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void mostPopularBook(){
        for (BookInfo bookInfo : bookDB.mostPopularBook()) {
            System.out.print("Cim: "+bookInfo.getBookTitle()+", szerzo: " +
                    ""+bookInfo.getBookAuthor()+" popularitas:"+bookInfo.getMostPopularBook());
        }
    }

}
