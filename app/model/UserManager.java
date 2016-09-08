package model;


public class UserManager
{
  /** connection to the mongo server */
  private MongoClient mongoClient;

  /** creates a session manager and makes it's client to the mongo server */
  protected SessionManager()
  {
    mongoClient = new MongoClient("turing.une.edu.au", 27017);
  }

  /** gives you access to the database */
  protected MongoDatabase getDB()
  {
    return mongoClient.getDatabase("comp391_dbarron2");
  }

  /** gives you access to the user collection */
  protected MongoCollection<Document> getUsersCollection()
  {
      return getDB().getCollection("users");
  }

  /** creates a user */
  public User createUser(String username,String password)
  {
    if (getUsersCollection().find(new Document("username",username)).count() != 0)
    {
      throw new IllegalArgumentException();
    }

    User user = new User();//TODO: the stuff

    getUsersCollection().insertOne(user.toBson());
    return user;
  }
}
