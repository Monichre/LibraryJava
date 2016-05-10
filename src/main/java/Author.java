import java.util.ArrayList;
import java.util.List;
import org.sql2o.*;


public class Author {
  private String name;
  private int id;

  public Author(String name) {
    this.name = name;
  }

  @Override
  public boolean equals(Object otherAuthor){
    if (!(otherAuthor instanceof Author)) {
      return false;
    } else {
      Author newAuthor = (Author) otherAuthor;
      return this.getName().equals(newAuthor.getName()) &&
             this.getId() == newAuthor.getId();
    }
  }

  public String getName() {
    return name;
  }

  public int getId(){
    return id;
  }

  public static List<Author> all() {
    try(Connection con = DB.sql2o.open()) {
      String sql = "SELECT name FROM author";
      return con.createQuery(sql).executeAndFetch(Author.class);
    }
  }

  public void save() {
    try(Connection con = DB.sql2o.open()) {
      String sql = "INSERT INTO author (name) VALUES (:name)";
      this.id = (int)con.createQuery(sql, true).addParameter("name", this.getName()).executeUpdate().getKey();
    }
  }

  public static Author find(int id){
    try(Connection con = DB.sql2o.open()){
      String sql = "SELECT * FROM author WHERE id = :id";
      return con.createQuery(sql).addParameter("id", id).executeAndFetchFirst(Author.class);

    }
  }

  public void update(String name) {
    try(Connection con = DB.sql2o.open()) {
      String sql = "UPDATE author SET name = :name WHERE id = :id";
      con.createQuery(sql).addParameter("name", name).addParameter("id", this.getId()).executeUpdate();
    }
  }

  public void delete(){
    try(Connection con = DB.sql2o.open()){
      String sql = "DELETE FROM author WHERE id = :id";
      con.createQuery(sql).addParameter("id", this.getId()).executeUpdate();
    }
  }

  public void addBook(Book book){
    try(Connection con = DB.sql2o.open()){
      String joinQuery = "INSERT INTO author_books (author_id, book_id) VALUES (:author_id, :book_id)";
      con.createQuery(joinQuery).addParameter("author_id", this.getId()).addParameter("book_id", book.getId()).executeUpdate();
    }
  }

  public List<Book> getBooks() {
    try(Connection con = DB.sql2o.open()) {
      String joinQuery = "SELECT book_id FROM author_books WHERE author_id = :author_id";
      List<Integer> book_ids = con.createQuery(joinQuery).addParameter("author_id", this.getId()).executeAndFetch(Integer.class);

      List<Book> books = new ArrayList<Book>();

      for(Integer book_id : book_ids) {
        String sql = "SELECT * FROM books WHERE id = :book_id";
        Book book = con.createQuery(sql).addParameter("book_id", book_id).executeAndFetchFirst(Book.class);
        books.add(book);
      }
      return books;
    }
  }


}
