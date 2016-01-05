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

package org.openmrs.module.chartsearch.web.resources;

import org.openmrs.api.context.Context;
import org.openmrs.module.chartsearch.api.ChartSearchService;
import org.openmrs.module.chartsearch.cache.ChartSearchBookmark;
import org.openmrs.module.webservices.rest.web.RequestContext;
import org.openmrs.module.webservices.rest.web.RestConstants;
import org.openmrs.module.webservices.rest.web.annotation.Resource;
import org.openmrs.module.webservices.rest.web.representation.DefaultRepresentation;
import org.openmrs.module.webservices.rest.web.representation.Representation;
import org.openmrs.module.webservices.rest.web.resource.impl.DelegatingCrudResource;
import org.openmrs.module.webservices.rest.web.resource.impl.DelegatingResourceDescription;
import org.openmrs.module.webservices.rest.web.response.ResponseException;

/**
 * REST resource representing a {@link ChartSearchBookmark}.
 */
@Resource(name = RestConstants.VERSION_1 + "/chartSearchBookmark", supportedClass = ChartSearchBookmark.class, supportedOpenmrsVersions = {"1.10.x", "1.11.*", "1.12.*", "2.0.*"})
public class ChartSearchBookmarkResource extends DelegatingCrudResource<ChartSearchBookmark> {
    @Override
    public ChartSearchBookmark getByUniqueId(String uuid) {
        return Context.getService(ChartSearchService.class).getSearchBookmarkByUuid(uuid);
    }

    @Override
    protected void delete(ChartSearchBookmark chartSearchBookmark, String s, RequestContext requestContext) throws ResponseException {
    }

    @Override
    public ChartSearchBookmark newDelegate() {
        return new ChartSearchBookmark();
    }

    @Override
    public ChartSearchBookmark save(ChartSearchBookmark chartSearchBookmark) {
        Context.getService(ChartSearchService.class).saveSearchBookmark(chartSearchBookmark);
        return chartSearchBookmark;
    }

    @Override
    public void purge(ChartSearchBookmark chartSearchBookmark, RequestContext requestContext) throws ResponseException {
        if (chartSearchBookmark == null){
            return;
        }
        Context.getService(ChartSearchService.class).deleteSearchBookmark(chartSearchBookmark);
    }

    @Override
    public DelegatingResourceDescription getRepresentationDescription(Representation representation) {
        if (representation instanceof DefaultRepresentation) {
            DelegatingResourceDescription description = new DelegatingResourceDescription();
            description.addProperty("uuid");
            description.addProperty("bookmarkName");
            description.addProperty("searchPhrase");
            description.addProperty("defaultSearch");
            description.addProperty("patient", Representation.DEFAULT);
            description.addProperty("bookmarkOwner", Representation.DEFAULT);
            description.addProperty("selectedCategories");

            description.addSelfLink();
            description.addLink("full", ".?v=" + RestConstants.REPRESENTATION_FULL);
            return description;
        }
        return null;

    }
}
