package com.dayang.dbx.process;

import com.dayang.dbx.service.SQLGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.trump.vincent.core.Generator;
import org.trump.vincent.defaults.JavaType;
import org.trump.vincent.model.ColumnMeta;
import org.trump.vincent.model.DataBase;
import org.trump.vincent.model.TableMeta;
import com.dayang.dbx.config.ConfigurationJpaser;
import com.dayang.dbx.config.XConfiguration;
import com.dayang.dbx.model.ConnectionManager;
import com.dayang.dbx.model.XTableModel;
import org.trump.vincent.utilities.CloseUtil;
import org.trump.vincent.utilities.ExistenceVerify;
import org.trump.vincent.utilities.JavaSQLUtils;
import org.trump.vincent.utilities.JdbcUtils;
import com.google.common.base.Strings;

import java.sql.*;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by Vincent on 2017/8/28 0028.
 */

/**
 * The Model for Migration process
 * Notice: process action is followings:
 * CleanData,
 * AppendData,
 * Handle SQL after migration
 */
public class Migration {

    public static DataBase source ;
    public static DataBase target ;
    public static Map<String,TableMeta> sourceAllTables ;
    public static Map<String,TableMeta> targetAllTables ;

    private static Logger logger = LoggerFactory.getLogger(Migration.class);

    /**
     * Essentially，first to be executed
     */
    public static void initialize(){
        if(ConnectionManager.source==null || ConnectionManager.target==null){
            throw new NullPointerException("You must initialize for ConnectionManager.");
        }
        source = ConnectionManager.source;
        target = ConnectionManager.target;
        sourceAllTables = source.getAllTables();
        targetAllTables = target.getAllTables();
    }

    /**
     * Drop the existing table structure
     * And then , rebuild the table based on the source table
     * Finally , copy rows to the new table from source table
     * @param tableName
     * @return
     */
    public static int migrateAfterDrop(String tableName){
        if(Strings.isNullOrEmpty(tableName)){
            throw new NullPointerException("You can process the migration for null table name");
        }
        if(source==null|| target==null||
                source.getConnection()==null|| target.getConnection()==null){
            throw new NullPointerException("You must build base information for this Migration.");
        }
        if(!ExistenceVerify.hasTable(source.getConnection(),tableName.toUpperCase())){
            throw new NullPointerException("The table is none existence in source database.");
        }
        Map config = ConfigurationJpaser.fromConfig(XConfiguration.currentConfigs,tableName);
        String toTable = ConfigurationJpaser.fromConfigs(config,XTableModel.SETTING_TOTABLE);
        toTable = Strings.isNullOrEmpty(toTable)||"".equals(toTable)||"null".equals(toTable) ? tableName:toTable;

        if(ExistenceVerify.hasTable(target.getConnection(),toTable.toUpperCase())){
            dropTable(target.getConnection(),toTable.toUpperCase());
        }
        return generalMigrate(tableName);
    }

    /**
     * clean datas which is already existed ,then process migration;
     * Cautiously, you must confirm it is needed to clean all datas.
     * @param tableName
     * @return
     */
    public static int migrateAfterClean(String tableName){
        if(Strings.isNullOrEmpty(tableName)){
            throw new NullPointerException("You can process the migration for null table name");
        }
        if(source==null|| target==null||
                source.getConnection()==null|| target.getConnection()==null){
            throw new NullPointerException("You must build base information for this Migration.");
        }
        if(!ExistenceVerify.hasTable(source.getConnection(),tableName.toUpperCase())){
            throw new NullPointerException("The table is none existence in source database.");
        }

        Map config = ConfigurationJpaser.fromConfig(XConfiguration.currentConfigs,tableName);
        String toTable = ConfigurationJpaser.fromConfigs(config,XTableModel.SETTING_TOTABLE);
        toTable = Strings.isNullOrEmpty(toTable)||"".equals(toTable)||"null".equals(toTable) ? tableName:toTable;

        if(!ExistenceVerify.hasTable(target.getConnection(),toTable.toUpperCase())){
            logger.debug("Notice: Table[ "+toTable+" ] do not exist in target database, it will be created.");
            TableMeta sourceTable = source.getAllTables().get(tableName.toUpperCase());
            String ddlSQL = SQLGenerator.generateDDL(sourceTable,toTable,source.getDriver(),target.getDriver());
            if(!Strings.isNullOrEmpty(ddlSQL)){
                try (
                        PreparedStatement statement = target.getConnection().prepareStatement(ddlSQL) ){
                    statement.execute();
                    ConnectionManager.target.buildSelf();
                }catch (SQLException sqle){
                    logger.error("Exception occurs in creating Table[ "+tableName+" ]",sqle);
                }
            }
        }else {
            cleanDatasFromTable(target.getConnection(),toTable);
        }
        XTableModel xTableModel = new XTableModel(tableName.toUpperCase());
        PreparedStatement statement = null;
        int counts = 0;
        try {
            statement = buildInsertStatement(xTableModel);
            if(statement!=null) {
                counts = appendData(statement, xTableModel);
                statement.executeBatch();
                target.getConnection().commit();
            }
        }catch (SQLException sqle){
            logger.error("Exception occurs in migrating the table[ "+tableName+" ]" ,sqle);
        }finally {
            CloseUtil.close(statement);
        }
        afterExecute(xTableModel,target.getConnection());
        return counts;
    }

