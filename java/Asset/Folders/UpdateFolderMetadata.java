/*
   Marketo REST API Sample Code
   Copyright (C) 2016 Marketo, Inc.

   This software may be modified and distributed under the terms
   of the MIT license.  See the LICENSE file for details.
*/
package dev.marketo.samples.Folders;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.NoSuchElementException;
import java.util.Scanner;

import javax.net.ssl.HttpsURLConnection;

import com.eclipsesource.json.JsonObject;

//the Java sample code on dev.marketo.com uses the minimal-json package
//minimal-json provides easy and fast representations of JSON
//for more information check out https://github.com/ralfstx/minimal-json

public class UpdateFolder {
	public String marketoInstance = "https://299-BYM-827.mktorest.com";//Replace this with the host from Admin Web Services
	public String marketoIdURL = marketoInstance + "/identity";	
	public String clientId = ;	//Obtain from your Custom Service in Admin>Launchpoint
	public String clientSecret = ;	//Obtain from your Custom Service in Admin>Launchpoint
	public String idEndpoint = marketoIdURL + "/oauth/token?grant_type=client_credentials&client_id=" + clientId + "&client_secret=" + clientSecret;
	public int id; //ID of the folder to update, required
	public String type; //Type of folder, Program or Folder, required
	public String name; //new name of folder
	public String description; //new description for the folder, up to 2,000 characters
	public Boolean isArchive; // set to true to Archive the folder
	
	public static void main(String[] args){
		UpdateFolder folder = new UpdateFolder();
		folder.id = 5565;
		folder.name = "API Test Folder3";
		folder.type = "folder";
		String result = folder.postData();
		System.out.println(result);
	}
	//Make Request
	public String postData(){
		String result = null;
		try {
			//Assemble the URL
			StringBuilder endpoint = new StringBuilder(marketoInstance + "/rest/asset/v1/folder/" + id + ".json?access_token=" + getToken() + "&type=" + type);
			//Add optional params
			if (description != null){
				endpoint.append("&description=" + description);
			}
			if (name != null){
				endpoint.append("&name=" + name);
			}
			if (isArchive != null){
				endpoint.append("&isArchive=" + isArchive);
			}
			URL url = new URL(endpoint.toString());
			HttpsURLConnection urlConn = (HttpsURLConnection) url.openConnection();
			urlConn.setRequestMethod("POST");
			//urlConn.setRequestProperty("Content-type", "application/json");//"application/json" content-type is required.
            urlConn.setRequestProperty("accept", "text/json");
//            urlConn.setDoOutput(true);
//			OutputStreamWriter wr = new OutputStreamWriter(urlConn.getOutputStream());
//			wr.write(requestBody);
//			wr.flush();
			int responseCode = urlConn.getResponseCode();
			
			if (responseCode == 200){
				InputStream inStream = urlConn.getInputStream();
				result = convertStreamToString(inStream);
			}else{
				result = "Status Code: " + responseCode;
			}
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
		
	}
	public String getToken(){
		String token = null;
		try {
			URL url = new URL(idEndpoint);
			HttpsURLConnection urlConn = (HttpsURLConnection) url.openConnection();
			urlConn.setRequestMethod("GET");
            urlConn.setRequestProperty("accept", "application/json");
            int responseCode = urlConn.getResponseCode();
            if (responseCode == 200) {
                InputStream inStream = urlConn.getInputStream();
                Reader reader = new InputStreamReader(inStream);
                JsonObject jsonObject = JsonObject.readFrom(reader);
                token = jsonObject.get("access_token").asString();
            }else {
                throw new IOException("Status: " + responseCode);
            }
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}catch (IOException e) {
            e.printStackTrace();
        }
		return token;
	}
    private String convertStreamToString(InputStream inputStream) {

        try {
            return new Scanner(inputStream).useDelimiter("\\A").next();
        } catch (NoSuchElementException e) {
            return "";
        }
    }
}
