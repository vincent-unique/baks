package org.trump.vincent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.*;

/**
 * Created by Vincent on 2017/9/26 0026.
 */
public class TimeFunction {

    private static Logger logger = LoggerFactory.getLogger(TimeFunction.class);
    public static Timestamp convertDate2Timestamp(Date date){
        return new Timestamp(date.getTime());
    }

    public static Timestamp convertString2Timestamp(String dateStr , SimpleDateFormat dateFormat){
        try {
            Date date = (Date)dateFormat.parse(dateStr);
            return convertDate2Timestamp(date);
        }catch (ParseException pe){
            logger.error("Exception occurs in Paserring Date from String.",pe);
        }
        return null;
    }
    public static Date parseDate(String dateStr,String formatPattern){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(formatPattern);
        try {
            return simpleDateFormat.parse(dateStr);
        }catch (ParseException pe){
            logger.error("Exception occurs in Paserring Date from String.",pe);
        }
        return null;
    }

    public static String formatSolrDate(Date date){
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        return formatter.format(date);
    }

    public static Date formatDate(String dateStr, String pattern){
        try {
            SimpleDateFormat format = new SimpleDateFormat(pattern);
            return format.parse(dateStr);
        }catch (ParseException pe){
            logger.error("Exception occurs in parse date.",pe);
        }
        return null;
    }

    public static void main(String[] args) throws InterruptedException{
        String date = formatSolrDate(formatDate("2017-09-25","yyyy-MM-dd"));
        Thread thread = new Thread();
        thread.start();
        thread.interrupt();
        System.out.print(thread.getState());
        System.out.print(date);
        ExecutorService threadPool = Executors.newCachedThreadPool();
//        Executor threadpool = Executors.newSingleThreadExecutor();
        Semaphore semaphore = new Semaphore(8);
    }
}