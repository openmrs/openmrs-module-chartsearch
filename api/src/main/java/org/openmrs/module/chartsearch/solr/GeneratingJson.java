package org.openmrs.module.chartsearch.solr;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.openmrs.module.chartsearch.ChartListItem;
import org.openmrs.module.chartsearch.SearchAPI;

/**
 * Created by Eli on 16/03/14.
 */

public class GeneratingJson {
    public static String generateJson(){
        JSONObject json = new JSONObject();
        SearchAPI searchAPI =SearchAPI.getInstance();
        //switchCase - obs

        JSONObject observation;
        //JSONObject observations = new JSONObject();
        JSONArray arr_of_obs = new JSONArray();
        for(ChartListItem item : searchAPI.getResults()){
            observation = new JSONObject();
            observation.put("date",item.getObsDate());
            //observation.put("concept_name",item.getConceptName());
            observation.put("concept_name","Temp concept name");
            observation.put("value",item.getValue());
            //json.put("observation", observation);
            //observation.put("locations",item.());   TODO
            arr_of_obs.add(observation);
        }
        return arr_of_obs.toString();
    }


   /* public static void main(String[] args) throws Exception {
observations = [{
"date":"14.14.14",
"concept_name":"name",
"value":"val",
"locations":"locasdmkad",
},...]

        JSONObject json = new JSONObject();

        // Put a simple element
        json.put( "aircraft", "A320");

        // Add a JSON Object
        JSONObject pilot = new JSONObject();
        pilot.put( "firstName", "John");
        pilot.put( "lastName", "Adams");
        json.put( "pilot", pilot);

        // Accumulate values in an array
        json.accumulate("passenger", "George Washington");
        json.accumulate("passenger", "Thomas Jefferson");

        // Passing a number to toString() adds indentation
        System.out.println( "JSON: " + json.toString(2) );
    }*/
}