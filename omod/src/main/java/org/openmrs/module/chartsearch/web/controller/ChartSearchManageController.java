/**
 * The contents of this file are subject to the OpenMRS Public License
 * Version 1.0 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * http://license.openmrs.org
 *
 * Software distributed under the License is distributed on an "AS IS"
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
 * License for the specific language governing rights and limitations
 * under the License.
 *
 * Copyright (C) OpenMRS, LLC.  All Rights Reserved.
 */
package org.openmrs.module.chartsearch.web.controller;

import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.api.context.Context;
import org.openmrs.messagesource.MessageSourceService;
import org.openmrs.module.chartsearch.api.ChartSearchService;
import org.openmrs.module.chartsearch.categories.CategoryFilter;
import org.openmrs.module.chartsearch.categories.FacetForACategoryFilter;
import org.openmrs.web.WebConstants;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.request.WebRequest;

/**
 * The main controller.
 */
@Controller
public class ChartSearchManageController {
	
	private ChartSearchService chartSearchService;
	
	protected final Log log = LogFactory.getLog(getClass());
	
	public ChartSearchService getChartSearchService() {
		return chartSearchService;
	}
	
	@RequestMapping(value = "/module/chartsearch/manageCategories", method = RequestMethod.GET)
	public void manage(ModelMap model) {
		model.addAttribute("user", Context.getAuthenticatedUser());
		
		List<FacetForACategoryFilter> facets = getChartSearchService().getAllFacets();
		Iterator<FacetForACategoryFilter> facetsIterator = facets.iterator();
		List<CategoryFilter> categories = getChartSearchService().getAllCategoryFilters();
		Iterator<CategoryFilter> categoriesIterator = categories.iterator();
		
		while (categoriesIterator.hasNext()) {
			String inBuiltCategories = "<tr>"
			        + "<td><input type='checkbox' disabled></td>"
			        + "<td><input type='text' value='"
			        + categoriesIterator.next().getCategoryName()
			        + "' disabled></td>"
			        + "<td><textarea placeholder='inbuilt categories' disabled style='width:400px; height:37px;'></textarea></td>"
			        + "<td><textarea disabled style='width:400px; height:37px;'>"
			        + categoriesIterator.next().getCategoryDescription() + "</textarea></td>" + "</tr>";
			
			/*
			 <tr>
			 	<td><input type='checkbox' disabled></td>
			 	<td><input type='text' value='' disabled></td>
			 	<td><textarea placeholder='inbuilt categories' disabled style='width:400px; height:37px;'></textarea></td>
			 	<td><textarea disabled style='width:400px; height:37px;'></textarea></td>
			 </tr>
			 * */
			
			log.debug(inBuiltCategories);
			
			//use only the first 7 category filters which are in-built
			if (categoriesIterator.next().getId() <= 7) {
				model.addAttribute("inBuiltCategories", inBuiltCategories);
			}
		}
		while (facetsIterator.hasNext()) {
			String customCategories = "<tr>" + "<td><input type='checkbox' ></td>" + "<td><input type='text' value='"
			        + facetsIterator.next().getCategoryFilter().getCategoryName() + "' ></td>"
			        + "<td><textarea placeholder='" + facetsIterator.next().getFacetQuery()
			        + "' style='width:400px; height:37px;'></textarea></td>"
			        + "<td><textarea style='width:400px; height:37px;'>"
			        + facetsIterator.next().getCategoryFilter().getCategoryDescription() + "</textarea></td>" + "</tr>";
			log.debug(customCategories);
			
			//use the rest of the available category filters
			if (facetsIterator.next().getCategoryFilter().getId() > 7) {
				model.addAttribute("customCategories", customCategories);
			}
		}
	}
	
	/*
	@RequestMapping(value = "/module/chartsearch/manageCategories.form", method = RequestMethod.POST)
	public String submitCategoryFilter(WebRequest request, HttpSession httpSession, ModelMap model,
	                                   @RequestParam(required = false, value = "action") String action,
	                                   @ModelAttribute("category") CategoryFilter category,
	                                   @ModelAttribute("facet") FacetForACategoryFilter facet, BindingResult errors) {
		
		MessageSourceService mss = Context.getMessageSourceService();
		ChartSearchService chartSearchService = Context.getService(ChartSearchService.class);
		if (!Context.isAuthenticated()) {
			errors.reject("chartsearch.auth.required");
		} else if (mss.getMessage("chartsearch.manage.category.delete").equals(action)) {
			try {
				chartSearchService.deleteACategoryFilter(category);
				httpSession.setAttribute(WebConstants.OPENMRS_MSG_ATTR, "chartsearch.manage.category.delete.success");
				return "redirect:manageCategories.form";
			}
			catch (Exception ex) {
				httpSession.setAttribute(WebConstants.OPENMRS_ERROR_ATTR, "chartsearch.manage.category.delete.failure");
				log.error("Failed to delete CategoryFilter", ex);
				return "redirect:manageCategories.form";
			}
		} else {
			chartSearchService.createACategoryFilter(category);
			httpSession.setAttribute(WebConstants.OPENMRS_MSG_ATTR, "chartsearch.manage.category.saved");
		}
		return "redirect:manageCategories.form";
	}
	*/
}
