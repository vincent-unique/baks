package org.trump.vincent.defaults.mappings;

/**
 * Created by Vincent on 2017/8/30 0030.
 */

/**
 * Construct default mapping of type, from Oracle or  Microsoft SqlServer
 */
public class MySQLType {

    public static String from(String typeName,String driverType){
        if("sqlserver".equalsIgnoreCase(driverType)){
            return MySQLType.FromMSSQL.type(typeName);
        }else if("oracle".equalsIgnoreCase(driverType)){
            return MySQLType.FromOracle.type(typeName);
        }else {
            throw new UnsupportedOperationException("Warn: Sorry, None Mappings for " + driverType);
        }
    }

    enum FromMSSQL{

        //TODO  please do settings in accordance with OFFICIAL REFERENCE

        /**
         * The followings is one-to-one datatypes which are from MS-SQLServer to MySQL
         */

        BIGINT("BIGINT","BIGINT"),
        BIT("BIT","BIT"),

        TINYINT("TINYINT","TINYINT"),
        SMALLINT("SMALLINT","SMALLINT"),
        INT("INT","INT"),
        INTEGER("INTEGER","INTEGER"),
        REAL("REAL","REAL"),
        FLOAT("FLOAT","FLOAT"),
        DOUBLE("DOUBLE","DOUBLE"),
        DOUBLE_PRECESION("DOUBLE PRECESION","DOUBLE PRECESION"),

        NUMERIC("NUMERIC","NUMERIC"),
        DECIMAL("DECIMAL","DECIMAL"),
        DEC("DEC","DEC"),

        CHAR("CHAR","CHAR"),
        CHARACTER("CHARACTER","CHARACTER"),
        CHAR_VARYING("CHAR VARYING","CHAR VARYING"),
        CHARACTER_VARYING("CHARACTER VARYING","CHARACTER VARYING"),
        NATIONAL_CHARACTER("NATIONAL CHARACTER","NATIONAL CHARACTER"),
        NATIONAL_CHAR_VARYING("NATIONAL CHAR VARYING","NATIONAL CHAR VARYING"),
        NATIONAL_CHARACTER_VARYING("NATIONAL CHARACTER VARYING","NATIONAL CHARACTER VARYING"),

        NCHAR("NCHAR","NCHAR"),
        NCHAR_VARYING("NCHAR VARYING","NCHAR VARYING"),

        VARCHAR("VARCHAR","VARCHAR"),
        NVARCHAR("NVARCHAR","NVARCHAR"),

        BINARY("BINARY","BINARY"),
        VARBINARY("VARBINARY","VARBINARY"),

        TEXT("TEXT","TEXT"),

        DATETIME("DATETIME","DATETIME"),
        TIMESTAMP("TIMESTAMP","TIMESTAMP"),


        /**
         * The others ,which are required conversion
         */

        IDENTITY("IDENTITY","AUTO_INCREMENT"),
        NTEXT("NTEXT","TEXT CHARACTER SET UTF8"),
        NATIONAL_TEXT("NATIONAL TEXT","TEXT CHARACTER SET UTF8"),
        SMALLDATETIME("SMALLDATETIME","DATETIME"),
      /*  MONEY("MONEY","DECIMAL(19,4)"),
        SMALL_MONEY("SMALL MONEY","DECIMAL(10,4)"),
        UNIQUEIDENTIFIER("UNIQUEIDENTIFIER","BINARY(16)"),
        SYSNAME("SYSNAME","CHAR(256)"),*/
        /**
         * Abandon Reference's Conversion , Customize proper and concise types' conversion
         */
        MONEY("MONEY","DECIMAL"),
        SMALL_MONEY("SMALL MONEY","DECIMAL"),
        UNIQUEIDENTIFIER("UNIQUEIDENTIFIER","BINARY"),
        SYSNAME("SYSNAME","VARCHAR"),

        /**
         * Add
         */
        IMAGE("IMAGE","BLOB"),

        ;

        public static String type(String MSSQLType) {
            FromMSSQL[] types = FromMSSQL.values();
            if (types != null && types.length > 0) {
                for (FromMSSQL type : types) {
                    if (type.getMSSQLType().equalsIgnoreCase(MSSQLType)) {
                        return type.getOracleType();
                    }
                }
            }
            return null;
        }

