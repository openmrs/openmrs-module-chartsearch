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
              model.addAttribute("patientId", patient);

    }





    private <T> T getComponent(Class<T> clazz) {
        List<T> list = Context.getRegisteredComponents(clazz);
        if (list == null || list.size() == 0)
            throw new RuntimeException("Cannot find component of " + clazz);
        return list.get(0);
    }
}
