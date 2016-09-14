package controllers;


import com.google.inject.Inject;
import com.google.inject.Singleton;
import model.User;
import model.UserManager;
import play.api.PlayException;
import play.mvc.Controller;
import play.mvc.Result;
import org.mindrot.jbcrypt.BCrypt;
import scala.reflect.internal.Kinds;


@Singleton
public class Game extends Controller
{

  public Result doLogin()
  {
    String name = request().body().asFormUrlEncoded().get("name")[0];

    User u;

    try
    {
      u = UserManager.startUser(name);
    }
    catch (IllegalArgumentException e)
    {
      return ok(views.html.application.login.render(e.getMessage()));
    }
    catch (Exception e)
    {
      return internalServerError(e.getMessage());
    }

    return ok(views.html.application.game.render(u.id));
  }


  public Result getRoomDetails()
  {
    //get the user id from the request to figure out which user it is
  }

  public Result getUserDetails()
  {

  }

  public Result execute()
  {
    return ok("yeah, I'll get around to it");
  }


}
