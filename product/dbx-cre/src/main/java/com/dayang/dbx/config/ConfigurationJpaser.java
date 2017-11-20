package com.dayang.dbx.config;

import com.google.common.base.Strings;
import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.ResourceUtils;

import java.io.*;
import java.util.List;
import java.util.Map;

/**
 * Created by Vincent on 2017/8/28 0028.
 */
public class ConfigurationJpaser {

    private static Logger logger = LoggerFactory.getLogger(ConfigurationJpaser.class);


    public static List getMigrationConfig(String filePath){
        try {
            ClassPathResource configResource = new ClassPathResource(filePath);
            InputStream configInput = configResource.getInputStream();
//            InputStream configFile = ConfigurationJpaser.class.getResourceAsStream(filePath);
            Reader fileReader = new InputStreamReader(configInput);
           /* JsonElement json = new JsonParser().parse(fileReader);
            JsonArray config = json.getAsJsonArray();*/
            Gson gson = new Gson();
            return gson.fromJson(fileReader, List.class);
        }catch (IOException ioe){
            logger.error("Exception occurs in building migration configuration [ "+filePath+" ].",ioe);
        }
        return null;
    }


    /**
     * return first config from root configuration ,which is required
     * @param globalConfig
     * @param name
     * @return
     */
    public static Map fromConfig(List globalConfig , String name){
        if(globalConfig==null|| globalConfig.size()<1){
            return null;
        }
        if(Strings.isNullOrEmpty(name)){
            logger.error("Warnning, the name must not be Null.");
        }
        for (Object object : globalConfig){
            Map config = (Map)object;
            if(config.containsKey(name)){
                return (Map) config.get(name);
            }
        }
        return null;
    }

    public static <T>T fromConfigs(List parentConfig, String name){
        if(parentConfig==null|| parentConfig.size()<1){
            return null;
        }
        if(Strings.isNullOrEmpty(name)){
            logger.error("Warnning, the name must not be Null.");
        }
        for (Object object : parentConfig){
            Map config = (Map)object;
            if(config.containsKey(name)){
                return (T)config.get(name);
            }
        }
        return null;
    }

    public static <T>T fromConfigs(Map configs ,String name){
        if(configs==null|| configs.size()<1){
            return null;
        }
        if(Strings.isNullOrEmpty(name)){
            logger.error("Warnning, the name must not be Null.");
        }
        return (T)configs.get(name);
    }
}
