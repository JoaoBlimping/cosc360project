package model;

import java.util.concurrent.CopyOnWriteArrayList;

/** a room in which the game takes place */
public class Room
{
  public final long id;
  public final String description;
  public CopyOnWriteArrayList<Long> exits = new CopyOnWriteArrayList<>();

  /** creates the room by setting everything explicitly */
  public Room(long id,String description)
  {
    this.id = id;
    this.description = description;
  }
}
