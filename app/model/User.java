public class User
{
  private ObjectId id;
  private String username;
  private String password;

  public User(String username,String password)
  {
    this.username = username;
    this.password = password;
  }

  public Document toBson()
  {
    List<Document> sessions = new ArrayList<>();
    for (Session s : u.getSessions())
    {
        sessions.add(sessionToBson(s));
    }

    return new Document("_id", new ObjectId(u.getId()))
            .append("email", u.email)
            .append("hash", u.getHash())
            .append("sessions", sessions);

  }
}
