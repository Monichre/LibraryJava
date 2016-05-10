import java.util.ArrayList;
import java.util.List;
import org.sql2o.*;

public class Patron {
  private String name;
  private int id;

  public Patron(String name) {
    this.name = name;
  }

  public String getName() {
    return name;
  }

  public void save() {
    try(Connection con = DB.sql2o.open()) {
      String sql = "INSERT INTO patrons (name) VALUES (:name)";
      this.id = (int) con.createQuery(sql, true).addParameter("name", this.getName()).executeUpdate().getKey();
    }
  }

  public static Patron findPatron(int id) {
    try(Connection con = DB.sql2o.open()) {
      String sql = "SELECT * FROM patrons WHERE id = :id";
      return con.createQuery(sql).addParameter("id", id).executeAndFetchFirst(Patron.class);
    }
  }

  public int getId(){
    return id;
  }

  public void checkOut(Book book) {
   if(!(book.isCheckedOut())){
     try(Connection con = DB.sql2o.open()) {
       String sql = "UPDATE books SET copies = :copies WHERE id = :id";
       con.createQuery(sql).addParameter("copies", book.getCopies() - 1).addParameter("id", book.getId()).executeUpdate();
       String joinQuery = "INSERT INTO checkouts (book_id, patron_id) VALUES (:book_id, :patron_id)";
       con.createQuery(joinQuery).addParameter("book_id", book.getId()).addParameter("patron_id", this.getId()).executeUpdate();
      }
    }
  }

  public List<Book> getBooks(){
    try(Connection con = DB.sql2o.open()) {
      String sql = "SELECT book_id FROM checkouts WHERE patron_id = :patron_id";
      List<Integer> book_ids = con.createQuery(sql).addParameter("patron_id", this.getId()).executeAndFetch(Integer.class);

      List<Book> books = new ArrayList<Book>();

      for (Integer book_id : book_ids){
        String bookQuery = "SELECT * FROM books WHERE id = :book_id";
        Book thisBook = con.createQuery(bookQuery).addParameter("book_id", book_id).executeAndFetchFirst(Book.class);
        books.add(thisBook);
      }
      return books;
    }
  }
}
