package controllers;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import model.*;

/**
 * Created by daly on 14/09/16.
 */
@Singleton
public class Commands
{
    RoomManager roomManager;

    @Inject
    public Commands(RoomManager roomManager)
    {
        this.roomManager = roomManager;
    }

    void take(String userId,String path) throws IllegalArgumentException
    {
        User u = UserManager.getUser(userId);
        Room r = roomManager.getRoom(u.room);

        int chosenPath;
        if (Character.isDigit(path.charAt(0))) chosenPath = Integer.parseInt(path);
        else chosenPath = Integer.parseInt(path.substring(5));


        System.out.println("P"+path);
        System.out.println("ewf"+chosenPath);

        Room newRoom = roomManager.getRoom(r.exits.get(chosenPath));
        u.room = newRoom.id;

        EventManager.activate(r.id,"leave "+u.name);
        EventManager.activate(newRoom.id,"newUser "+u.name);
    }

    boolean explore(String userId) throws IllegalArgumentException
    {
        User u = UserManager.getUser(userId);
        Room r = roomManager.getRoom(u.room);

        double chance = 1 / (r.exits.size() + 0.1);

        if (Math.random() < chance)
        {
            Room newRoom = RoomGenerator.generateRoom();
            roomManager.addRoom(newRoom);
            r.exits.add(newRoom.id);
            newRoom.exits.add(r.id);

            EventManager.activate(r.id,"newPath "+newRoom.description);

            return true;
        }
        else return false;
    }

    void attack(String userId,String targetId)
    {

    }

    void say(String userId,String content) throws IllegalArgumentException
    {
        User u = UserManager.getUser(userId);
        EventManager.activate(u.room,"say "+u.name+":"+content);
    }
}
