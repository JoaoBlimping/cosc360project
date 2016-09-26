package controllers;

import scala.concurrent.Promise;
import sun.awt.image.ImageWatched;

import java.util.LinkedList;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Created by daly on 21/09/16.
 */
public class EventManager
{
    private static ConcurrentHashMap<Long,ConcurrentLinkedQueue<Promise<String>>> eventRooms = new ConcurrentHashMap<>();


    /** add a promise to a set of promises to be fulfilled for a given room */
    static void addPromise(long roomId,Promise<String> promise)
    {
        if (!eventRooms.containsKey(roomId)) eventRooms.put(roomId,new ConcurrentLinkedQueue<>());
        eventRooms.get(roomId).add(promise);
    }

    /** send an event to all clients connected from a given room */
    static void activate(long roomId, String event)
    {
        if (!eventRooms.containsKey(roomId)) return;
        ConcurrentLinkedQueue<Promise<String>> room = eventRooms.get(roomId);
        if (room == null) return;
        while (!room.isEmpty()) room.remove().success(event);
    }




}
