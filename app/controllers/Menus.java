package controllers;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import model.Database;
import model.UserManager;
import play.mvc.Controller;
import play.mvc.Result;


/** sends things that are menus related */
@Singleton
public class Menus extends Controller
{
  private Database database;

  @Inject
  public Menus(Database database)
  {
    this.database = database;
  }

  public Result index()
  {
    return ok(views.html.application.login.render(null));
  }

  public Result sessions()
  {
    return ok(views.html.application.sessions.render(UserManager.getUsernames()));
  }

  public Result graph()
  {
    return ok(database.getEventCollection().find().first().get("room").toString());
    //return ok(views.html.application.graph.render());
  }
}
