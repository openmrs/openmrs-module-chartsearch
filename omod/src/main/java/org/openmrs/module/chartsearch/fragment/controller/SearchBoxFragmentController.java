package org.openmrs.module.chartsearch.fragment.controller;





import java.util.ArrayList;
import java.util.List;

import org.openmrs.api.context.Context;
import org.openmrs.module.chartsearch.ChartListItem;
import org.openmrs.module.chartsearch.SearchAPI;
import org.openmrs.module.chartsearch.SearchPhrase;
import org.openmrs.module.chartsearch.solr.ChartSearchSearcher;
import org.openmrs.ui.framework.annotation.BindParams;
import org.openmrs.ui.framework.page.PageModel;
import org.springframework.web.bind.annotation.RequestParam;

public class SearchBoxFragmentController {
    private ChartSearchSearcher searcher = getComponent(ChartSearchSearcher.class);

	public void get() {
	}
	
	
	
	
	
	
	public String post(PageModel model, @BindParams SearchPhrase searchPhrase ,@RequestParam("patientId") Integer patient) {

        SearchAPI searchAPI =SearchAPI.getInstance();
        model.addAttribute("patientID_from_get", patient);
		//SearchAPI.setSearchPhrase(searchPhrase);
		//SearchAPI.search();
		//List<String> temp = new ArrayList<String>();
		//temp.add(searchPhrase.getPhrase());
        Integer length = Integer.valueOf(10);
        Integer start = Integer.valueOf(0);
        List<ChartListItem> items = new ArrayList<ChartListItem>();

        try {
            items = searcher.getDocumentList(patient, searchPhrase.getPhrase(), start, length);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if(items == null){
           items = new ArrayList<ChartListItem>();
            ChartListItem itemsIsNull = new ChartListItem();
            itemsIsNull.setConceptName("items list returned from search is null");
            items.add(itemsIsNull);

        }

        searchAPI.setResults(items);


		return "redirect:chartsearch/chartsearch.page?patientId=" + patient;
	}


    private <T> T getComponent(Class<T> clazz) {
        List<T> list = Context.getRegisteredComponents(clazz);
        if (list == null || list.size() == 0)
            throw new RuntimeException("Cannot find component of " + clazz);
        return list.get(0);
    }
	
	
	
	
	
}