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
import org.openmrs.module.chartsearch.cache.ChartSearchNote;
import org.openmrs.module.webservices.rest.web.RequestContext;
import org.openmrs.module.webservices.rest.web.RestConstants;
import org.openmrs.module.webservices.rest.web.annotation.Resource;
import org.openmrs.module.webservices.rest.web.representation.DefaultRepresentation;
import org.openmrs.module.webservices.rest.web.representation.Representation;
import org.openmrs.module.webservices.rest.web.resource.impl.DelegatingCrudResource;
import org.openmrs.module.webservices.rest.web.resource.impl.DelegatingResourceDescription;
import org.openmrs.module.webservices.rest.web.response.ResponseException;

/**
 * REST resource representing a {@link ChartSearchNote}.
 */
@Resource(name = RestConstants.VERSION_1 + "/chartSearchNote", supportedClass = ChartSearchBookmark.class, supportedOpenmrsVersions = {"1.10.x", "1.11.*", "1.12.*", "2.0.*"})

public class ChartSearchNoteResource extends DelegatingCrudResource<ChartSearchNote> {
    @Override
    public ChartSearchNote getByUniqueId(String uuid) {
        return Context.getService(ChartSearchService.class).getSearchNoteByUuid(uuid);
    }

    @Override
    protected void delete(ChartSearchNote chartSearchNote, String s, RequestContext requestContext) throws ResponseException {

    }

    @Override
    public void purge(ChartSearchNote chartSearchNote, RequestContext requestContext) throws ResponseException {

    }

    @Override
    public ChartSearchNote newDelegate() {
        return new ChartSearchNote();
    }

    @Override
    public ChartSearchNote save(ChartSearchNote chartSearchNote) {
        Context.getService(ChartSearchService.class).saveSearchNote(chartSearchNote);
        return chartSearchNote;
    }

    @Override
    public DelegatingResourceDescription getRepresentationDescription(Representation representation) {

        if (representation instanceof DefaultRepresentation) {
            DelegatingResourceDescription description = new DelegatingResourceDescription();
            description.addProperty("uuid");
            description.addProperty("comment");
            description.addProperty("searchPhrase");
            description.addProperty("patient", Representation.DEFAULT);
            description.addProperty("noteOwner", Representation.DEFAULT);
            description.addProperty("displayColor");

            description.addSelfLink();
            description.addLink("full", ".?v=" + RestConstants.REPRESENTATION_FULL);
            return description;
        }

        return null;
    }
}
