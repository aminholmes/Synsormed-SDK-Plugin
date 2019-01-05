var exec = require("cordova/exec");

var sdkPlugin = {};

sdkPlugin.init = function()
{
	console.log("*** I am going to let init");
	exec(null,null,"sdkPlugin", "init", []);
}


sdkPlugin.CMI_POD1W_Connect = function(success, fail, address)
{
	console.log("*** I am about to connect to CMI_POD1W_Connect in native");
	exec(success,fail,"sdkPlugin","CMI_POD1W_Connect",[address]);
}

sdkPlugin.CMI_POD1W_Disconnect = function(success, fail)
{
	console.log("*** I am about to disconnect from CMI_POD1W in native");
	exec(success,fail,"sdkPlugin","CMI_POD1W_Disconnect",[]);
}

sdkPlugin.CMI_POD1W_Subscribe = function(success, fail)
{
	console.log("*** I am about to subscribe to CMI_POD1W_Subscribe in native");
	exec(success,fail,"sdkPlugin","CMI_POD1W_Subscribe",[]);
}

sdkPlugin.CMI_POD1W_Unsubscribe = function(success, fail)
{
	console.log("*** I am about to unsubscribe from CMI_POD1W in native");
	exec(success,fail,"sdkPlugin","CMI_POD1W_Unsubscribe",[]);
}


module.exports = sdkPlugin;

