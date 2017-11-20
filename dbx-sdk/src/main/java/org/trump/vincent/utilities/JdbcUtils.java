package org.trump.vincent.utilities;

import com.google.common.base.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.trump.vincent.defaults.JDBCType;

import javax.validation.constraints.NotNull;
import java.io.InputStream;
import java.io.Reader;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.*;

/**
 * Created by vincent on 2017/8/20 0020.
 */
public class JdbcUtils {
    private static Logger logger = LoggerFactory.getLogger(JdbcUtils.class);

    // we can't rely on the default mapping for JDBC's ResultSet.getObject() for numerics, they're not one-to-one with PreparedStatement.setObject
    /*
    * Above ,notice is provided by MySQL
    * */


    /**
     * Get column Value oriented to source ResultSet column type
     * @param resultSet
     * @param labelIndex
     * @return
     */
    public static Object getValue(ResultSet resultSet,Integer labelIndex){
        //TODO 一定要将返回值构造成JDBC API 中定义的普适类型，可以参考MySQL 的实现

        try {
            ResultSetMetaData metaData = resultSet.getMetaData();
           /* String type = metaData.getColumnClassName(labelIndex);
            Class typeClass = Class.forName(type);*/
            /**
             * Most of DBMS type class is different
             * must not do constructing based on type name
             */
            int sqlType = metaData.getColumnType(labelIndex);
            return getValue(resultSet,labelIndex,sqlType);
        }catch (SQLException sqle){
            logger.error("Exception occurs in building parameter for PrepareStatement from ResultSet.",sqle);
        }
        return null;
    }

