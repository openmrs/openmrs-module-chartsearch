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
