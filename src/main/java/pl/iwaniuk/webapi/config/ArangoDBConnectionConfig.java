package pl.iwaniuk.webapi.config;

import com.arangodb.ArangoDB;
import com.arangodb.springframework.annotation.EnableArangoAuditing;
import com.arangodb.springframework.config.ArangoConfiguration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableArangoAuditing
public class ArangoDBConnectionConfig implements ArangoConfiguration {

    @Value("${project.db_name}")
    String name;

    @Override
    public ArangoDB.Builder arango() {
        return new ArangoDB.Builder()
                .user("jakub")
                .password("0SckgreiGYqX2O1K");
    }

    @Override
    public String database() {

        return "MyDB";
    }
}