    /**
     *Set parameter value from select resultset , and the type is based on java type of target table 's column
     * @param resultSet
     * @param statement
     * @param labelName  source column 's name
     * @param paraIndex  the index in the PrepareStatement
     * @param javaSQLType
     */
    public static void setValue2Parameter(@NotNull ResultSet resultSet,@NotNull PreparedStatement statement,String labelName,Integer paraIndex,Integer javaSQLType){
        //TODO simple calling
       /* Object value = getValue(resultSet,labelName,javaSQLType);
        setParameter(statement,paraIndex,javaSQLType,value);*/

        try {
            /**
             * Notice: Forcibly convert the type ,may result in exception
             */
            switch (javaSQLType) {
                case Types.BIT:
                case Types.BOOLEAN: {
                    Boolean paraValue = resultSet.getBoolean(labelName);
                    statement.setBoolean(paraIndex, paraValue);
                    break;
                }

                case Types.TINYINT:{
                    Byte paraValue = resultSet.getByte(labelName);
                    statement.setByte(paraIndex,paraValue);
                    break;
                }

                case Types.SMALLINT: {
                    Short paraValue = resultSet.getShort(labelName);
                    statement.setShort(paraIndex, paraValue);
                    break;
                }
                case Types.INTEGER: {
                    Integer paraValue = resultSet.getInt(labelName);
                    statement.setInt(paraIndex, paraValue);
                    break;
                }
                case Types.BIGINT: {
                    Long paraValue = resultSet.getLong(labelName);
                    statement.setLong(paraIndex, paraValue);
                    break;
                }
                case Types.REAL: {
                    Float paraValue = resultSet.getFloat(labelName);
                    statement.setFloat(paraIndex, paraValue);
                    break;
                }
                //According to mysql driver's style
                case Types.FLOAT:
                case Types.DOUBLE: {
                    Double paraValue = resultSet.getDouble(labelName);
                    statement.setDouble(paraIndex, paraValue);
                    break;
                }
                case Types.NUMERIC:
                case Types.DECIMAL: {
                    BigDecimal paraValue = resultSet.getBigDecimal(labelName);
                    statement.setBigDecimal(paraIndex, paraValue);
                    break;
                }
                case Types.CHAR:
                case Types.VARCHAR:
                case Types.NCHAR:
                case Types.NVARCHAR: {
                    String paraValue = resultSet.getString(labelName);
                    statement.setString(paraIndex, paraValue);
                    break;
                }

                case Types.LONGVARCHAR:
                case Types.LONGNVARCHAR:{
                    Reader valueReader = resultSet.getCharacterStream(labelName);
                    statement.setCharacterStream(paraIndex,valueReader);
                    break;
                }

                case Types.DATE: {
                    Date paraValue = resultSet.getDate(labelName);
                    statement.setDate(paraIndex, paraValue);
                    break;
                }
                case Types.TIME:
//                case Types.TIME_WITH_TIMEZONE:
                {
                    Time paraValue = resultSet.getTime(labelName);
                    statement.setTime(paraIndex, paraValue);
                    break;
                }
                case Types.TIMESTAMP:
//                case Types.TIMESTAMP_WITH_TIMEZONE:
                {
                    Timestamp paraValue = resultSet.getTimestamp(labelName);
                    statement.setTimestamp(paraIndex, paraValue);
                    break;
                }

                case Types.BINARY:
                case Types.VARBINARY:{
                    byte[] paraValue = resultSet.getBytes(labelName);
                    statement.setBytes(paraIndex, paraValue);
                    break;
                }

                case Types.LONGVARBINARY: {
                    InputStream valueInS = resultSet.getBinaryStream(labelName);
                    statement.setBinaryStream(paraIndex,valueInS);
                    break;
                }

//                    case Types.NULL:
                case Types.ARRAY: {
                    Array paraValue = resultSet.getArray(labelName);
                    statement.setArray(paraIndex, paraValue);
                    break;
                }
                case Types.BLOB: {
                    Blob paraValue = resultSet.getBlob(labelName);
                    statement.setBlob(paraIndex,  paraValue);
                    break;
                }
                case Types.CLOB: {
                    Clob paraValue = resultSet.getClob(labelName);
                    statement.setClob(paraIndex, paraValue);
                    break;
                }
                case Types.REF: {
                    Ref paraValue = resultSet.getRef(labelName);
                    statement.setRef(paraIndex, (Ref) paraValue);
                    break;
                }
                case Types.DATALINK: {
                    URL paraValue = resultSet.getURL(labelName);
                    statement.setURL(paraIndex, paraValue);
                    break;
                }
                case Types.ROWID: {
                    RowId paraValue = resultSet.getRowId(labelName);
                    statement.setRowId(paraIndex, paraValue);
                    break;
                }
                case Types.NCLOB: {
                    NClob paraValue = resultSet.getNClob(labelName);
                    statement.setNClob(paraIndex, paraValue);
                    break;
                }
                case Types.SQLXML: {
                    SQLXML paraValue = resultSet.getSQLXML(labelName);
                    statement.setSQLXML(paraIndex,paraValue);
                    break;
                }


                case Types.STRUCT:
                case Types.DISTINCT:
                case Types.JAVA_OBJECT:
                case Types.OTHER:
                default:
                    statement.setObject(paraIndex, resultSet.getObject(labelName));
                    break;
            }
        }catch ( final SQLException sqle){
            logger.error("Exception occurs in building parameter for PrepareStatement from ResultSet.",sqle);
        }
    }

