package controllers;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import play.mvc.Controller;
import play.mvc.Result;


@Singleton
public class Menus extends Controller
{
  public Result index()
  {
    return ok(views.html.application.index.render());
  }
}
