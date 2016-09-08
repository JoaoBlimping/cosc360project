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

    return ok("yeah loged in and stuff " + u.id);
  }


  public Result doRegister()
  {
    return ok("nice");
    /*
    String email = request().body().asFormUrlEncoded().get("username")[0];
    String password = request().body().asFormUrlEncoded().get("password")[0];

    User u = new User(UUID.randomUUID().toString(),email,BCrypt.hashpw(password,BCrypt.gensalt()));

    if (getUserService().registerUser(u) == null)
    {
      return badRequest(views.html.application.register.render("this user exists"));
    }
    else
    {
      return ok("nice");
    }
    */
  }

  public Result loginForm()
  {
    return ok(views.html.application.login.render(null));
  }

  public Result registerForm()
  {
    return ok(views.html.application.register.render(null));

  }
}
