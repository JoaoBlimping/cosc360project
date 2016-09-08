package model;


import java.util.UUID;

/** since users are not persistent, this stores both the user data,
 * and their session
 */
public class User
{
  public final String id;
  public final String name;

  public int level;

  private long time;


  /** creates a user */
  public User(String name)
  {
    this.id = UUID.randomUUID().toString();
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
