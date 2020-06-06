package library.database;

import java.util.Date;

public class BookCopyInfo {
    private String bookTitle;
    private String bookAuthor;
    private String copyId;
    private String bookISBN;
    private int publishYear;
    private int rentalId;
    private String status;
    private String genre;



    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getBookTitle() {
        return bookTitle;
    }

    public void setBookTitle(String bookTitle) {
        this.bookTitle = bookTitle;
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

    public int getRentalId() {
        return rentalId;
    }

    public void setRentalId(int rentalId) {
        this.rentalId = rentalId;
    }

    @Override
    public String toString() {
        return
                "Cím: '" + bookTitle + '\'' + "Iro: '" +bookAuthor + '\'' +
                        ", ISBN: '" + bookISBN + '\'' +
                        ", kiadás éve: " + publishYear + '\'' +"status: "+status;
    }
}
