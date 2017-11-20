/**
 * Created by Vincent on 2017/3/17 0017.
 */
PageUtil = function(){
    return {
        init: function () {
            PageUtil.readData2Init("resources/connections.json");
        },
        supportHomePage:function(data){
            if(data && data!= null){
                if(data.srcInfo && data.srcInfo!= null) {
                    var srcInfo = data.srcInfo;
                    $('#source-conn-info input').each(function () {
                        var field = $(this);
                        if ( srcInfo.host && field.prop("name") == "host")
                            field.attr("value",srcInfo.host);
                        if (srcInfo.dbName && field.prop("name")=="dbName")
                            field.attr("value",srcInfo.dbName);
                        if (srcInfo.port && field.prop("name")== "port")
                            field.attr("value",srcInfo.port);
                        if (srcInfo.schema && field.prop("name") === "schema")
                            field.attr("value",srcInfo.schema);
                        //if (srcInfo.mamVersion && field.prop("name") == "mamVersion")
                        //    field.attr("value",srcInfo.mamVersion);
                        if (srcInfo.username && field.prop("name") == "username")
                            field.attr("value",srcInfo.username);
                        if (srcInfo.password && field.prop("name") == "password")
                            field.attr("value",srcInfo.password);
                    });
                }
                logger.info("exception do not interrupt execution.");
                if(data.tgtInfo && data.tgtInfo != null) {
                    var tgtInfo = data.tgtInfo;
                    $("#target-conn-info input").each(function(){
                       var field = $(this);
                        //console.log($this.prop("name"));
                        if(tgtInfo.dbName && field.prop("name") === "dbName")
                            field.attr('value',tgtInfo.dbName);
                        if(tgtInfo.host && field.prop("name") === "host")
                            field.attr("value",tgtInfo.host);
                        if(tgtInfo.port && field.prop("name") === "port")
                            field.attr("value",tgtInfo.port);
                        if(tgtInfo.schema && field.prop("name") === "schema")
                            field.attr("value",tgtInfo.schema);
                        if(tgtInfo.username && field.prop("name") === "username")
                            field.attr("value",tgtInfo.username);
                        if(tgtInfo.password && field.prop("name") === "password")
                            field.attr("value",tgtInfo.password);
                    });
                }
            }
        },


        /**
         * save and load datas via h5 localstorage
         * Disadvantage: cleer the Cookie whin write time ,the storage would lost.
         */

        loadStorageData:function(){
            var jsonObject;
            if(typeof Storage != undefined){
                if(localStorage.connectionInfos){
                    jsonObject = localStorage.connectionInfos;
                }
            }else{
                console.warn("The browser do not supported html5 web storage.")
            }
            return jsonObject;
        },
        write2Storage:function(data){
            if(typeof Storage != undefined){
                localStorage.removeItem("connectionInfos");
                localStorage.setItem("connectionInfos",data);
                return true;
            }else{
                console.warn("The browser do not supported html5 web storage.")
                return false;
            }
        },

        /**
         * sava and load infos via File (*.json)
         */
        clearJsonFile:function(filePath){

        },
        write2JsonFile:function(data,filePath){
        },

        readData2Init:function(filePath){
            if(typeof filePath == "string") {
                $.getJSON(filePath,function(data){
                    //console.info(JSON.stringify(data));
                    PageUtil.supportHomePage(data);
                });
            }
        },
        empty:null,
    }
}();
$(PageUtil.init());