package com.dayang.dbx.model;

import org.trump.vincent.model.DataBase;

/**
 * Created by Vincent on 2017/8/20 0020.
 */
public class ConnectionManager {

    /**
     * Initialize Connection Context ,does do it completely
     * @param source
     * @param target
     */

    public static DataBase source;

    public static DataBase target;

    /**
     * maintain the state for these connections
     * TRUE : connections are usable
     * FALSE: connections are released
     */
    public static boolean online = false;
}
