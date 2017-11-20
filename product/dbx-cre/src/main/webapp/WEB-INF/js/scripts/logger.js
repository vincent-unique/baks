/**
 * Created by Vincent on 2017/8/31 0031.
 */

(function (context) {
    if(!context){
        throw "Not Found the web context.";
    }else {
        context.logger = {
            info: function (msg) {
                console.log(msg);
            },

            warn: function (msg) {
                console.warn(msg);
            },

            error: function (msg) {
                console.error(msg);
            }
        }
    }
})(window||self||this||global);
