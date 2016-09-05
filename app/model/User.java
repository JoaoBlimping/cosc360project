package model;

public class User
{
  public final ObjectId id;
  public final ObjectId level;
  public final String username;
  public final String password;


  public User(ObjectId id,ObjectId level,String username,String password)
  {
    this.id = id;
    this.level = level;
    this.username = username;
    this.password = password;
  }


  public User(Document bson)
  {
    id = bson.getObjectId("id");
    level = bson.getObjectId("level");
    username = bson.getString("username");
    password = bson.getString("password");
  }


  public Document toBson()
  {
    Document bson = new Document("_id",id);
    bson.append("level",level);
    bson.append("username",username);
    bson.append("password",password);

    return bson;
  }
}
