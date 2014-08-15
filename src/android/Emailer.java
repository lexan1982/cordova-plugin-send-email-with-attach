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
 
     
     /**
     * Executes the request and returns PluginResult.
     *
     * @param action            The action to execute.
     * @param args              JSONArry of arguments for the plugin.
     * @param callbackContext   The callback context from which we were invoked.
     */
    @SuppressLint("NewApi") 
    public boolean execute(String action, final JSONObject obj, final CallbackContext callbackContext) throws JSONException {
        if (action.equals("emailer")) {

        	Log.d(TAG, "..action == emailer" );
        	Log.d(TAG, obj.toString());
        	
        	if(!isOnline()){
        		Log.d(TAG, ".. no internet connetion");
        		callbackContext.sendPluginResult(new PluginResult(PluginResult.Status.ERROR, "No Internet Connection"));
        		 
        	}else{
        		
        		Log.d(TAG, ".. prepare letter");
        		String mail = obj.getString("mail");
        		Log.d(TAG, mail);
        		String subject = obj.getString("subject");
        		Log.d(TAG, subject);
        		String text = obj.getString("text");
        		Log.d(TAG, text);
        		String attachPath = obj.getString("attachPath");
        		Log.d(TAG, attachPath);
        		
        		SendEmail(mail, subject, text, attachPath);
        		
        		callbackContext.sendPluginResult(new PluginResult(PluginResult.Status.OK, "Mail sended"));
        		
        	}
        	
        	
          
        }  else {
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
    	
    	Log.d(TAG, ".." + email);
    	
    	  Intent intent = new Intent(Intent.ACTION_SEND);    
    	  intent.setType("text/plain");      
    	  intent.putExtra(Intent.EXTRA_SUBJECT, email);
    	  intent.putExtra(Intent.EXTRA_SUBJECT, subject);      
    	  intent.putExtra(Intent.EXTRA_STREAM, attachPath);      
    	  intent.putExtra(Intent.EXTRA_TEXT, text);         
         
    	  intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK); 

    	  this.cordova.getActivity().startActivity(Intent.createChooser(intent,
    	            "Send email..."));
    	 
    	
    }
}
