var exec = require("cordova/exec");

var sdkPlugin = {};


sdkPlugin.sayHello = function()
{
	console.log("I am about to go to native");
	exec(null, null, "sdkPlugin", "sayHello", []);

}

sdkPlugin.init = function()
{
	console.log("*** I am going to let init");
	//exec(null,null,"sdkPlugin", "init", []);
}


module.exports = sdkPlugin;

