package library.database;

import library.exceptions.InvalidBookException;
import library.exceptions.InvalidUserException;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class BookDB {
    private Connection conn = null;
    private static final String URL = "jdbc:mysql://localhost/library?serverTimezone=UTC";


    public void makeConnectionWithDatabase() {
        try {
            conn = DriverManager.getConnection(URL, "root", "root");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public BookDB() {
        makeConnectionWithDatabase();
    }

    public void insertNewUserToDatabase(String userName, String userAddress, String userEmail, long userPhone,
                                        String userId, String userDate) {
        try {
            PreparedStatement ps = conn.prepareStatement("INSERT INTO `library`.`user` (`name`, `addres`, `e-mail`, `phone_number`," +
                    " `Identity_card`, `birth_date`) VALUES " + "(?, ?, ?, ?, ?, ?);");

            ps.setString(1, userName);
            ps.setString(2, userAddress);
            ps.setString(3, userEmail);
            ps.setLong(4, userPhone);
            ps.setString(5, userId);
            ps.setString(6, userDate);

            int i = ps.executeUpdate();

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }


    public List<BookInfo> listUsersAndTheBooksTheyHave() throws InvalidUserException {
        try {
            PreparedStatement ps = conn.prepareStatement("SELECT \n" +
                    "    user.name,\n" +
                    "    book.title,\n" +
                    "    book_copy.ISBN,\n" +
                    "    book_copy.publication_year,\n" +
                    "    book_copy.is_book_cover_hard\n" +
                    "FROM\n" +
                    "    user\n" +
                    "        JOIN\n" +
                    "    book_rent ON user.id = book_rent.user_id\n" +
                    "        JOIN\n" +
                    "    book_copy ON book_rent.book_copy_id = book_copy.id\n" +
                    "        JOIN\n" +
                    "    book ON book_copy.book_id = book.id\n" +
                    "    ORDER BY user.name");

            ResultSet rs = ps.executeQuery();
            return saveBookInfo(rs);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return new ArrayList<>();
    }

    public List<BookInfo> listParticularUser(String userName) throws InvalidUserException {
        try {
            PreparedStatement ps = conn.prepareStatement("SELECT user.name, book.title, book_copy.ISBN, book_copy.publication_year, book_copy.is_book_cover_hard\n" +
                    "FROM user\n" +
                    "JOIN book_rent ON user.id = book_rent.user_id\n" +
                    "JOIN book_copy ON book_rent.book_copy_id = book_copy.id\n" +
                    "JOIN book ON book_copy.book_id = book.id\n" +
                    "WHERE user.name = ?");

            ps.setString(1, userName);
            ResultSet rs = ps.executeQuery();
            return saveBookInfo(rs);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return new ArrayList<>();
    }


    public List<BookInfo> saveBookInfo(ResultSet rs) throws SQLException, InvalidUserException {
        List<BookInfo> bookInfoList = new ArrayList<>();
        if (rs.next()) {
            while (rs.next()) {
                BookInfo bi = new BookInfo();
                bi.setUserName(rs.getString("name"));
                bi.setBookTitle(rs.getString("title"));
                bi.setBookISBN(rs.getString("ISBN"));
                bi.setPublishYear(rs.getInt("publication_year"));
                bookInfoList.add(bi);
            }
        } else {
            throw new InvalidUserException("Nincs ilyen felhasználó!");
        }
        return bookInfoList;
    }

    private List<BookCopyInfo> getBookCopyInfo(ResultSet rs) throws SQLException, InvalidUserException {
        List<BookCopyInfo> bookCopyInfoList = new ArrayList<>();
        while (rs.next()) {
            BookCopyInfo bci = new BookCopyInfo();
            //rs.findColumn()
            bci.setStatus(rs.getString("status"));
            bci.setBookTitle(rs.getString("title"));
            bci.setBookISBN(rs.getString("ISBN"));
            bci.setBookAuthor(rs.getString("Author"));
            bci.setGenre(rs.getString("genre"));
            bci.setPublishYear(rs.getInt("publication_year"));
            bookCopyInfoList.add(bci);
        }
        return bookCopyInfoList;
    }

    public List<BookInfo> previousBorrowsOfABook(String title) throws InvalidBookException {
        List<BookInfo> prevBorrowList = new ArrayList<>();

        try {
            PreparedStatement ps = conn.prepareStatement("SELECT book.title, book_copy.id, book_copy.ISBN, book_copy.is_book_cover_hard, book_rent.rent_date, book_rent.return_date, user.name\n" +
                    "FROM book\n" +
                    "JOIN book_copy ON book.id = book_copy.book_id\n" +
                    "JOIN book_rent ON book_copy.id = book_rent.book_copy_id\n" +
                    "JOIN user ON book_rent.user_id = user.id\n" +
                    "WHERE title = ?");
            ps.setString(1, title);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                while (rs.next()) {
                    BookInfo bi = new BookInfo();
                    bi.setBookTitle(rs.getString("title"));
                    bi.setCopyId(rs.getString("id"));
                    bi.setBookISBN(rs.getString("ISBN"));
                    bi.setRentDate(rs.getDate("rent_date"));
                    bi.setUserName(rs.getString("name"));
                    prevBorrowList.add(bi);
                }
            }
            if (prevBorrowList.isEmpty()) {
                throw new InvalidBookException("Nincs ilyen könyv a könyvtárunkban!");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return prevBorrowList;
    }

    public List<BookInfo> nrOfBorrowsForAParticularUser(String userName) throws InvalidUserException {
        List<BookInfo> nrOfBorrowList = new ArrayList<>();
        try {
            PreparedStatement ps = conn.prepareStatement("SELECT user.name, COUNT(book_rent.user_id) AS numberOfBorrows\n" +
                    "FROM user\n" +
                    "JOIN book_rent ON user.id = book_rent.user_id\n" +
                    "JOIN book_copy ON book_rent.book_copy_id = book_copy.id\n" +
                    "JOIN book ON book_copy.book_id = book.id\n" +
                    "WHERE user.name = ?");
            ps.setString(1, userName);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                BookInfo bi = new BookInfo();
                bi.setUserName(rs.getString("name"));
                bi.setNumberOfBooks(rs.getInt("numberOfBorrows"));
                nrOfBorrowList.add(bi);

            }
            if (nrOfBorrowList.get(0).getNumberOfBooks() == 0 && nrOfBorrowList.get(0).getUserName() == null) {
                throw new InvalidUserException("Nincs ilyen felhasználó a rendszerben!");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return nrOfBorrowList;
    }

    public List<BookInfo> rentBook(String title, String author, int userId) throws InvalidBookException {
        List<BookInfo> availableBooks = new ArrayList<>();
        try {
            PreparedStatement ps = conn.prepareStatement("SELECT title, Author, book_copy.id as copyId FROM book_copy\n" +
                    "JOIN book ON book_copy.book_id = book.id\n" +
                    "LEFT JOIN book_rent ON book_copy.book_id = book_rent.book_copy_id " +
                    "WHERE title = ? AND Author = ? AND status = 'available'");
            ps.setString(1, title);
            ps.setString(2, author);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                BookInfo bi = new BookInfo();
                bi.setBookTitle(rs.getString("title"));
                bi.setBookAuthor(rs.getString("Author"));
                bi.setCopyId(rs.getString("copyID"));
                availableBooks.add(bi);
            }
            if (!availableBooks.isEmpty()) {
                updateBookCopyAsRented(availableBooks);
                createRentalRecord(userId, availableBooks);
            } else {
                throw new InvalidBookException("Nincs ilyen könyv!");
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return availableBooks;
    }

    private void updateBookCopyAsRented(List<BookInfo> availableBooks) {
        try {
            //update book_copy table

            PreparedStatement ps = conn.prepareStatement("UPDATE `library`.`book_copy` SET `status` = 'rented' WHERE (`id` = ?)");
            ps.setInt(1, Integer.parseInt(availableBooks.get(0).getCopyId()));
            ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    private void createRentalRecord(int userId, List<BookInfo> availableBooks) {
        try {
            PreparedStatement ps = conn.prepareStatement("INSERT INTO `library`.`book_rent` (`rent_date`, `user_id`," +
                    " `book_copy_id`, `expected_return_date`) VALUES (?, ?, ?, ?)");

            Date date = new Date();
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            String dateOfToday = formatter.format(date);
            Date date2 = new Date();
            date2.setMonth(date.getMonth() + 1);
            String returnDate = formatter.format(date2);

            ps.setString(1, dateOfToday);
            ps.setInt(2, userId);
            ps.setInt(3, Integer.parseInt(availableBooks.get(0).getCopyId()));
            ps.setString(4, returnDate);

            ps.executeUpdate();
            System.out.println("sikeresen kivetted a konyvet");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public String returnBook(String userID, String title, String author) throws InvalidBookException {
        try {
            BookInfo bookForReturn = new BookInfo();

            PreparedStatement ps = conn.prepareStatement("SELECT book_rent.id AS rental_id,book_copy.id AS copy_id, title FROM book_copy\n" +
                    "JOIN book_rent ON book_copy.id = book_rent.book_copy_id\n" +
                    "JOIN book ON book_copy.book_id = book.id\n" +
                    "WHERE status = 'rented' AND user_id = ? AND title = ? AND Author = ? AND return_date IS NULL");
            ps.setString(1, userID);
            ps.setString(2, title);
            ps.setString(3, author);
            ResultSet rs = ps.executeQuery();
            String proofOfValidity = null;
            if (rs.next()) {
                proofOfValidity = rs.getString("title");
            }
            if (proofOfValidity != null) {

                bookForReturn.setRentalId(rs.getInt("rental_id"));
                bookForReturn.setCopyId(rs.getString("copy_id"));

                updateBookCopyAsAvailable(bookForReturn);
                updateReturnDateForReturnment(bookForReturn);
                return "sikeresen visszaadtad a konyvet a konyvtarba!";
            } else {
                throw new InvalidBookException("problema akadt a visszavetel soran");
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    private void updateBookCopyAsAvailable(BookInfo bi) {
        try {
            PreparedStatement ps = conn.prepareStatement("UPDATE `library`.`book_copy` SET `status` = 'available' WHERE (`id` = ?)");
            ps.setString(1, bi.getCopyId());
            ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    private void updateReturnDateForReturnment(BookInfo bi) {
        try {
            PreparedStatement ps = conn.prepareStatement("UPDATE `library`.`book_rent` SET `return_date` = ?" +
                    " WHERE (`id` = ?)");

            Date date = new Date();
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            String dateOfToday = formatter.format(date);

            ps.setString(1, dateOfToday);
            ps.setInt(2, bi.getRentalId());
            ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public List<BookCopyInfo> searchForBook(String author, String title, String genre, String publicationYear, String ISBN)
            throws InvalidUserException {
        try {
            String basicQuery = "SELECT book_copy.id, author, book.title,book_copy.publication_year,\n" +
                    "                     book_copy.is_book_cover_hard, status, ISBN, genre.genre FROM book_has_genre\n" +
                    "                                        JOIN book ON book_has_genre.book_id = book.id\n" +
                    "                                        JOIN genre ON book_has_genre.genre_id = genre.id\n" +
                    "                                       JOIN book_copy ON book.id = book_copy.book_id\n" +
                    "                    WHERE 1=1 ";

            ResultSet rs = multiSearch(author, title, genre, publicationYear, ISBN, basicQuery).executeQuery();
            return getBookCopyInfo(rs);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return new ArrayList<>();
    }

    public PreparedStatement multiSearch(String author, String title, String genre, String publicationYear, String ISBN,
                                         String basicQuery) throws SQLException {
        if (!author.equals("")) {
            basicQuery += (" AND Author LIKE ?");
        }
        if (!title.equals("")) {
            basicQuery += (" AND title LIKE ?");
        }
        if (!genre.equals("")) {
            basicQuery += (" AND genre LIKE ?");
        }
        if (!publicationYear.equals("")) {
            basicQuery += (" AND publication_year LIKE ?");
        }
        if (!ISBN.equals("")) {
            basicQuery += (" AND ISBN LIKE ?");
        }
        PreparedStatement ps = conn.prepareStatement(basicQuery);
        int parameterCounter = 0;
        if (!author.equals("")) {
            ps.setString(++parameterCounter, "%" + author + "%");
        }
        if (!title.equals("")) {
            ps.setString(++parameterCounter, "%" + title + "%");
        }
        if (!genre.equals("")) {
            ps.setString(++parameterCounter, "%" + genre + "%");
        }
        if (!publicationYear.equals("")) {
            ps.setString(++parameterCounter, "%" + publicationYear + "%");
        }
        if (!ISBN.equals("")) {
            ps.setString(++parameterCounter, "%" + ISBN + "%");
        }
        return ps;
    }


    public void insertBook(String title, String writer,
                           String publishDate, String isbnNr, int numberOfCopies, String publisher) throws SQLException {
        PreparedStatement ps2 = conn.prepareStatement("INSERT INTO `library`.`book` (`title`, `author`)" +
                " VALUES (?,?);\n");
        ps2.setString(1, title);
        ps2.setString(2, writer);
        ps2.executeUpdate();
        for (int j = 0; j < numberOfCopies; j++) {
            insertCopy(title, publishDate, isbnNr,publisher);
        }
    }

    public void insertCopy(String bookTitle,  String publishDate, String isbnNr,String publisher) throws SQLException {
        int book_id = 0;
        PreparedStatement id = conn.prepareStatement("SELECT id FROM book WHERE title = ?");
        id.setString(1, bookTitle);
        ResultSet rs = id.executeQuery();
        if (rs.next()) {
            book_id = rs.getInt("id");
        }
        PreparedStatement ps3 = conn.prepareStatement("INSERT INTO `library`.`book_copy` " +
                "(`publication_year`, `book_id`, `ISBN`,`publisher`) VALUES ( ?, ?, ?,?)");

        ps3.setString(1, publishDate);
        ps3.setInt(2, book_id);
        ps3.setString(3, isbnNr);
        ps3.setString(4,publisher);

        ps3.executeUpdate();
    }

    public void insertCopyOnly(int id, int cover, String publishDate, String isbnNr) throws SQLException {
        PreparedStatement ps4 = conn.prepareStatement("INSERT INTO `library`.`book_copy`" +
                " (`is_book_cover_hard`, `publication_year`, `book_id`, `ISBN`) VALUES (?, ?, ?, ?)");
        ps4.setInt(1, cover);
        ps4.setString(2, publishDate);
        ps4.setInt(3, id);
        ps4.setString(4, isbnNr);
        int i = ps4.executeUpdate();
    }

    public void deleteBook(String ISBN) throws SQLException {
        PreparedStatement delete = conn.prepareStatement("DELETE book, book_copy FROM book \n" +
                "INNER JOIN book_copy ON book.id = book_copy.book_id\n" +
                "WHERE book_copy.ISBN = ?");

        delete.setString(1, ISBN);
        delete.executeUpdate();
    }


    public String trafficOnSelectedDate(java.sql.Date date) {
        try {
            PreparedStatement ps = conn.prepareStatement("SELECT count(*) AS total  FROM book_rent\n" +
                    "WHERE rent_date = ? OR return_date = ?");
            ps.setDate(1, date);
            ps.setDate(2, date);
            ResultSet rs = ps.executeQuery();
            int total;
            if (rs.next()) {
                total = rs.getInt("total");
                return String.valueOf(total);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return "";
    }

    public List<BookInfo> mostPopularBook(){
        List<BookInfo> mostPopularbook = new ArrayList<>();
        try {
            PreparedStatement ps = conn.prepareStatement("SELECT Author, title, count(rent_date) as value FROM book_rent\n" +
                    "JOIN book_copy ON book_copy_id = book_copy.id\n" +
                    "JOIN book ON book.id = book_copy.book_id\n" +
                    "GROUP BY title\n" +
                    " ORDER BY count(rent_date) desc LIMIT 1\n");

            ResultSet rs = ps.executeQuery();
            if(rs.next()){
                BookInfo bi =  new BookInfo();
                bi.setBookAuthor(rs.getString("Author"));
                bi.setBookTitle(rs.getString("title"));
                bi.setMostPopularBook(rs.getInt("value"));
                mostPopularbook.add(bi);
            }
        } catch (SQLException  e) {
            System.out.println(e.getMessage());
        }
        return mostPopularbook;
    }

}