        FromMSSQL(String MSSQLType, String oracleType) {
            this.MSSQLType = MSSQLType;
            OracleType = oracleType;
        }

        private String MSSQLType;
        private String OracleType;

        public String getMSSQLType() {
            return MSSQLType;
        }

        public void setMSSQLType(String MSSQLType) {
            this.MSSQLType = MSSQLType;
        }

        public String getOracleType() {
            return OracleType;
        }

        public void setOracleType(String oracleType) {
            OracleType = oracleType;
        }

    }

    enum FromOracle{

        //TODO  please do settings in accordance with OFFICIAL REFERENCE
        BOOLEAN("BOOLEAN","BIT"),
        BIT("BIT","BIT"),
        SIGNTYPE("SIGNTYPE","SMALLINT"),
        SMALLINT("SMALLINT","SMALLINT"),
        INT("INT","INT"),
        INTEGER("INTEGER","INT"),
        FLOAT("FLOAT","FLOAT"),
        REAL("REAL","FLOAT"),
        PLS_INTEGER("PLS_INTEGER","INT"),
        LONG("LONG","VARCHAR"),
        LONG_RAW("LONG RAW","VARCHAR"),

        NUMBER("NUMBER","NUMERIC"),
        NUMERIC("NUMERIC","NUMERIC"),
        DEC("DEC","NUMERIC"),
        DECIMAL("DECIMAL","NUMERIC"),
        DOUBLE_PRECISION("DOUBLE PRECISION","NUMERIC"),

        CHAR("CHAR","CHAR"),
        CHAR_VARYING("CHAR VARYING","VARCHAR"),
        CHARACTER("CHARACTER","CHAR"),
        CHARACTER_VARYING("CHARACTER VARYING","VARCHAR"),
        CLOB("CLOB","VARCHAR"),
        VARCHAR("VARCHAR","VARCHAR"),
        VARCHAR2("VARCHAR2","VARCHAR"),

        NATIONAL_CHAR("NATIONAL CHAR","NVARCHAR"),
        NATIONAL_CHAR_VARYING("NATIONAL CHAR VARYING","NVARCHAR"),
        NATIONAL_CHARACTER("NATIONAL CHARACTER","NVARCHAR"),
        NATIONAL_CHARACTER_VARYING("NATIONAL CHARACTER VARYING","NVARCHAR"),
        NCHAR("NCHAR","NVARCHAR"),
        NCLOB("NCLOB","NVARCHAR"),
        NVARCHAR2("NVARCHAR2","NVARCHAR"),
        STRING("STRING","VARCHAR"),

        BFILE("BFILE","VARBINARY"),
        BINARY_DOUBLE("BINARY_DOUBLE","FLOAT"),
        BINARY_FLOAT("BINARY_FLOAT","FLOAT"),
        BINARY_INTEGER("BINARY_INTEGER","INT"),
        BLOB("BLOB","VARBINARY"),
        RAW("RAW","VARBINARY"),
        LONGRAW("LONGRAW","TEXT"),

        DATE("DATE","DATETIME"),
        TIMESTAMP("TIMESTAMP","TIMESTAMP"),

        ;

        FromOracle(String oracleTypeName, String mssqlTypeName) {
            this.mssqlTypeName = mssqlTypeName;
            this.oracleTypeName = oracleTypeName;
        }

        public static String type(String oracleTypeName){
            FromOracle[] types = FromOracle.values();
            if(types!=null&& types.length>0){
                for(FromOracle type :types){
                    if(type.getOracleTypeName().equalsIgnoreCase(oracleTypeName)){
                        return type.getMssqlTypeName();
                    }
                }
            }
            return null;
        }


        private String oracleTypeName;
        private String mssqlTypeName;


        public String getOracleTypeName() {
            return oracleTypeName;
        }

        public void setOracleTypeName(String oracleTypeName) {
            this.oracleTypeName = oracleTypeName;
        }

        public String getMssqlTypeName() {
            return mssqlTypeName;
        }

        public void setMssqlTypeName(String mssqlTypeName) {
            this.mssqlTypeName = mssqlTypeName;
        }

    }
}
