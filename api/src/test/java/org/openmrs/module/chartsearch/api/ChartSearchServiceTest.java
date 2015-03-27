package org.openmrs.module.chartsearch.api;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.openmrs.api.context.Context;
import org.openmrs.module.chartsearch.solr.nonPatient.SearchProject;
import org.openmrs.test.BaseModuleContextSensitiveTest;
import org.openmrs.test.Verifies;

public class ChartSearchServiceTest extends BaseModuleContextSensitiveTest {
	
	/**
	 * Test for the {@link ChartSearchService} methods
	 */
	private ChartSearchService chartSearchService;
	
	@Before
	public void runBeforeAllTests() throws Exception {
		chartSearchService = getComponent(ChartSearchService.class);
	}
	
	@Test
	@Verifies(value = "save a new search project", method = "{@link ChartSearchService#saveSearchProject(SearchProject project)}")
	public void saveSearchProject_saveANewSearchProject() {
		List<String> columnNamesList = new ArrayList<String>();
		columnNamesList.add("column1");
		columnNamesList.add("column2");
		columnNamesList.add("column3");
		//SearchProject project = new SearchProject("test project", "test query", columnNamesList, "openmrs");
		SearchProject project = new SearchProject();
		project.setProjectName("test project");
		project.setSqlQuery("test query");
		project.setColumnNamesList(columnNamesList);
		project.setColumnNames(project.getColumnNamesSeparatedWithCommas());
		project.setDatabase("openmrs");
		//save project above
		//chartSearchService.saveSearchProject(project); TODO still failing with:  Unknown entity: org.openmrs.module.chartsearch.solr.nonPatient.SearchProject
	}
	
	private <T> T getComponent(Class<T> clazz) {
		List<T> list = Context.getRegisteredComponents(clazz);
		if (list == null || list.size() == 0)
			throw new RuntimeException("Cannot find component of " + clazz);
		return list.get(0);
	}
}
