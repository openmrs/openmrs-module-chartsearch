package org.openmrs.module.chartsearch.page.controller;

/**
 * Created by Eli on 10/03/14.
 */

import org.openmrs.api.context.Context;
import org.openmrs.module.appui.UiSessionContext;
import org.openmrs.module.chartsearch.SearchAPI;
import org.openmrs.module.chartsearch.solr.ChartSearchIndexer;
import org.openmrs.ui.framework.page.PageModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.ArrayList;
import java.util.List;




public class ChartsearchPageController {

    private static final Logger log = LoggerFactory.getLogger(ChartsearchPageController.class);


    private ChartSearchIndexer chartSearchIndexer = getComponent(ChartSearchIndexer.class);

    public void controller(PageModel model, UiSessionContext sessionContext, @RequestParam("patientId") Integer patient) {
        //model.addAttribute("user", sessionContext.getCurrentUser());
    	
        model.addAttribute("patientID_from_get", patient);
        log.info("getting :" + patient);
        log.info("trying to index a patient");
        ArrayList<String> lst = new ArrayList<String>();
        if (patient == null) {
            lst.add("PersonID is null");
            SearchAPI.setResults(lst);
        }
        else {
            lst.add("personID is "+ patient.toString());
            SearchAPI.setResults(lst);
        }
        
        chartSearchIndexer.indexPatientData(patient);
        //log.info("indexed successfully");
    }
    
	private <T> T getComponent(Class<T> clazz) {
		List<T> list = Context.getRegisteredComponents(clazz);
		if (list == null || list.size() == 0)
			throw new RuntimeException("Cannot find component of " + clazz);
		return list.get(0);
	}


}
