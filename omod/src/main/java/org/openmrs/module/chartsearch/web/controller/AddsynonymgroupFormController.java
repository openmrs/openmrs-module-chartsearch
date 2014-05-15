package org.openmrs.module.chartsearch.web.controller;

import org.openmrs.module.chartsearch.synonyms.SynonymGroup;
import org.openmrs.module.chartsearch.synonyms.SynonymGroups;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.Map;


@Controller("chartsearch.AddsynonymgroupFormController")
@RequestMapping("/module/chartsearch/addsynonymgroup")
public class AddsynonymgroupFormController {
    @RequestMapping(method = RequestMethod.GET)
    public void showForm() {
    }

    @RequestMapping(method = RequestMethod.POST)
    public String handleSubmission(@RequestParam Map<String, String> requestParams) {
        String groupName = requestParams.get("groupName");

        int synLen = "synonym".length();
        ArrayList<String> synonymList = new ArrayList<String>();
        for (String param : requestParams.keySet()) {
            if (param.substring(0, synLen).equals("synonym")) {
                if (requestParams.get(param) != null || !requestParams.get(param).equals("")) {
                    synonymList.add(requestParams.get(param));
                }
            }
        }
        for(String str : synonymList){
            System.out.println(str);
        }
        boolean category;
        if(requestParams.get("category")==null){
            category=false;
        }
        else{
            category=true;
        }
        SynonymGroup synGrp = new SynonymGroup(groupName, category, synonymList);

        SynonymGroups.getInstance().addSynonymGroup(synGrp);

        return "redirect:addsynonymgroup.form";
    }


}
