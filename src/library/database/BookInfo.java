package library.database;

import java.util.Date;

public class BookInfo {
    private String userName;
    private String bookTitle;
    private String bookAuthor;
    private String copyId;
    private String bookISBN;
    private int publishYear;
    private int rentalId;
    private Date rentDate;
    private Date returnDate;
    private int numberOfBooks;
    private int mostPopularBook;

    public int getMostPopularBook() {
        return mostPopularBook;
    }

    public void setMostPopularBook(int mostPopularBook) {
        this.mostPopularBook = mostPopularBook;
    }
    public int getNumberOfBooks() {
        return numberOfBooks;
    }

    public void setNumberOfBooks(int numberOfBooks) {
        this.numberOfBooks = numberOfBooks;
    }

    public Date getRentDate() {
        return rentDate;
    }

    public void setRentDate(Date rentDate) {
        this.rentDate = rentDate;
    }

    public Date getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(Date returnDate) {
        this.returnDate = returnDate;
    }

    public int getRentalId() {
        return rentalId;
    }

    public void setRentalId(int rentalId) {
        this.rentalId = rentalId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getBookTitle() {
        return bookTitle;
    }

    public void setBookTitle(String bookTitle) {
        this.bookTitle = bookTitle;
    }

    public String getBookISBN() {
        return bookISBN;
    }

    public void setBookISBN(String bookISBN) {
        this.bookISBN = bookISBN;
    }


    public int getPublishYear() {
        return publishYear;
    }

    public void setPublishYear(int publishYear) {
        this.publishYear = publishYear;
    }

    public String getBookAuthor() {
        return bookAuthor;
    }

    public void setBookAuthor(String bookAuthor) {
        this.bookAuthor = bookAuthor;
    }

    public String getCopyId() {
        return copyId;
    }

    public void setCopyId(String copyId) {
        this.copyId = copyId;
    }

    @Override
    public String toString() {
        return
                "Felhasználó: '" + userName + '\'' +
                ", cím: '" + bookTitle + '\'' +
                ", ISBN: '" + bookISBN + '\'' +
                ", kiadás éve: " + publishYear;
    }
}
