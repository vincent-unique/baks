package com.dayang.dbx;

/**
 * Created by Vincent on 2017/8/31 0031.
 */
public class OverwriteTest {

    public static void main(String[] args) {
      /*  Child.handle();
        Child.init();*/
      testConvert();
    }

    static class Parent{
        public  static void handle(){
            init();
        }

        public static void init(){
            System.out.print("I am parent.");
        }
    }

    static class Child extends Parent{

        public static void init(){
            System.out.print("I am child.");
        }
    }

    public static void testConvert(){
        String str = "Byte[]";
        System.out.print(str.toUpperCase());
    }
}
