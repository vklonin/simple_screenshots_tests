package vklonin;

import org.aeonbits.owner.Config;

@Config.Sources({"classpath:credentials.properties"})
public interface ConfigReader extends Config {
    String userLogin();
    String userPassword();
}

