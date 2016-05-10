import org.sql2o.*;
import org.junit.*;
import static org.junit.Assert.*;

public class AuthorTest {

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
  public void Author_InstanceOfAuthor() {
    Author testAuthor = new Author("James Joyce");
    assertTrue(testAuthor instanceof Author);
  }

  @Test
  public void Author_GetName() {
    Author testAuthor = new Author("James Joyce");
    assertEquals("James Joyce", testAuthor.getName());
  }

  @Test
  public void Author_saveAuthor() {
    Author testAuthor = new Author("James Joyce");
    testAuthor.save();
    assertEquals(1, Author.all().size());
  }

  @Test
  public void Author_find(){
    Author testAuthor = new Author("Bill Bryson");
    testAuthor.save();
    assertTrue(testAuthor.equals(Author.find(testAuthor.getId())));
  }

  @Test
  public void Author_update(){
    Author testAuthor = new Author("Bill Brson");
    testAuthor.save();
    testAuthor.update("Bill Bryson");
    assertFalse(testAuthor.equals(Author.find(testAuthor.getId())));
  }

  @Test
  public void Author_deleteAuthor(){
    Author testAuthor = new Author("Bill Bryson");
    testAuthor.save();
    testAuthor.delete();
    assertEquals(0, Author.all().size());
  }

  @Test
  public void Author_addBook(){
    Author testAuthor = new Author("Bill Bryson");
    Book newBook = new Book("The Sun Also Rises", "Fiction");
    testAuthor.save();
    newBook.save();
    testAuthor.addBook(newBook);
    Book savedBook = testAuthor.getBooks().get(0);
    assertTrue(newBook.equals(savedBook));
  }



}
