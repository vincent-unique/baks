package org.trump.vincent.defaults.mappings;

/**
 * Created by Vincent on 2017/8/30 0030.
 */

/**
 * Construct default mapping of type, from MySQL or  Microsoft SqlServer
 */
public class OracleType {


    public static String from(String typeName,String driverType){
        if("sqlserver".equalsIgnoreCase(driverType)){
            return OracleType.FromMSSQL.type(typeName);
        }else if("mysql".equalsIgnoreCase(driverType)){
            return OracleType.FromMySQL.type(typeName);
        }else {
            throw new UnsupportedOperationException("Warn: Sorry, None Mappings for " + driverType);
        }
    }

    /**
     * refer to @
     * http://docs.oracle.com/cd/E12151_01/doc.150/e12156/ss_oracle_compared.htm
     */
    enum FromMSSQL{
        BIT("BIT","NUMERIC"),
        INT("INT","NUMERIC"),
        INTEGER("INTEGER","NUMERIC"),
        SMALLINT("SMALLINT","NUMERIC"),
        TINYINT("TINYINT","NUMERIC"),
        REAL("REAL","FLOAT"),
        NVARCHAR("NVARCHAR","NVARCHAR2"),
        CHAR("CHAR","CHAR"),
        NCHAR("NCHAR","VARCHAR2"),
        VARCHAR("VARCHAR","VARCHAR2"),
        TEXT("TEXT","CLOB"),
        NTEXT("NTEXT","NCLOB"),
        IMAGE("IMAGE","BLOB"),
        BINARY("BINARY","BLOB"),
        VARBINARY("VARBINARY","BLOB"),
        DATETIME("DATETIME","DATE"),
        SMALLDATETIME("SMALLDATETIME","DATE"),
        MONEY("MONEY","NUMBER"),
        SMALLMONEY("SMALLMONEY","NUMBER"),
        TIMESTAMP("TIMESTAMP","NUMBER"),
        SYSNAME("SYSNAME","VARCHAR2")

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


    /**
     * refer to @
     * http://docs.oracle.com/cd/E12151_01/doc.150/e12155/oracle_mysql_compared.htm
     */
    enum FromMySQL {
        BIT("BIT","RAW"),
        BIGINT("BIGINT","NUMBER"),
        BLOB("BLOB","BLOB"),
        CHAR("CHAR","CHAR"),
        DATE("DATE","DATE"),
        DATETIME("DATETIME","DATE"),
        DECIMAL("DECIMAL","FLOAT"),
        DOUBLE("DOUBLE","FLOAT"),
        DOUBLE_PRECISION("DOUBLE PRECISION","FLOAT"),
        ENUM("ENUM","VARCHAR2"),
        FLOAT("FLOAT","FLOAT"),
        INT("INT","NUMBER"),
        INTEGER("INTEGER","NUMBER"),
        LONGBLOB("LONGBLOB","BLOB"),
        LONGTEXT("LONGTEXT","CLOB"),
        MEDIUMBLOB("MEDIUMBLOB","BLOB"),
        MEDIUMINT("MEDIUMINT","NUMBER"),
        MEDIUMTEXT("MEDIUMTEXT","CLOB"),
        NUMERIC("NUMERIC","NUMBER"),
        REAL("REAL","FLOAT"),
        SET("SET","VARCHAR2"),
        SMALLINT("SMALLINT","NUMBER"),
        TEXT("TEXT","CLOB"),
        TIME("TIME","DATE"),
        TIMESTAMP("TIMESTAMP","DATE"),
        TINYBLOB("TINYBLOB","RAW"),
        TINYINT("TINYINT","NUMBER"),
        TINYTEXT("TINYTEXT","VARCHAR2"),
        VARCHAR("VARCHAR","VARCHAR2"),
        YEAR("YEAR","NUMBER")

        ;
        FromMySQL(String mysqlTypeName, String oracleType) {
            this.mysqlTypeName = mysqlTypeName;
            this.oracleType = oracleType;
        }

        public static String type(String mysqlTypeName) {
            FromMySQL[] types = FromMySQL.values();
            if (types != null && types.length > 0) {
                for (FromMySQL type : types) {
                    if (type.getMysqlTypeName().equalsIgnoreCase(mysqlTypeName)) {
                        return type.getOracleType();
                    }
                }
            }
            return null;
        }

        public String getMysqlTypeName() {
            return mysqlTypeName;
        }

        public void setMysqlTypeName(String mysqlTypeName) {
            this.mysqlTypeName = mysqlTypeName;
        }

        public String getOracleType() {
            return oracleType;
        }

        public void setOracleType(String oracleType) {
            this.oracleType = oracleType;
        }

        private String mysqlTypeName;
        private String oracleType;

    }
}
