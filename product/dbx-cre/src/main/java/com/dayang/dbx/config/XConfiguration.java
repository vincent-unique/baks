package com.dayang.dbx.config;

import com.google.common.base.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.constraints.NotNull;
import java.util.*;

/**
 * Created by Vincent on 2017/8/28 0028.
 */
public class XConfiguration {

    public static final String settings_directory = "/mapping_settings/";
    public static final String default_settings_format = ".json";
    public static final String defaultConfigFileLocation = "/mapping_settings/default.json";

    public static String configFile;
    public static List currentConfigs;


    public static Map<String,Map> tablesConfig;

    private static Logger logger = LoggerFactory.getLogger(XConfiguration.class);


    /**
     * Cautiously: must not to use this method
     * @return
     */
    public static List buildDefaultConfig(){
        return ConfigurationJpaser.getMigrationConfig(defaultConfigFileLocation);
    }


    public static void buildCurrentConfig(String... configFiles){
        if(configFiles.length<1){
            logger.error("Warnning: config name is required!");
        }
        if(currentConfigs ==null){
            XConfiguration.currentConfigs = new ArrayList();
        }
        for(String file: configFiles){
            if(Strings.isNullOrEmpty(file)||"".equals(file)){
                continue;
            }
            if(!file.startsWith(settings_directory)){
                file = settings_directory + file;
            }
            if(!file.endsWith(default_settings_format)){
                file += default_settings_format;
            }
            List config = ConfigurationJpaser.getMigrationConfig(file);
            if(config!=null){
                currentConfigs.addAll(config);
            }
        }
    }

    public static void buildTablesConfig(){
        if(XConfiguration.currentConfigs==null){
            XConfiguration.buildCurrentConfig(XConfiguration.configFile);
        }
        if(currentConfigs==null|| currentConfigs.size()<1){
            return;
        }
        if(XConfiguration.tablesConfig==null){
            XConfiguration.tablesConfig = new HashMap<>();
        }
        for(Object object: XConfiguration.currentConfigs){
            if(object!=null){
                Map element = (Map) object;
                Set keys = element.keySet();
                if(keys!=null &&keys.size()>0){
                    for(Object key: keys){
                        Map config = ConfigurationJpaser.fromConfigs(element,(String)key);
                        if(config!=null){
                            XConfiguration.tablesConfig.put((String)key,config);
                        }
                    }
                }
            }
        }
    }

    public static String getConfigFile(@NotNull String fromVersion, @NotNull String toVersion){
        StringBuffer fileName = new StringBuffer().append(fromVersion.toUpperCase())
                .append("_2_").append(toVersion.toUpperCase())
                .append(".json");
        return fileName.toString();
    }
}
