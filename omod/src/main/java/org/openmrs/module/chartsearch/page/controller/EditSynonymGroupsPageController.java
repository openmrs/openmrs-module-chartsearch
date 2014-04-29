package org.openmrs.module.chartsearch.page.controller;

import org.openmrs.module.chartsearch.synonyms.SynonymGroups;
import org.openmrs.ui.framework.fragment.FragmentModel;

/**
 * Created by Eli on 21/04/14.
 */
public class EditSynonymGroupsPageController {
    public void controller(FragmentModel fragmentModel) {

        fragmentModel.addAttribute("synonymGroups", SynonymGroups.getSynonymGroups().toArray());
    }
}
