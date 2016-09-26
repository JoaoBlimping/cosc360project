package controllers;


import akka.NotUsed;
import akka.actor.Cancellable;
import akka.dispatch.Futures;
import akka.stream.javadsl.Flow;
import akka.stream.javadsl.Sink;
import akka.stream.javadsl.Source;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.mongodb.Block;
import com.mongodb.DBObject;
import com.mongodb.client.FindIterable;
import model.Room;
import model.RoomManager;
import model.User;
import model.UserManager;
import model.Database;
import org.bson.Document;
import org.bson.types.ObjectId;
import play.libs.EventSource;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;
import scala.concurrent.Promise;
import scala.concurrent.duration.Duration;

import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;

import static java.util.concurrent.TimeUnit.MILLISECONDS;


@Singleton
public class Game extends Controller
{
  private RoomManager roomManager;
  private Commands commands;
  private Database database;

  @Inject
  public Game(RoomManager roomManager,Commands commands,Database database)
  {
    this.roomManager = roomManager;
    this.commands = commands;
    this.database = database;
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
        EventManager.activate(roomManager.starterRoom,"newUser "+u.name);
        return ok(views.html.application.game.render((u.id.toString())));
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
    ObjectId userId = new ObjectId(request().body().asFormUrlEncoded().get("userId")[0]);

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

    ObjectId userId = new ObjectId(data.get("userId")[0]);
    String rawCommand = data.get("command")[0];

    try
    {
      if (rawCommand.charAt(0) == 'e') commands.explore(userId);
      else if (rawCommand.charAt(0) == 't') commands.take(userId,rawCommand.substring(rawCommand.indexOf(' ')));
      else if (rawCommand.charAt(0) == 's') commands.say(userId,rawCommand.substring(rawCommand.indexOf(' ')));
      else throw new IllegalArgumentException();
    }
    catch (IllegalArgumentException e)
    {
      return badRequest("invalid command");
    }

    return ok("done");
  }


  /** gets all the past events from the database to be graphed */
  public Result getPastEvents()
  {
    ObjectNode data = Json.newObject();
    ArrayNode eventList = data.putArray("events");
    database.getEventCollection().find().forEach(new Block<Document>()
    {
      @Override
      public void apply(final Document document)
      {
        Room r = roomManager.getRoom((long)document.get("room"));

        ObjectNode event = Json.newObject();
        event.put("user",document.get("user").toString());
        event.put("event",(String)document.get("event"));
        event.put("roomId",r.id);
        roomManager.exitsToJson(r,event.putArray("exits"));
        eventList.add(event);
      }
    });
    return ok(data);
  }

  /** sends a stream of events occuring in the game, or maybe just in a room i dunno */
  public Result events()
  {
    Map<String,String[]> data = request().queryString();

    if (!data.containsKey("roomId")) return badRequest("needed arguments not given");
    long roomId = Long.parseLong(data.get("roomId")[0]);

    final Source<EventSource.Event, ?> eventSource = getStringSource(roomId).map(EventSource.Event::event);
    return ok().chunked(eventSource.via(EventSource.flow())).as(Http.MimeTypes.EVENT_STREAM);
  }


  private Source<String, ?> getStringSource(long roomId)
  {
    Promise<String> promise = Futures.promise();
    EventManager.addPromise(roomId,promise);
    final Source<String, NotUsed> dataSource = Source.fromFuture(promise.future());
    return dataSource.map((list) -> list);
  }


}
