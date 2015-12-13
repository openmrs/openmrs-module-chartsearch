package org.openmrs.module.chartsearch.web.controller;

import org.openmrs.api.context.Context;
import org.openmrs.module.chartsearch.api.ChartSearchService;
import org.openmrs.module.chartsearch.cache.ChartSearchHistory;
import org.openmrs.module.webservices.rest.web.RequestContext;
import org.openmrs.module.webservices.rest.web.RestConstants;
import org.openmrs.module.webservices.rest.web.representation.DefaultRepresentation;
import org.openmrs.module.webservices.rest.web.representation.Representation;
import org.openmrs.module.webservices.rest.web.resource.impl.DelegatingCrudResource;
import org.openmrs.module.webservices.rest.web.annotation.Resource;
import org.openmrs.module.webservices.rest.web.resource.impl.DelegatingResourceDescription;
import org.openmrs.module.webservices.rest.web.response.ResponseException;

/*
 * The contents of this file are subject to the OpenMRS Public License
 * Version 2.0 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * http://license.openmrs.org
 *
 * Software distributed under the License is distributed on an "AS IS"
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See
 * the License for the specific language governing rights and
 * limitations under the License.
 *
 * Copyright (C) OpenHMIS.  All Rights Reserved.
 */
@Resource(name = RestConstants.VERSION_1 + "/chartSearchBookmark", supportedClass = ChartSearchHistory.class, supportedOpenmrsVersions = {"1.10.x", "1.11.*", "1.12.*", "2.0.*"})
public class ChartSearchHistoryResource extends DelegatingCrudResource<ChartSearchHistory> {

    @Override
    public ChartSearchHistory getByUniqueId(String uuid) {
        return Context.getService(ChartSearchService.class).getSearchHistoryByUuid(uuid);
    }

    @Override
    protected void delete(ChartSearchHistory chartSearchHistory, String s, RequestContext requestContext) throws ResponseException {

    }

    @Override
    public ChartSearchHistory newDelegate() {
        return new ChartSearchHistory();
    }

    @Override
    public ChartSearchHistory save(ChartSearchHistory chartSearchHistory) {
        Context.getService(ChartSearchService.class).saveSearchHistory(chartSearchHistory);
        return chartSearchHistory;
    }

    @Override
    public void purge(ChartSearchHistory chartSearchHistory, RequestContext requestContext) throws ResponseException {
        if (chartSearchHistory == null){
            return;
        }
        Context.getService(ChartSearchService.class).deleteSearchHistory(chartSearchHistory);
    }

    @Override
    public DelegatingResourceDescription getRepresentationDescription(Representation representation) {
        if (representation instanceof DefaultRepresentation) {
            DelegatingResourceDescription description = new DelegatingResourceDescription();
            description.addProperty("uuid");
            description.addProperty("searchPhrase");
            description.addProperty("defaultSearch");
            description.addProperty("patient", Representation.DEFAULT);
            description.addProperty("historyOwner", Representation.DEFAULT);
            description.addSelfLink();
            description.addLink("full", ".?v=" + RestConstants.REPRESENTATION_FULL);
            return description;
        }
        return null;

    }
}
