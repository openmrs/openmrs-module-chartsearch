/**
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */
package org.openmrs.module.chartsearch.api.db;

import java.util.List;

import org.openmrs.module.chartsearch.synonyms.SynonymGroup;

@SuppressWarnings("rawtypes")
public interface SynonymGroupDAO extends SingleClassDAO {
	
	/**
	 * Retrieve synonym group by group name
	 * 
	 * @param groupName - The group name.
	 * @return the synonym group that matches the given groupName.
	 * @should not return voided group
	 */
	SynonymGroup getSynonymGroupByName(String groupName);
	
	/**
	 * Retrieve a list of synonym groups by is category.
	 * 
	 * @param isCategory - Determines if a group is a category.
	 * @return if isCategory == true => return list of synonym groups that are categories, else,
	 *         return list of groups that aren't categories.
	 * @should not return voided groups
	 */
	List<SynonymGroup> getSynonymGroupsIsCategory(boolean isCategory);
	
	/**
	 * Retrieve a count of synonym groups.
	 * 
	 * @param byIsCategory - Determine if a group is a category.
	 * @return if byIsCategory == false => return count of all groups, else, returns count for
	 *         categorized groups only.
	 * @should not return voided groups
	 */
	Integer getCountOfSynonymGroups(boolean byIsCategory);
	
}
