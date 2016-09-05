package model;


import java.util.UUID;


/** a session for a specific user's specific session on the system
  * make sure that a session is only created and added to the list of active
  * sessions  */
public class Session
{
  /** unique Id of the session */
  public final String sessionId;

  /** unique Id of the user using the session */
  public final int userId;

  /** stores the time since the session was last used */
  private long time;

  /** creates a session
   * @param sessionId is the session id for this session
   * @param userId id the user id for the user using this session */
  public Session(int userId)
  {
    this.sessionId = UUID.randomUUID().toString();
    this.userId = userId;
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
