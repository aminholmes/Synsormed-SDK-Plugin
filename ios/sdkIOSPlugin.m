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
	mainUUID = [[NSUUID alloc] initWithUUIDString:UUIDString];
	NSLog(@"*** I created a NSUUID");
	
	[[CRCreativeSDK sharedInstance] startScan:2.0];
	
	/*
	CreativePeripheral *myPeripheral;
	NSLog(@"*** I created a blank myPeripheral");
	myPeripheral.myUUID = myUUID;
	NSLog(@"*** I assing the UUID");


	[[CRCreativeSDK sharedInstance] connectDevice:myPeripheral];
	 */
}

- (void) CMI_POD1W_Disconnect:(CDVInvokedUrlCommand *) command
{
	[[CRCreativeSDK sharedInstance] disconnectDevice:mainPeripheral];
	CMI_POD1W_Disconnect_Callback = command.callbackId;
	[[self commandDelegate]sendPluginResult:[CDVPluginResult resultWithStatus:CDVCommandStatus_OK messageAsString:@"Disconnected OK"]
								 callbackId:CMI_POD1W_Disconnect_Callback];

}

- (void) CMI_POD1W_Subscribe:(CDVInvokedUrlCommand *) command
{

	CMI_POD1W_Subscribe_Callback = command.callbackId;
	if(CMI_POD1W_Connected){
		[[CRSpo2 sharedInstance] SetParamAction:TRUE port:mainPeripheral];
		
		[[CRSpo2 sharedInstance] SetWaveAction:TRUE port:mainPeripheral];
	}else{
		[[self commandDelegate]sendPluginResult:[CDVPluginResult resultWithStatus:CDVCommandStatus_ERROR messageAsString:@"Tried to subscribe without being connected"] callbackId:CMI_POD1W_Subscribe_Callback];
	}

}

- (void) CMI_POD1W_Unsubscribe:(CDVInvokedUrlCommand *) command
{

	CMI_POD1W_Unsubscribe_Callback = command.callbackId;
	[[CRSpo2 sharedInstance] SetParamAction:FALSE port:mainPeripheral];
	
	[[CRSpo2 sharedInstance] SetWaveAction:FALSE port:mainPeripheral];
	
	[[CRCreativeSDK sharedInstance] disconnectDevice:mainPeripheral];
	
	CDVPluginResult* pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK messageAsString:@"Disconnected"];
	[self.commandDelegate sendPluginResult:pluginResult callbackId:CMI_POD1W_Unsubscribe_Callback];

}


#pragma mark - Delegate Methods
-(void)OnSearchCompleted:(CRCreativeSDK *)bleSerialComManager{
	NSLog(@"scan complete");
	foundDevices = [[CRCreativeSDK sharedInstance] GetDeviceList];
	NSLog(@"foundDevices = %@",foundDevices);
	for (CreativePeripheral *device in foundDevices) {
		NSLog(@"The UUID is: %@", [device.myUUID UUIDString]);
		if ([device.myUUID isEqual:mainUUID]) {
			NSLog(@"Found a UUID that matches so going to try to connect");
			mainPeripheral = device;
			[[CRCreativeSDK sharedInstance] connectDevice:device];
			break;
		}
		else
		{
			NSLog(@"Did not find a UUID that matches");
		}
		
	}
	
}



-(void)crManager:(CRCreativeSDK *)crManager OnConnected:(CreativePeripheral *)peripheral withResult:(resultCodeType)result CurrentCharacteristic:(CBCharacteristic *)theCurrentCharacteristic{
	NSLog(@"Inside the native OnConnected function");
    if (result == RESULT_SUCCESS) {
		CMI_POD1W_Connected = true;
        NSLog(@"*** connection success");
		[[self commandDelegate]sendPluginResult:[CDVPluginResult resultWithStatus:CDVCommandStatus_OK messageAsString:@"OK"]
									 callbackId:CMI_POD1W_Connect_Callback];
    }
    
}

-(void)crManager:(CRCreativeSDK *)crManager OnConnectFail:(CBPeripheral *)port{
    NSLog(@"*** Connection failed");
	[[self commandDelegate]sendPluginResult:[CDVPluginResult resultWithStatus:CDVCommandStatus_ERROR messageAsString:@"Failed to connect"]
								 callbackId:CMI_POD1W_Connect_Callback];
        
}

-(void)crSpo2:(CRSpo2 *)crSpo2 OnGetSpo2Param:(BOOL)bProbeOff spo2Value:(int)nSpO2 prValue:(int)nPR piValue:(int)nPi mMode:(int)nMode batPower:(int)nPower spo2Status:(int)nStatus gradePower:(int)nGradePower
{
	NSString *returnString = [NSString stringWithFormat:@"{\"SpO2\":%d,\"pulse\":%d}",nSpO2,nPR];
	NSLog(@"The returnString is: %@",returnString);
	
	CDVPluginResult* pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK messageAsString:returnString];
	[pluginResult setKeepCallback:[NSNumber numberWithBool:YES]];
	[self.commandDelegate sendPluginResult:pluginResult callbackId:CMI_POD1W_Subscribe_Callback];
	
	/*spo2Num.text = [NSString stringWithFormat:@"%d",nSpO2];
	piNum.text = [NSString stringWithFormat:@"%d",nPi];
	pulseNum.text = [NSString stringWithFormat:@"%d",nPR];*/
	
}


@end
