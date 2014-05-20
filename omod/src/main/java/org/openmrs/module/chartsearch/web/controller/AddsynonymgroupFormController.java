package org.openmrs.module.chartsearch.web.controller;

import org.openmrs.api.context.Context;
import org.openmrs.module.chartsearch.api.ChartSearchService;
import org.openmrs.module.chartsearch.synonyms.Synonym;
import org.openmrs.module.chartsearch.synonyms.SynonymGroup;
import org.openmrs.module.chartsearch.synonyms.SynonymGroups;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


@Controller("chartsearch.AddsynonymgroupFormController")
@RequestMapping("/module/chartsearch/addsynonymgroup")
public class AddsynonymgroupFormController {
    @RequestMapping(method = RequestMethod.GET)
    public void showForm(ModelMap map, @RequestParam(value = "synonymGroupId", required = false) Integer groupId) {
        if (groupId != null) {

            ChartSearchService chartSearchService = Context.getService(ChartSearchService.class);
            SynonymGroups synonymGroupsInstance = SynonymGroups.getInstance();
            synonymGroupsInstance.clearSynonymGroups();
            SynonymGroup synonymGroup = chartSearchService.getSynonymGroupById(groupId);
            int synonymCount = synonymGroup.getSynonymSet().size();
            map.put("synonymGroup", synonymGroup);
            if (synonymGroup.getIsCategory()) {
                map.put("isCategory", "checked");
            }

            map.put("synonymCount", synonymCount);
        }

    }

    @RequestMapping(method = RequestMethod.POST)
    public String handleSubmission(@RequestParam Map<String, String> requestParams) {
        String groupName = requestParams.get("groupName");

        int synLen = "synonym".length();
        ArrayList<Synonym> synonymList = new ArrayList<Synonym>();
        for (String param : requestParams.keySet()) {
            if (param.substring(0, synLen).equals("synonym")) {
                if (requestParams.get(param) != null || !requestParams.get(param).equals("")) {
                    Synonym newSyn = new Synonym(requestParams.get(param));
                    synonymList.add(newSyn);
                }
            }
        }
        for (Synonym syn : synonymList) {
            System.out.println(syn.getSynonymName());
        }
        boolean category;
        if (requestParams.get("category") == null) {
            category = false;
        } else {
            category = true;
        }

        if (Context.isAuthenticated()) {
            ChartSearchService chartSearchService = Context.getService(ChartSearchService.class);
            SynonymGroups synonymGroupsInstance = SynonymGroups.getInstance();
            synonymGroupsInstance.clearSynonymGroups();
            List synGroups = chartSearchService.getAllSynonymGroups();

            if (synGroups != null) {
                synonymGroupsInstance.setSynonymGroupsHolder(synGroups);

                SynonymGroup synGrp = new SynonymGroup(groupName, category, synonymList);

                if (synonymGroupsInstance.addSynonymGroup(synGrp)) {
                    chartSearchService.saveSynonymGroup(synGrp);
                }
            } else {
                System.out.println("synonym groups from db are null");
            }
            synonymGroupsInstance.clearSynonymGroups();
        }


        return "redirect:addsynonymgroup.form";
    }


}
