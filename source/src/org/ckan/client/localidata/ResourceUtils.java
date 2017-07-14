package org.ckan.client.localidata;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import javax.net.ssl.HttpsURLConnection;

import org.apache.commons.logging.Log;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.ckan.client.Connection;
import org.pentaho.di.core.logging.LogChannelInterface;

import com.google.gson.Gson;

public class ResourceUtils {
	
	 private static final String pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSSSS";
	 private static SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
	
	

	public static synchronized String updateResource(Connection ckanConnection,String idResource, String dataSetId, String path, String label, String format, String description, int timeout) throws Exception {

		//update
		StringBuffer sb = new StringBuffer();

		Map<String, String> headers = new HashMap<String, String>();

		headers.put("X-CKAN-API-Key", ckanConnection.getApiKey());

		String url = ckanConnection.getM_host() + "/api/action/resource_update";
		URL targetUrl = new URL(url);

		HttpURLConnection connection = null;
		if (url.startsWith("https")){
			connection = (HttpsURLConnection) targetUrl.openConnection();			
		}else{
			connection = (HttpURLConnection) targetUrl.openConnection();
		}
		
		
		connection.setDoOutput(true);
		connection.setRequestMethod("POST");

		File upload = new File(path);

		FileBody fileBody = new FileBody(upload);
		MultipartEntity multipartEntity = new MultipartEntity(HttpMultipartMode.STRICT);
		multipartEntity.addPart("upload", fileBody);
		multipartEntity.addPart("id", new StringBody(idResource));
		multipartEntity.addPart("package_id", new StringBody(dataSetId));
		multipartEntity.addPart("name", new StringBody(label));
		multipartEntity.addPart("format", new StringBody(format));
		multipartEntity.addPart("description", new StringBody(description));
		multipartEntity.addPart("last_modified", new StringBody(dateFormat.format(new Date())));
		//Esta linea de abajo es para que funcione con CKAN 2.5.2
		multipartEntity.addPart("url", new StringBody(""));
		
		connection.setRequestProperty("X-CKAN-API-Key", ckanConnection.getApiKey());

		connection.setRequestProperty("Content-Type", multipartEntity.getContentType().getValue());		
		connection.setReadTimeout(timeout);		
		
		OutputStream out = connection.getOutputStream();
		boolean error=false;
		try {
			multipartEntity.writeTo(out);
		}
		catch (Exception e)
		{
			error=true;
			e.printStackTrace();
		}
		finally {
			out.close();
		}
		
		if (error)
			throw new Exception("Error uploading resource");
		
		

		BufferedReader responseBuffer = new BufferedReader(new InputStreamReader((connection.getInputStream())));

		String output;

		while ((output = responseBuffer.readLine()) != null) {
			sb.append(output);
		}

		
	   		
		
		return sb.toString();

	}
	
	

	
	
	public static synchronized boolean createResource(Connection ckanConnection, String dataSetId, String path, String label, String format, String description, int timeout) throws Exception {
		
		StringBuffer sb = new StringBuffer();

		Map<String, String> headers = new HashMap<String, String>();

		headers.put("X-CKAN-API-Key", ckanConnection.getApiKey());

		String url = ckanConnection.getM_host() + "/api/action/resource_create";
		URL targetUrl = new URL(url);

		
		HttpURLConnection connection=null;
		
		if (url.startsWith("https")){			
			connection = (HttpsURLConnection) targetUrl.openConnection();			
		}else{
			connection = (HttpURLConnection) targetUrl.openConnection();
		}
		
		connection.setDoOutput(true);
		connection.setRequestMethod("POST");
		connection.setConnectTimeout(timeout);
		connection.setReadTimeout(timeout);	

		File upload = new File(path);

		FileBody fileBody = new FileBody(upload);
		MultipartEntity multipartEntity = new MultipartEntity(HttpMultipartMode.STRICT);
		multipartEntity.addPart("upload", fileBody);

		multipartEntity.addPart("package_id", new StringBody(dataSetId));
		multipartEntity.addPart("name", new StringBody(label));
		multipartEntity.addPart("format", new StringBody(format));
		multipartEntity.addPart("description", new StringBody(description));
		multipartEntity.addPart("last_modified", new StringBody(dateFormat.format(new Date())));
		//Esta linea de abajo es para que funcione con CKAN 2.5.2
		multipartEntity.addPart("url", new StringBody(""));
		

		connection.setRequestProperty("X-CKAN-API-Key", ckanConnection.getApiKey());

		connection.setRequestProperty("Content-Type", multipartEntity.getContentType().getValue());		
			
		
		
		OutputStream out = null;
		boolean error=false;
		try {			
			out = connection.getOutputStream();
			multipartEntity.writeTo(out);			
		}
		catch (Exception e)
		{
			error=true;
			e.printStackTrace();			
		}
		finally {
			if ((out!=null))
				out.close();
		}
		
		if (error)
			//throw new Exception("Error uploading resource");
			return false;
		
		BufferedReader responseBuffer = new BufferedReader(new InputStreamReader((connection.getInputStream())));
		

		String output;

		while ((output = responseBuffer.readLine()) != null) {
			sb.append(output);
		}
		
		 Boolean success = new Boolean("false");
		 Gson gson=new Gson();		 
		 
		 try
		 {
   	 	 	HashMap hm  = gson.fromJson(sb.toString(),HashMap.class);
   	 	 	if (hm.get("success")!=null)
   	 	 		success = (Boolean)hm.get("success");
		 }
		 catch (Exception e){}
		
		
		return success.booleanValue();

	}

	

	
}
