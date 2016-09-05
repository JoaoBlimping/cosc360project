package model;

import java.util.concurrent.ConcurrentHashMap;


/** manages sessions */
public class SessionManager
{
  /** links session ids to sessions */
  private static ConcurrentHashMap<String,Session> sessions = new ConcurrentHashMap<>();


  /** start a session for a given user id */
  public static Session startSession(int userId)
  {
    Session session = new Session(userId);
    sessions.put(session.sessionId,session);
    return session;
  }

  /** end a session by it's session id */
  public static void endSession(String sessionId)
  {
    sessions.remove(sessionId);
  }

  /** gives you a session by it's session id */
  public static Session getSession(String sessionId)
  {
    return sessions.get(sessionId);
  }
}
