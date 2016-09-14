package model;

import javax.inject.Singleton;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

/**
 * contains all the rooms, and creates new rooms and all that kind of thing
 * Created by daly on 8/09/16.
 */
@Singleton
public class RoomManager
{
    ConcurrentHashMap<Long,Room> rooms;
    long starterRoom;

    public RoomManager()
    {
        Room starter = RoomGenerator.generateRoom();
        System.out.println(starter.description);
        addRoom(starter);
        this.starterRoom = starter.id;
    }

    public void addRoom(Room room)
    {
        this.rooms.put(room.id,room);
    }


}
