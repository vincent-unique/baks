package com.dayang.dbx.service;

import com.dayang.dbx.config.TypesConfig;
import com.google.common.base.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.trump.vincent.core.Generator;
import org.trump.vincent.defaults.mappings.MappingUtils;
import org.trump.vincent.model.ColumnMeta;
import org.trump.vincent.model.TableMeta;

import javax.validation.constraints.NotNull;
import java.util.Map;
import java.util.Set;

/**
 * Created by Vincent on 2017/8/31 0031.
 */

/**
 * Default generate functions have be provided in DBX-sdk [ Generator ]
 * Nevertheless, you may has special type mappings ,etc.
 * SO to build no-default generator by yourselves .
 * AND , static method is unavailable to Override;
 * you must to overwrite the same name static Method to sunk the previous.
 */
public class SQLGenerator extends Generator {

    private static Logger logger = LoggerFactory.getLogger(SQLGenerator.class);

    public static String generateDDL(@NotNull TableMeta sourceTable , String targetTableName, String fromDriver , String targetDriverType){
        if(Strings.isNullOrEmpty(targetTableName)){
            targetTableName = sourceTable.getName();
        }
        Set<Map.Entry<String,ColumnMeta>> columns =  sourceTable.getColumns()!=null? sourceTable.getColumns().entrySet():null;
        if(columns!=null) {
            StringBuilder ddlClause = new StringBuilder("CREATE TABLE ").append(targetTableName.toUpperCase()).append("( ");
            for (Map.Entry<String,ColumnMeta> column : columns){
                ColumnMeta columnMeta = column.getValue();
                if(columnMeta!=null && !Strings.isNullOrEmpty(columnMeta.getName())) {
                    ddlClause.append(columnMeta.getName().toUpperCase());
                    String typeName = null;
                    if (fromDriver.equalsIgnoreCase(targetDriverType)) {
                        typeName = columnMeta.getTypeName();
                    } else {
                        /**
                         * Mapping type from type-settings
                         * OR conditional type mappings
                         */

                        // special type mapping settings
                        typeName = TypesConfig.fromConfigs(columnMeta.getTypeName().toUpperCase(), fromDriver, targetDriverType);

                        //from default type mappings
                        if (Strings.isNullOrEmpty(typeName) || "".equals(typeName)) {
                            typeName = MappingUtils.typeFrom(columnMeta.getTypeName(), targetDriverType, fromDriver);
                        }
                        if (Strings.isNullOrEmpty(typeName) || "".equals(typeName)) {
                            logger.warn("None type mapping for " + columnMeta.getName() + "[ " + columnMeta.getTypeName() + " ]; original type will be applied.");
                            typeName = columnMeta.getTypeName();
                        }
                    }
                    ddlClause.append(" ").append(typeName.toUpperCase());
                    //Some types may need limit Length Property
                    switch (typeName.toUpperCase()){
                        case "NVARCHAR2":
                        case "NVARCHAR":
                        case "VARCHAR2":
                        case "VARCHAR":
                        case "NTEXT":
//                        case "TIMESTAMP":
                            ddlClause.append("(").append(columnMeta.getLength()).append(")");
                        break;
                    }
                    if (!columnMeta.getNullable()) {
                        ddlClause.append(" ").append("NOT NULL");
                    }
                    ddlClause.append(",");
                }
            }
            //delete the last comma
            ddlClause.setLength(ddlClause.length()-1);
            if(sourceTable.getPKS()!=null && sourceTable.getPKS().size()>0){
                ddlClause.append(",")
                        .append("CONSTRAINT ").append(targetTableName.toUpperCase()+"_PKS").append(" ")
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



}
