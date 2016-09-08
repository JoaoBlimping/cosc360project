package model;

public class Room
{
  public final ObjectId id;
  public final ObjectId[] exits;
  public final String[] exitDescriptions;
  public final String description;

  public Level(ObjectId id,ObjectId[] exits,String[] exitDescriptions,
               String description)
  {
    this.id = id;
    this.exits = exits;
    this.exitDescriptions = exitDescriptions;
    this.description = description;
  }

  public Room(Document bson)
  {
    id = bson.getObjectId("_id");
    exits = bson.get("exits",ObjectId[]);
    exitDescriptions = bson.get("exitDescriptions",String[]);
    description = bson.getString();
  }


  public Document toBson()
  {
    Document bson = new Document("_id",id);
    bson.append("exits",exits);
    bson.append("exitDescriptions",exitDescriptions);
    bson.append("description",description);

    return bson;

  }

}
