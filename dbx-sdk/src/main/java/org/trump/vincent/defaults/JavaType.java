package org.trump.vincent.defaults;

import com.google.common.base.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.Date;

/**
 * Created by Vincent on 2017/9/5 0005.
 */
public class JavaType {
    private static Logger logger = LoggerFactory.getLogger(JavaType.class);

    public static org.trump.vincent.defaults.JavaType.Types fromSqlType(@NotNull String sqlTypeName){
        String javaTypeName = javaType(sqlTypeName);
        if(Strings.isNullOrEmpty(javaTypeName)){
            logger.warn("Not Found the target java type for [ "+sqlTypeName +" ].");
            return null;
        }
        return JavaType.Types.forType(javaTypeName);
    }
    public static String javaType(@NotNull String sqlTypeName){
        org.trump.vincent.defaults.JavaType.FromSqlType [] types = FromSqlType.values();
        if(types != null && types.length>0){
            for(FromSqlType type : types){
                if(sqlTypeName.equalsIgnoreCase(type.getSqlType())){
                    return type.getJavaType();
                }
            }
        }
        return null;
    }


    public enum FromSqlType{
        BIT(JDBCType.BIT.getTypeName(),Types.BOOLEAN.getTypeName()),
        BOOL("BOOL",Types.BOOLEAN.getTypeName()),
        BOOLEAN(JDBCType.BOOLEAN.getTypeName(),Types.BOOLEAN.getTypeName()),

        BYTE("BYTE",Types.BYTE.getTypeName()),
        TINYINT(JDBCType.TINYINT.getTypeName(),Types.BYTE.getTypeName()),

        SMALLINT(JDBCType.SMALLINT.getTypeName(),Types.SHORT.getTypeName()),
        MEDIUMINT("MEDIUMINT",Types.SHORT.getTypeName()),

        INT("INT",Types.INT.getTypeName()),
        INTEGER(JDBCType.INTEGER.getTypeName(),Types.INT.getTypeName()),

        BIGINT(JDBCType.BIGINT.getTypeName(),Types.LONG.getTypeName()),
        LONG("LONG",Types.LONG.getTypeName()),

        REAL(JDBCType.REAL.getTypeName(),Types.FLOAT.getTypeName()),

        FLOAT(JDBCType.FLOAT.getTypeName(),Types.DOUBLE.getTypeName()),
        DOUBLE(JDBCType.DOUBLE.getTypeName(),Types.DOUBLE.getTypeName()),

        DECIMAL(JDBCType.DECIMAL.getTypeName(),Types.BIGDECIMAL.getTypeName()),
        BIGDECIMAL("BIGDECIMAL",Types.BIGDECIMAL.getTypeName()),
        NUMERIC(JDBCType.NUMERIC.getTypeName(),Types.BIGDECIMAL.getTypeName()),
        NUMBER("NUMBER",Types.BIGDECIMAL.getTypeName()),

        CHAR(JDBCType.CHAR.getTypeName(),Types.STRING.getTypeName()),
        NCHAR(JDBCType.NCHAR.getTypeName(),Types.STRING.getTypeName()),
        VARCHAR(JDBCType.VARCHAR.getTypeName(),Types.STRING.getTypeName()),
        VARCHAR2("VARCHAR2",Types.STRING.getTypeName()),
        NVARCHAR(JDBCType.NVARCHAR.getTypeName(),Types.STRING.getTypeName()),
        NVARCHAR2("NVARCHAR2",Types.STRING.getTypeName()),
        LONGVARCHAR(JDBCType.LONGVARCHAR.getTypeName(),Types.STRING.getTypeName()),
        LONGNVARCHAR(JDBCType.LONGNVARCHAR.getTypeName(),Types.STRING.getTypeName()),

        DATE(JDBCType.DATE.getTypeName(),Types.DATE.getTypeName()),
        TIME(JDBCType.TIME.getTypeName(),Types.TIME.getTypeName()),

