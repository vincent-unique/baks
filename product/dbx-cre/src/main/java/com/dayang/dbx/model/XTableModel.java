package com.dayang.dbx.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.trump.vincent.model.ColumnMeta;
import org.trump.vincent.model.TableMeta;
import com.dayang.dbx.config.ConfigurationJpaser;
import com.dayang.dbx.config.XConfiguration;
import com.google.common.base.Strings;

import java.util.*;

/**
 * Created by Vincent on 2017/8/28 0028.
 */

/**
 * Model for migration metadata from settings
 */
public class XTableModel {

    public static final String SETTING_TOTABLE = "toTable";
    public static final String SETTING_COLUMNS = "columns";
    public static final String SETTING_ADD = "add";
    public static final String SETTING_DIFFERENT = "different";
    public static final String SETTING_AFTER_SQL = "aftersql";

    /**
     * the table name represent from table 's name
     */
    private String tableName;
    private TableMeta targetTable;
    private TableMeta sourceTable;
    private Map migrateConfig;

    /**
     * Extra data for required columns ,it had no in the source table
     */
    private List<ColumnMeta> addColumns;
    /**
     * different mappings
     * AND , columns for the same column name
     */
    private Map<ColumnMeta,ColumnMeta> target2sourceColumns ;

    /**
     * the standard sql statements which have to be executed after data migration.
     */
    private List<String> afterExection;


    private Logger logger = LoggerFactory.getLogger(getClass());

    public XTableModel(String tableName){
        this.tableName = tableName;
        this.sourceTable = ConnectionManager.source.getAllTables().get(tableName.toUpperCase());
        Map config = ConfigurationJpaser.fromConfig(XConfiguration.currentConfigs,tableName);
        this.migrateConfig = config;
        if(config==null){
            targetTable = ConnectionManager.target.getAllTables().get(tableName.toUpperCase());
        }else {
            String toTable = ConfigurationJpaser.fromConfigs(config, XTableModel.SETTING_TOTABLE);
            if (Strings.isNullOrEmpty(toTable) || "".equals(toTable) || "null".equals(toTable)) {
                toTable = tableName;
            }
            this.targetTable = ConnectionManager.target.getAllTables().get(toTable.toUpperCase());
        }
        this.buildSelf();
    }

    public XTableModel buildSelf(){
        if(Strings.isNullOrEmpty(this.tableName)||
                this.targetTable==null|| this.sourceTable==null){
            throw new NullPointerException("Must construct base information for this model.");
        }
        this.buildColumns();
        this.buildAfterExecutions();
        return this;
    }

    public XTableModel buildColumns(){
        Map config = this.getMigrateConfig();
        if(config==null){
            this.buildEntireMapping(null);
        }
        Map columns = ConfigurationJpaser.fromConfigs(config,XTableModel.SETTING_COLUMNS);
        if(columns!=null&& columns.size()>0){
            List adds = ConfigurationJpaser.fromConfigs(columns,XTableModel.SETTING_ADD);
            Map<String,String> differents = ConfigurationJpaser.fromConfigs(columns,XTableModel.SETTING_DIFFERENT);
            if(adds!=null && adds.size()>0){
                this.buildAddColumns(adds);
            }
           this.buildEntireMapping(differents);
        }
        return this;
    }

    public XTableModel buildAfterExecutions(){
        Map config = this.getMigrateConfig();
        if(config==null){
            return this;
        }
        List<String> afterSql = ConfigurationJpaser.fromConfigs(config,XTableModel.SETTING_AFTER_SQL);
        this.afterExection = afterSql;
        return this;
    }


    public List<ColumnMeta> buildAddColumns(List addColumns){
        if(addColumns ==null || addColumns.size()<1){
            return null;
        }
        if(targetTable==null){
            throw new NullPointerException("Must construct base information for this model.");
        }
        if(this.addColumns == null){
            this.addColumns = new ArrayList<>();
        }
        for(Object object : addColumns){
            String name = (String)object;
            if(!Strings.isNullOrEmpty(name)){
                //Caution, the table name or column name are UpperCase in general
                ColumnMeta columnMeta = this.targetTable.getColumns().get(name.toUpperCase());
                if(columnMeta !=null){
                    this.addColumns.add(columnMeta);
                }
            }
        }
        return this.addColumns;
    }

