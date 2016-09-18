package model;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;


/** manages sessions */
public class UserManager
{
  /** links session ids to sessions */
  private static ConcurrentHashMap<String,User> users = new ConcurrentHashMap<>();


  /** start a session for a given user id */
  public static User startUser(String name)
  {
    for (User u:users.values())
    {
      if (u.name.equals(name))
      {
        throw new IllegalArgumentException("name "+name+" is already in use");
      }
    }

    User u = new User(name);
    users.put(u.id,u);
    return u;
  }

  /** end a session by it's session id */
  public static void endUser(String id)
  {
    users.remove(id);
  }

  /** gives you a session by it's session id */
  public static User getUser(String id) throws IllegalArgumentException
  {
    if (users.containsKey(id)) return users.get(id);
    else throw new IllegalArgumentException("user doesn't exist");
  }

  /** gives you the names of all users */
  public static List<String> getUsernames()
  {
    LinkedList<String> names = new LinkedList<>();
    for (User u:users.values()) names.add(u.name);
    return names;
  }

  /** gives you the names of all users in a room */
  public static List<String> getUsersInRoom(long room)
  {
    LinkedList<String> names = new LinkedList<>();
    for (User u:users.values())if (u.room == room) names.add(u.name);
    return names;
  }
}
