package org.trump.vincent.defaults;

import com.google.common.base.Strings;

import java.net.URI;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.sql.Types;

/**
 * Created by Vincent on 2017/8/30 0030.
 */
public enum JDBCType {
    /**
     * Numeric Types
     */
    BIT(Types.BIT,"BIT",0),
    BOOL(null,"BOOL",false),
    BOOLEAN(Types.BOOLEAN,"BOOLEAN",false),

    BYTE(null,"BYTE",0),
    TINYINT(Types.TINYINT,"TINYINT",0),

    SMALLINT(Types.SMALLINT,"SMALLINT",0),
    MEDIUMINT(null,"MEDIUMINT",0),
    // INT is synonym for INTEGER
    INT(null,"INT",0),
    INTEGER(Types.INTEGER,"INTEGER",0),
    BIGINT(Types.BIGINT,"BIGINT",0),
    LONG(null,"LONG",0),

    REAL(Types.REAL,"REAL",0),
    FLOAT(Types.FLOAT,"FLOAT",0),
    DOUBLE(Types.DOUBLE,"DOUBLE",0),

    NUMERIC(Types.NUMERIC,"NUMERIC",0),
    DECIMAL(Types.DECIMAL,"DECIMAL",0),
    BIGDECIMAL(null,"BIGDECIMAL",0),
    NUMBER(null,"NUMBER",0),

    /**
     * Date and Time
     */
    TIME(Types.TIME,"TIME",new Time(System.currentTimeMillis())),
    //    TIME(Types.TIME,"TIME",null),
    DATE(Types.DATE,"DATE",new Date(System.currentTimeMillis())),
    DATETIME(null,"DATETIME",new Date(System.currentTimeMillis())),
    DATETIME2(null,"DATETIME2",new Date(System.currentTimeMillis())),

    //    DATE(Types.DATE,"DATE",null),
    TIMESTAMP(Types.TIMESTAMP,"TIMESTAMP",new Timestamp(System.currentTimeMillis())),

    /**
     * String Types
     */

    CHAR(Types.CHAR,"CHAR","_null"),
    VARCHAR(Types.VARCHAR,"VARCHAR","_null"),
    VARCHAR2(null,"VARCHAR2","_null"),
    NCHAR(Types.NCHAR,"NCHAR","_null"),
    NVARCHAR(Types.NVARCHAR,"NVARCHAR","_null"),
    NVARCHAR2(null,"NVARCHAR2","_null"),
    LONGVARCHAR(Types.LONGVARCHAR,"LONGVARCHAR","_null"),
    LONGNVARCHAR(Types.LONGNVARCHAR,"LONGNVARCHAR","_null"),

    BINARY(Types.BINARY,"BINARY","_null"),
    VARBINARY(Types.VARBINARY,"VARBINARY","_null"),
    LONGVARBINARY(Types.LONGVARBINARY,"LONGVARBINARY","_null"),
    IMAGE(null,"IMAGE","_null"),

    BLOB(Types.BLOB,"BLOB","_null"),

    CLOB(Types.CLOB,"CLOB","_null"),
    TINYTEXT(null,"TINYTEXT","_null"),
    MEDIUMTEXT(null,"MEDIUMTEXT","_null"),

    NCLOB(Types.NCLOB,"NCLOB","_null"),
    TEXT(null,"TEXT","_null"),
    NTEXT(null,"NTEXT","_null"),
    LONGTEXT(null,"LONGTEXT","_null"),

    // object
    JAVA_OBJECT(Types.JAVA_OBJECT,"JAVA_OBJECT",new Object()),

    /**
     * Others, Illegal
     */
    ARRAY(Types.ARRAY,"ARRAY",null),
    REF(Types.REF,"REF",null),
    DATALINK(Types.DATALINK,"DATALINK",null),
    ROWID(Types.ROWID,"ROWID",null),
    SQLXML(Types.SQLXML,"SQLXML",null),
    STRUCT(Types.STRUCT,"STRUCT",null),
    DISTINCT(Types.DISTINCT,"DISTINCT",null),

    ;

    JDBCType(Integer javaType, String typeName, Object defaultValue){
        this.javaType = javaType;
        this.typeName = typeName;
        this.defaultValue = defaultValue;
    }

    /**
     * primary method
     * @param typeName
     * @return
     */
    public static JDBCType fromJavaType(String typeName){
        if(Strings.isNullOrEmpty(typeName)){
            throw new NullPointerException("Can not construct Type for null java type.");
        }
        JDBCType[] all = JDBCType.values();
        if(all!=null&& all.length>0){
            for(JDBCType item : all){
                if(item.getTypeName().equalsIgnoreCase(typeName)){
                    return item;
                }
            }
        }
        return null;
    }

    /**
     * Various Driver may have its own JDBC Type category which is in no accordance with java.sql.Types
     * So , please apply another method ( refer to Type Name)
     * @param javaType
     * @return
     */
    @Deprecated
    public static JDBCType fromJavaType(Integer javaType){
        if(javaType==null){
            throw new NullPointerException("Can not construct Type for null java type.");
        }
        JDBCType[] all = JDBCType.values();
        if(all!=null&& all.length>0){
            for(JDBCType item : all){
                if(item.getJavaType()==javaType){
                    return item;
                }
            }
        }
        return null;
    }
    private Integer javaType;
    private String typeName;
    private Object defaultValue;


    public Object getDefaultValue() {
        return defaultValue;
    }

    public JDBCType setDefaultValue(Object defaultValue) {
        this.defaultValue = defaultValue;
        return this;
    }
    public Integer getJavaType() {
        return javaType;
    }

    public JDBCType setJavaType(Integer javaType) {
        this.javaType = javaType;
        return this;
    }

    public String getTypeName() {
        return typeName;
    }

    public JDBCType setTypeName(String typeName) {
        this.typeName = typeName;
        return this;
    }

}