        DATETIME("DATETIME",Types.TIMESTAMP.getTypeName()),
//        DATETIME("DATETIME",Types.TIME.getTypeName()),
        DATETIME2("DATETIME2",Types.TIME.getTypeName()),
        TIMESTAMP(JDBCType.TIMESTAMP.getTypeName(),Types.TIMESTAMP.getTypeName()),

        BINARY(JDBCType.BINARY.getTypeName(),Types.BYTES.getTypeName()),
        VARBINARY(JDBCType.VARBINARY.getTypeName(),Types.BYTES.getTypeName()),
        LONGVARBINARY(JDBCType.LONGVARBINARY.getTypeName(),Types.BYTES.getTypeName()),
        IMAGE("IMAGE",Types.BYTES.getTypeName()),

        CLOB(JDBCType.CLOB.getTypeName(),Types.CLOB.getTypeName()),
        TINYTEXT("TINYTEXT",Types.CLOB.getTypeName()),
        MEDIUMTEXT("MEDIUMTEXT",Types.CLOB.getTypeName()),

        NCLOB(JDBCType.NCLOB.getTypeName(),Types.NCLOB.getTypeName()),
        TEXT("TEXT",Types.NCLOB.getTypeName()),
        NTEXT("NTEXT",Types.NCLOB.getTypeName()),
        LONGTEXT("LONGTEXT",Types.NCLOB.getTypeName()),

        BLOB(JDBCType.BLOB.getTypeName(),Types.BLOB.getTypeName()),

        ARRAY(JDBCType.ARRAY.getTypeName(),Types.ARRAY.getTypeName()),
        REF(JDBCType.REF.getTypeName(),Types.REF.getTypeName()),
        DATALINK(JDBCType.DATALINK.getTypeName(),Types.URL.getTypeName()),
        STRUCT(JDBCType.STRUCT.getTypeName(),Types.STRUCT.getTypeName()),
        JAVAOBJECT(JDBCType.JAVA_OBJECT.getTypeName(),Types.OBJECT.getTypeName())
        ;
        FromSqlType(String sqlType,String javaType){
            this.sqlType = sqlType;
            this.javaType = javaType;
        }

        public String getSqlType() {
            return sqlType;
        }

        public void setSqlType(String sqlType) {
            this.sqlType = sqlType;
        }

        public String getJavaType() {
            return javaType;
        }

        public void setJavaType(String javaType) {
            this.javaType = javaType;
        }

        private String sqlType;
        private String javaType;
    }
    public enum Types{
        BOOLEAN("Boolean",boolean.class),
        BYTE("Byte",byte.class),
        BYTES("Byte[]",byte[].class),

        INT("Integer",int.class),
        SHORT("Short",short.class),
        LONG("Long",long.class),
        FLOAT("Float",float.class),
        DOUBLE("Double",double.class),
        BIGDECIMAL("BigDecimal", java.math.BigDecimal.class),

        STRING("String",String.class),
        CLOB("Clob",java.sql.Clob.class),
        NCLOB("NClob",java.sql.NClob.class),

        BLOB("Blob",java.sql.Blob.class),

        DATE("Date",java.sql.Date.class),
        TIME("Time",java.sql.Time.class),
        TIMESTAMP("Timestamp",java.sql.Timestamp.class),

        ARRAY("Array",java.sql.Array.class),
        STRUCT("Struct",java.sql.Struct.class),
        REF("Ref",java.sql.Ref.class),
        URL("URL", java.net.URL.class),
        OBJECT("Object",Object.class),
        ;
        Types(String typeName,Class typeClass){
            this.typeName = typeName;
            this.typeClass = typeClass;
        }

        public static Types forType(String typeName){
            org.trump.vincent.defaults.JavaType.Types[] types = Types.values();
            if(types != null && types.length>0){
                for(Types type : types){
                    if(typeName.equalsIgnoreCase(type.getTypeName())){
                        return type;
                    }
                }
            }
            return null;
        }
        public String getTypeName() {
            return typeName;
        }

        public void setTypeName(String typeName) {
            this.typeName = typeName;
        }

        private String typeName;
        private Class typeClass;

    }
}

