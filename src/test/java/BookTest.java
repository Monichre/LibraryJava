import org.sql2o.*;
import org.junit.*;
import static org.junit.Assert.*;

public class BookTest {

  @Before
  public void setUp() {
    DB.sql2o = new Sql2o("jdbc:postgresql://localhost:5432/test_library", null, null);
  }

  @After
  public void tearDown() {
    try(Connection con = DB.sql2o.open()) {
      String deleteAuthorQuery = "DELETE FROM author *;";
      String deleteBooksQuery = "DELETE FROM books *;";
      String deleteAuthorBooksQuery = "DELETE FROM author_books *;";
      String deletePatronQuery = "DELETE FROM patrons *;";
      String deleteCheckoutsQuery = "DELETE FROM books *;";
      con.createQuery(deleteAuthorQuery).executeUpdate();
      con.createQuery(deleteBooksQuery).executeUpdate();
      con.createQuery(deleteAuthorBooksQuery).executeUpdate();
      con.createQuery(deletePatronQuery).executeUpdate();
      con.createQuery(deleteCheckoutsQuery).executeUpdate();
    }
  }

  @Test
  public void Book_InstanceOfBook() {
    Book testBook = new Book("Guns, Germs and Steel", "General Non-Fiction");
    assertTrue(testBook instanceof Book);
  }

  @Test
  public void Book_BookGetTitle() {
    Book testBook = new Book("Guns, Germs and Steel", "General Non-Fiction");
    assertEquals("Guns, Germs and Steel", testBook.getTitle());
  }

  @Test
  public void Book_BookGetGenre() {
    Book testBook = new Book("Guns, Germs and Steel", "General Non-Fiction");
    assertEquals("General Non-Fiction", testBook.getGenre());
  }

  @Test
  public void Book_BookGetisCheckedOut() {
    Book testBook = new Book("Guns, Germs and Steel", "General Non-Fiction");
    assertEquals(false, testBook.isCheckedOut());
  }

  @Test
  public void Book_getIdReturnsId(){
    Book testBook = new Book("Guns, Germs and Steel", "General Non-Fiction");

  }
  @Test
  public void Book_findBookbyIdReturnsBook(){
    Book testBook = new Book("Guns, Germs and Steel", "General Non-Fiction");
    testBook.save();
    Book savedBook = Book.findBook(testBook.getId());
    assertTrue(testBook.equals(savedBook));

  }

  @Test
  public void Book_BookSaved() {
    Book testBook = new Book("Guns, Germs and Steel", "General Non-Fiction");
    testBook.save();
    assertEquals(1, Book.all().size());
  }

  @Test
  public void Book_BookUpdate() {
    Book testBook = new Book("Guns, Germs and Steel", "General Non-Fiction");
    testBook.save();
    String newTitle = "Oh The Places You'll Go";
    testBook.update("title", newTitle);
    assertFalse(testBook.equals(Book.findBook(testBook.getId())));
  }

  @Test
  public void Book_deleteMethodDeletesFromDatabase(){
    Book testBook = new Book("Guns, Germs and Steel", "General Non-Fiction");
    testBook.save();
    testBook.delete();
    assertEquals(0, Book.all().size());
  }


  @Test
  public void Book_addAuthor() {
    Book testBook = new Book("Guns, Germs and Steel", "General Non-Fiction");
    Author testAuthor = new Author("Dr. Seuss");
    testBook.save();
    testAuthor.save();
    testBook.addAuthor(testAuthor);
    Author bookAuthor = testBook.getAuthors().get(0);
    assertTrue(testAuthor.equals(bookAuthor));
  }
  @Test
  public void Book_findBookByTitleReturnsBook(){
    Book testBook = new Book("Guns, Germs and Steel", "General Non-Fiction");
    testBook.save();
    assertEquals(testBook, Book.findBookByTitle("Guns, Germs and Steel"));
  }
}
