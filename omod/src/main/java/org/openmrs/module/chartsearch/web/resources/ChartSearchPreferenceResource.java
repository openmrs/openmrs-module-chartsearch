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
import org.openmrs.module.chartsearch.cache.ChartSearchPreference;
import org.openmrs.module.webservices.rest.web.RequestContext;
import org.openmrs.module.webservices.rest.web.RestConstants;
import org.openmrs.module.webservices.rest.web.annotation.Resource;
import org.openmrs.module.webservices.rest.web.representation.DefaultRepresentation;
import org.openmrs.module.webservices.rest.web.representation.Representation;
import org.openmrs.module.webservices.rest.web.resource.impl.DelegatingCrudResource;
import org.openmrs.module.webservices.rest.web.resource.impl.DelegatingResourceDescription;
import org.openmrs.module.webservices.rest.web.response.ResponseException;

/**
 * REST resource representing a {@link org.openmrs.module.chartsearch.cache.ChartSearchPreference}.
 */
@Resource(name = RestConstants.VERSION_1 + "/chartSearchPreference", supportedClass = ChartSearchPreference.class, supportedOpenmrsVersions = {"1.10.x", "1.11.*", "1.12.*", "2.0.*"})
public class ChartSearchPreferenceResource extends DelegatingCrudResource<ChartSearchPreference> {
    @Override
    public ChartSearchPreference getByUniqueId(String uuid) {
        return Context.getService(ChartSearchService.class).getChartSearchPreferenceByUuid(uuid);
    }

    @Override
    protected void delete(ChartSearchPreference chartSearchPreference, String s, RequestContext requestContext) throws ResponseException {
    }

    @Override
    public ChartSearchPreference newDelegate() {
        return new ChartSearchPreference();
    }

    @Override
    public ChartSearchPreference save(ChartSearchPreference chartSearchPreference) {
        Context.getService(ChartSearchService.class).saveANewChartSearchPreference(chartSearchPreference);
        return chartSearchPreference;
    }

    @Override
    public void purge(ChartSearchPreference chartSearchPreference, RequestContext requestContext) throws ResponseException {
        if (chartSearchPreference == null){
            return;
        }
        Context.getService(ChartSearchService.class).deleteChartSearchPreference(chartSearchPreference);
    }

    @Override
    public DelegatingResourceDescription getRepresentationDescription(Representation representation) {
        if (representation instanceof DefaultRepresentation) {
            DelegatingResourceDescription description = new DelegatingResourceDescription();
            description.addProperty("uuid");
            description.addProperty("enableHistory");
            description.addProperty("enableBookmarks");
            description.addProperty("enableNotes");
            description.addProperty("enableDuplicateResults");
            description.addProperty("enableMultipleFiltering");
            description.addProperty("personalNotesColors");
            description.addProperty("enableQuickSearches");
            description.addProperty("enableDefaultSearch");
            description.addProperty("preferenceOwner", Representation.DEFAULT);

            description.addSelfLink();
            description.addLink("full", ".?v=" + RestConstants.REPRESENTATION_FULL);
            return description;
        }
        return null;
    }
}
