package com.dayang.dbx.web.controller;

import com.dayang.dbx.process.Migration;
import com.dayang.dbx.process.customize.CustomizeMigration;
import com.dayang.dbx.web.WebHelper;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.trump.vincent.model.ConnectionInfo;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Vincent on 2017/8/31 0031.
 */
@RestController
public class GeneralController {

    private static Logger logger = LoggerFactory.getLogger(GeneralController.class);

    @RequestMapping(value = "/connection", method = RequestMethod.POST)
    public Map getConnectorInfo(HttpServletRequest request, HttpServletResponse response) throws IllegalAccessException,IOException{
        String connectionInfos = ServletRequestUtils.getStringParameter(request, "connectionInfo", "");
        JsonElement element = new JsonParser().parse(connectionInfos);
        Gson gson = new Gson();
        Map sourceMap = null;
        Map targetMap = null;
        if (element.isJsonObject()){
            JsonObject root = element.getAsJsonObject();
            JsonObject srcData = root.getAsJsonObject("srcInfo");
            sourceMap =  gson.fromJson(srcData, Map.class);

            JsonObject tgtData = root.getAsJsonObject("tgtInfo");
            targetMap =  gson.fromJson(tgtData, Map.class);

        }
        if(sourceMap==null || targetMap==null){
            throw new NullPointerException("Null connection information");
        }
        Map<String, String> result = new HashMap<String, String>();

        try {

            WebHelper.releaseConnection();
            WebHelper.sourceConnectionInfo = sourceMap;
            WebHelper.targetConnectionInfo = targetMap;
            ConnectionInfo sourceInfo = WebHelper.fillConnectionInfo(sourceMap);
            ConnectionInfo targetInfo = WebHelper.fillConnectionInfo(targetMap);

            WebHelper.initializeConnection(sourceInfo, targetInfo);
            WebHelper.initlizeConfiguration(sourceMap, targetMap);

            result.put("response", "success");
        }catch (final Exception e){
            logger.error("Exception occurs in building connections.",e);
            result.put("response", "failure");
        }
//        response.sendRedirect("tables");
        return result;
    }




    @RequestMapping("/migrate")
    public Map processTable(HttpServletRequest request) {
        Map<String, String> migratedDataSize = new HashMap<String, String>();
        String parameters = ServletRequestUtils.getStringParameter(request, "table", "");
        JsonElement element = new JsonParser().parse(parameters);
        Gson gson = new Gson();
        Map<String, String> tableMap = new HashMap<String, String>();
        if (element.isJsonObject()) {
            JsonObject root =  element.getAsJsonObject();
            tableMap = (Map<String, String>) gson.fromJson(root, tableMap.getClass());
        }
        String tableName = tableMap.get("table");
        logger.info("Migration begins for the table[ "+tableName+" ]");
        Migration.initialize();

//        int dataSize =  Migration.generalMigrate(tableName);
        int dataSize = CustomizeMigration.creMigrate(tableName);
        migratedDataSize.put("size", String.valueOf(dataSize));
        logger.info("Migration finished for the table[ "+tableName+" ], and "+dataSize+ " records are transfered.");

        return migratedDataSize;
    }


}
