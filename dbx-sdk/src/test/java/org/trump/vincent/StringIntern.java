package org.trump.vincent;

import java.io.PrintStream;
import java.util.ArrayList;

/**
 * Created by Vincent on 2017/10/9 0009.
 */
public class StringIntern {
    private static PrintStream out = System.out;

    private void classLiteral(){
        Class clazz = this.getClass();
        Class clazzz = StringIntern.super.getClass();
        out.print(clazzz.getName());
        StringIntern.out.print(ArrayList.class.getSuperclass());
    }

    public static void main(String[] args) {
        String str = "s"+"t"+"r"+"ing"+"";
        String st = "str";
        String ing = "ing";
        String str1 = "string";
        out.print((str == str1)+"\n");
        out.print((str == (st+ing).intern())+"\n");
        String str2 = "string".intern();
        out.print((str == str2)+"\n");

      /*  "string" == new String("string"); // false
        "string" == new String("string").intern(); // true*/

        out.print((str == new String("string").intern())+"\n");
    }
}
