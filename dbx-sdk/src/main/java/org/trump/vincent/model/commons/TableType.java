package org.trump.vincent.model.commons;

/**
 * Created by Administrator on 2017/8/24 0024.
 */
public enum TableType {
    TABLE("TABLE"),
    VIEW("VIEW"),
    SYNONYSM("SYNONYM"),
    SYSTEMTABLE("SYSTEM TABLE"),
    GLOBAL_TEMPORARY("GLOBAL TEMPORARY"),
    LOCAL_TEMPORARY("LOCAL_TEMPORARY"),
    ALIAS("ALIAS")
    ;

    public String getTypeName() {
        return typeName;
    }

    private String typeName;
    TableType(String name){
        this.typeName = name;
    }
}
