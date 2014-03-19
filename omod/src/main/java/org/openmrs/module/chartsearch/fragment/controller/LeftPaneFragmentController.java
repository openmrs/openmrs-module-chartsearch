package org.openmrs.module.chartsearch.fragment.controller;

/**
 * Created by Tal on 3/19/14.
 */
import org.openmrs.api.context.Context;
import org.openmrs.module.chartsearch.ChartListItem;
import org.openmrs.module.chartsearch.SearchAPI;
import org.openmrs.module.chartsearch.SearchPhrase;
import org.openmrs.module.chartsearch.solr.ChartSearchSearcher;
import org.openmrs.module.chartsearch.web.dwr.DWRChartSearchService;
import org.openmrs.ui.framework.annotation.BindParams;
import org.openmrs.ui.framework.page.PageModel;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;

public class LeftPaneFragmentController
{
    private ChartSearchSearcher searcher = getComponent(ChartSearchSearcher.class);

    public void get()
    {
        //TODO - Eli - what's need to be here?
    }

    /*
    *   //TODO - Eli please fill it
    *   Function post
    *   @param model -
    *   @param searchPhrase with @BindParams
    *   patient
    *   @return ?
     */
    public String post(PageModel model, @BindParams SearchPhrase searchPhrase ,@RequestParam("patientId") Integer patient)
    {
        SearchAPI searchAPI =SearchAPI.getInstance();
        model.addAttribute("patientID_from_get", patient);
        // TODO - fix comments formatting ELI
        //SearchAPI.setSearchPhrase(searchPhrase);
        //SearchAPI.search();
        //List<String> temp = new ArrayList<String>();
        //temp.add(searchPhrase.getPhrase());
        Integer length = Integer.valueOf(999999999);
        Integer start = Integer.valueOf(0);
        List<ChartListItem> items = new ArrayList<ChartListItem>();
        try
        {
            items = searcher.getDocumentList(patient, searchPhrase.getPhrase(), start, length);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        if(items == null)
        {
            items = new ArrayList<ChartListItem>();
            ChartListItem itemsIsNull = new ChartListItem();
            itemsIsNull.setConceptName("items list returned from search is null");
            items.add(itemsIsNull);
        }
        List<ChartListItem> updatedItems = new ArrayList<ChartListItem>();
        for(ChartListItem observation : items)
        {
            ChartListItem updatedObservation = DWRChartSearchService.getObservationDetails(observation.getObsId());
            updatedItems.add(updatedObservation);
        }
        searchAPI.setResults(updatedItems);
        return "redirect:chartsearch/chartsearch.page?patientId=" + patient;
    }

    // TODO - ELI - add javadoc please
    private <T> T getComponent(Class<T> clazz)
    {
        List<T> list = Context.getRegisteredComponents(clazz);
        if (list == null || list.size() == 0)
            throw new RuntimeException("Cannot find component of " + clazz);
        return list.get(0);
    }
}