    /**
     * Get select value for target sql type
     * @param resultSet
     * @param labelName
     * @param targetType java SQLType ,refer to " java.sql.Types "
     * @return
     */
    public static Object getValue(@NotNull ResultSet resultSet,String labelName ,@NotNull Integer targetType){
        try {

            switch (targetType) {
                case Types.BIT:
                case Types.BOOLEAN:
                    return resultSet.getBoolean(labelName);
                case Types.TINYINT:
                    return resultSet.getByte(labelName);

                case Types.SMALLINT:
                    return resultSet.getShort(labelName);

                case Types.INTEGER:
                    return new Integer(resultSet.getInt(labelName));

                case Types.BIGINT:
                    return new Long(resultSet.getLong(labelName));
                case Types.REAL:
                    return resultSet.getFloat(labelName);
                case Types.FLOAT:
                case Types.DOUBLE:
                    return resultSet.getDouble(labelName);
                case Types.NUMERIC:
                case Types.DECIMAL:
                    return resultSet.getBigDecimal(labelName);

                case Types.CHAR:
                case Types.VARCHAR:
                case Types.NCHAR:
                case Types.NVARCHAR:
                    return resultSet.getString(labelName);

                //Referring to Java-JDBC-API doc
                //Cautiously, it returns an instance of Reader
                case Types.LONGVARCHAR:
                case Types.LONGNVARCHAR:
                    return resultSet.getCharacterStream(labelName);

                case Types.DATE:
                    return resultSet.getDate(labelName);

                case Types.TIME:
//                case Types.TIME_WITH_TIMEZONE:
                    return resultSet.getTime(labelName);

                case Types.TIMESTAMP:
//                case Types.TIMESTAMP_WITH_TIMEZONE:
                    return resultSet.getTimestamp(labelName);

                case Types.BINARY:
                case Types.VARBINARY:
                    return resultSet.getBytes(labelName);

                //Referring to Java-JDBC-API doc
                case Types.LONGVARBINARY:
                    return resultSet.getBinaryStream(labelName);

                case Types.ARRAY:
                    return resultSet.getArray(labelName);
                case Types.BLOB:
                    return resultSet.getBlob(labelName);
                case Types.CLOB:
                    return resultSet.getClob(labelName);
                case Types.REF:
                    return resultSet.getRef(labelName);
                case Types.DATALINK:
                    return resultSet.getURL(labelName);
                case Types.ROWID:
                    return resultSet.getRowId(labelName);
                case Types.NCLOB:
                    return resultSet.getNClob(labelName);
                case Types.SQLXML:
                    return resultSet.getSQLXML(labelName);

                case Types.STRUCT:
                case Types.DISTINCT:
                case Types.JAVA_OBJECT:
                case Types.OTHER:
                default:
                    return resultSet.getObject(labelName);
            }
        }catch (SQLException sqle){
            logger.error("Exception occurs in Getting Value from ResultSet.",sqle);
        }
        return null;
    }

    public static Object getValue(@NotNull ResultSet resultSet, @NotNull Integer paraIndex,@NotNull Integer targetType){
        try {

            switch (targetType) {
                case Types.BIT:
                case Types.BOOLEAN:
                    return resultSet.getBoolean(paraIndex);
                case Types.TINYINT:
                    return resultSet.getByte(paraIndex);

                case Types.SMALLINT:
                    return resultSet.getShort(paraIndex);

                case Types.INTEGER:
                    return new Integer(resultSet.getInt(paraIndex));

                case Types.BIGINT:
                    return new Long(resultSet.getLong(paraIndex));
                case Types.REAL:
                    return resultSet.getFloat(paraIndex);
                case Types.FLOAT:
                case Types.DOUBLE:
                    return resultSet.getDouble(paraIndex);
                case Types.NUMERIC:
                case Types.DECIMAL:
                    return resultSet.getBigDecimal(paraIndex);

                case Types.CHAR:
                case Types.VARCHAR:
                case Types.NCHAR:
                case Types.NVARCHAR:
                    return resultSet.getString(paraIndex);

                //Cautiously, it returns an instance of Reader
                case Types.LONGVARCHAR:
                case Types.LONGNVARCHAR:
                    return resultSet.getCharacterStream(paraIndex);

                case Types.DATE:
                    return resultSet.getDate(paraIndex);

                case Types.TIME:
//                case Types.TIME_WITH_TIMEZONE:
                    return resultSet.getTime(paraIndex);

                case Types.TIMESTAMP:
//                case Types.TIMESTAMP_WITH_TIMEZONE:
                    return resultSet.getTimestamp(paraIndex);

                case Types.BINARY:
                case Types.VARBINARY:
                    return resultSet.getBytes(paraIndex);

                //Cautoiusly ,it returns an instance of InputStream
                case Types.LONGVARBINARY:
                    return resultSet.getBinaryStream(paraIndex);

                case Types.ARRAY:
                    return resultSet.getArray(paraIndex);
                case Types.BLOB:
                    return resultSet.getBlob(paraIndex);
                case Types.CLOB:
                    return resultSet.getClob(paraIndex);
                case Types.REF:
                    return resultSet.getRef(paraIndex);
                case Types.DATALINK:
                    return resultSet.getURL(paraIndex);
                case Types.ROWID:
                    return resultSet.getRowId(paraIndex);
                case Types.NCLOB:
                    return resultSet.getNClob(paraIndex);
                case Types.SQLXML:
                    return resultSet.getSQLXML(paraIndex);

                case Types.STRUCT:
                case Types.DISTINCT:
                case Types.JAVA_OBJECT:
                case Types.OTHER:
                default:
                    return resultSet.getObject(paraIndex);
            }
        }catch (SQLException sqle){
            logger.error("Exception occurs in Getting Value from ResultSet.",sqle);
        }
        return null;
    }



