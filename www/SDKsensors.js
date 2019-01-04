var exec = require("cordova/exec");

var sdkPlugin = {};


sdkPlugin.sayHello = function()
{
	console.log("I am about to go to native");
	exec(null, null, "sdkPlugin", "sayHello", []);

}



module.exports = sdkPlugin;

