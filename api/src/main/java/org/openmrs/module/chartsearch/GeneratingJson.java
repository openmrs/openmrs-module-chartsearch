package org.openmrs.module.chartsearch;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.openmrs.module.chartsearch.ChartListItem;
import org.openmrs.module.chartsearch.EncounterItem;
import org.openmrs.module.chartsearch.FormItem;
import org.openmrs.module.chartsearch.ObsItem;
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
        	if(item instanceof  ObsItem){
                observation = new JSONObject();
                observation.put("date",((ObsItem) item).getObsDate());
                observation.put("obsGroupId",((ObsItem) item).getObsGroupId());
                observation.put("concept_name",((ObsItem) item).getConceptName());
                observation.put("value",((ObsItem) item).getValue());
                //json.put("observation", observation);
                //observation.put("locations",item.());   TODO
                arr_of_obs.add(observation);
        	}
        	
        	else if (item instanceof  FormItem){
                observation = new JSONObject();
                observation.put("date",((FormItem) item).getFormId());
                observation.put("concept_name",((FormItem) item).getEncounterType());
                observation.put("value",((FormItem) item).getFormName());
                //json.put("observation", observation);
                //observation.put("locations",item.());   TODO
                arr_of_obs.add(observation);
        	}
        	else if (item instanceof  EncounterItem){
                observation = new JSONObject();
                observation.put("date",((EncounterItem) item).getEncounterId());
                observation.put("concept_name",((EncounterItem) item).getEncounterType());
                observation.put("value",((EncounterItem) item).getUuid());
                //json.put("observation", observation);
                //observation.put("locations",item.());   TODO
                arr_of_obs.add(observation);
        	}
        }
        return arr_of_obs.toString();
    }
    
}
    
    