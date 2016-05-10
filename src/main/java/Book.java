import java.util.ArrayList;
import java.util.List;
import org.sql2o.*;


public class Book {
  private String title;
  private int id;
  private boolean checked_out;
  private String genre;

  public Book(String title, String genre) {
    this.title = title;
    this.genre = genre;
    this.checked_out = false;
  }

  public String getTitle() {
    return title;
  }

  public String getGenre() {
    return genre;
  }

  public boolean isCheckedOut() {
    return checked_out;
  }

  public void checkOut() {
     checked_out = true;
     try(Connection con = DB.sql2o.open()) {
       String sql = "UPDATE books SET checked_out = :checked_out WHERE id = :id";
       con.createQuery(sql).addParameter("checked_out", checked_out).addParameter("id", this.getId()).executeUpdate();
     }
  }

  public int getId(){
    return id;
  }

  public void save() {
    try(Connection con = DB.sql2o.open()) {
      String sql = "INSERT INTO books (title, genre, checked_out) VALUES (:title, :genre, :checked_out)";
    this.id = (int) con.createQuery(sql, true).addParameter("title", this.getTitle()).addParameter("genre", this.getGenre()).addParameter("checked_out", this.isCheckedOut()).executeUpdate().getKey();
    }
  }

  public static Book findBook(int num){
    try(Connection con = DB.sql2o.open()){
      String sql = "SELECT * FROM books WHERE id = :id";
      return con.createQuery(sql).addParameter("id", num).executeAndFetchFirst(Book.class);

    }
  }
  @Override
  public boolean equals(Object otherBook){
    if (!(otherBook instanceof Book)) {
      return false;
    } else {
      Book newBook = (Book) otherBook;
      return this.getTitle().equals(newBook.getTitle()) &&
             this.getId() == newBook.getId() && this.getGenre().equals(newBook.getGenre());
    }
  }
  public static List<Book> all() {
    try(Connection con = DB.sql2o.open()) {
      String sql = "SELECT title, genre, checked_out FROM books";
      return con.createQuery(sql).executeAndFetch(Book.class);
    }
  }

  public void update(String prop, String newValue){
    try(Connection con = DB.sql2o.open()) {
      String sql = "UPDATE books SET " + prop + " = :newPropValue WHERE id = :id";
      con.createQuery(sql).addParameter("newPropValue", newValue).addParameter("id", this.getId()).executeUpdate();
    }
  }

  public void delete(){
    try(Connection con = DB.sql2o.open()){
      String sql = "DELETE FROM books WHERE id = :id";
      con.createQuery(sql).addParameter("id", this.getId()).executeUpdate();
    }
  }

  public void addAuthor(Author author) {
    try(Connection con = DB.sql2o.open()) {
      String joinQuery = "INSERT INTO author_books (book_id, author_id) VALUES (:book_id, :author_id)";
      con.createQuery(joinQuery).addParameter("book_id", this.getId()).addParameter("author_id", author.getId()).executeUpdate();
    }
  }

  public List<Author> getAuthors() {
    try(Connection con = DB.sql2o.open()) {
      String joinQuery = "SELECT author_id FROM author_books WHERE book_id = :book_id";
      List<Integer> author_ids = con.createQuery(joinQuery).addParameter("book_id", this.getId()).executeAndFetch(Integer.class);

      List<Author> authors = new ArrayList<Author>();

      for(Integer author_id : author_ids) {
        String sql = "SELECT * FROM author WHERE id = :author_id";
        Author thisAuthor = con.createQuery(sql).addParameter("author_id", author_id).executeAndFetchFirst(Author.class);
        authors.add(thisAuthor);
      }
      return authors;
    }
  }

}
