package org.trump.vincent.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.trump.vincent.model.commons.SQLConstants;
import org.trump.vincent.model.commons.TableType;
import org.trump.vincent.utilities.CloseUtil;
import com.google.common.base.Strings;

import javax.validation.constraints.NotNull;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Vincent on 2017/8/22 0022.
 */
public class DataBase {
    private ConnectionInfo connectionInfo;

//    @NotNull(message = "Error: Null Connection")
    private Connection connection;
    private String dbName;
    private String dbVendor;
    private String driver;
    private DatabaseMetaData databaseMetaData;
    private Map<String,TableMeta> allTables = new HashMap<>();

    //Notice: schema and catalog has differrent meannings in most of DBMSs.
    private String schema;
    private String catalog;


    private Logger logger = LoggerFactory.getLogger(getClass());

    public DataBase(ConnectionInfo connectionInfo){
        this.connectionInfo = connectionInfo;
    }

    public DataBase buildSelf(){
        //TODO
        if(this.connectionInfo==null){
            throw new NullPointerException("Null Conncetion INFO.");
        }
        this.connection = connectionInfo.buildConnection(connectionInfo.buildURL(),connectionInfo.getUserName(),connectionInfo.getPassword());
        this.driver = this.connectionInfo.getDriverType();

        if("oracle".equalsIgnoreCase(this.driver)){
            this.dbName = this.connectionInfo.getSchema();
        }else {
            this.dbName = this.connectionInfo.getInstanceName();
        }

        if(this.connection!=null) {
            ResultSet tables = null;
            try {
                /**
                 * Warnning: Oracle connection does not implement the method - getSchema
                 */
//                this.schema = this.connection.getSchema();
//                this.catalog = this.connection.getCatalog();
                buildSchemaAndCatalog(this);

                DatabaseMetaData databaseMetaData = connection.getMetaData();
                this.databaseMetaData = databaseMetaData;

                //this.dbVendor = databaseMetaData.getDatabaseProductName();
                if ("mysql".equalsIgnoreCase(this.driver)) {
                    tables = databaseMetaData.getTables(this.catalog, null, null, new String[]{TableType.TABLE.getTypeName()});
                } else if("oracle".equalsIgnoreCase(this.driver)){
                    tables = databaseMetaData.getTables(null, this.schema, null, new String[]{TableType.TABLE.getTypeName()});
                }else if("sqlserver".equalsIgnoreCase(this.driver)){
                    tables = databaseMetaData.getTables(this.catalog,Strings.isNullOrEmpty(this.getSchema())?"dbo":this.getSchema()
                            , null, new String[]{TableType.TABLE.getTypeName()});
                }else {
                    throw new UnsupportedOperationException("Warn: Sorry, Unsupported for " + this.driver);
                }
                if(tables!=null) {
                    for (; tables.next(); ) {
//                        String tableName = tables.getString(TableType.TABLE.getTypeName());
                        String tableName = tables.getString(SQLConstants.TABLE_TABLENAME);
                        if (!Strings.isNullOrEmpty(tableName)) {
                            TableMeta tableMeta = new TableMeta(this.connectionInfo, tableName, this.schema).buildSelf(this.connection);
                            allTables.put(tableName.toUpperCase(), tableMeta);
                        }
                    }
                }
            } catch (final SQLException sqle) {
                logger.error("Build DataBase Instance MetaData occurs exception:", sqle);
            }finally {
                CloseUtil.close(tables);
            }
        }
        return this;
    }

    /**
     * @see UnsupportedOperationException
     * @param database
     */
    public void buildSchemaAndCatalog(@NotNull DataBase database){
        Connection connection = database.connection;
        try {
            if ("oracle".equalsIgnoreCase(database.driver)) {
                database.schema = database.dbName;
                database.catalog = null;
            } else if ("mysql".equalsIgnoreCase(database.driver)) {
                database.schema = null;
                database.catalog = database.dbName;
            } else if ("sqlserver".equalsIgnoreCase(database.driver)) {
                database.schema = connection.getSchema();
                database.catalog = connection.getCatalog();
            }else {
                throw new UnsupportedOperationException("Warn: Sorry, Unsupported for " + database.driver);
            }
        }catch (SQLException sqle){
            logger.error("Build DataBase schema OR catalog occurs exception:",sqle);
        }
    }


    public Connection getConnection() {
        return connection;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    public String getDbName() {
        return dbName;
    }

    public void setDbName(String dbName) {
        this.dbName = dbName;
    }

    public String getDbVendor() {
        return dbVendor;
    }

    public void setDbVendor(String dbVendor) {
        this.dbVendor = dbVendor;
    }

    public String getDriver() {
        return driver;
    }

    public void setDriver(String driver) {
        this.driver = driver;
    }

    public DatabaseMetaData getDatabaseMetaData() {
        return databaseMetaData;
    }

    public void setDatabaseMetaData(DatabaseMetaData databaseMetaData) {
        this.databaseMetaData = databaseMetaData;
    }

    public ConnectionInfo getConnectionInfo() {
        return connectionInfo;
    }

    public void setConnectionInfo(ConnectionInfo connectionInfo) {
        this.connectionInfo = connectionInfo;
    }

    public Map<String, TableMeta> getAllTables() {
        return allTables;
    }
    public void setAllTables(Map<String, TableMeta> allTables) {
        this.allTables = allTables;
    }

    public String getSchema() {
        return schema;
    }

    public void setSchema(String schema) {
        this.schema = schema;
    }

    public String getCatalog() {
        return catalog;
    }

    public void setCatalog(String catalog) {
        this.catalog = catalog;
    }
}
