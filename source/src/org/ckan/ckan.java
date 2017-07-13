package org.ckan;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import org.ckan.client.Client;
import org.ckan.client.Connection;
import org.ckan.client.localidata.Constants;
import org.ckan.client.result.impl.DataStore;
import org.ckan.client.result.impl.Field;
import org.ckan.client.result.impl.Resource;
import org.pentaho.di.core.exception.KettleException;
import org.pentaho.di.core.row.RowMetaInterface;
import org.pentaho.di.trans.Trans;
import org.pentaho.di.trans.TransMeta;
import org.pentaho.di.trans.step.BaseStep;
import org.pentaho.di.trans.step.StepDataInterface;
import org.pentaho.di.trans.step.StepInterface;
import org.pentaho.di.trans.step.StepMeta;
import org.pentaho.di.trans.step.StepMetaInterface;


public class ckan extends BaseStep implements StepInterface {
	
	final int timeout = Constants.timeout;
	
	private ckanData data;
	private ckanMeta meta;
	
	private String ckanDomain = "";
	private String ckanApiKey = "";
	private String ckanPackageId = "";
	private String ckanResourceTitle = "";
	private String ckanResourceDescription = "";
	private String ckanResourceId = "";	
	private int fieldLength = 0;
	
	private List<Field> fields = new ArrayList<Field>();
	private LinkedHashMap<String, Object> dataRow = new LinkedHashMap<String, Object>();
	private List<LinkedHashMap<String, Object>> records = new ArrayList<LinkedHashMap<String, Object>>();
	
	
	public ckan(StepMeta s, StepDataInterface stepDataInterface, int c, TransMeta t, Trans dis) {
		super(s,stepDataInterface,c,t,dis);
	}
	
	public synchronized boolean processRow(StepMetaInterface smi, StepDataInterface sdi) throws KettleException {
		meta = (ckanMeta)smi;
		data = (ckanData)sdi;
		
		String fieldName = "";
		String fieldType = "";
		int indexOfValue = 0;
		String fieldValue = "";		
		
		// Get row, blocks when needed!
		Object[] r = getRow();
		
		// No more input to be expected...
		if (r==null) {			
			uploadResource(records);
			setOutputDone();
			return false;
		}
		
		if (first) {			
			first = false;
			
			// Determine output field structure
			data.inputRowMeta = getInputRowMeta();
			data.outputRowMeta = (RowMetaInterface)getInputRowMeta().clone();
			meta.getFields(data.outputRowMeta, getStepname(), null, null, this);

			fieldLength = data.outputRowMeta.getFieldNames().length;
			for(int i=0; i < fieldLength; i++) {
				fieldName = data.outputRowMeta.getFieldNames()[i];
				fieldType = data.outputRowMeta.getFieldNamesAndTypes(0)[i].trim();
				
				if (fieldType.equals("(Integer)") || fieldType.equals("(Number)")) {
					fields.add(new Field(fieldName, "numeric"));
					
				}
				else if (fieldType.equals("(Date)")) {
					fields.add(new Field(fieldName, "timestamp"));
					
				}
				else {
					fields.add(new Field(fieldName, "text"));
					
				}
			}
			
			
			
		
			
		}
		
		
		
		dataRow = new LinkedHashMap<String, Object>();
		for(int i=0; i < fieldLength; i++) {
			fieldName = data.outputRowMeta.getFieldNames()[i];
			
			indexOfValue = data.inputRowMeta.indexOfValue(environmentSubstitute(fieldName));
			fieldValue = data.inputRowMeta.getString(r, indexOfValue);
			
			dataRow.put(fieldName, fieldValue);
		}
		
		records.add(dataRow);
		
		// Copy row to possible alternate rowset(s)
		putRow(data.outputRowMeta, r);
		
		// Some basic logging every 5000 rows
		if (checkFeedback(getLinesRead())) {
			if (log.isBasic()) logBasic("Lines read: " + getLinesRead());
		}
		
		return true;
	}
		
	public boolean init(StepMetaInterface smi, StepDataInterface sdi) {
		meta = (ckanMeta)smi;
		data = (ckanData)sdi;
		
		// Get user parameters
		if (meta.getDomain() != null) {
			ckanDomain = meta.getDomain();
		}
		
		if (meta.getApiKey() != null) {
			ckanApiKey = meta.getApiKey();
		}
		
		if (meta.getPackageId() != null) {
			ckanPackageId = meta.getPackageId();
		}
		
		if (meta.getResourceTitle() != null) {
			ckanResourceTitle = meta.getResourceTitle();
		}
		
		if (meta.getResourceDescription() != null) {
			ckanResourceDescription = meta.getResourceDescription();
		}
		
		if (meta.getResourceId() != null) {
			ckanResourceId = meta.getResourceId();
		}
		
		
		return super.init(smi, sdi);
	}

	public void dispose(StepMetaInterface smi, StepDataInterface sdi) {
		meta = (ckanMeta)smi;
		data = (ckanData)sdi;
		
		super.dispose(smi, sdi);
	}
	
	
	
	public boolean uploadResource(List<LinkedHashMap<String, Object>> records) {
		
		if (log.isBasic()) logBasic("Uploading resource with " + records.size()+ " records");
		
		Client ckanClient = new Client( new Connection(ckanDomain), ckanApiKey);
		File temp = null;
		
		
		try {
						
			String titles="";
			StringBuffer lines=new StringBuffer();
			for (LinkedHashMap<String, Object> record:records)
			{
				if ("".equals(titles))
				{
					for (String key : record.keySet())
					{
						titles+=key+",";
					}
					titles=titles.replaceFirst(".$","")+System.getProperty("line.separator");
				}
				String tempLine="";
				for (String key : record.keySet()){
					tempLine+=record.get(key)+",";									
		        }
				lines.append(tempLine.replaceFirst(".$","")+System.getProperty("line.separator"));
			}
			
		
			 //create a temp file
    	    temp = File.createTempFile(ckanResourceTitle, ".csv");

    	    //write it
    	    BufferedWriter bw = new BufferedWriter(new FileWriter(temp));
    	    bw.write(titles);
    	    bw.write(lines.toString());    	    
    	    bw.close();
    	    
		} catch ( Exception e ) {
			logError("Error generating the temp file",e);
			return false;
		}
		
		if (log.isBasic()) logBasic("Temporal file generated");
    	    
    	    try
    	    {   
				if (ckanResourceId.equals(""))
	    	    {			
	    	    	if (log.isBasic()) logBasic("Creating resource");
	    	    	ckanClient.createResourceFromFile(ckanPackageId, temp.getAbsolutePath(), ckanResourceTitle, "CSV", ckanResourceDescription, timeout);
	    	    }else{	    	    	
	    	    	if (log.isBasic()) logBasic("Updating resource");
	    	    	ckanClient.updateResource(ckanResourceId, ckanPackageId, temp.getAbsolutePath(), ckanResourceTitle, "CSV",  ckanResourceDescription, timeout);
	    	    }
    	    }
    	    catch (Exception e)
    	    {
    	    	if (log.isBasic()) {
    	    		if (ckanResourceId.equals(""))
    	    	    {
    	    			logError ("Error creating the resource");
    	    	    }
    	    		else{
    	    			logError ("Error updating the resource, please check the ID");
    	    		}
    	    	}
    	    	return false;
    	    }
			
			
			
			return true;
		
	}
	
	
	
	
}
