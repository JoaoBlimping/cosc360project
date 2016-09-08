package model;

/** a room in which the game takes place, contains a description, ids of other rooms that
 * are it's exits, and descriptions of each of them */
public class Room
{
  public final int id;
  public final int[] exits;
  public final String[] exitDescriptions;
  public final String description;

  /** creates the room by setting everything explicitly */
  public Room(int id,int[] exits,String[] exitDescriptions,String description)
  {
    this.id = id;
    this.exits = exits;
    this.exitDescriptions = exitDescriptions;
    this.description = description;
  }
}
