package controllers;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import model.*;
import org.bson.types.ObjectId;

/**
 * Created by daly on 14/09/16.
 */
@Singleton
public class Commands
{
    RoomManager roomManager;
    Database database;

    @Inject
    public Commands(RoomManager roomManager,Database database)
    {
        this.roomManager = roomManager;
        this.database = database;
    }

    void take(ObjectId userId,String path) throws IllegalArgumentException
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

        database.addEvent(userId,"take",r.id);

        EventManager.activate(r.id,"leave "+u.name);
        EventManager.activate(newRoom.id,"newUser "+u.name);
    }

    boolean explore(ObjectId userId) throws IllegalArgumentException
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
            database.addEvent(userId,"explore",r.id);

            return true;
        }
        else return false;
    }

    void say(ObjectId userId,String content) throws IllegalArgumentException
    {
        User u = UserManager.getUser(userId);
        database.addEvent(userId,"say",u.room);
        EventManager.activate(u.room,"say "+u.name+":"+content);
    }
}
