package org.trump.vincent.model;

import com.google.common.base.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.constraints.NotNull;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Created by vincent on 2017/8/20 0020.
 */
public class ConnectionInfo {
    private Logger logger = LoggerFactory.getLogger(getClass());

    public Connection buildConnection(@NotNull String url, @NotNull Properties properties){
        try {
            return DriverManager.getConnection(url,properties);
        }catch (SQLException sqle){
            logger.error("Build Connection occurs exception:",sqle);
        }
        return null;
    }
    public Connection buildConnection(@NotNull String url, @NotNull String userName, @NotNull String password){
        Properties properties = new Properties();
        properties.setProperty("user",userName);
        properties.setProperty("password",password);
        return this.buildConnection(url,properties);
    }

    public String buildURL(){
        this.url = buildURL(this.driverType,this.host,this.port,this.instanceName);
        return this.url;
    }

    public String buildURL(@NotNull String driverType, @NotNull String host, Integer port, @NotNull String dbName ){
        if(Strings.isNullOrEmpty(driverType)|| Strings.isNullOrEmpty(host)|| Strings.isNullOrEmpty(dbName)){
            throw new NullPointerException("Warn: Null Paras for connection URL. ");
        }
        if ("mysql".equalsIgnoreCase(driverType)) {
            StringBuilder urlStr = new StringBuilder("jdbc:mysql://");
            urlStr.append(host).append(":").append(
                    (port != null && port > 0) ? port : 3306
                ).append("/").append(dbName);
            return String.valueOf(urlStr);

        } else if ("sqlserver".equalsIgnoreCase(driverType)) {
            StringBuilder urlStr = new StringBuilder("jdbc:sqlserver://");
            urlStr.append(host).append(":")
                    .append(
                            (port!=null&&port>0)?port:1433
                    ).append(";").append("databaseName=").append(dbName);
            return String.valueOf(urlStr);

        } else if ("oracle".equalsIgnoreCase(driverType)) {
            StringBuilder urlStr = new StringBuilder("jdbc:oracle:thin").append(":");
            urlStr.append("@").append(host)
                    .append(":").append(
                    (port!=null&&port>0)?port:1521
                ).append(":").append(dbName);
            return String.valueOf(urlStr);

        }else {
            throw new UnsupportedOperationException("Warn: Sorry, Unsupported for "+driverType);
        }
    }

    public String getHost() {
        return host;
    }

    public ConnectionInfo setHost(String host) {
        this.host = host;
        return this;
    }

    public Integer getPort() {
        return port;
    }

    public ConnectionInfo setPort(Integer port) {
        this.port = port;
        return this;
    }

    public String getUrl() {
        return url;
    }

    public ConnectionInfo setUrl(String url) {
        this.url = url;
        return this;
    }

    public String getDriverType() {
        return driverType;
    }

    public ConnectionInfo setDriverType(String driverType) {
        this.driverType = driverType;
        return this;
    }

    public String getInstanceName() {
        return instanceName;
    }

    public ConnectionInfo setInstanceName(String instanceName) {
        this.instanceName = instanceName;
        return this;
    }

    public String getUserName() {
        return userName;
    }

    public ConnectionInfo setUserName(String userName) {
        this.userName = userName;
        return this;
    }

    public String getPassword() {
        return password;
    }

    public ConnectionInfo setPassword(String password) {
        this.password = password;
        return this;
    }

    public String getSchema() {
        if(this.schema==null&&this.driverType!=null&&"sqlserver".equalsIgnoreCase(this.driverType)){
            return "dbo";
        }
        return schema;
    }

    public ConnectionInfo setSchema(String schema) {
        this.schema = schema;
        return this;
    }

    public String getCatalog() {
        return catalog;
    }

    public ConnectionInfo setCatalog(String catalog) {
        this.catalog = catalog;
        return this;
    }

    //hostname for the DBMS server
    private String host;
    //service listener port
    private Integer port;
    //connection url, must build
    private String url;

    //DataBase Driver Type ,supported at current ["mysql", "sqlserver" , "oracle"]
    private String driverType;

    /**
     * Notice： oracle'schema  = user name
     * For instance name,must use it.[oracle default value : oradb]
     */
    private String instanceName;
    private String userName;
    private String password;
    /**
     * Notice ,schema and catalog has inequable meannings in differnet DataBase System
     */
    private String schema;
    private String catalog;
}
