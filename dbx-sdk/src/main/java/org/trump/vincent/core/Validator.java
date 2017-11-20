package org.trump.vincent.core;

import org.trump.vincent.model.commons.SQLConstants;
import org.trump.vincent.utilities.CloseUtil;
import com.google.common.base.Strings;

import javax.validation.constraints.NotNull;
import java.sql.*;

/**
 * Created by Vincent on 2017/8/25 0025.
 */
public class Validator {
    /**
     * Validate existence of the table
     * @param connection
     * @param tableName
     * @return
     */
    public static Boolean hasTable(@NotNull Connection connection, @NotNull String tableName){
        if(Strings.isNullOrEmpty(tableName)){
            return false;
        }
        Statement statement = null;
        try {
            statement = connection.createStatement();
            statement.executeQuery("SELECT * FROM "+tableName +" WHERE 1=0");
            return true;
        }catch (SQLException sqle){
            return false;
        }finally {
            CloseUtil.close(statement);
        }
    }

    /**
     * Validate existence of the column in the table
     * @param connection
     * @param tableName
     * @param columnName
     * @return
     */
    public static Boolean hasColumn(@NotNull Connection connection,@NotNull String tableName,@NotNull String columnName){
        if (Strings.isNullOrEmpty(tableName) || Strings.isNullOrEmpty(columnName)){
            return false;
        }
        Statement statement = null;
        try{
            statement = connection.createStatement();
            statement.executeQuery("SELECT "+columnName+" FROM "+tableName+" WHERE 1=0");
            statement.close();
        }catch (SQLException sqle){
            return false;
        }finally {
            CloseUtil.close(statement);
        }
        return false;
    }

    /**
     * Validate
     * Notice：giving the parameter for schema and catalog
     * @param connection
     * @param schema     Oracle schema is instanceName, mysql schema has no meanning ;  Sqlserver schema may be dbo and must specialize
     * @param catalog      Oracle catalog has no certain meannig, mysql catalog equalize instanceName ; Sqlserver catalog is equal to instanceName too.
     * @param tableName
     * @param columnName
     * @return
     */
    public static Boolean isPrimaryKey(@NotNull Connection connection,String schema,String catalog,@NotNull String tableName,@NotNull String columnName){
        ResultSet resultSet = null;
        try {
            DatabaseMetaData databaseMetaData = connection.getMetaData();
            resultSet = databaseMetaData.getPrimaryKeys(catalog,schema,tableName);
            for(;resultSet.next();){
                String pk = resultSet.getString(SQLConstants.COLUMN_COLUMNNAME);
                if(columnName.equalsIgnoreCase(pk)){
                    return true;
                }
            }
        }catch(SQLException sqle){
            throw new RuntimeException("Exception occurs in Validating Primary Key.",sqle);
        }finally {
            CloseUtil.close(resultSet);
        }
        return false;
    }
}
