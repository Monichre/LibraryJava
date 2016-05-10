import org.sql2o.*;
import org.junit.*;
import static org.junit.Assert.*;

public class PatronTest {

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
  public void Patron_InstanceOfPatron() {
    Patron newPatron = new Patron("Will Legos");
    assertTrue(newPatron instanceof Patron);
  }

  @Test
  public void Patron_getName() {
    Patron newPatron = new Patron("Will Legos");
    assertEquals("Will Legos", newPatron.getName());
  }

  @Test
  public void Patron_SavePatron() {
    Patron newPatron = new Patron("Will Legos");
    newPatron.save();
    assertEquals("Will Legos", Patron.findPatron(newPatron.getId()).getName());
  }

  @Test
  public void Patron_CheckOut() {
    Patron newPatron = new Patron("Will Legos");
    newPatron.save();
    Book newBook = new Book("The Things We Carried", "Historical Fiction");
    newPatron.checkOut(newBook);
    assertEquals(1, newPatron.getBooks().size());
  }

  @Test
  public void Patron_CheckOutUpdatesDatabase() {
    Patron newPatron = new Patron("Will Legos");
    newPatron.save();
    Book newBook = new Book("The Things We Carried", "Historical Fiction");
    newBook.save();
    newPatron.checkOut(newBook);
    Book foundBook = Book.findBook(newBook.getId());
    assertEquals(9, foundBook.getCopies());
  }
}