    public Map<ColumnMeta,ColumnMeta> buildEntireMapping(Map<String,String> differents){
        if(targetTable==null || sourceTable==null){
            throw new NullPointerException("Must construct base information for this model.");
        }
        if(this.target2sourceColumns ==null){
            this.target2sourceColumns = new HashMap<>();
        }
        if(differents==null || differents.size()<1){
            Set<Map.Entry<String,ColumnMeta>> entries = sourceTable.getColumns().entrySet();
            for (Map.Entry<String,ColumnMeta> entry: entries){
                String columnName = entry.getKey();
                if(!Strings.isNullOrEmpty(columnName)){
                    if (this.isAddColumn(columnName.toUpperCase())){
                        continue;
                    }
                    if(this.targetTable.getColumns().keySet().contains(columnName.toUpperCase())){
                        this.target2sourceColumns.put(this.targetTable.getColumns().get(columnName.toUpperCase()),entry.getValue());
                    }
                }
            }
        }else {
            // be prior to build different columns
            Set<Map.Entry<String,String>> entries = differents.entrySet();
            for(Map.Entry<String,String> entry : entries){
                if(!Strings.isNullOrEmpty(entry.getKey()) && !Strings.isNullOrEmpty(entry.getValue())) {
                    if(isAddColumn(entry.getValue())){
                        throw new RuntimeException("You have different configuration for the target column[ "+entry.getValue()+" ]");
                    }
                    if (this.sourceTable.getColumns().keySet().contains(entry.getKey().toUpperCase())
                            && this.targetTable.getColumns().keySet().contains(entry.getValue().toUpperCase())) {
                        this.target2sourceColumns.put(this.targetTable.getColumns().get(entry.getValue().toUpperCase()),
                                this.sourceTable.getColumns().get(entry.getKey().toUpperCase()));
                    }
                }
            }

            /**
             * build general column mappings
             * */
            Set<Map.Entry<String,ColumnMeta>> sourceColumns = sourceTable.getColumns().entrySet();
            for (Map.Entry<String,ColumnMeta> entry: sourceColumns){
                String columnName = entry.getKey();
                if(!Strings.isNullOrEmpty(columnName)){
                    if (this.isAddColumn(columnName.toUpperCase()) || isDifferent(differents,columnName.toUpperCase()) ){
                        continue;
                    }
                    if(this.targetTable.getColumns().keySet().contains(columnName.toUpperCase())){
                        this.target2sourceColumns.put(this.targetTable.getColumns().get(columnName.toUpperCase()),entry.getValue());
                    }
                }
            }
        }
        return this.target2sourceColumns;
    }

    public boolean isAddColumn(String name){
        if(this.addColumns==null|| this.addColumns.size()<1){
            return false;
        }
        for(ColumnMeta columnMeta : this.addColumns){
            if(columnMeta.getName().equalsIgnoreCase(name)){
                return true;
            }
        }
        return false;
    }

    public boolean isDifferent(Map<String,String> differents ,String name){
        if(differents==null || differents.size()<1){
            return false;
        }
        if(!Strings.isNullOrEmpty(name) &&differents.keySet().contains(name.toUpperCase())){
            return true;
        }
        return false;
    }

    public String getTableName() {
        return tableName;
    }

    public XTableModel setTableName(String tableName) {
        this.tableName = tableName;
        return this;
    }

    public TableMeta getTargetTable() {
        return targetTable;
    }

    public XTableModel setTargetTable(TableMeta targetTable) {
        this.targetTable = targetTable;
        return this;
    }

    public TableMeta getSourceTable() {
        return sourceTable;
    }

    public XTableModel setSourceTable(TableMeta sourceTable) {
        this.sourceTable = sourceTable;
        return this;
    }

    public List<ColumnMeta> getAddColumns() {
        return addColumns;
    }

    public XTableModel setAddColumns(List<ColumnMeta> addColumns) {
        this.addColumns = addColumns;
        return this;
    }

    public Map<ColumnMeta, ColumnMeta> getTarget2sourceColumns() {
        return target2sourceColumns;
    }

    public XTableModel setTarget2sourceColumns(Map<ColumnMeta, ColumnMeta> target2sourceColumns) {
        this.target2sourceColumns = target2sourceColumns;
        return this;
    }

    public List<String> getAfterExection() {
        return afterExection;
    }

    public XTableModel setAfterExection(List<String> afterExection) {
        this.afterExection = afterExection;
        return this;
    }

    public Map getMigrateConfig() {
        return migrateConfig;
    }

    public XTableModel setMigrateConfig(Map migrateConfig) {
        this.migrateConfig = migrateConfig;
        return this;
    }

}

