import com.google.inject.Singleton;
import com.mongodb.MongoClient;

@Singleton
public class Database
{
    private MongoClient mongoClient;

    public Database()
    {
        mongoClient = new MongoClient("127.0.0.1", 27017);
    }
}