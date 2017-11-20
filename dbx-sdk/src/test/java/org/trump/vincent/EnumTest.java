package org.trump.vincent;

import org.trump.vincent.defaults.JavaType;

/**
 * Created by Vincent on 2017/9/5 0005.
 */
public class EnumTest {


    public static void main(String[] args) {
        JavaType.FromSqlType[] types = JavaType.FromSqlType.values();
        for(JavaType.FromSqlType type :types){
            System.out.print("From "+type.getJavaType()+"\n");
        }
    }

    enum Type{
        INT("INTEGER","INT"),
        CHAR("CHAR","CHAR"),
        NCHAR("NCHAR","STRING")
        ;

        public String getFrom() {
            return from;
        }

        public void setFrom(String from) {
            this.from = from;
        }

        public String getTo() {
            return to;
        }

        public void setTo(String to) {
            this.to = to;
        }

        private String from;
        private String to;
        Type(String from ,String to){
            this.from = from;
            this.to = to;
        }
    }
}
