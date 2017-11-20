package org.trump.vincent.defaults.mappings;

import javax.validation.constraints.NotNull;

/**
 * Created by Vincent on 2017/8/30 0030.
 */
public class MappingUtils {

    public static String typeFrom(@NotNull String fromType, String targetDriverType, String sourceDriverType){
        if("oracle".equalsIgnoreCase(targetDriverType)){
            return OracleType.from(fromType,sourceDriverType);
        }else if("mysql".equalsIgnoreCase(targetDriverType)){
            return MySQLType.from(fromType,sourceDriverType);
        }else if("sqlserver".equalsIgnoreCase(targetDriverType)){
            return MSSQLType.from(fromType,sourceDriverType);
        }else {
            throw new UnsupportedOperationException("Warn: Sorry, None Mappings' config for " + targetDriverType);
        }
    }
}
