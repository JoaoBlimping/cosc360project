package controllers;

import com.google.inject.Singleton;
import model.UserManager;
import play.mvc.Controller;
import play.mvc.Result;


@Singleton
public class Menus extends Controller
{
  public Result index()
  {
    return ok(views.html.application.index.render());
  }

  public Result sessions()
  {
    return ok(views.html.application.sessions.render(UserManager.getUsernames()));
  }
}
