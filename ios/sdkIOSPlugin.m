#import "sdkIOSPlugin.h"

@implementation sdkIOSPlugin{
	
}

- (void) init:(CDInvokedUrlCommand *) command
{
	NSLog(@"I am going to initQB");
	[[self commandDelegate]sendPluginResult:[CDVPluginResult resultWithStatus:CDVCommandStatus_OK messageAsString:@"OK"]
		callbackId:command.callbackId];
}

- (void) CMI_POD1W_Connect:(CDInvokedUrlCommand *) command
{
	CMI_POD1W_Connect_Callback = command.callbackId;
}

- (void) CMI_POD1W_Disconnect:(CDInvokedUrlCommand *) command
{

	CMI_POD1W_Disconnect_Callback = command.callbackId;

}

- (void) CMI_POD1W_Subscribe:(CDInvokedUrlCommand *) command
{

	CMI_POD1W_Subscribe_Callback = command.callbackId;

}

- (void) CMI_POD1W_Unsubscribe:(CDInvokedUrlCommand *) command
{

	CMI_POD1W_Unsubscribe_Callback = command.callbackId;

}


#pragma mark - Delegate Methods




@end
