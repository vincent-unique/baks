package org.trump.vincent.core;

import com.google.common.base.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.trump.vincent.defaults.mappings.MSSQLType;
import org.trump.vincent.defaults.mappings.MySQLType;
import org.trump.vincent.defaults.mappings.OracleType;
import org.trump.vincent.model.ColumnMeta;
import org.trump.vincent.model.TableMeta;

import javax.validation.constraints.NotNull;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

/**
 * Created by Vincent on 2017/8/25 0025.
 */

/**
 * Default generate functions have be provided in DBX-sdk [ Generator ]
 * Nevertheless, you may has special type mappings ,etc.
 * SO to build no-default generator by yourselves .
 * AND , static method is unavailable to Override;
 * you must to overwrite the same name static Method to sunk them .
 */
public class Generator {

    private static Logger logger = LoggerFactory.getLogger(Generator.class);

    public static String generateDDL(@NotNull TableMeta sourceTable , String targetTableName, String fromDriver , String targetDriverType){
        switch (targetDriverType){
            case "mysql":
                return generateDDL4MSSQL(sourceTable,fromDriver,targetTableName);
            case "sqlserver":
                return generateDDL4MSSQL(sourceTable,fromDriver,targetTableName);
            case "oracle":
            default:
                return generateDDL4Oracle(sourceTable,fromDriver,targetTableName);
        }
    }

    /**
     * Generate the Oracle clause of creating table from source TableMeta
     * @param sourceTable
     * @param replaceTableName
     * @return
     */
    public static String generateDDL4Oracle(@NotNull TableMeta sourceTable ,String sourceDriverType, String replaceTableName){

        if(Strings.isNullOrEmpty(replaceTableName)){
            replaceTableName = sourceTable.getName();
        }
        Set<Map.Entry<String,ColumnMeta>> columns =  sourceTable.getColumns()!=null? sourceTable.getColumns().entrySet():null;
        if(columns!=null) {
            StringBuilder ddlClause = new StringBuilder("CREATE TABLE ").append(replaceTableName.toUpperCase()).append("( ");
            for (Map.Entry<String,ColumnMeta> column : columns){
                ColumnMeta columnMeta = column.getValue();
                ddlClause.append(columnMeta.getName().toUpperCase());
                String typeName = columnMeta.getTypeName();
                //mapping type for different driver
                if(!"oracle".equalsIgnoreCase(sourceDriverType)) {
                   typeName = OracleType.from(columnMeta.getTypeName().toUpperCase(), sourceDriverType);
                }
                if(Strings.isNullOrEmpty(typeName)||"".equals(typeName)){
                    logger.warn("None type mapping for "+columnMeta.getName()+"[ "+columnMeta.getTypeName()+" ]; original type will be applied.");
                    typeName = columnMeta.getTypeName();
                }
                ddlClause.append(" ").append(typeName.toUpperCase());
                //Some types may need limit Length Property
                switch (typeName.toUpperCase()){
                    case "NVARCHAR2":
                    case "VARCHAR2":
//                    case "TIMESTAMP":
                        ddlClause.append("(").append(columnMeta.getLength()).append(")");
                        break;
                }
                if(!columnMeta.getNullable()){
                    ddlClause.append(" ").append("NOT NULL").append(",");
                }
            }
            //delete the last comma
            ddlClause.setLength(ddlClause.length()-1);
            if(sourceTable.getPKS()!=null && sourceTable.getPKS().size()>0){
                ddlClause.append(",")
                        .append("CONSTRAINT ").append(replaceTableName.toUpperCase()+"_PKS").append(" ")
                        .append("PRIMARY KEY").append(" (");
                for (String pk: sourceTable.getPKS()){
                    if(!Strings.isNullOrEmpty(pk)){
                        ddlClause.append(pk.toUpperCase()).append(",");
                    }
                }
                ddlClause.setLength(ddlClause.length()-1);
                ddlClause.append(" )");
            }
            ddlClause.append(" )");
            return ddlClause.toString();
        }else{
            return null;
        }
    }

