package org.trump.vincent.model;

/**
 * Created by vincent on 2017/8/21 0021.
 */
public class ColumnMeta {

    public ColumnMeta(String name, String typeName, int type, Boolean nullable, int length, int originalPosition){
        this.name = name;
        this.typeName = typeName;
        this.type = type;
        this.nullable = nullable;
        this.length = length;
        this.columnIndex = originalPosition;
    }
    public ColumnMeta(){

    }

    private Boolean nullable;
    private Boolean isPrimaryKey;
    private String name;

    //original position ,cautiously to apply
    private Integer columnIndex;

    //different database has different DATA_TYPE, must build mappings based on TYPE_NAME
    private String typeName;
    private int type;
    private Integer length;

    private Object defaultValue;



    public Boolean getNullable() {
        return nullable;
    }

    public ColumnMeta setNullable(Boolean nullable) {
        this.nullable = nullable;
        return this;
    }

    public Boolean getPrimaryKey() {
        return isPrimaryKey;
    }

    public ColumnMeta setPrimaryKey(Boolean primaryKey) {
        isPrimaryKey = primaryKey;
        return this;
    }

    public String getName() {
        return name;
    }

    public ColumnMeta setName(String name) {
        this.name = name;
        return this;
    }

    public Integer getColumnIndex() {
        return columnIndex;
    }

    public ColumnMeta setColumnIndex(Integer columnIndex) {
        this.columnIndex = columnIndex;
        return this;
    }

    public String getTypeName() {
        return typeName;
    }

    public ColumnMeta setTypeName(String typeName) {
        this.typeName = typeName;
        return this;
    }

    public int getType() {
        return type;
    }

    public ColumnMeta setType(int type) {
        this.type = type;
        return this;
    }

    public Integer getLength() {
        return length;
    }

    public ColumnMeta setLength(Integer length) {
        this.length = length;
        return this;
    }

    public Object getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(Object defaultValue) {
        this.defaultValue = defaultValue;
    }


}
