package model;

import com.google.inject.Singleton;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.bson.types.ObjectId;


/** deals with the database */
@Singleton
public class Database
{
  private MongoClient mongoClient;

  /** just creates the connection */
  public Database()
  {
    mongoClient = new MongoClient("127.0.0.1", 27017);
  }

  /** gives you all the events */
  public MongoCollection<Document> getEventCollection()
  {
    return getDB().getCollection("event");
  }

  /** adds an event to the database */
  public void addEvent(ObjectId userId,String event,long room)
  {
    Document doc = new Document("_id",new ObjectId()).append("user",userId)
            .append("event",event).append("room",room);
    getEventCollection().insertOne(doc);
  }

  /** returns a connection to the database */
  private MongoDatabase getDB()
  {
    return mongoClient.getDatabase("comp360_dbarron2");
  }
}
