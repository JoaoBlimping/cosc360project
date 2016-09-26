package model;


import org.bson.BSON;
import org.bson.types.ObjectId;

import java.util.UUID;

/** since users are not persistent, this stores both the user data,
 * and their session
 */
public class User
{
  public final ObjectId id;
  public final String name;

  public long room;

  private long time;


  /** creates a user */
  public User(String name)
  {
    this.id = new ObjectId();
    this.name = name;
    this.time = System.currentTimeMillis();
  }

  /** resets the session's time to the current time, to be called whenever the
   * session is used so you can tell the time since it was last used */
  public void resetTime()
  {
    this.time = System.currentTimeMillis();
  }

  /** gives you the last time the session was used */
  public long getTime()
  {
    return this.time;
  }
}
