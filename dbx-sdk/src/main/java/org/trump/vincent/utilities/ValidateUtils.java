package org.trump.vincent.utilities;

import com.google.common.base.Strings;

import java.util.List;

/**
 * Created by Vincent on 2017/8/24 0024.
 */
public class ValidateUtils {

    public static boolean isPrimaryKey(List<String> pkList,String columnName){
        if(pkList==null || pkList.size()<1){
            return false;
        }
        for(String pk:pkList){
            if(!Strings.isNullOrEmpty(pk) && pk.equalsIgnoreCase(columnName)){
                return true;
            }
        }
        return false;
    }

}