    /**
     * Generate the MySQL clause of creating table from source TableMeta
     * @param sourceTable
     * @param replaceTableName
     * @return
     */
    public static String generateDDL4MySQL(@NotNull TableMeta sourceTable,String sourceDBType,String replaceTableName){
        if(Strings.isNullOrEmpty(replaceTableName)){
            replaceTableName = sourceTable.getName();
        }
        Set<Map.Entry<String,ColumnMeta>> columns =  sourceTable.getColumns()!=null? sourceTable.getColumns().entrySet():null;
        if(columns!=null) {
            StringBuilder ddlClause = new StringBuilder("CREATE TABLE ").append(replaceTableName.toUpperCase()).append("( ");
            for (Map.Entry<String,ColumnMeta> column : columns){
                ColumnMeta columnMeta = column.getValue();
                ddlClause.append(columnMeta.getName().toUpperCase());

                String typeName = columnMeta.getTypeName();
                if(!"mysql".equalsIgnoreCase(sourceDBType)){
                    typeName = MySQLType.from(columnMeta.getTypeName().toUpperCase(),sourceDBType);
                }
                if(Strings.isNullOrEmpty(typeName) || "".equals(typeName)){
                    logger.warn("None type mapping for "+columnMeta.getName()+"[ "+columnMeta.getTypeName()+" ]; original type will be applied.");
                    typeName = columnMeta.getTypeName();
                }
                ddlClause.append(" ").append(typeName.toUpperCase());
                //Some types may need limit Length Property
                switch (typeName.toUpperCase()){
                    case "VARCHAR":
                    case "INT":
                        ddlClause.append("(").append(columnMeta.getLength()).append(")");
                        break;
                }
                if(!columnMeta.getNullable()){
                    ddlClause.append(" ").append("NOT NULL").append(",");
                }
            }
            //delete the last comma
            ddlClause.setLength(ddlClause.length()-1);
            if(sourceTable.getPKS()!=null && sourceTable.getPKS().size()>0){
                ddlClause.append(",")
                        .append("CONSTRAINT ").append(replaceTableName.toUpperCase()+"_PKS").append(" ")
                        .append("PRIMARY KEY").append(" (");
                for (String pk: sourceTable.getPKS()){
                    if(!Strings.isNullOrEmpty(pk)){
                        ddlClause.append(pk.toUpperCase()).append(",");
                    }
                }
                ddlClause.setLength(ddlClause.length()-1);
                ddlClause.append(" )");
            }
            ddlClause.append(" )");
            return ddlClause.toString();
        }else{
            return null;
        }
    }

    /**
     * Generate the MS sql-server clause of creating table from source TableMeta
     * @param sourceTable
     * @param replaceTableName
     * @return
     */
    public static String generateDDL4MSSQL(@NotNull TableMeta sourceTable, String sourceDBType,String replaceTableName ){
        if(Strings.isNullOrEmpty(replaceTableName)){
            replaceTableName = sourceTable.getName();
        }
        Set<Map.Entry<String,ColumnMeta>> columns =  sourceTable.getColumns()!=null? sourceTable.getColumns().entrySet():null;
        if(columns!=null) {
            StringBuilder ddlClause = new StringBuilder("CREATE TABLE ").append(replaceTableName.toUpperCase()).append("( ");
            for (Map.Entry<String,ColumnMeta> column : columns){
                ColumnMeta columnMeta = column.getValue();
                ddlClause.append(columnMeta.getName().toUpperCase());

                String typeName = columnMeta.getTypeName();
                if(!"sqlserver".equalsIgnoreCase(typeName)){
                    typeName = MSSQLType.from(columnMeta.getTypeName().toUpperCase(),sourceDBType);
                }
                if(Strings.isNullOrEmpty(typeName) || "".equals(typeName)){
                  logger.warn("None type mapping for "+columnMeta.getName()+"[ "+columnMeta.getTypeName()+" ]; original type will be applied.");
                  typeName = columnMeta.getTypeName();
                }
                ddlClause.append(" ").append(typeName.toUpperCase());
                //Some types may need limit Length Property
                switch (typeName.toUpperCase()){
                    case "NVARCHAR":
                    case "NTEXT":
                        ddlClause.append("(").append(columnMeta.getLength()).append(")");
                        break;
                }
                if(!columnMeta.getNullable()){
                    ddlClause.append(" ").append("NOT NULL").append(",");
                }
            }
            //delete the last comma
            ddlClause.setLength(ddlClause.length()-1);
            if(sourceTable.getPKS()!=null && sourceTable.getPKS().size()>0){
                ddlClause.append(",")
                        .append("CONSTRAINT ").append(replaceTableName.toUpperCase()+"_PKS").append(" ")
                        .append("PRIMARY KEY").append(" (");
                for (String pk: sourceTable.getPKS()){
                    if(!Strings.isNullOrEmpty(pk)){
                        ddlClause.append(pk.toUpperCase()).append(",");
                    }
                }
                ddlClause.setLength(ddlClause.length()-1);
                ddlClause.append(" )");
            }
            ddlClause.append(" )");
            return ddlClause.toString();
        }else{
            return null;
        }
    }

    /**
     * Generate sql cluase for deleting the table's datas
     * @param tableName
     * @return
     */
    public static String generateDML_DELETE(String tableName){
        if(!Strings.isNullOrEmpty(tableName)){
            StringBuilder dmlClause = new StringBuilder("DELETE FROM ").append(tableName.toUpperCase())
                    .append(" WHERE 1=1");
            return dmlClause.toString();
        }
        return null;
    }

    /**
     * Generate sql clause for truncate the table
     * Notice : truncate statement perform that clear the table datas,.etc but only table structure is reserved.
     * Cautiously, execured with specified prevelidge users
     * @param tableName
     * @return
     */
    public static String generateTRUNCATE(String tableName){
        if(!Strings.isNullOrEmpty(tableName)){
            StringBuilder dmlClause = new StringBuilder("TRUNCATE TABLE").append(tableName.toUpperCase());
            return dmlClause.toString();
        }
        return null;
    }

    /**
     * Generate sql clause for drop the table
     * @param tableName
     * @return
     */
    public static String generateDROP(String tableName){
        if(!Strings.isNullOrEmpty(tableName)){
            StringBuilder dmlClause = new StringBuilder("DROP TABLE").append(tableName.toUpperCase());
            return dmlClause.toString();
        }
        return null;
    }
    /**
     * Generate uuid
     * @return
     */
    public static String generateId(){
        return UUID.randomUUID().toString();
    }
}
