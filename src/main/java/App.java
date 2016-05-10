import java.util.HashMap;
import java.util.ArrayList;

import spark.ModelAndView;
import spark.template.velocity.VelocityTemplateEngine;

import static spark.Spark.*;



public class App {

  public static void main(String[] args) {
    staticFileLocation("/public");
    String layout = "/templates/layout.vtl";

    get("/", (request, response) -> {
      HashMap model = new HashMap();
      model.put("books", Book.all());
      model.put("template", "templates/home.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    post("/addBook", (request, response) -> {
      HashMap model = new HashMap();
      String title = request.queryParams("title");
      String author = request.queryParams("author");
      String genre = request.queryParams("genre");
      Book newBook = new Book(title, genre);
      Author newAuthor = new Author(author);
      newBook.save();
      newAuthor.save();
      newBook.addAuthor(newAuthor);
      String url = "/";
      response.redirect(url);
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    post("/patronSearch", (request, response) -> {
      HashMap model = new HashMap();
      String patronName = request.queryParams("patronName");
      String patronTitle = request.queryParams("patronTitle");
      Patron newPatron = new Patron(patronName);
      newPatron.save();
      Book foundBook = Book.findBookByTitle(patronTitle);
      model.put("patron", newPatron);
      model.put("searchedBook", foundBook);
      model.put("template", "templates/searchResults.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    post("/checkoutBook/:id/:idPatron", (request, response) -> {
      HashMap model = new HashMap();
      Book foundBook = Book.findBook(Integer.parseInt(request.params("id")));
      Patron foundPatron = Patron.findPatron(Integer.parseInt(request.params("idPatron")));
      foundPatron.checkOut(foundBook);
      String url = "/";
      response.redirect(url);
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());
  }

}
