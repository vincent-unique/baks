package org.trump.vincent;

import org.trump.vincent.model.ConnectionInfo;
import org.trump.vincent.model.DataBase;
import org.trump.vincent.model.TableMeta;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by Vincent on 2017/8/29 0029.
 */
public class TableMetaTest {
    private static DataBase dataBase;

    public static void initialize(){
        ConnectionInfo connectionInfo = new ConnectionInfo();
      /*  connectionInfo.setDriverType("sqlserver")
                .setHost("192.168.8.229")
                .setInstanceName("dymamdb_gugong_0623")
                .setPort(1433)
                .setUserName("sa")
                .setPassword("sa")
                .setSchema("dbo");*/
        connectionInfo.setDriverType("oracle")
                .setHost("192.168.8.231")
                .setInstanceName("ORAMAM")
                .setPort(1521)
                .setUserName("cre_tw_0922")
                .setPassword("cre_tw_0922")
                .setSchema("cre_tw_0922".toUpperCase());
        connectionInfo.buildURL();
        dataBase = new DataBase(connectionInfo).buildSelf();
    }

    public static void showTableMeta(){
        Map<String,TableMeta> tables = dataBase.getAllTables();
        if(tables!=null && tables.size()>0){
            Set<String> keys = tables.keySet();
            for(String key : keys){
                List<String> pks =  tables.get(key).getPKS();
                System.out.print(key+"\t"+(pks!=null? pks.size():0)+"\n\n");
            }
        }
    }
    public static void main(String[] args) {
        initialize();
        showTableMeta();
    }
}
