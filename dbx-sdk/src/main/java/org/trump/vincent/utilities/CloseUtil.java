package org.trump.vincent.utilities;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;

/**
 * Created by Vincent on 2017/8/24 0024.
 */
public class CloseUtil {
    private static Logger logger = LoggerFactory.getLogger(CloseUtil.class);
    /**
     * cautious to use
     * @param connection
     * @return
     */
    public static boolean close(Connection connection){
        if(connection==null){
            logger.debug("Warnning: the connection expecting to close is Null.");
            return false;
        }
        try{
//            if(!connection.isClosed()) {
                connection.close();
//            }
        }catch (final SQLException e){
            logger.error("Exception occurs in Closing the DB connection.",e);
            return false;
        }
        return true;
    }

    /**
     * Close the ResultSet
     * @param resultSet
     * @return
     */
    public static boolean close(ResultSet resultSet){
        if(resultSet==null){
            logger.debug("Warnning: the resultSet expecting to close is Null.");
            return false;
        }
        try{
//            if(!resultSet.isClosed()){
                resultSet.close();
//            }
        }catch (final SQLException e){
            logger.error("Exception occurs in closing the ResultSet.",e);
            return false;
        }
        return true;
    }

    /**
     * Close the Statement
     * @param statement
     * @return
     */
    public static boolean close(Statement statement){
        if(statement==null){
            logger.debug("Warnning: the statement expecting to close is Null.");
            return false;
        }
        try{
//            if(!statement.isClosed()) {
                statement.close();
//            }
        }catch (final SQLException e){
            logger.error("Exception occurs in closing Statement");
            return false;
        }
        return true;
    }
    public static boolean close(PreparedStatement statement){
//        return close(statement);

        if(statement==null){
            logger.debug("Warnning: the PreparedStatement expecting to close is Null.");
            return false;
        }
        try {
            if(!statement.isClosed()) {
                statement.close();
            }
        }catch (final SQLException e){
            logger.error("Exception occurs in closing the PrepareStatement.");
            return false;
        }
        return true;
    }
}
