package org.trump.vincent.defaults.mappings;

/**
 * Created by Vincent on 2017/8/30 0030.
 */
/**
 * Construct default mapping of type, from Oracle or MySQL
 */
public class MSSQLType {

    public static String from(String typeName,String driverType){
        if("oracle".equalsIgnoreCase(driverType)){
            return MSSQLType.FromOracle.type(typeName);
        }else if("mysql".equalsIgnoreCase(driverType)){
            return MSSQLType.FromMySQL.type(typeName);
        }else {
            throw new UnsupportedOperationException("Warn: Sorry, None Mappings for " + driverType);
        }
    }



    enum FromOracle{
        //TODO  please do settings in accordance with OFFICIAL REFERENCE

        BOOLEAN("BOOLEAN","BIT"),
        SIGNTYPE("SIGNTYPE","SMALLINT"),
        SMALLINT("SMALLINT","SMALLINT"),
        INT("INT","INT"),
        INTEGER("INTEGER","INT"),
        FLOAT("FLOAT","FLOAT"),
        REAL("REAL","FLOAT"),
        PLS_INTEGER("PLS_INTEGER","INT"),
        LONG("LONG","VARCHAR"),
        LONG_RAW("LONG RAW","VARCHAR"),
        NUMBER("NUMBER","FLOAT"),
        NUMERIC("NUMERIC","FLOAT"),
        DEC("DEC","DEC"),
        DECIMAL("DECIMAL","FLOAT"),
        DOUBLE_PRECISION("DOUBLE PRECISION","FLOAT"),

        CHAR("CHAR","VARCHAR"),
        CHAR_VARYING("CHAR VARYING","VARCHAR"),
        CHARACTER("CHARACTER","VARCHAR"),
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

        DATE("DATE","DATETIME"),
        TIMESTAMP("TIMESTAMP","DATETIME2"),
        TIMESTAMP_WITH_LOCAL_TIME_ZONE("TIMESTAMP WITH LOCAL TIME ZONE","DATETIMEOFFSET"),
        TIMESTAMP_WITH_TIME_ZONE("TIMESTAMP WIHT TIME ZONE","DATETIMEOFFSET"),

        ROWID("ROWID","UNIQUEIDENTIFIER"),
        UROWID("UROWID","UNIQUEIDENTIFIER"),
        XMLTYPE("XMLTYPE","XML")

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

    /**
     * refer to @
     * https://technet.microsoft.com/library/Cc966396
     */
    enum FromMySQL{
        //Numeric types
        TINYINT("TINYINT","TINYINT"),
        SMALLINT("SMALLINT","SMALLINT"),
        MEDIUMINT("MEDIUMINT","INT"),
        INT("INT","INT"),
        INTEGER("INTEGER","INT"),
        BIGINT("BIGINT","BIGINT"),
        FLOAT("FLOAT","FLOAT"),
        DOUBLE("DOUBLE","FLOAT"),
        DOUBLE_PRECISION("DOUBLE PRECISION","FLOAT"),
        REAL("REAL","REAL"),
        DECIMAL("DECIMAL","DECIMAL"),
        NUMERIC("NUMERIC","NUMERIC"),

        //Date and Time
        DATE("DATE","SMALLDATETIME"),
        DATETIME("DATETIME","DATETIME"),
        TIMESTAMP("TIMESTAMP","TIMESTAMP"),
        TIME("TIME","SMALLDATETIME"),
        YEAR("YEAR","SMALLDATETIME"),

        //String Types
        CHAR("CHAR","CHAR"),
        VARCHAR("VARCHAR","VARCHAR"),
        TINYBLOB("TINYBLOB","BINARY"),
        BLOB("BLOB","VARBINARY"),
        TEXT("TEXT","TEXT"),
        MEDIUMBLOB("MEDIUMBLOB","IMAGE"),
        MEDIUMTEXT("MEDIUMTEXT","TEXT"),
        LONGBLOB("LONGBLOB","IMAGE"),
        LONGTEXT("LONGTEXT","TEXT"),
        ;

        FromMySQL(String mysqlTypeName, String mssqlTypeName) {
            this.mysqlTypeName = mysqlTypeName;
            this.mssqlTypeName = mssqlTypeName;
        }

        public static String type(String mysqlTypeName){
            FromMySQL[] types = FromMySQL.values();
            if(types!=null&& types.length>0){
                for(FromMySQL type :types){
                    if(type.getMssqlTypeName().equalsIgnoreCase(mysqlTypeName)){
                        return type.getMssqlTypeName();
                    }
                }
            }
            return null;
        }

        private String mysqlTypeName;
        private String mssqlTypeName;

        public String getMysqlTypeName() {
            return mysqlTypeName;
        }

        public void setMysqlTypeName(String mysqlTypeName) {
            this.mysqlTypeName = mysqlTypeName;
        }

        public String getMssqlTypeName() {
            return mssqlTypeName;
        }

        public void setMssqlTypeName(String mssqlTypeName) {
            this.mssqlTypeName = mssqlTypeName;
        }

    }


}