    /**
     * Universal Logic for general migrating tasks
     * @param tableName
     * @return
     */
    public static int generalMigrate(String tableName){
        if(Strings.isNullOrEmpty(tableName)){
            throw new NullPointerException("You can process the migration for null table name");
        }
        if(source==null|| target==null||
                source.getConnection()==null|| target.getConnection()==null){
            throw new NullPointerException("You must build base connection for this Migration.");
        }
        if(!ExistenceVerify.hasTable(source.getConnection(),tableName.toUpperCase())) {
            throw new NullPointerException("The table is none existence in source database.");
        }

        Map config = ConfigurationJpaser.fromConfig(XConfiguration.currentConfigs,tableName);
        String toTable = ConfigurationJpaser.fromConfigs(config,XTableModel.SETTING_TOTABLE);
        toTable = Strings.isNullOrEmpty(toTable)||"".equals(toTable)?tableName:toTable;

        if(!ExistenceVerify.hasTable(target.getConnection(),toTable.toUpperCase())){
            TableMeta sourceTable = source.getAllTables().get(tableName.toUpperCase());
            if(sourceTable==null){
                logger.error("The table[ "+tableName+" ] can not be found in source tables.");
                return -1;
            }
            String ddlSQL = SQLGenerator.generateDDL(sourceTable,toTable,source.getDriver(),target.getDriver());
            if(!Strings.isNullOrEmpty(ddlSQL)){
                try (
                    PreparedStatement statement = target.getConnection().prepareStatement(ddlSQL) ){
                    logger.info("Create Table clause : "+ddlSQL);
                    statement.execute();
                    ConnectionManager.target.buildSelf();
                }catch (SQLException sqle){
                    logger.error("Exception occurs in creating Table[ "+tableName+" ]",sqle);
                }
            }
        }
        XTableModel xTableModel = new XTableModel(tableName.toUpperCase());
        PreparedStatement statement = null;
        int counts = 0;
        try {
            statement = buildInsertStatement(xTableModel);
//            logger.info("Insert statement IsNull》 "+(statement==null));
            if(statement!=null) {
                counts = appendData(statement, xTableModel);
                statement.executeBatch();
                target.getConnection().commit();
            }
        }catch (SQLException sqle){
            logger.error("Exception occurs in migrating the table[ "+tableName+" ]" ,sqle);
        }finally {
            CloseUtil.close(statement);
        }
        afterExecute(xTableModel,target.getConnection());
        return counts;
    }

