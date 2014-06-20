/**
 * Version 1.0 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * http://license.openmrs.org
 *
 * Software distributed under the License is distributed on an "AS IS"
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
 * License for the specific language governing rights and limitations
 * under the License.
 *
 * Copyright (C) OpenMRS, LLC. All Rights Reserved.
 */
package org.openmrs.module.chartsearch.categories;

import java.io.Serializable;

import org.openmrs.BaseOpenmrsObject;
import org.openmrs.module.chartsearch.api.ChartSearchService;
import org.openmrs.module.chartsearch.solr.ChartSearchSearcher;

/**
 * A {@link CategoryFilter} can have facet(s) to filter results, this implements
 * {@link http://wiki.apache.org/solr/SolrFacetingOverview}
 */
public class FacetForACategoryFilter extends BaseOpenmrsObject implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private Integer facetId;
	
	private String facetQuery;
	
	private String facetDescription;
	
	private String facetUuid;
	
	private ChartSearchService chartSearchService;
	
	/**
	 * The Category where the facet belongs
	 */
	private CategoryFilter categoryFilter;
	
	public ChartSearchService getChartSearchService() {
	    return chartSearchService;
    }

	public FacetForACategoryFilter(CategoryFilter categoryFilter) {
		if (categoryFilter != null) {
			setCategoryFilter(categoryFilter);
		} else {
			//set categoryFilter to "Others" is categoryFilter is not supplied
			String othersUuid = "0717136a-5c5f-4d68-b099-cbf1ad820363";
			setCategoryFilter(getChartSearchService().getACategoryFilterByItsUuid(othersUuid));
		}
	}
	
	@Override
	public Integer getId() {
		return getFacetId();
	}
	
	@Override
	public void setId(Integer facetId) {
		setFacetId(facetId);
	}
	
	public CategoryFilter getCategoryFilter() {
		return categoryFilter;
	}
	
	public void setCategoryFilter(CategoryFilter categoryFilter) {
		this.categoryFilter = categoryFilter;
	}
	
	public Integer getFacetId() {
		return facetId;
	}
	
	public void setFacetId(Integer facetId) {
		this.facetId = facetId;
	}
	
	public String getFacetQuery() {
		return facetQuery;
	}
	
	public void setFacetQuery(String facetQuery) {
		this.facetQuery = facetQuery;
	}
	
	public String getFacetDescription() {
		return facetDescription;
	}
	
	public void setFacetDescription(String facetDescription) {
		this.facetDescription = facetDescription;
	}
	
	public String getFacetUuid() {
		return facetUuid;
	}
	
	public void setFacetUuid(String facetUuid) {
		this.facetUuid = facetUuid;
	}
	
}
