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
	
	/**
	 * Meant to handle all search syntax behavior at once and provide a final formatted phrase,
	 * designed by Cummins for Regenstrief's chartsearch<br />
	 * <br />
	 * NOT USED BY version 1.4 <br />
	 * <br />
	 * TODO Fix bug; {'fieldName:\"blood pressure\"' returns '(fieldName OR fieldName*):"blood
	 * pressure"' instead of 'fieldName:"blood pressure"'} before using this Regex otherwise support
	 * {'blood:pressure' to translate to '(blood:pressure OR blood:pressure*)'} for the currently
	 * used algorithm
	 * 
	 * @param originalSearchText
	 * @return finalSearchText
	 */
	public static String handleSeachSyntaxBehaviorUsingRegenstriefsRegex(String originalSearchText) {
		//Steps to do:
		//  1. Clean up the spaces in the query (trim and replace multiple spaces with a single space).
		//  2. Replace all commas that aren't in parentheses with " OR ".
		//	          This makes it so people can do "or'ed commas" where a search like bmp,cmp,cbc searches for bmp OR cmp OR cbc
		//  3. Replace all "and"s, "or"s, and "not"s with their upper case versions.
		//	          This makes it so people can search for cmp or cbc and we convert "or" to "OR" so we can be
		//	          sure solr knows it's an operator and not a search term.
		//  4. Replace all words that:
		//	      1. Aren't "and", "or", or "not".
		//	      2. Aren't inside quotes.
		//	      3. Aren't followed by a *
		//	     with "(word OR word*)".
		//	          This makes it so a search for word1 word2 becomes (word1 OR word1*) AND (word2 OR word2*)
		//	          The reasons it's done like this are:
		//	              1. We need to be able to automatically search with a wildcard on the end so that a word like
		//	                 "meta" matches "metabolic".
		//	              2. We need to have the original word in there so that stemming works.  Words with wildcards on
		//	                 the end in the query aren't stemmed but the words in the index are stemmed.  For example, 
		//	                 the word "metabolic" becomes "metabol".  If we don't put the original word in the query,
		//	                 and somebody searches for "metabolic", and search for "metabolic*" we won't find the match
		//	                 because metabolic* doesn't match "metabol".  But, if we leave the original word in the query, it gets
		//	                 stemmed to "metabol" and we get our match.
		//	          So, basically, this lets us match on stemming and match on wildcards without the user having to do anything.
		//  5. Replace all spaces that aren't directly preceded or followed by an "and", "or", or "not" and
		//	     aren't in quotes with " AND ".
		//	          This makes it so the "conjunction" between our words produced in step 3 is "AND".  So, if they
		//	          search for word1 word2, we get out (word1 OR word1*) AND (word2 OR word2*).  After a lot of trial and error,
		//	          I found that replacing the spaces that were between words (and not in the middle of a quote) with our conjuction 
		//	          was the best way to do it.
		
		//Clean up the spaces and turn multiple spaces into one.
		String preparedSearchQuery = originalSearchText.trim();
		preparedSearchQuery = preparedSearchQuery.replaceAll("\\s+", " ");
		
		//Replace all commas that aren't in parentheses with " OR ".
		preparedSearchQuery = preparedSearchQuery.replaceAll("\\s*,\\s*(?=([^\"]*\"[^\"]*\")*[^\"]*$)", " OR ");
		
		//There might be a smarter way to do this (with like back references) but this way is pretty straight forward.
		preparedSearchQuery = preparedSearchQuery.replaceAll("\\s+and\\s+", " AND ");
		preparedSearchQuery = preparedSearchQuery.replaceAll("\\s+or\\s+", " OR ");
		preparedSearchQuery = preparedSearchQuery.replaceAll("\\s+not\\s+", " NOT ");
		
		//Replace all the "normal" words with (word1 OR word1*)
		preparedSearchQuery = preparedSearchQuery.replaceAll(
		    "(?i)(?!and\\b|or\\b|not\\b)(\\b[^\\s]+)\\b(?=([^\"]*\"[^\"]*\")*[^\"]*$)(?!\\*)", "($1 OR $1*)");
		
		//Replace all spaces that aren't:
		//  1. preceded by and, or, or not
		//  2. followed by and, or, or not
		//  3. followed by an odd number of quotes
		//With " AND "
		//
		//(?<!AND\b|OR\b|NOT\b)\s+?(?!AND\b|OR\b|NOT\b)(?=([^\"]*\"[^\"]*\")*[^\"]*$)
		//
		//If a word is preceded or followed by an "and", "or", or "not", then it's either from the previous step or something the user actually
		//put there.  In both of those cases, we want to ignore that space.  If there's a space between two words and those words aren't in the middle
		//of quotes, then we want to put "AND" in the middle of those words (or groups of words).
		preparedSearchQuery = preparedSearchQuery.replaceAll(
		    "(?i)(?<!and\\b|or\\b|not\\b)\\s+?(?!and\\b|or\\b|not\\b)(?=([^\"]*\"[^\"]*\")*[^\"]*$)", " AND ");
		
		return preparedSearchQuery;
	}
}
