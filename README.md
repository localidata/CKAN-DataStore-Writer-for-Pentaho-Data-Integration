# CKAN-Resource-Updater-for-Pentaho-Data-Integration
CKAN Resource Updater for Pentaho Data Integration (Kettle)

### Installation

Copy the files below to their respective folders under the Pentaho Data Integration installation directory:
- ../data-integration/plugins/steps/ckan-updater-plugin/ckan\_ckanUpdater.jar
- ../data-integration/plugins/steps/ckan-updater-plugin/ckan\_logo.png
- ../data-integration/plugins/steps/ckan-updater-plugin/plugin.xml
- ../data-integration/plugins/steps/ckan-updater-plugin/lib/commons-logging-1.2.jar
- ../data-integration/plugins/steps/ckan-updater-plugin/lib/gson-2.2.jar
- ../data-integration/plugins/steps/ckan-updater-plugin/lib/httpclient-4.2.jar
- ../data-integration/plugins/steps/ckan-updater-plugin/lib/httpcore-4.2.jar
- ../data-integration/plugins/steps/ckan-updater-plugin/lib/httpmime-4.2.jar
- ../data-integration/plugins/steps/ckan-updater-plugin/lib/jsoup-1.8.1.jar


### User Guide

The CKAN Resource Updater plugin allows users to upload tabular data to a CKAN data portal.
Drag and drop the CKAN DataStore Upload step from the output category to the workspace.
Link another step to the CKAN Resource Resource step to upload the output of the previous step.

### Options
To create an new resource provide a valid Package ID and omit the Resource ID

| Option         | Description                                                                                             |
| -------------- | ------------------------------------------------------------------------------------------------------- |
| Step name	     |Name of the step. This name should be unique in a single transformation.                                 |
| Domain         |The domain of the CKAN data portal to which data will be uploaded. (eg: http://demo.ckan.org)            |
| API Key        |Users can find their API key in their user profile on the CKAN data portal.                              |
| Package ID     |The ID of an existing package where data will be uploaded in a DataStore resource. (eg: test-dataset)    |
| Resource Title |The title of the resource.                                                                               |
| Description    |A short description about the resource. (optional)                                                       |
| Resource ID    |The ID of an existing DataStore resource to update. If left empty a new DataStore resource will be made. |



### PDI Sample Transformation
You can find an article (spanish) in our web: https://www.localidata.com/como-actualizar-recursos-de-ckan-a-traves-de-pentaho-data-integration/

### Tests
We are tested this plugin with Pentaho Data Integration 7.1 and 8.2

Note: the tests agains https://demo.ckan.org always return a 403 error, but in another enviroments everything works