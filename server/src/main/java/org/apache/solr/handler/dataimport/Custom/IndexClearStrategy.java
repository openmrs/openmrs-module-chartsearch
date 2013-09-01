package org.apache.solr.handler.dataimport.custom;

import java.util.Collection;
import java.util.List;


public interface IndexClearStrategy {
	List<PatientInfo> getPatientsToDelete(Collection<PatientInfo> patients);	
}
