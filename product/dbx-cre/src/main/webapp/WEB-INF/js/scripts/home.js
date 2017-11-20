/**
 * Created by Vincent on 2017/8/30 0030.
 */

$(function () {
    // connect to source db and target db
    $('#dbx-connect').click(function () {
        var connectionData = {
            srcInfo: {},
            tgtInfo: {}
        };

        $.each($('#source-conn-info').serializeArray(), function () {
            connectionData.srcInfo[this.name] = $.trim(this.value);
        });

        $.each($('#target-conn-info').serializeArray(), function () {
            connectionData.tgtInfo[this.name] = $.trim(this.value);
        });

        logger.info("Being connecting for :"+connectionData);

        if (connectionData.srcInfo['dbName'] && connectionData.srcInfo['host'] &&
            connectionData.srcInfo['username'] && connectionData.srcInfo['password'] &&
            connectionData.tgtInfo['dbName'] && connectionData.tgtInfo['host'] &&
            connectionData.tgtInfo['username'] && connectionData.tgtInfo['password'] ) {

            // Remove white spaces in product name
            connectionData.srcInfo['productName'] =  connectionData.srcInfo['productName'].replace(/\s+/g, '');
            connectionData.tgtInfo['productName'] =  connectionData.tgtInfo['productName'].replace(/\s+/g, '');

            connectionData = {connectionInfo: JSON.stringify(connectionData)};
            $.ajax({
                url: 'connection',
                type: 'POST',
                data:connectionData,

                success: function(data, status) {
                    if(data.response && data.response=='success') {
                        $('#success-hint').css('display', 'block');
                        window.location.href = "tables";
                    }else {
                        alert("建立数据库连接失败!");
                    }
                },

                error: function(data, status) {
                    alert('数据库连接失败!!! 请填写正确的数据库连接信息！');
                    // window.location.href = "home";
                }

            });
        } else {
            alert('星号项不能为空！');
        }

    });

});