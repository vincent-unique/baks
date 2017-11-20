package com.dayang.dbx.web;

import com.dayang.dbx.config.XConfiguration;
import com.dayang.dbx.model.ConnectionManager;
import com.dayang.dbx.service.GeneralService;
import com.google.common.base.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.trump.vincent.model.ConnectionInfo;
import org.trump.vincent.model.DataBase;
import org.trump.vincent.utilities.CloseUtil;

import java.util.Map;


/**
 * Created by Vincent on 2017/8/28 0028.
 */
public class WebHelper {

    public static Map sourceConnectionInfo;
    public static Map targetConnectionInfo;

    private static Logger logger = LoggerFactory.getLogger(WebHelper.class);

    public static boolean initializeConnection(ConnectionInfo sourceInfo , ConnectionInfo targetInfo){

        logger.info("WebHelper initialize source database connection.");
        DataBase source = new DataBase(sourceInfo).buildSelf();
        ConnectionManager.source = source;

        logger.info("WebHelper initialize target database connection.");
        DataBase target = new DataBase(targetInfo).buildSelf();
        ConnectionManager.target = target;

        ConnectionManager.online = true;
        logger.info("WebHelper ends connections' initializing.");
        return true;
    }

    public static boolean initlizeConfiguration(Map sourceConnectionInfo,Map targetConnectionInfo){
        String fromVersion = (String)sourceConnectionInfo.get("version");
        String toVersion = (String)targetConnectionInfo.get("version");
        if(Strings.isNullOrEmpty(fromVersion) || Strings.isNullOrEmpty(toVersion)){
            logger.error("You must specialize the migration version.");
            return false;
        }
        XConfiguration.configFile = XConfiguration.getConfigFile(fromVersion,toVersion);
        XConfiguration.buildCurrentConfig(XConfiguration.configFile);

        //determine current default tables
        defaultTableFile(fromVersion,toVersion);
        return true;
    }

    /**
     * configuration for some producing environment
     * @param fromVersion
     * @param toVersion
     */
    public static void defaultTableFile(String fromVersion,String toVersion){
        if(!Strings.isNullOrEmpty(fromVersion) && fromVersion.contains("CCTV")){
            GeneralService.currentTableFile = GeneralService.CCTVMAM_TABLES_CONFIGFILE;
        }else {
            GeneralService.currentTableFile = GeneralService.DEFAULT_TABLES_CONFIGFILE;
        }
    }

    public static boolean releaseConnection() {

        ConnectionManager.online = false;
        if (ConnectionManager.source !=null) {
            CloseUtil.close(ConnectionManager.source.getConnection());
            ConnectionManager.source = null;
        }
        if(ConnectionManager.target!=null) {
            CloseUtil.close(ConnectionManager.target.getConnection());
            ConnectionManager.target = null;
        }
        sourceConnectionInfo = null;
        targetConnectionInfo = null;
        XConfiguration.currentConfigs = null;
        return true;
    }

    public static ConnectionInfo fillConnectionInfo(Map info){
        if(info==null&& info.size()<1){
            throw new NullPointerException("None connection information.");
        }
        String host = (String)info.get("host");
        int port = Integer.valueOf((String) info.get("port"));
        String driverType = ((String)info.get("productName")).toLowerCase().replaceAll(" ","");
        String instanceName = (String)info.get("dbName");
        String schema = (String)info.get("schema");
        String userName = (String)info.get("username");
        String password = (String)info.get("password");
        if(Strings.isNullOrEmpty(host)|| Strings.isNullOrEmpty(userName)|| Strings.isNullOrEmpty(password)){
            throw new NullPointerException("None entire connection information.");
        }
        ConnectionInfo connectionInfo= new ConnectionInfo()
                .setDriverType(driverType)
                .setHost(host).setPort(port)
                .setUserName(userName).setPassword(password)
                .setInstanceName(instanceName)
                .setSchema(schema);
        if(!Strings.isNullOrEmpty(connectionInfo.getDriverType())){
            if("sqlserver".equalsIgnoreCase(driverType)){
                connectionInfo.setCatalog(instanceName)
                        .setSchema(schema);
            }else if ("oracle".equalsIgnoreCase(driverType)){
                connectionInfo.setSchema(userName.toUpperCase())
                        .setCatalog(null);
            }else if("mysql".equalsIgnoreCase(driverType)){
                connectionInfo.setCatalog(instanceName)
                        .setSchema(null);
            }
        }
        return connectionInfo;
    }
}
