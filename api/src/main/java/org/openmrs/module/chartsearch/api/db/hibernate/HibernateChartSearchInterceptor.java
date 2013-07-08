package org.openmrs.module.chartsearch.api.db.hibernate;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Stack;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.common.SolrInputDocument;
import org.hibernate.EmptyInterceptor;
import org.hibernate.Transaction;
import org.hibernate.type.Type;
import org.openmrs.Obs;
import org.openmrs.OpenmrsObject;
import org.openmrs.api.context.Context;
import org.openmrs.module.chartsearch.Solr;
import org.openmrs.util.OpenmrsUtil;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

public class HibernateChartSearchInterceptor extends EmptyInterceptor {

	private HashSet<Object> changes = new HashSet<Object>();
	private ThreadLocal<Stack<HashSet<OpenmrsObject>>> updates = new ThreadLocal<Stack<HashSet<OpenmrsObject>>>();
	private ThreadLocal<Stack<HashSet<OpenmrsObject>>> deletes = new ThreadLocal<Stack<HashSet<OpenmrsObject>>>();

	private HashSet<Object> addList = new HashSet<Object>();
	private HashSet<Object> removeList = new HashSet<Object>();

	protected Log log = LogFactory.getLog(getClass());

	/**
	 * 
	 */

	public HibernateChartSearchInterceptor() {
		log.info("Instantiating Chart Search interceptor");
	}

	private static final long serialVersionUID = 1763887410091234878L;

	@Override
	public void onDelete(Object entity, Serializable id, Object[] state,
			String[] propertyNames, Type[] types) {
		log.info("Chart Search Interceptor onDelete");
		// do nothing
	}

	@Override
	public boolean onFlushDirty(Object entity, Serializable id,
			Object[] currentState, Object[] previousState,
			String[] propertyNames, Type[] types) {
		log.info("Chart Search Interceptor onFlushDirty");
		if (isIndexable(entity)) {
			if (isVoided(entity)) {
				removeList.add(entity);
			} else {
				addList.add(entity);
			}
		}
		
		/*
		 * if (isIndexable(entity)){
		 * updates.get().peek().add((OpenmrsObject)entity); }
		 */
		return false;
	}

	@Override
	public boolean onLoad(Object entity, Serializable id, Object[] state,
			String[] propertyNames, Type[] types) {
		if (!isIndexable(entity)) {
			return false;
		}

		log.info("Chart Search Interceptor onLoad");
		// addToIndex(entity);

		// do nothing
		return false;
	}

	@Override
	public boolean onSave(Object entity, Serializable id, Object[] state,
			String[] propertyNames, Type[] types) {
		log.info("Chart Search Interceptor onSave");
		if (isIndexable(entity)) {
			if (isVoided(entity)) {
				removeList.add(entity);
			} else {
				addList.add(entity);
			}
		}
		/*
		 * if (isIndexable(entity)) { inserts.get().peek().add((OpenmrsObject)
		 * entity); }
		 */
		/*
		 * if (!isIndexable(entity)) { return false; } addToIndex(entity);
		 */
		return false;
	}

	@Override
	public void postFlush(Iterator iterator) {
		while (iterator.hasNext()) {
			Object entity = iterator.next();
			/*if (isIndexable(entity)) {
				if (isVoided(entity)) {
					removeList.add(entity);
				} else {
					addList.add(entity);
				}
			}*/
		}
		/*
		 * try{ for (OpenmrsObject insert : inserts.get().peek()) {
		 * addToIndex(insert); }
		 * 
		 * for (OpenmrsObject update : updates.get().peek()) {
		 * addToIndex(update); }} finally { inserts.get().pop();
		 * inserts.remove(); updates.get().pop(); updates.remove(); }
		 */
	}

	/**
	 * @see org.hibernate.EmptyInterceptor#afterTransactionCompletion(org.hibernate.Transaction)
	 */
	@Override
	public void afterTransactionCompletion(Transaction tx) {
		try {
			if (tx.wasCommitted()) {
				for (Object object : addList) {
					addToIndex(object);
				}
				for (Object object : removeList) {
					removeFromIndex(object);
				}
			}
		} finally {
			addList.clear();
			removeList.clear();
		}
	}

	private boolean isIndexable(Object entity) {
		return (entity instanceof Obs);
	}

	private boolean isVoided(Object entity) {
		Obs obs = ((Obs) entity);
		return (obs.isVoided());
	}

	private void addToIndex(Object entity) {
		if (!Solr.isStarted())
			return;
		Obs obs = ((Obs) entity);
		try {
			SolrServer server = Solr.getInstance().getServer();
			
			try {
				SolrInputDocument document = new SolrInputDocument();
				document.addField("uuid", obs.getUuid());
				document.addField("obs_id", obs.getId());
				document.addField("obs_datetime", obs.getObsDatetime());
				document.addField("person_id", obs.getPersonId());
				document.addField("concept_id", obs.getConcept().getId());
				document.addField("location_id", obs.getLocation().getId());
				document.addField("value_boolean", obs.getValueBoolean());
				document.addField("value_coded", obs.getValueCoded());
				document.addField("value_complex", obs.getValueComplex());
				document.addField("value_datetime", obs.getValueDatetime());
				document.addField("value_drug", obs.getValueDrug());
				document.addField("value_numeric", obs.getValueNumeric());
				document.addField("value_text", obs.getValueText());
				try {
					server.add(document, 15000);
					//server.commit();
				} catch (SolrServerException e) {
					// TODO Auto-generated catch block
					log.error("Solr server add exception");
					log.error(e.getMessage(), e);
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					log.error("IO exception!");
					log.error(e.getMessage(), e);
					e.printStackTrace();
				}
			} catch (Exception e) {
				log.error("Enother exception");
				e.printStackTrace();

			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			log.error("Solr server exception");
			e.printStackTrace();
		}

	}

	private void removeFromIndex(Object entity) {
		if (!Solr.isStarted())
			return;
		Obs obs = ((Obs) entity);
		try {
			SolrServer server = Solr.getInstance().getServer();
			server.deleteById(obs.getUuid().toString(), 15000);
			//server.commit();
		} catch (Exception e) {
			log.error("Solr server exception");
			e.printStackTrace();
		}
	}
}
