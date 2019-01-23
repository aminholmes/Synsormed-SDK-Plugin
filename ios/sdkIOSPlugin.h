#import <Cordova/CDVPlugin.h>
#import "CreativePeripheral.h"
#import "CRCreativeSDK.h"
#import "CRCommon.h"
#import "CRSpo2.h"


@interface sdkIOSPlugin : CDVPlugin
{

	NSString *CMI_POD1W_Connect_Callback;
	NSString *CMI_POD1W_Subscribe_Callback;
	NSString *CMI_POD1W_Disconnect_Callback;
	NSString *CMI_POD1W_Unsubscribe_Callback;

	BOOL CMI_POD1W_Connected;
	
}


@end