    /**
     * Build PrepareStatement of insert data from XTableModel
     * @param xTableModel
     * @return
     */
    private static PreparedStatement buildInsertStatement(XTableModel xTableModel){
        logger.info("Build Insert PrepareStatement for TABLE[ "+xTableModel.getTableName()+" ].");
        PreparedStatement statement = null;

        TableMeta sourceTable = xTableModel.getSourceTable();
        TableMeta targetTable = xTableModel.getTargetTable();
        if(sourceTable==null || targetTable==null){
            throw new NullPointerException("You must construct the entire XTableModel for "+xTableModel.getTableName()+" .");
        }

        /**
         * Being prior to process the add-columns
         */
        StringBuilder insetSQl = new StringBuilder("INSERT INTO "+targetTable.getName().toUpperCase()+" (");
        StringBuilder valueMarks = new StringBuilder();
        List<ColumnMeta> addColumns = xTableModel.getAddColumns();
        if(addColumns!=null && addColumns.size()>0){
            for(ColumnMeta columnMeta : addColumns){
              insetSQl.append(" ").append(columnMeta.getName().toUpperCase()).append(",");
              valueMarks.append(" ?,");
            }
        }
        /**
         *Afterwards, build other columns' mappings for insert sql
         */

        Map<ColumnMeta, ColumnMeta> target2sourceColumns = xTableModel.getTarget2sourceColumns();
        if (target2sourceColumns != null && target2sourceColumns.size() > 0) {
            Set<Map.Entry<ColumnMeta,ColumnMeta>> mappings = target2sourceColumns.entrySet();
            for(Map.Entry<ColumnMeta,ColumnMeta> entry: mappings){
                ColumnMeta targetColumn = entry.getKey();
                ColumnMeta sourceColumn = entry.getValue();
                if(targetColumn!=null && sourceColumn!=null){
                    insetSQl.append(" ").append(targetColumn.getName()).append(",");
                    valueMarks.append(" ?,");
                }
            }
        }

        if(valueMarks.length()>1) {
            insetSQl.setLength(insetSQl.length() - 1);
            valueMarks.setLength(valueMarks.length() - 1);
            insetSQl = insetSQl.append(" ) VALUES (").append(valueMarks).append(" )");
            /**
             * build PrepareStatement instance
             */
            try {
                Connection targetConnection = target.getConnection();
                targetConnection.setAutoCommit(false);
                logger.info("TABLE[ "+xTableModel.getTableName()+" ] Insert Clause :\t"+insetSQl);
                statement = targetConnection.prepareStatement(insetSQl.toString());
            } catch (SQLException sqle) {
                logger.error("Exception occurs in building PrepareStatement .[TABLE: "+xTableModel.getTableName()+" ]" ,sqle);
            }
        }
        return statement;
    }

    /**
     * Fill data for the prepare statement
     * Notice, the process and sequence refer to the Method " buildInsertStatement "
     * @param preparedStatement
     * @param xTableModel
     * @return
     */
    private static int appendData(PreparedStatement preparedStatement,XTableModel xTableModel){
        logger.info("Append datas for the table [ "+xTableModel.getTableName()+" ].");
        TableMeta sourceTable = xTableModel.getSourceTable();
        TableMeta targetTable = xTableModel.getTargetTable();
        Connection sourceConnection = source.getConnection();
        if(sourceTable==null || targetTable==null){
            throw new NullPointerException("You must construct the entire XTableModel for "+xTableModel.getTableName()+" .");
        }
        if(sourceConnection==null){
            throw new NullPointerException("You have a null connection to source database.");
        }
        StringBuilder dqlSql = new StringBuilder("SELECT * FROM "+sourceTable.getName().toUpperCase()+" WHERE 1=1");
        /**
         * Being prior to process the add-columns
         */
        List<ColumnMeta> addColumns = xTableModel.getAddColumns();
        int counts2migrate = 0;
        ResultSet queryCursor = null;
        try {
            Statement query = sourceConnection.createStatement();
            queryCursor = query.executeQuery(dqlSql.toString());
            int batchNum = 0;
            while (queryCursor.next()) {
                /**
                 * Primary : index for parameter
                 */
                int index = 1;
                if (addColumns != null && addColumns.size() > 0) {
                    for (ColumnMeta columnMeta : addColumns) {
                        if (columnMeta.getPrimaryKey()) {
                            preparedStatement.setString(index++, Generator.generateId());
                        }else if(columnMeta.getDefaultValue()!=null){
                            JdbcUtils.setParameter(preparedStatement,index++,columnMeta.getDefaultValue());
                        }else if (!columnMeta.getNullable() || !typeNullable(JavaType.javaType(columnMeta.getTypeName()))) {
                            String javaType = JavaType.javaType(columnMeta.getTypeName());
                            JavaSQLUtils.setDefaultValue4NotNull(preparedStatement,index++,javaType);
//                            JdbcUtils.setDefaultValue4NotNull(preparedStatement, index++, columnMeta.getTypeName());
                        } else {
                            preparedStatement.setNull(index++, columnMeta.getType());
                        }
                    }
                }

                /**
                 *Afterwards, build other columns' mappings for insert sql
                 */
                Map<ColumnMeta, ColumnMeta> target2sourceColumns = xTableModel.getTarget2sourceColumns();
                if (target2sourceColumns != null && target2sourceColumns.size() > 0) {
                    Set<Map.Entry<ColumnMeta, ColumnMeta>> mappings = target2sourceColumns.entrySet();
                    for (Map.Entry<ColumnMeta, ColumnMeta> entry : mappings) {
                        ColumnMeta targetColumn = entry.getKey();
                        ColumnMeta sourceColumn = entry.getValue();
                        String lableName = sourceColumn.getName().toUpperCase();
                        String javaType = JavaType.javaType(targetColumn.getTypeName());
                        Object value = JavaSQLUtils.getValue(queryCursor,lableName,javaType);
                        if(value==null && (!targetColumn.getNullable()|| !typeNullable(javaType))){
                            if(targetColumn.getDefaultValue()!=null){
                                JdbcUtils.setParameter(preparedStatement,index++,targetColumn.getDefaultValue());
                            }else {
                                JavaSQLUtils.setDefaultValue4NotNull(preparedStatement, index++, javaType);
                            }
                        }else {
//                        logger.info("Source column ["+sourceColumn.getName()+" ] with index = " +index+" and sqltype= "+targetColumn.getTypeName()+ " and javatype = "+JavaType.javaType(targetColumn.getTypeName()));
                            JavaSQLUtils.setValue2Parameter(queryCursor, preparedStatement, lableName, index++, javaType);
//                        JdbcUtils.setValue2Parameter(queryCursor,preparedStatement,sourceColumn.getName().toUpperCase(),index++,targetColumn.getTypeName());
//                        JdbcUtils.setValue2Parameter(queryCursor,preparedStatement,sourceColumn.getName().toUpperCase(),index++,targetColumn.getType());
                        }
                    }
                }
                ++batchNum;
                ++counts2migrate;
                preparedStatement.addBatch();
//
//                if (batchNum>=1000){
//                    preparedStatement.executeBatch();
//                }
            }
        }catch (SQLException sqle){
            logger.error("Exception occurs in appending data for PrepareStatement .[TABLE: "+xTableModel.getTableName()+" ]" ,sqle);
        }finally {
            CloseUtil.close(queryCursor);
        }
        return counts2migrate;
    }

