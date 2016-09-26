package model;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.inject.Singleton;
import play.libs.Json;

import java.util.ArrayList;
import java.util.IllegalFormatCodePointException;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * contains all the rooms, and creates new rooms and all that kind of thing
 * Created by daly on 8/09/16.
 */
@Singleton
public class RoomManager
{
    private ConcurrentHashMap<Long,Room> rooms = new ConcurrentHashMap<>();
    public long starterRoom;

    /** called when the room manager is started */
    public RoomManager()
    {
        Room starter = RoomGenerator.generateRoom();
        System.out.println(starter.description);
        addRoom(starter);
        this.starterRoom = starter.id;
    }

    /** add a room to the room manager */
    public void addRoom(Room room)
    {
        this.rooms.put(room.id,room);
    }

    /** gives you a room from that room's id. throws if you give a bad room number */
    public Room getRoom(long roomId) throws IllegalArgumentException
    {
        if (this.rooms.containsKey(roomId)) return this.rooms.get(roomId);
        else throw new IllegalArgumentException("no such room");
    }

    /** gives you the descriptions of all of a room's exits */
    public List<String> getExitDescriptions(Room r) throws IllegalArgumentException
    {
        ArrayList<String> exitDescriptions = new ArrayList<String>();
        for (long exit:r.exits)
        {
            exitDescriptions.add(getRoom(exit).description);
        }
        return exitDescriptions;
    }

    public ObjectNode roomToJson(long roomId) throws IllegalArgumentException
    {
        Room r;
        if (this.rooms.containsKey(roomId)) r = this.rooms.get(roomId);
        else throw new IllegalArgumentException("roomId doesn't exist");

        ObjectNode roomData = Json.newObject();
        roomData.put("description",r.description);

        List<String> exitDescriptions = this.getExitDescriptions(r);
        ArrayNode exitList = roomData.putArray("exits");
        for (String exit:exitDescriptions) exitList.add(exit);
        return roomData;
    }
}