    public static PreparedStatement setParameter(PreparedStatement statement, Integer paraIndex, Object paraValue){

        //TODO 针对JDBC API 规范的数据类型做类型转换

        Class objectType = null;
        if(paraValue!=null){
            objectType = paraValue.getClass();
        }else {
            try {
                //Class of class owns static fields ; on account of using style of class.fieldName
                statement.setNull(paraIndex, java.sql.Types.class.getField("****".toUpperCase()).getInt(java.sql.Types.class));
            }catch (NoSuchFieldException nos){

            }catch (IllegalAccessException ile){

            }catch (SQLException sqle){

            }
        }

        try {
            int targetSqlType = 0000;
            //Notice: SQLType refer to " java.sql.Types"

            statement.setObject(paraIndex,paraIndex,targetSqlType);
        }catch (SQLException sqle){

        }

        return statement;
    }

    /**
     * Set required default value for NOT NULL column
     * @param statement
     * @param paraIndex
     * @param typeName
     * @return
     */
    public static PreparedStatement setDefaultValue4NotNull(@NotNull PreparedStatement statement, @NotNull Integer paraIndex,@NotNull String typeName){

        JDBCType targetType = JDBCType.fromJavaType(typeName);
        if(targetType==null){
            throw new NullPointerException("None default value for [java.sql.Types: "+typeName+" ]" );
        }
        setParameter(statement,paraIndex,targetType.getDefaultValue());

        return statement;
    }

    /**
     * Spring jdbc template
     * @param statement
     * @param paraIndex
     * @param paraValue
     * @param javaSQLType
     * @return
     */
/*    public static PreparedStatement setParameter(@NotNull PreparedStatement statement, @NotNull Integer paraIndex,@NotNull Integer sqlType, Object paraValue){
        try {
            StatementCreatorUtils.setParameterValue(statement, paraIndex, sqlType, paraValue);
        }catch (SQLException sqle){
            logger.error("Exception occurs in setting parameter (SpringJDBC style) for PrepareStatement.",sqle);
        }
        return statement;
    }*/


