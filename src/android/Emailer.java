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

package com.ideateam.plugin;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.PluginResult;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.util.Base64;
import android.util.Log;

/**
* This class exposes methods in Cordova that can be called from JavaScript.
*/
public class Emailer extends CordovaPlugin {

	 private String TAG = "CordovaPlugin Emailer";
	 
	 private String url;
	 private String remoteVersion;
	 private String remoteChecksum;
	 private String zipChecksum;
	 private Activity activity;
	 private ProgressDialog mProgressDialog;
     private volatile boolean bulkEchoing;
     
     /**
     * Executes the request and returns PluginResult.
     *
     * @param action            The action to execute.
     * @param args              JSONArry of arguments for the plugin.
     * @param callbackContext   The callback context from which we were invoked.
     */
    @SuppressLint("NewApi") 
    public boolean execute(String action, final JSONArray args, final CallbackContext callbackContext) throws JSONException {
        if (action.equals("emailer")) {

        	Log.d(TAG, "..action == emailer" );
        	Log.d(TAG, args.getString(0));
        	
        	if(!isOnline()){
        		Log.d(TAG, ".. no internet connetion");
        		callbackContext.sendPluginResult(new PluginResult(PluginResult.Status.ERROR, "No Internet Connection"));
        		 
        	}else{
        		
        		JSONObject obj = args.getJSONObject(0);
        		String mail = obj.getString("mail");
        		String subject = obj.getString("subject");
        		String text = obj.getString("text");
        		String attachPath = obj.getString("attachPath");
        		
        		SendEmail(mail, subject, text, attachPath);
        		
        		callbackContext.sendPluginResult(new PluginResult(PluginResult.Status.OK, "Mail sended"));
        		
        	}
        	
        	
          
        } else if(action.equals("echoAsync")) {
            cordova.getActivity().runOnUiThread(new Runnable() {
                public void run() {
                    callbackContext.sendPluginResult( new PluginResult(PluginResult.Status.OK, args.optString(0)));
                }
            });
        } else if(action.equals("echoArrayBuffer")) {
            String data = args.optString(0);
            byte[] rawData= Base64.decode(data, Base64.DEFAULT);
            callbackContext.sendPluginResult( new PluginResult(PluginResult.Status.OK, rawData));
        } else if(action.equals("echoArrayBufferAsync")) {
            cordova.getThreadPool().execute(new Runnable() {
                public void run() {
                    String data = args.optString(0);
                    byte[] rawData= Base64.decode(data, Base64.DEFAULT);
                    callbackContext.sendPluginResult( new PluginResult(PluginResult.Status.OK, rawData));
                }
            });
        } else if(action.equals("echoMultiPart")) {
            callbackContext.sendPluginResult( new PluginResult(PluginResult.Status.OK, args.getJSONObject(0)));
        } else if(action.equals("stopEchoBulk")) {
            bulkEchoing = false;
        } else if(action.equals("echoBulk")) {
            if (bulkEchoing) {
                return true;
            }
            final String payload = args.getString(0);
            final int delayMs = args.getInt(1);
            bulkEchoing = true;
            cordova.getThreadPool().execute(new Runnable() {
                public void run() {
                    while (bulkEchoing) {
                        try {
                            Thread.sleep(delayMs);
                        } catch (InterruptedException e) {}
                        PluginResult pr = new PluginResult(PluginResult.Status.OK, payload);
                        pr.setKeepCallback(true);
                        callbackContext.sendPluginResult(pr);
                    }
                    PluginResult pr = new PluginResult(PluginResult.Status.OK, payload);
                    callbackContext.sendPluginResult(pr);
                }
            });
        } else {
            return false;
        }
        return true;
    }
    private boolean isOnline() {
    	  ConnectivityManager cm = (ConnectivityManager) this.cordova.getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
    	  NetworkInfo ni = cm.getActiveNetworkInfo();
    	  if (ni == null) {
    	   // There are no active networks.
    	   return false;
    	  } else
    	   return true;
    }
    private void SendEmail(String email, String subject, String text, String attachPath){
    	  Intent intent = new Intent(Intent.ACTION_SENDTO);    
    	  intent.setType("text/plain");      
    	  intent.putExtra(Intent.EXTRA_SUBJECT, email);
    	  intent.putExtra(Intent.EXTRA_SUBJECT, subject);      
    	  intent.putExtra(Intent.EXTRA_STREAM, attachPath);      
    	  intent.putExtra(Intent.EXTRA_TEXT, text);         
         
    	  intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK); 

    	  this.cordova.getActivity().startActivity(intent);
    	 // activity.finish();
    	
    }
}
