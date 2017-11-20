package org.trump.vincent.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.trump.vincent.defaults.JavaType;
import org.trump.vincent.model.commons.SQLConstants;
import org.trump.vincent.model.commons.TableType;
import org.trump.vincent.utilities.CloseUtil;
import org.trump.vincent.utilities.JavaSQLUtils;
import org.trump.vincent.utilities.JdbcUtils;
import org.trump.vincent.utilities.ValidateUtils;
import com.google.common.base.Strings;

import javax.validation.constraints.NotNull;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Vincent on 2017/8/21 0021.
 */
public class TableMeta {

    private ConnectionInfo connectionInfo;
    private String name;
    private String schema;  //  default ,it refers to database name
    private List<String> PKS;
    private String type = TableType.TABLE.getTypeName();
    private Map<String,ColumnMeta> columns = new HashMap<>();

    private Logger logger = LoggerFactory.getLogger(getClass());


    public TableMeta(ConnectionInfo connectionInfo){
        this.connectionInfo = connectionInfo;
    }
    public TableMeta(ConnectionInfo connectionInfo, String tableName){
        this.connectionInfo = connectionInfo;
        this.name= tableName;
    }
    public TableMeta(ConnectionInfo connectionInfo, String tableName, String schema){
        this(connectionInfo);
        this.name = tableName;
        this.schema = schema;
    }

    /**
     * Build primary information after being constructed
     * @param selfConnection
     * @return
     */
    public TableMeta buildSelf(@NotNull Connection selfConnection){
        if(Strings.isNullOrEmpty(name)){
            throw new NullPointerException("Null Table Name.");
        }
        if("oracle".equalsIgnoreCase(this.connectionInfo.getDriverType())){
            this.PKS = buildPrimaryKeys(selfConnection,this.connectionInfo.getDriverType(),this.connectionInfo.getSchema(),this.name);
            this.columns = this.buildColumns(selfConnection,this.connectionInfo.getDriverType(),this.connectionInfo.getSchema(),this.name);
        }else {
            this.PKS = buildPrimaryKeys(selfConnection, this.connectionInfo.getDriverType(), this.connectionInfo.getInstanceName(), this.name);
            this.columns = this.buildColumns(selfConnection, this.connectionInfo.getDriverType(), this.connectionInfo.getInstanceName(), this.name);
        }
        return this;
    }

    /**
     * Get Primary Keys , exection before buildColumns
     * @param connection
     * @param driverType
     * @param dbName
     * @param tableName
     * @return
     */
    public List<String> buildPrimaryKeys(@NotNull Connection connection,@NotNull String driverType,String dbName, String tableName){
        ResultSet pkSet = null;
        DatabaseMetaData metaData = null;
        try {
            List<String> pkList = new ArrayList<>();
            metaData = connection.getMetaData();
            if ("mysql".equalsIgnoreCase(driverType)) {
                pkSet = metaData.getPrimaryKeys(dbName, null, tableName);
            } else if ("sqlserver".equalsIgnoreCase(driverType)) {
                String schema = connection.getSchema();
                pkSet = metaData.getPrimaryKeys(dbName,schema,tableName);
            } else if ("oracle".equalsIgnoreCase(driverType)) {
                pkSet = metaData.getPrimaryKeys(null,dbName,tableName);
            } else {
                throw new UnsupportedOperationException("Warn: Sorry, Unsupported for " + driverType);
            }
            while (pkSet.next()){
                String primaryKey = pkSet.getString(SQLConstants.COLUMN_COLUMNNAME);
                if(!Strings.isNullOrEmpty(primaryKey)){
                    pkList.add(primaryKey);
                }
            }
            return pkList;
        }catch (SQLException sqle){
            logger.error("Get PrimaryKeys for table["+tableName+"] occurs exception:",sqle);
        }finally {
            CloseUtil.close(pkSet);
            metaData = null;
        }
        return null;
    }

    /**
     * Before Dependence : buildPrimaryKeys
     * @param connection
     * @param driverType
     * @param dbName
     * @param tableName
     * @return
     */
    public Map<String,ColumnMeta> buildColumns(@NotNull Connection connection, @NotNull String driverType, String dbName, String tableName){
        ResultSet columnSet = null;
        DatabaseMetaData metaData = null;
        try {
            Map<String,ColumnMeta> columnModelMap = new HashMap<>();
            metaData = connection.getMetaData();
            if ("mysql".equalsIgnoreCase(driverType)) {
                columnSet = metaData.getColumns(dbName, null, tableName, null);
            } else if ("sqlserver".equalsIgnoreCase(driverType)) {
                String schema = connection.getSchema();
                columnSet = metaData.getColumns(dbName,schema,tableName,null);
            } else if ("oracle".equalsIgnoreCase(driverType)) {
                columnSet = metaData.getColumns(null,dbName,tableName,null);
            } else {
                throw new RuntimeException("Warn: Sorry, Unsupported for " + driverType);
            }
            while (columnSet.next()){
                ColumnMeta column = buildColumn(columnSet);
                if(column!=null) {
                    columnModelMap.put(column.getName().toUpperCase(), column);
                }
            }
            return columnModelMap;
        }catch (SQLException sqle){
            logger.error("Exception occurs in building columns.",sqle);
        }finally {
            CloseUtil.close(columnSet);
            metaData = null;
        }
        return null;
    }

    public ColumnMeta buildColumn(ResultSet colCursor){
        if(colCursor!=null){
            try {
                String columnName = colCursor.getString(SQLConstants.COLUMN_COLUMNNAME);
                int type = colCursor.getInt(SQLConstants.COLUMN_DATATYPE);
                String typeName = colCursor.getString(SQLConstants.COLUMN_TYPENAME);
                if(!Strings.isNullOrEmpty(typeName) && typeName.indexOf("(")>0){
                    typeName = typeName.substring(0,typeName.indexOf("("));
                }
                int length = colCursor.getInt(SQLConstants.COLUMN_COLUMNSIZE);
                boolean nullable = colCursor.getShort(SQLConstants.COLUMN_ISNULLABLE)== (short) 1;
                int position = colCursor.getInt(SQLConstants.COLUMN_ORGINALPOSITION);
//                Object defaultValue = JavaSQLUtils.getValue(colCursor,SQLConstants.COLUMN_DEFAULTVALUE, JavaType.javaType(typeName));
                /**
                 * Cautiously, most DBMS maintains uppercase for tableName , columnName
                 */
                ColumnMeta columnMeta = new ColumnMeta(Strings.isNullOrEmpty(columnName)?columnName:columnName.toUpperCase(),
                        Strings.isNullOrEmpty(typeName)?typeName:typeName.toUpperCase(),
                        type,nullable,length,position);
                columnMeta.setPrimaryKey(ValidateUtils.isPrimaryKey(this.PKS,columnName));
                return columnMeta;
            }catch (SQLException sqle){
                logger.error("Exception occurs in building columns for the table[ "+this.name+" ].",sqle);
            }
        }
        return null;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSchema() {
        return schema;
    }

    public void setSchema(String schema) {
        this.schema = schema;
    }

    public List<String> getPKS() {
        return PKS;
    }

    public void setPKS(List<String> PKS) {
        this.PKS = PKS;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Map<String, ColumnMeta> getColumns() {
        return columns;
    }
    public void setColumns(Map<String, ColumnMeta> columns) {
        this.columns = columns;
    }

}