/*
 Licensed to the Apache Software Foundation (ASF) under one
 or more contributor license agreements.  See the NOTICE file
 distributed with this work for additional information
 regarding copyright ownership.  The ASF licenses this file
 to you under the Apache License, Version 2.0 (the
 "License"); you may not use this file except in compliance
 with the License.  You may obtain a copy of the License at

 http://www.apache.org/licenses/LICENSE-2.0

 Unless required by applicable law or agreed to in writing,
 software distributed under the License is distributed on an
 "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 KIND, either express or implied.  See the License for the
 specific language governing permissions and limitations
 under the License.
 */

#import <Cordova/CDV.h>

#import <UIKit/UIKit.h>
#import <MessageUI/MessageUI.h>
#import <MessageUI/MFMailComposeViewController.h>

#import "AppDelegate.h"

@interface Emailer : CDVPlugin {

    
}

@end



@implementation Emailer


- (void)pluginInitialize
{
    
}

- (void)emailer:(CDVInvokedUrlCommand*)command
{
    /*id message = [command.arguments objectAtIndex:0];
    CDVPluginResult* pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK messageAsString:message];
    
    [self.commandDelegate sendPluginResult:pluginResult callbackId:command.callbackId];*/
    
    NSMutableDictionary* args = [command.arguments objectAtIndex:0];
    
    NSString* email = [args objectForKey:@"mail"];
    NSString* subject = [args objectForKey:@"subject"];
    NSString* text = [args objectForKey:@"text"];
    NSString* fileName = [args objectForKey:@"fileName"];
    
    [self sendEmailTo:email withSubject:subject withText:text withFile:fileName];
}

-(void)sendEmailTo:(NSString*)email withSubject:(NSString*)subject withText:(NSString*)text withFile:(NSString*)myFile {
    
    NSString *documentsDirectory = [NSSearchPathForDirectoriesInDomains(NSDocumentDirectory, NSUserDomainMask, YES) objectAtIndex:0];
    NSString *myLocalFile = [documentsDirectory stringByAppendingPathComponent:myFile];
    
    
    MFMailComposeViewController *mailController = [[MFMailComposeViewController alloc] init];
    
    mailController.mailComposeDelegate = self;
    [mailController setSubject:subject];
    [mailController setToRecipients:[NSArray arrayWithObject:email]];
    [mailController setMessageBody:text isHTML:NO];
    
    NSData *myData = [NSData dataWithContentsOfFile:myLocalFile];
    [mailController addAttachmentData:myData mimeType:@"application/octet-stream" fileName:myFile];
    
    
    AppDelegate* appDlg = (AppDelegate*)[[UIApplication sharedApplication] delegate];
    [appDlg.window.rootViewController presentModalViewController:mailController animated:YES];

}

- (void)mailComposeController:(MFMailComposeViewController*)controller didFinishWithResult:(MFMailComposeResult)result error:(NSError*)error
{
	// Notifies users about errors associated with the interface
	switch (result)
	{
		case MFMailComposeResultCancelled:
			NSLog(@"Result: canceled");
			break;
		case MFMailComposeResultSaved:
			NSLog(@"Result: saved");
			break;
		case MFMailComposeResultSent:
			NSLog(@"Result: sent");
			break;
		case MFMailComposeResultFailed:
			NSLog(@"Result: failed");
			break;
		default:
			NSLog(@"Result: not sent");
			break;
	}
    
    AppDelegate* appDlg = (AppDelegate*)[[UIApplication sharedApplication] delegate];
    [appDlg.window.rootViewController dismissModalViewControllerAnimated:YES];
	
}

- (void)echo:(CDVInvokedUrlCommand*)command
{
    id message = [command.arguments objectAtIndex:0];
    CDVPluginResult* pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK messageAsString:message];

    [self.commandDelegate sendPluginResult:pluginResult callbackId:command.callbackId];
}

- (void)echoAsyncHelper:(NSArray*)args
{
    [self.commandDelegate sendPluginResult:[args objectAtIndex:0] callbackId:[args objectAtIndex:1]];
}

- (void)echoAsync:(CDVInvokedUrlCommand*)command
{
    id message = [command.arguments objectAtIndex:0];
    CDVPluginResult* pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK messageAsString:message];

    [self performSelector:@selector(echoAsyncHelper:) withObject:[NSArray arrayWithObjects:pluginResult, command.callbackId, nil] afterDelay:0];
}

- (void)echoArrayBuffer:(CDVInvokedUrlCommand*)command
{
    id message = [command.arguments objectAtIndex:0];
    CDVPluginResult* pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK messageAsArrayBuffer:message];

    [self.commandDelegate sendPluginResult:pluginResult callbackId:command.callbackId];
}

- (void)echoArrayBufferAsync:(CDVInvokedUrlCommand*)command
{
    id message = [command.arguments objectAtIndex:0];
    CDVPluginResult* pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK messageAsArrayBuffer:message];

    [self performSelector:@selector(echoAsyncHelper:) withObject:[NSArray arrayWithObjects:pluginResult, command.callbackId, nil] afterDelay:0];
}

- (void)echoMultiPart:(CDVInvokedUrlCommand*)command
{
    CDVPluginResult* pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK messageAsMultipart:command.arguments];

    [self.commandDelegate sendPluginResult:pluginResult callbackId:command.callbackId];
}

@end
