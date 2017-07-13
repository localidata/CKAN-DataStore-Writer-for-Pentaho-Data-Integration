package org.ckan.client;

import java.io.BufferedReader;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.ckan.client.resource.impl.Dataset;
import org.ckan.client.resource.impl.Organization;
import org.ckan.client.resource.impl.Resource;
import org.ckan.client.result.impl.DatasetSearchResult;
import org.ckan.client.result.list.impl.DatasetSearchList;

public class Example {

	public static void createDataSet() throws UnsupportedEncodingException {
		
		
		org.ckan.client.Client client = new org.ckan.client.Client(new org.ckan.client.Connection("http://ckanserver"), "1c9540cc-1791-44d7-9ec0-373883c0f0fc");

		Dataset ds = null;
		try {
			ds = client.getDataset("sensores");
			System.out.println(ds.getId());
			ds = client.getDataset("08d3243b-7ac5-43d3-98c0-293bb69be1ed");
			System.out.println(ds.getName());
		} catch (CKANException e1) {

			e1.printStackTrace();
		}

		List<Resource> recursos = ds.getResources();

		Resource recurso = recursos.get(0);
		recurso.getHash();

		/*
		 * @todo Crear grupo y asignarle los dataset
		 */
		/*
		 * 
		 * recursos.add(recurso);
		 * 
		 * ds.setResources(recursos);
		 * 
		 * try { client.updateDataset(ds);
		 * System.out.println("Recursos añadidos al dataset"); } catch
		 * (CKANException e) { 
		 * e.printStackTrace(); }
		 */

	}

	public static void listDataSet() {
		org.ckan.client.Client client = new org.ckan.client.Client(new org.ckan.client.Connection("http://ckanserver"), "1c9540cc-1791-44d7-9ec0-373883c0f0fc");

		Dataset ds = null;
		DatasetSearchResult sr = null;
		try {
			sr = client.searchDatasets("");
		} catch (Exception cke) {
			System.out.println(cke);
		}
		DatasetSearchList dsl = sr.result;
		List results = dsl.results;
		Iterator iterator = results.iterator();
		while (iterator.hasNext()) {
			ds = (Dataset) iterator.next();
			System.out.println(ds);

			List recursos = ds.getResources();
			Iterator it2 = recursos.iterator();
			while (it2.hasNext()) {
				Resource r2 = (Resource) it2.next();

				/*
				 * System.out.println(r2.getCache_last_updated());
				 * System.out.println(r2.getCache_url());
				 * System.out.println(r2.getCreated());
				 * System.out.println(r2.getDescription());
				 * System.out.println(r2.getFormat()); System.out.println(r2.getId());
				 * System.out.println(r2.getMessage());
				 * System.out.println(r2.getMimetype());
				 * System.out.println(r2.getMimetype_inner());
				 * System.out.println(r2.getWebstore_url());
				 * 
				 * System.out.println(r2.getSize());
				 */
				System.out.println("-" + r2.getUrl());
				System.out.println("-" + r2.getFormat());
				System.out.println("-" + r2.getMimetype());
				System.out.println("-" + r2.getMimetype_inner());
				System.out.println("-" + r2.getWebstore_url());
				System.out.println("-" + r2.getCache_url());
				System.out.println("t2:" + r2.getResource_type());

			}

		}

		try {
			ds = client.debugThis().getDataset("test-dataset");
		} catch (CKANException cke) {
			System.out.println(cke);
		}
		System.out.println(ds);
	}

	public static void subeRecurso() {

		org.ckan.client.Client client = new org.ckan.client.Client(new org.ckan.client.Connection("http://ckanserver"), "1c9540cc-1791-44d7-9ec0-373883c0f0fc");

		String response = null;
		try {
			response = client.createResourceFromFile("08d3243b-7ac5-43d3-98c0-293bb69be1ed", "example.json", "clienteBueno", "json");
		} catch (Exception e) {
			e.printStackTrace();
		}		
	}

	public static void main(String[] args) {

		org.ckan.client.Client client = new org.ckan.client.Client(new org.ckan.client.Connection("http://sandbox-ckan.localidata.com"), "9fa91904-1623-47f1-8475-c7bb88262b94");

		Dataset ds = null;
		try {
			//ds = client.getDataset("Prueba");
			//System.out.println(ds.getId());
			ds = client.getDataset("44e0c28c-9c32-46aa-a2c3-4b39bcf5662c");
			System.out.println(ds.getName());
		} catch (CKANException e1) {

			e1.printStackTrace();
		}

		List recursos = ds.getResources();
		Iterator it2 = recursos.iterator();
		if (it2.hasNext()) {
			Resource r2 = (Resource) it2.next();
			System.out.println(r2.getName());
			try {
				client.deleteResource(r2.getId());
			} catch (Exception e) {

				e.printStackTrace();
			}
		}
	}

}
