package com.dayang.dbx.process.customize;

import com.dayang.dbx.process.Migration;
import com.google.common.base.Strings;
import org.trump.vincent.model.TableMeta;

import java.util.Map;

/**
 * Created by Vincent on 2017/10/13 0013.
 */
public class CustomizeMigration extends Migration {

    /**
     * cutomize simple migrate approach for cre data base
     * @param nameRegex
     * @return
     */
    public static int creMigrate(String nameRegex){
        if(source==null|| target==null||
                source.getConnection()==null|| target.getConnection()==null){
            throw new NullPointerException("You must build base information for this Migration.");
        }
        if(source.getAllTables()!=null && source.getAllTables().size()>0){
            int counter = 0;
            for(Map.Entry<String,TableMeta> fromTable : source.getAllTables().entrySet()){
                if(fromTable!=null && !Strings.isNullOrEmpty(fromTable.getKey())){
                    if(nameRegex.contains("COB") && fromTable.getKey().matches(nameRegex)){
                        counter += migrateAfterDrop(fromTable.getKey());
                    }else if(fromTable.getKey().matches(nameRegex)){
                        counter += generalMigrate(fromTable.getKey());
                    }
                }
            }
            return counter;
        }
        return -1;
    }

}