    public static PreparedStatement setParameter(@NotNull PreparedStatement statement, @NotNull Integer paraIndex, Object paraValue, @NotNull Integer javaSQLType){
        try {
            if (paraValue == null) {
                statement.setNull(paraIndex, javaSQLType);
            }else {
                /**
                 * Notice: Forcibly convert the type ,may result in exception
                 */
                switch (javaSQLType){
                    case Types.BIT:
                    case Types.BOOLEAN:
//                        statement.setBoolean(paraIndex,(Boolean) paraValue);break;
                        statement.setBoolean(paraIndex,Boolean.valueOf((String)paraValue));break;
                    case Types.TINYINT:
                        statement.setByte(paraIndex,Byte.valueOf((String)paraValue));break;
                    case Types.SMALLINT:
//                        statement.setShort(paraIndex,(Short)paraValue);break;
                        statement.setShort(paraIndex,Short.valueOf((String)paraValue));break;
                    case Types.INTEGER:
//                        statement.setInt(paraIndex,(Integer)paraValue);break;
                        statement.setInt(paraIndex,Integer.valueOf((String)paraValue));break;
                    case Types.BIGINT:
//                        statement.setLong(paraIndex,(Long)paraValue);break;
                        statement.setLong(paraIndex,Long.valueOf((String)paraValue));break;
                    case Types.REAL:
//                        statement.setFloat(paraIndex,(Float)paraValue);break;
                        statement.setFloat(paraIndex,Float.valueOf((String)paraValue));break;
                    //According to mysql driver's style
                    case Types.FLOAT:
                    case Types.DOUBLE:
                        statement.setDouble(paraIndex,Double.valueOf((String)paraValue));break;
                    case Types.NUMERIC:
                    case Types.DECIMAL:
                        statement.setBigDecimal(paraIndex,(BigDecimal)paraValue);break;
                    case Types.CHAR:
                    case Types.VARCHAR:
                    case Types.NCHAR:
                    case Types.NVARCHAR:
                        statement.setString(paraIndex,String.valueOf(paraValue));break;

                    case Types.LONGVARCHAR:
                    case Types.LONGNVARCHAR:
                        statement.setCharacterStream(paraIndex,(Reader)paraValue);break;


                    case Types.DATE:
//                        DateFormat dateFormat = new SimpleDateFormat(String.valueOf(paraValue));
                        statement.setDate(paraIndex,(Date)paraValue);break;

                    case Types.TIME:
//                    case Types.TIME_WITH_TIMEZONE:
                        statement.setTime(paraIndex,(Time)paraValue);break;
                    case Types.TIMESTAMP:
//                    case Types.TIMESTAMP_WITH_TIMEZONE:
                        statement.setTimestamp(paraIndex,(Timestamp)paraValue);break;

                    case Types.BINARY:
                    case Types.VARBINARY:
                        statement.setBytes(paraIndex,((String)paraValue).getBytes());break;

                    case Types.LONGVARBINARY:
                        statement.setBinaryStream(paraIndex,(InputStream)paraValue);break;

//                    case Types.NULL:
                    case Types.ARRAY:
                        statement.setArray(paraIndex,(Array)paraValue);break;
                    case Types.BLOB:
                        statement.setBlob(paraIndex,(Blob)paraValue);break;
                    case Types.CLOB:
                        statement.setClob(paraIndex,(Clob)paraValue);break;
                    case Types.REF:
                        statement.setRef(paraIndex,(Ref)paraValue);break;
                    case Types.DATALINK:
                        statement.setURL(paraIndex,(URL)paraValue);break;
                    case Types.ROWID:
                        statement.setRowId(paraIndex,(RowId)paraValue);break;
                    case Types.NCLOB:
                        statement.setNClob(paraIndex,(NClob)paraValue);break;
                    case Types.SQLXML:
                        statement.setSQLXML(paraIndex,(SQLXML)paraValue);break;


                    case Types.STRUCT:
                    case Types.DISTINCT:
                    case Types.JAVA_OBJECT:
                    case Types.OTHER:
                    default:
                        statement.setObject(paraIndex,paraValue);break;
                }
            }
        }catch ( final SQLException sqle){
            logger.error("Exception occurs in setting parameter for PrepareStatement.",sqle);
        }
        return statement;
    }


    /****************** TypeName style********************
     * Various Driver may has different sql.types category or be in no accordance with java.sql.Types
     */

