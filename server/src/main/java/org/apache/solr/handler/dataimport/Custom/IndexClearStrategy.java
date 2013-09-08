package org.apache.solr.handler.dataimport.custom;

import java.util.Collection;
import java.util.List;

import org.openmrs.module.chartsearch.server.PatientInfo;


public interface IndexClearStrategy {
	List<PatientInfo> getPatientsToDelete(Collection<PatientInfo> patients);	
}
