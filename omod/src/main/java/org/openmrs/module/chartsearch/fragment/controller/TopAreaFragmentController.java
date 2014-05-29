package org.openmrs.module.chartsearch.fragment.controller;

/**
 * Created by Tal on 3/19/14.
 */

import org.openmrs.api.context.Context;
import org.openmrs.module.chartsearch.solr.ChartSearchSearcher;
import org.openmrs.ui.framework.fragment.FragmentModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

public class TopAreaFragmentController {
    private static final Logger log = LoggerFactory.getLogger(TopAreaFragmentController.class);
    private ChartSearchSearcher searcher = getComponent(ChartSearchSearcher.class);


    public void controller(FragmentModel model,  @RequestParam("patientId") Integer patient){
        System.out.println("CONTROLLER: patientID is:" + patient);

        model.addAttribute("patientId", patient);

    }



/*

    public String post(FragmentModel model, @BindParams SearchPhrase search_phrase, @RequestParam("patientId") Integer patient) {
        SearchAPI searchAPIInstance = SearchAPI.getInstance();
        List<ChartListItem> items = searchAPIInstance.search(patient,search_phrase);
        List<ChartListItem> updatedItems = new ArrayList<ChartListItem>();

        for (ChartListItem observation : items) //loop to get full details about observations.
        {
            int itemObsId = -1;
            if (observation instanceof ObsItem) {
                itemObsId = ((ObsItem) observation).getObsId();
            }
            ChartListItem updatedObservation = DWRChartSearchService.getObservationDetails(itemObsId);
            updatedItems.add(updatedObservation);
        }
        searchAPIInstance.setResults(updatedItems); //setting results to show.

        return "redirect:chartsearch/chartsearch.page?patientId=" + patient;
    }

*/

    private <T> T getComponent(Class<T> clazz) {
        List<T> list = Context.getRegisteredComponents(clazz);
        if (list == null || list.size() == 0)
            throw new RuntimeException("Cannot find component of " + clazz);
        return list.get(0);
    }
}
