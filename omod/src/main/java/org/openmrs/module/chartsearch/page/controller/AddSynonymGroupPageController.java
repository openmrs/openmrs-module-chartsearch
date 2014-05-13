package org.openmrs.module.chartsearch.page.controller;

/**
 * Created by Eli on 21/04/14.
 */
import org.openmrs.module.chartsearch.synonyms.SynonymGroup;
import org.openmrs.module.chartsearch.synonyms.SynonymGroups;
import org.openmrs.ui.framework.annotation.BindParams;
public class AddSynonymGroupPageController {
    public void get() {
    }

    public String post(@BindParams SynonymGroup synGrp) {
        SynonymGroups.addSynonymGroup(synGrp);

        return "redirect:admin/index.htm";
    }

}


