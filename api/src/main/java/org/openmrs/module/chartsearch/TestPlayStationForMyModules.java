package org.openmrs.module.chartsearch;


public class TestPlayStationForMyModules {
	private static String appendBackwardSlashBeforeMustBePassedCharacters(String str) {
		if (str.contains("'")) {
			str = str.replace("'", "\'");
		}
		if (str.contains("\"")) {
			str = str.replace("\"", "\\\"");
		}
		return str;
	}
	
	public static void main(String[] dsds) {
		System.out.println(appendBackwardSlashBeforeMustBePassedCharacters("testst's"));
	}
}