    public static void setValue2Parameter(@NotNull ResultSet resultSet,@NotNull PreparedStatement statement,String labelName,Integer paraIndex,String typeName){
        if(Strings.isNullOrEmpty(typeName)){
            throw new NullPointerException("You can not set paramater with null type name.");
        }
        try {
            /**
             * Notice: Forcibly convert the type ,may result in exception
             */
            switch (typeName.toUpperCase()) {
                case "BIT":
                case "BOOL":
                case "BOOLEAN": {
                    Boolean paraValue = resultSet.getBoolean(labelName);
                    statement.setBoolean(paraIndex, paraValue);
                    break;
                }

                case "TINYINT":{
                    Byte paraValue = resultSet.getByte(labelName);
                    statement.setByte(paraIndex,paraValue);
                    break;
                }

                case "MEDIUMINT":
                case "SMALLINT": {
                    Short paraValue = resultSet.getShort(labelName);
                    statement.setShort(paraIndex, paraValue);
                    break;
                }
                case "INT":
                case "INTEGER": {
                    Integer paraValue = resultSet.getInt(labelName);
                    statement.setInt(paraIndex, paraValue);
                    break;
                }
                case "LONG":
                case "BIGINT": {
                    Long paraValue = resultSet.getLong(labelName);
                    statement.setLong(paraIndex, paraValue);
                    break;
                }
                case "REAL": {
                    Float paraValue = resultSet.getFloat(labelName);
                    statement.setFloat(paraIndex, paraValue);
                    break;
                }
                //According to mysql driver's style
                case "FLOAT":
                case "DOUBLE": {
                    Double paraValue = resultSet.getDouble(labelName);
                    statement.setDouble(paraIndex, paraValue);
                    break;
                }
                case "NUMBER":
                case "NUMERIC":
                case "DECIMAL": {
                    BigDecimal paraValue = resultSet.getBigDecimal(labelName);
                    statement.setBigDecimal(paraIndex, paraValue);
                    break;
                }
                case "CHAR":
                case "VARCHAR":
                case "VARCHAR2":
                case "NCHAR":
                case "NVARCHAR":
                case "NVARCHAR2":{
                    String paraValue = resultSet.getString(labelName);
                    statement.setString(paraIndex, paraValue);
                    break;
                }

                case "LONGVARCHAR":
                case "LONGNVARCHAR":{
                    String paraValue = resultSet.getString(labelName);
                    statement.setString(paraIndex,paraValue);
//                    Reader valueReader = resultSet.getCharacterStream(labelName);
//                    statement.setCharacterStream(paraIndex,valueReader);
                    break;
                }

                case "DATE": {
                    Date paraValue = resultSet.getDate(labelName);
                    statement.setDate(paraIndex, paraValue);
                    break;
                }
                case "TIME":
                case "DATETIME":
                case "DATETIME2":
//                case Types.TIME_WITH_TIMEZONE:
                {
                    Time paraValue = resultSet.getTime(labelName);
                    statement.setTime(paraIndex, paraValue);
                    break;
                }
                case "TIMESTAMP":
//                case Types.TIMESTAMP_WITH_TIMEZONE:
                {
                    Timestamp paraValue = resultSet.getTimestamp(labelName);
                    statement.setTimestamp(paraIndex, paraValue);
                    break;
                }

                case "BINARY":
                case "VARBINARY":{
                    byte[] paraValue = resultSet.getBytes(labelName);
                    statement.setBytes(paraIndex, paraValue);
                    break;
                }

                case "IMAGE":
                case "LONGVARBINARY": {
                    byte[] paraValue = resultSet.getBytes(labelName);
                    statement.setBytes(paraIndex,paraValue);
//                    InputStream valueInS = resultSet.getBinaryStream(labelName);
//                    statement.setBinaryStream(paraIndex,valueInS);
                    break;
                }

//                    case Types.NULL:
                case "ARRAY": {
                    Array paraValue = resultSet.getArray(labelName);
                    statement.setArray(paraIndex, paraValue);
                    break;
                }
                case "BLOB": {
                    Blob paraValue = resultSet.getBlob(labelName);
                    statement.setBlob(paraIndex,  paraValue);
                    break;
                }

                case "TINYTEXT":
                case "MEDIUMTEXT":
                case "CLOB": {
                    Clob paraValue = resultSet.getClob(labelName);
                    statement.setClob(paraIndex, paraValue);
                    break;
                }
                case "REF": {
                    Ref paraValue = resultSet.getRef(labelName);
                    statement.setRef(paraIndex, (Ref) paraValue);
                    break;
                }
                case "DATALINK": {
                    URL paraValue = resultSet.getURL(labelName);
                    statement.setURL(paraIndex, paraValue);
                    break;
                }
                case "ROWID": {
                    RowId paraValue = resultSet.getRowId(labelName);
                    statement.setRowId(paraIndex, paraValue);
                    break;
                }
                case "TEXT":
                case "NTEXT":
                case "LONGTEXT":
                case "NCLOB": {
                    NClob paraValue = resultSet.getNClob(labelName);
                    statement.setNClob(paraIndex, paraValue);
                    break;
                }
                case "SQLXML": {
                    SQLXML paraValue = resultSet.getSQLXML(labelName);
                    statement.setSQLXML(paraIndex,paraValue);
                    break;
                }


                case "STRUCT":
                case "DISTINCT":
                case "JAVA_OBJECT":
                case "OTHER":
                default:
                    statement.setObject(paraIndex, resultSet.getObject(labelName));
                    break;
            }
        }catch ( final SQLException sqle){
            logger.error("Exception occurs in building parameter for PrepareStatement from ResultSet.",sqle);
        }
    }
}
