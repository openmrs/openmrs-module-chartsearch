package org.openmrs.module.chartsearch.api;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.FileUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.openmrs.api.context.Context;
import org.openmrs.module.chartsearch.solr.SolrUtils;
import org.openmrs.module.chartsearch.solr.nonPatient.SearchProject;
import org.openmrs.test.BaseModuleContextSensitiveTest;
import org.openmrs.test.Verifies;

/**
 * Manually tested and runs well, TODO: i will later on write tests since i have to do more stuffs
 * as at: {@linkplain https
 * ://github.com/openmrs/openmrs-module-allergyapi/tree/master/api/src/test/resources}
 */
@Ignore
public class ChartSearchServiceTest extends BaseModuleContextSensitiveTest {
	
	/**
	 * Test for the {@link ChartSearchService} methods
	 */
	private ChartSearchService chartSearchService;
	
	@Before
	public void runBeforeAllTests() throws Exception {
		chartSearchService = getComponent(ChartSearchService.class);
	}
	
	@Verifies(value = "save a new search project", method = "{@link ChartSearchService#saveSearchProject(SearchProject project)}")
	@Ignore
	public void saveSearchProject_saveANewSearchProject() {
		List<String> columnNamesList = new ArrayList<String>();
		columnNamesList.add("column1");
		columnNamesList.add("column2");
		columnNamesList.add("column3");
		
		SearchProject project = new SearchProject("test project", "test query", columnNamesList, "test-db");
		project.setUuid("66f0081c-93e9-4c54-ad73-caf34b104a9c");
		project.setColumnNamesList(columnNamesList);
		project.setColumnNames(project.getColumnNamesSeparatedWithCommas());
		project.setDatabase("openmrs");
		
		chartSearchService.saveSearchProject(project); //TODO still failing with:  Unknown entity: org.openmrs.module.chartsearch.solr.nonPatient.SearchProject
		
		Assert.assertNotNull(chartSearchService.getSearchProjectByUuid("66f0081c-93e9-4c54-ad73-caf34b104a9c"));
	}
	
	@Test
	public void testGetAllColumnsFromAllProjects() {
		List<SearchProject> allProjects = new ArrayList<SearchProject>();
		List<String> columnNamesList1 = new ArrayList<String>();
		List<String> columnNamesList2 = new ArrayList<String>();
		
		columnNamesList1.add("column1 p1");
		columnNamesList1.add("column2 p1");
		columnNamesList1.add("column3 p1");
		columnNamesList2.add("column1 p2");
		columnNamesList2.add("column2 p2");
		
		SearchProject proj1 = new SearchProject("test proj1", "test q1", columnNamesList1, "test db1");
		SearchProject proj2 = new SearchProject("test proj2", "test q2", columnNamesList2, "test db2");
		
		allProjects.add(proj1);
		allProjects.add(proj2);
		
		String columnsFromAllProject = "";
		
		for (int i = 0; i < allProjects.size(); i++) {
			if (!columnsFromAllProject.contains(allProjects.get(i).getColumnNames())) {
				if (i == allProjects.size() - 1) {//if on the last project
					columnsFromAllProject += allProjects.get(i).getColumnNames();
				} else {
					columnsFromAllProject += allProjects.get(i).getColumnNames() + ", ";
				}
			}
		}
		System.out.println(columnsFromAllProject);
		Assert.assertEquals("column1 p1, column2 p1, column3 p1, column1 p2, column2 p2", columnsFromAllProject);
	}
	
	private <T> T getComponent(Class<T> clazz) {
		List<T> list = Context.getRegisteredComponents(clazz);
		if (list == null || list.size() == 0)
			throw new RuntimeException("Cannot find component of " + clazz);
		return list.get(0);
	}
}
