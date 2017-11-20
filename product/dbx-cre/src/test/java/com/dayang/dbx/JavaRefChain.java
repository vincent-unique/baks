package com.dayang.dbx;

import java.lang.ref.*;

/**
 * Created by Vincent on 2017/9/6 0006.
 */
public class JavaRefChain {
    public static void main(String...args){
        ReferenceChain.main("Hello World");
    }
}

class ReferenceChain{
    public static void main(String... args) {
        /**
         * Strong Reference
         * GC never destroy it
         */
        String strongString = "Strong Reference";

        /**
         * Weak Reference
         */
        WeakReference<String> weakString = new WeakReference<String>("Weak Reference");
        System.out.print(weakString.get());

        /**
         * Soft Reference
         */
        SoftReference<String> softString = new SoftReference<String>("Soft Reference");

        /**
         * Phantom Reference
         */
        ReferenceQueue<String> referenceQueue = new ReferenceQueue<>();
        PhantomReference<String> phantomString = new PhantomReference<String>("Phantom Reference",referenceQueue);

//        if(args!=null&&args.length>0){
//            phantomString = new PhantomReference<>(args[0],referenceQueue);
//        }
        System.out.print(phantomString.get());
    }
}