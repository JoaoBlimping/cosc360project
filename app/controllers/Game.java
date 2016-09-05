package controllers;


import com.google.inject.Inject;
import com.google.inject.Singleton;
import play.mvc.Controller;
import play.mvc.Result;
import org.mindrot.jbcrypt.BCrypt;


@Singleton
public class Game extends Controller
{

  public Result doLogin()
  {
    String username = request().body().asFormUrlEncoded().get("username")[0];
    String password = request().body().asFormUrlEncoded().get("password")[0];




    return ok("yeah loged in and stuff");
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
