#import "sdkIOSPlugin.h"

@implementation sdkIOSPlugin{
	
}

- (void) init:(CDVInvokedUrlCommand *) command
{
	NSLog(@"I am going to initQB");
	[[self commandDelegate]sendPluginResult:[CDVPluginResult resultWithStatus:CDVCommandStatus_OK messageAsString:@"OK"]
		callbackId:command.callbackId];
}

- (void) CMI_POD1W_Connect:(CDVInvokedUrlCommand *) command
{
	CMI_POD1W_Connect_Callback = command.callbackId;
}

- (void) CMI_POD1W_Disconnect:(CDVInvokedUrlCommand *) command
{

	CMI_POD1W_Disconnect_Callback = command.callbackId;

}

- (void) CMI_POD1W_Subscribe:(CDVInvokedUrlCommand *) command
{

	CMI_POD1W_Subscribe_Callback = command.callbackId;

}

- (void) CMI_POD1W_Unsubscribe:(CDVInvokedUrlCommand *) command
{

	CMI_POD1W_Unsubscribe_Callback = command.callbackId;

}


#pragma mark - Delegate Methods




@end
