/**
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */
package org.openmrs.module.chartsearch.solr;

import java.util.LinkedList;

/**
 * Handles all behavior of interpreting the search phrase entered into the search box which we
 * return to solr as query, we are currently using Boolean search syntax.
 */
public class ChartSearchSyntax {
	
	/**
	 * Search phrase entered in the search box, e.g. "Blood Pressure"
	 */
	private String searchPhrase;
	
	/**
	 * A collection of words from the search phrase entered in the search box, e.g. "Blood" and
	 * "Pressure"
	 */
	private LinkedList<String> wordsFromSearchPhrase = new LinkedList<String>();
	
	public String getSearchPhrase() {
		return searchPhrase;
	}
	
	private LinkedList<String> getWordsFromSearchPhrase() {
		return wordsFromSearchPhrase;
	}
	
	/**
	 * The final search query after breaking down the received search phrase
	 */
	private String searchQuery = "";
	
	public String getSearchQuery() {
		return searchQuery;
	}
	
	/**
	 * To define Chart Search Syntax behavior, we must at all times have searchPhrase
	 * 
	 * @param searchPhrase
	 */
	public ChartSearchSyntax(String searchPhrase) {
		this.searchPhrase = searchPhrase;
		handleAllSearchSyntaxBehavior();
	}
	
	/**
	 * Takes search phrase and breaks it down into words: for-example. "Blood Pressure" into
	 * ["Blood", "Pressure"], then returns a linked list of the words
	 * 
	 * @param searchPhrase
	 * @return {@link #getWordsFromSearchPhrase()}
	 */
	private LinkedList<String> breakDownASearchPhraseIntoWords() {
		String searchPhrase = getSearchPhrase();
		if (!searchPhrase.equals("")) {
			String[] words = searchPhrase.split("\\s+");
			for (int i = 0; i < words.length; i++) {
				words[i] = words[i].replaceAll("[!?,]", "");
				this.wordsFromSearchPhrase.add(words[i]);
			}
		} else {
			this.wordsFromSearchPhrase.add("noSearchPhrase");
		}
		return getWordsFromSearchPhrase();
	}
	
	/**
	 * Handles all behavior required as described at: https://issues.openmrs.org/browse/CSM-25
	 */
	private void handleAllSearchSyntaxBehavior() {
		LinkedList<String> terms = breakDownASearchPhraseIntoWords();
		if (terms.isEmpty() || terms.get(0).equals("noSearchPhrase")) {
			this.searchQuery = getSearchPhrase();
		} else {
			int firstIndex = terms.indexOf(terms.getFirst());
			int lastIndex = terms.indexOf(terms.getLast());
			for (int i = firstIndex; i <= lastIndex; i++) {
				String currentWord = terms.get(i);
				if (isStringInteger(currentWord)) {
					//if integer(36) return 36 or 36.*
					if (i != lastIndex) {
						this.searchQuery += currentWord + " OR " + currentWord + ".* OR ";
					} else
						this.searchQuery += currentWord + " OR " + currentWord + ".*";
				} else if (isStringDouble(currentWord)) {
					//if double take it as it is
					if (i != lastIndex) {
						this.searchQuery += currentWord + " OR ";
					} else
						this.searchQuery += currentWord;
				} else {
					if (i != lastIndex) {
						this.searchQuery += currentWord + " OR *" + currentWord + "* OR " + currentWord + "* OR ";
					} else
						this.searchQuery += currentWord + " OR *" + currentWord + "* OR " + currentWord + "*";
				}
			}
		}
	}
	
	public boolean isStringInteger(String s) {
		try {
			Integer.parseInt(s);
		}
		catch (NumberFormatException e) {
			return false;
		}
		return true;
	}
	
	public boolean isStringDouble(String s) {
		try {
			Double.parseDouble(s);
		}
		catch (NumberFormatException e) {
			return false;
		}
		return true;
	}
	
}
