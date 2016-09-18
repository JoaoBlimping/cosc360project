package controllers;


import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import model.Room;
import model.RoomManager;
import model.User;
import model.UserManager;
import play.api.PlayException;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import org.mindrot.jbcrypt.BCrypt;
import scala.reflect.internal.Kinds;

import java.util.IllegalFormatCodePointException;
import java.util.List;
import java.util.Map;


@Singleton
public class Game extends Controller
{
  private RoomManager roomManager;
  private Commands commands;

  @Inject
  public Game(RoomManager roomManager,Commands commands)
  {
    this.roomManager = roomManager;
    this.commands = commands;
  }

  /** log in a new user */
  public Result doLogin()
  {
    Map<String,String[]> data = request().body().asFormUrlEncoded();
    if (data == null) return badRequest("no data sent");

    if (data.containsKey("name"))
    {
      String name = data.get("name")[0];
      try
      {
        User u = UserManager.startUser(name);
        u.room = roomManager.starterRoom;
        return ok(views.html.application.game.render(u.id));
      }
      catch (IllegalArgumentException e)
      {
        return badRequest(views.html.application.login.render(e.getMessage()));
      }
    }
    else
    {
      return badRequest(views.html.application.login.render("name needed"));
    }
  }



  /** send a client the details on a room */
  public Result getRoomDetails()
  {
    Map<String,String[]> data = request().body().asFormUrlEncoded();
    if (data == null) return badRequest("no data sent");

    if (data.containsKey("roomId"))
    {
      try
      {
        long roomId = Long.parseLong(data.get("roomId")[0]);
        Room r = roomManager.getRoom(roomId);
        ObjectNode roomData = Json.newObject();
        roomData.put("description",r.description);

        List<String> exitDescriptions = roomManager.getExitDescriptions(r);
        ArrayNode exitList = roomData.putArray("exits");
        for (String exit:exitDescriptions) exitList.add(exit);

        List<String> userNames = UserManager.getUsersInRoom(roomId);
        ArrayNode userList = roomData.putArray("users");
        for (String name:userNames) userList.add(name);

        return ok(roomData);
      }
      catch (IllegalArgumentException e)
      {
        return badRequest("given room doesn't exist");
      }
    }
    else return badRequest("no roomId");
  }

  /** send the client the details on a user */
  public Result getUserDetails() {
    String userId = request().body().asFormUrlEncoded().get("userId")[0];

    try
    {
      User u = UserManager.getUser(userId);

      ObjectNode userData = Json.newObject();
      userData.put("name",u.name);
      userData.put("roomId",u.room);
      return ok(userData);
    }
    catch (IllegalArgumentException e)
    {
      return badRequest("no such userId");
    }
  }

  /** executes a user command */
  public Result execute()
  {
    Map<String,String[]> data = request().body().asFormUrlEncoded();

    if (!data.containsKey("userId") || !data.containsKey("command"))
    {
      return badRequest("needed arguments not given");
    }

    String userId = data.get("userId")[0];
    String rawCommand = data.get("command")[0];

    try
    {
      if (rawCommand.charAt(0) == 'e') commands.explore(userId);
      else if (rawCommand.charAt(0) == 't') commands.take(userId,rawCommand.substring(rawCommand.indexOf(' ')));
      else throw new IllegalArgumentException();
    }
    catch (IllegalArgumentException e)
    {
      return badRequest("invalid command");
    }

    return ok("done");
  }


}
