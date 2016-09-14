package model;

/**
 * generates rooms randomly
 * Created by daly on 8/09/16.
 */
public class RoomGenerator
{
    private static long nRooms = 0;

    public static final String[] STARTS = {"a windswept","a rugged","a sunny",
            "a rainy","a gloomy","a warm","a cold","an icy","a dry","a damp",
            "a solemn","a silent","a sparkling","a nice"};

    public static final String[] MIDDLES = {"hill","plain","valley","cave",
            "mountain","cove","cairn","hollow","clearing","ravine","mesa",
            "place","garden"};

    public static final String[] BRIDGES = {"covered in","densely populated with",
            "brimming with","lightly covered in","surrounded by"};

    public static final String[] ENDS = {"sand","grass","moss","ferns","shrubs",
            "flowers","clovers","thistles","dirt","dust","vines","snow","bones",
            "insects","gemstones","rocks"};


    public static Room generateRoom()
    {
        String start = STARTS[(int)Math.floor(Math.random() * STARTS.length)];
        String middle = MIDDLES[(int)Math.floor(Math.random() * MIDDLES.length)];
        String bridge = BRIDGES[(int)Math.floor(Math.random() * BRIDGES.length)];
        String end = ENDS[(int)Math.floor(Math.random() * ENDS.length)];
        return new Room(nRooms,start+middle+bridge+end);
    }
}
