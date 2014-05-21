package org.openmrs.module.chartsearch.web.controller;

import org.openmrs.api.context.Context;
import org.openmrs.module.chartsearch.api.ChartSearchService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

/**
 * Created by Eli on 20/05/14.
 */
@Controller("chartsearch.ManagesynonymGroupsFormController")
@RequestMapping("/module/chartsearch/managesynonymgroups")
public class ManagesynonymgroupsFormController {
    @RequestMapping(method = RequestMethod.GET)
    public void showForm(ModelMap map) {
        ChartSearchService chartSearchService = Context.getService(ChartSearchService.class);
        List synGroups = chartSearchService.getAllSynonymGroups();

        map.put("synonymGroups", synGroups);



    }







}
