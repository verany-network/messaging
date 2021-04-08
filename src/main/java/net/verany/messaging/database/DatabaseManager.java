package net.verany.messaging.database;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

public class DatabaseManager {

    private final String user;
    private final String host;
    private final String password;
    private final String databaseName;
    private MongoClient client;
    private MongoDatabase database;

    public DatabaseManager(String user, String host, String password, String databaseName) {
        this.user = user;
        this.host = host;
        this.password = password;
        this.databaseName = databaseName;
    }

    public void connect() {
        MongoClientURI uri = new MongoClientURI("mongodb://" + user + ":" + password + "@" + host + "/?authSource=admin");
        client = new MongoClient(uri);
        database = client.getDatabase(databaseName);
        System.out.println("Connected to Database");
    }

    public void disconnect() {
        client.close();
        System.out.println("Disconnected from Database");
    }

    public MongoCollection<Document> getCollection(String name){
        return database.getCollection(name);
    }
}