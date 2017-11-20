package com.dayang.dbx.config;

/**
 * Created by Vincent on 2017/8/30 0030.
 */

import com.google.common.base.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.ResourceUtils;
import org.yaml.snakeyaml.Yaml;

import javax.validation.constraints.NotNull;
import java.io.*;
import java.util.Map;
import java.util.Set;

/**
 * Entire Mappings of types
 * Default Mappings , refer to DBX-sdk 's java config ,it is global,
 * Special Mappings , refer to yaml file config of types mappings, it is prior
 */
public class TypesConfig {
    public static final String CONFIG_FILE_PATH = "/static/";

    public static final String CONFIGFILE_SQLSERVER2ORACLE = "mssql2oralce_typemapping.yml";
    public static final String CONFIGFILE_SQLSERVER2MYSQL = "mssql2mysql_typemapping.yml";
    public static final String CONFIGFILE_ORACLE2SQLSERVER = "oracle2mssql_typemapping.yml";
    public static final String CONFIGFILE_ORACLE2MYSQL = "oracle2mysql_typemapping.yml";
    public static final String CONFIGFILE_MYSQL2SQLSERVER = "mysql2mssql_typemapping.yml";
    public static final String CONFIGFILE_MYSQL2ORACLE = "mysql2oracle_typemapping.yml";


    private static Logger logger = LoggerFactory.getLogger(TypesConfig.class);

    public static String fromConfigs(@NotNull String fromTypeName, String sourceDriverType , String targetDriverType){
        Map<String,String> configs = null;
        if("sqlserver".equalsIgnoreCase(sourceDriverType) && "oracle".equalsIgnoreCase(targetDriverType)){
            configs = typesConfig(CONFIGFILE_SQLSERVER2ORACLE);

        }else if("sqlserver".equalsIgnoreCase(sourceDriverType)&&"mysql".equalsIgnoreCase(targetDriverType)){
            configs = typesConfig(CONFIGFILE_SQLSERVER2MYSQL);
        }else if("oracle".equalsIgnoreCase(sourceDriverType)&&"sqlserver".equalsIgnoreCase(targetDriverType)){
            configs = typesConfig(CONFIGFILE_ORACLE2SQLSERVER);
        }else if("oracle".equalsIgnoreCase(sourceDriverType)&&"mysql".equalsIgnoreCase(targetDriverType)){
            configs = typesConfig(CONFIGFILE_ORACLE2MYSQL);
        }else if("mysql".equalsIgnoreCase(sourceDriverType)&&"oracle".equalsIgnoreCase(targetDriverType)){
            configs = typesConfig(CONFIGFILE_MYSQL2ORACLE);
        }else if("mysql".equalsIgnoreCase(sourceDriverType)&& "sqlserver".equalsIgnoreCase(targetDriverType)){
            configs = typesConfig(CONFIGFILE_MYSQL2SQLSERVER);
        }else {
            logger.error("Not Found Configs from [ "+sourceDriverType+" ] to [ "+targetDriverType+" ].");
        }

        if(configs!=null&&configs.size()>0){
            Set<Map.Entry<String,String>> entries =  configs.entrySet();
            for(Map.Entry<String,String> entry : entries){
                if(!Strings.isNullOrEmpty(entry.getKey()) && entry.getKey().equalsIgnoreCase(fromTypeName)){
                    return entry.getValue();
                }
            }
        }
        return null;
    }

    /**
     * Configs from classpath:static/**.yml
     * @param file
     * @return
     */
    public static Map<String,String> typesConfig(String file){

        if(!Strings.isNullOrEmpty(file)){
            file = CONFIG_FILE_PATH + file;
            try {
                ClassPathResource configResource = new ClassPathResource(file);
                InputStream configStream = configResource.getInputStream();
                InputStreamReader reader = new InputStreamReader(configStream);
                Yaml yaml = new Yaml();
                Map configs = yaml.loadAs(reader,Map.class);
                return (Map) configs.get("types");
            }catch (final IOException ioe){
                logger.error("Exception occurs in parse the config file[ "+file+" ]",ioe);
            }
        }
        return null;

    }
}
