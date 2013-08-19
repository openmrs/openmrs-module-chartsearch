package org.apache.solr.handler.dataimport;

import static org.junit.Assert.*;

import java.io.File;
import java.net.URL;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;

import junit.framework.Assert;

import org.apache.solr.*;
import org.apache.solr.core.SolrCore;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class DataImportDaemon_TestJ4 extends SolrTestCaseJ4 {

	/*private DataImportDaemon dataImportDaemon;
	private BlockingQueue<PatientInfo> queue;
	private ExecutorService executorService;

	@BeforeClass
	public static void beforeClass() throws Exception {
		URL url = DataImportDaemon_TestJ4.class.getResource("/");
		String path = url.getFile();
		initCore("solrconfig.xml",
				"schema.xml", path);
	}

	@Before
	public void setUp() throws Exception {
		super.setUp();
		queue = new LinkedBlockingQueue<PatientInfo>();
		int id = 1;
		SolrCore core = h.getCore();
		String handlerName = "csdataimport";
		DataImporter dataImporter = new DataImporter(core, handlerName);
		dataImportDaemon = new DataImportDaemon(queue, id, dataImporter);
		executorService = Executors.newFixedThreadPool(1);
	}

	@After
	public void tearDown() throws Exception {
		

	}

	@Test
	public void testQueueContainsOneElementAfterAddingOneElement() {
		PatientInfo info = new PatientInfo(42);
		queue.add(info);

		Assert.assertTrue(queue.size() == 1);
	}

	@Test
	public void testQueueContainsElementAfterAdding() {
		PatientInfo info = new PatientInfo(42);
		queue.add(info);

		Assert.assertTrue(queue.contains(info));
	}

	@Test(timeout = 5000)
	public void testRun() {
		queue.add(new PatientInfo(7));
		Future<Boolean> future = executorService.submit(dataImportDaemon, true);
		try {
			Boolean result = future.get();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			log.error("Error generated", e);
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			log.error("Error generated", e);
		}

	}*/

}
