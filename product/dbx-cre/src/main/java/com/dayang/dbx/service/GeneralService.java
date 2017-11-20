package com.dayang.dbx.service;

import com.dayang.dbx.model.ConnectionManager;
import com.google.common.base.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.trump.vincent.model.DataBase;
import org.trump.vincent.model.TableMeta;
import org.yaml.snakeyaml.Yaml;

import java.io.*;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Vincent on 2017/8/29 0029.
 */
public class GeneralService {
    public static String currentTableFile;
    public static final String DEFAULT_TABLES_CONFIGFILE = "/default/default.yml";
    public static final String CCTVMAM_TABLES_CONFIGFILE = "/default/cctv_tables.yml";

    private static Logger logger = LoggerFactory.getLogger(GeneralService.class);

    public static List<String> getXTables(String expression){
        logger.info("Getting all matched tables for pattern[ "+expression+" ].");
        if(!validateConnections()){
            logger.error("Connections Exception, please rebuild.");
        }
        DataBase source = ConnectionManager.source;
        if(source!=null){
            Map<String,TableMeta> allTables = source.getAllTables();
            if(allTables!=null && allTables.size()>0){
                List<String> result = new ArrayList<>();
                for(String key: allTables.keySet()){
                    if(!Strings.isNullOrEmpty(key)&&key.toUpperCase().contains(expression.toUpperCase())){
                        result.add(key.toUpperCase());
                    }
                }
                logger.info("Result size:"+result.size());
                return result;
            }
        }

        return null;
    }


    public static List<String> getDefaultTables(String file){
        try {
            ClassPathResource classPathResource = new ClassPathResource(file);
            InputStream configInput = classPathResource.getInputStream();
//            InputStream inputStream = GeneralService.class.getClassLoader().getResourceAsStream(DEFAULT_TABLES_CONFIGFILE);
            Reader reader = new InputStreamReader(configInput);
            Yaml yaml = new Yaml();
            Map tables = yaml.loadAs(reader,Map.class);
            if(tables!=null && tables.size()>0){
                return (List)tables.get("tables");
            }
        }catch (final IOException ioe){
            logger.error("Exception occurs in getting configd from the file[ "+file+" ]",ioe);
        }
        return null;
    }

    public static boolean validateConnections(){
        logger.info("Validate the usability of connections.");
        if(!ConnectionManager.online){
            logger.info("Application is offline.");
            return false;
        }
        try {
            if(ConnectionManager.source==null|| ConnectionManager.source.getConnection()==null || ConnectionManager.source.getConnection().isClosed()){
                logger.info("Source connection is unusable.");
                return false;
            }
            if(ConnectionManager.target ==null || ConnectionManager.target.getConnection()==null || ConnectionManager.target.getConnection().isClosed()){
                logger.info("Target connection is unusable.");
                return false;
            }
        }catch (SQLException sqle){
            logger.error("Exception:",sqle);
        }
        return true;
    }
}
