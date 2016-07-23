package com.juson.helpme;

import com.juson.helpme.dao.User;
import com.mongodb.MongoClient;
import com.mongodb.ServerAddress;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by qianliang on 19/7/2016.
 */
@Configuration
public class MorphiaConfig {

    private Morphia morphia;
    private MongoClient mongoClient;
    private Datastore datastore;

    public MorphiaConfig() {
        morphia = new Morphia();
        // tell Morphia where to find your classes
        // can be called multiple times with different packages or classes
        morphia.mapPackage("com.northgatecode.helpme.common.dao");

        ServerAddress serverAddress = new ServerAddress("127.0.0.1", 27017);
        mongoClient = new MongoClient(serverAddress);


        datastore = morphia.createDatastore(mongoClient, "helpme");
        datastore.ensureIndexes(User.class,false);
    }

    @Bean
    public Datastore getDatastore() {
        return datastore;
    }
}
