package org.openmrs.module.chartsearch.api.db.hibernate;

import java.io.IOException;
import java.io.Serializable;
import java.util.Date;
import java.util.Iterator;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.common.SolrInputDocument;
import org.hibernate.EmptyInterceptor;
import org.hibernate.Transaction;
import org.hibernate.type.Type;
import org.openmrs.Obs;
import org.openmrs.module.chartsearch.Solr;

public class HibernateChartSearchInterceptor extends EmptyInterceptor {

	protected Log log = LogFactory.getLog(getClass());
	private SolrServer solr;
	/**
	 * 
	 */
	
	public HibernateChartSearchInterceptor() {
		log.info("Instantiating Chart Search interceptor");
	    solr = Solr.getInstance().getServer();
	}
	private static final long serialVersionUID = 1763887410091234878L;
	
	   @Override
	   public void onDelete(Object entity,
	                     Serializable id,
	                     Object[] state,
	                     String[] propertyNames,
	                     Type[] types) {
		   log.info("Chart Search Interceptor onDelete");
	       // do nothing
	   }

	   @Override
	   public boolean onFlushDirty(Object entity,
	                     Serializable id,
	                     Object[] currentState,
	                     Object[] previousState,
	                     String[] propertyNames,
	                     Type[] types) {	
		   log.info("Chart Search Interceptor onFlushDirty");
	       return false;
	   }
	   
	   @Override
	   public boolean onLoad(Object entity,
	                    Serializable id,
	                    Object[] state,
	                    String[] propertyNames,
	                    Type[] types) {
		   if (!isIndexable(entity)){
			   return false;
		   }

		   log.info("Chart Search Interceptor onLoad");
		   SolrInputDocument document = new SolrInputDocument();
		   Obs obs = ((Obs)entity);
		   document.addField("obs_id", obs.getId());
		   document.addField("person_id", obs.getPersonId());
		   document.addField("concept_id",  obs.getConcept().getId());
		   document.addField("location_id", obs.getLocation().getId());
		   document.addField("value_boolean", obs.getValueBoolean());
		   document.addField("value_coded", obs.getValueCoded());
		   document.addField("value_complex", obs.getValueComplex());
		   document.addField("value_datetime", obs.getValueDatetime());
		   document.addField("value_drug", obs.getValueDrug());
		   document.addField("value_numeric", obs.getValueNumeric());
		   document.addField("value_text", obs.getValueText());
		   try {
			solr.add(document);			
			solr.commit();
		} catch (SolrServerException e) {
			// TODO Auto-generated catch block
			log.error(e.getMessage(), e);
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			log.error(e.getMessage(), e);
			e.printStackTrace();
		}
	       // do nothing
	       return false;
	   }
	   
	   @Override
	   public boolean onSave(Object entity,
	                    Serializable id,
	                    Object[] state,
	                    String[] propertyNames,
	                    Type[] types) {
		   log.info("Chart Search Interceptor onSave");	      
	       return false;
	   }	   
	  
	   private boolean isIndexable(Object entity){
		   return entity instanceof Obs;
	   }

}