    public static boolean afterExecute(XTableModel xTableModel, Connection connection){
        if(connection==null || xTableModel==null){
            throw new NullPointerException("None entire execute parameter.");
        }
        List<String> executions = xTableModel.getAfterExection();
        if(executions != null && executions.size()>0) {
            try {
                connection.setAutoCommit(false);
                Statement statement = connection.createStatement();
                for(String sql: executions) {
                    statement.addBatch(sql);
                }
                statement.executeBatch();
                connection.commit();
                CloseUtil.close(statement);
                return true;
            }catch (SQLException sqle){
                logger.error("Exception occurs in executing batch aftersqls.");
            }
        }
        return false;
    }
    /**
     * Clean all datas from the table
     * @param connection
     * @param tableName
     * @return
     */
    public static boolean cleanDatasFromTable(Connection connection,String tableName){
        if(connection==null || Strings.isNullOrEmpty(tableName)){
            throw new NullPointerException("You must give entire parameters");
        }
        try(    Statement statement = connection.createStatement() ){
            statement.execute("DELETE FROM "+tableName.toUpperCase()+" WHERE 1=1");
            connection.commit();
            return true;
        }catch (SQLException sqle){
            logger.error("Exception occurs in deleting all datas from [ "+tableName+" ].");
            return false;
        }
    }

    public static boolean dropTable(Connection connection,String tableName){
        if(connection==null || Strings.isNullOrEmpty(tableName)){
            throw new NullPointerException("You must give entire parameters");
        }
        try(    Statement statement = connection.createStatement() ){
            statement.execute("DROP TABLE "+tableName.toUpperCase());
            connection.commit();
            return true;
        }catch (SQLException sqle){
            logger.error("Exception occurs in deleting all datas from [ "+tableName+" ].");
            return false;
        }
    }

    public static boolean typeNullable(String javaType) {
        if (Strings.isNullOrEmpty(javaType)) {
            logger.warn("You are validating for Null java type name.");
            return true;
        }
        if (javaType.equalsIgnoreCase(JavaType.Types.STRING.getTypeName())) {
            return true;
        }
        if (javaType.equalsIgnoreCase(JavaType.Types.BYTE.getTypeName())) {
            return false;
        }
        if (javaType.equalsIgnoreCase(JavaType.Types.INT.getTypeName())) {
            return false;
        }
        if (javaType.equalsIgnoreCase(JavaType.Types.SHORT.getTypeName())) {
            return false;
        }
        if (javaType.equalsIgnoreCase(JavaType.Types.LONG.getTypeName())) {
            return false;
        }
        if (javaType.equalsIgnoreCase(JavaType.Types.FLOAT.getTypeName())) {
            return false;
        }
        if (javaType.equalsIgnoreCase(JavaType.Types.DOUBLE.getTypeName())) {
            return false;
        }
        if (javaType.equalsIgnoreCase(JavaType.Types.BIGDECIMAL.getTypeName())) {
            return false;
        }
        return true;
    }
}
