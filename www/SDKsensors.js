var exec = require("cordova/exec");

var sdkPlugin = {};


sdkPlugin.sayHello = function(success,fail)
{
	console.log("I am about to go to native");
	exec(success, fail, "sdkPlugin", "sayHello", []);

}

sdkPlugin.init = function()
{
	console.log("*** I am going to let init");
	exec(null,null,"sdkPlugin", "init", []);
}


module.exports = sdkPlugin;

