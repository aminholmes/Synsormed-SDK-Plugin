#import "sdkIOSPlugin.h"

@implementation sdkIOSPlugin{
	
}

- (void) init:(CDVInvokedUrlCommand *) command
{
	NSLog(@"I am going to init in sdkIOSPlugin");

	[CRCreativeSDK sharedInstance].delegate = self;
    [CRSpo2 sharedInstance].delegate = self;

	[[self commandDelegate]sendPluginResult:[CDVPluginResult resultWithStatus:CDVCommandStatus_OK messageAsString:@"OK"]
		callbackId:command.callbackId];
}

- (void) CMI_POD1W_Connect:(CDVInvokedUrlCommand *) command
{
	CMI_POD1W_Connect_Callback = command.callbackId;

	NSString* UUIDString = [command.arguments objectAtIndex:0];
	NSLog(@"The incoming UUID is: %@" , UUIDString);
	NSUUID *myUUID = [[NSUUID alloc] initWithUUIDString:UUIDString];
	NSLog(@"*** I created a NSUUID");
	CreativePeripheral *myPeripheral;
	NSLog(@"*** I created a blank myPeripheral");
	myPeripheral.myUUID = myUUID;
	NSLog(@"*** I assing the UUID");


	[[CRCreativeSDK sharedInstance] connectDevice:myPeripheral];
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

-(void)crManager:(CRCreativeSDK *)crManager OnConnected:(CreativePeripheral *)peripheral withResult:(resultCodeType)result CurrentCharacteristic:(CBCharacteristic *)theCurrentCharacteristic{
    if (result == RESULT_SUCCESS) {
        NSLog(@"*** connection success");
        /*
        [[CRSpo2 sharedInstance] QueryDeviceVer:currentPort];
        
        [[CRSpo2 sharedInstance] SetParamAction:TRUE port:currentPort];
        
        [[CRSpo2 sharedInstance] SetWaveAction:TRUE port:currentPort];
        */
        
        
    }
    
}

-(void)crManager:(CRCreativeSDK *)crManager OnConnectFail:(CBPeripheral *)port{
    NSLog(@"*** Connection failed");

        
}


@end
