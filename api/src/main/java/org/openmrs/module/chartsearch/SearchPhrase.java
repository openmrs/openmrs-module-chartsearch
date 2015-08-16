/**
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */
package org.openmrs.module.chartsearch;

import org.apache.commons.lang3.StringUtils;

/**
 * Phrase or Text to search a match for entered from the User Interface
 */
public class SearchPhrase {
	
	String phrase;
	
	public SearchPhrase() {
		phrase = "";
	}
	
	public SearchPhrase(String phrase) {
		if (phrase == "," || StringUtils.isBlank(phrase)) {
			this.phrase = "";
		} else {
			this.phrase = phrase;
		}
	}
	
	public String getPhrase() {
		if (phrase == null || phrase == ",") {
			return "";
		}
		return phrase;
	}
	
	public void setPhrase(String phrase) {
		this.phrase = phrase;
	}
	
}
