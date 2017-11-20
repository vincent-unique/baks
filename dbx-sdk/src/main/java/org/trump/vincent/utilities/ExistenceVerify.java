package org.trump.vincent.utilities;

import com.google.common.base.Strings;

import javax.validation.constraints.NotNull;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Created by Vincent on 2017/8/24 0024.
 */
public class ExistenceVerify {

    public static Boolean hasTable(@NotNull Connection connection, @NotNull String tableName){
        if(Strings.isNullOrEmpty(tableName)){
            return false;
        }
//        Statement statement = null;
       /* try {
            statement = connection.createStatement();
            statement.executeQuery("SELECT * FROM "+tableName +" WHERE 1=0");
            return true;
        }catch (SQLException sqle){
            return false;
        }finally {
            CloseUtil.close(statement);
        }*/

        /**
         * Since Java1.7 , it has new statement for resource manipulating
         * which is try-with-resource clause ,it is for Implimentation of the interface " AutoCloseable "
         */
        try(   Statement  statement = connection.createStatement() ){
           statement.executeQuery("SELECT * FROM "+tableName +" WHERE 1=0");
           return true;
       }catch (SQLException sqle){
            return false;
       }
    }

    public static Boolean hasColumn(@NotNull Connection connection,@NotNull String tableName,@NotNull String columnName){
        if (Strings.isNullOrEmpty(tableName) || Strings.isNullOrEmpty(columnName)){
            return false;
        }
      /*  Statement statement = null;
        try{
            statement = connection.createStatement();
            statement.executeQuery("SELECT "+columnName+" FROM "+tableName+" WHERE 1=0");
            statement.close();
            return true;
        }catch (SQLException sqle){
            return false;
        }finally {
            CloseUtil.close(statement);
        }*/
      try(  Statement statement = connection.createStatement() ){
          statement.executeQuery("SELECT "+columnName+" FROM "+tableName+" WHERE 1=0");
          return true;
      }catch (SQLException sqle){
          return false;
      }
    }

}
