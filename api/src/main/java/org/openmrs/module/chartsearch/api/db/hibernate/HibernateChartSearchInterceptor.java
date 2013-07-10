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
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrInputDocument;
import org.apache.solr.common.params.ModifiableSolrParams;
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
	
	private HashSet<Object> addList = new HashSet<Object>();
	private HashSet<Object> removeList = new HashSet<Object>();
	private HashSet<Object> loadList = new HashSet<Object>();

	protected Log log = LogFactory.getLog(getClass());

	public HibernateChartSearchInterceptor() {
		log.info("Instantiating Chart Search interceptor");
	}

	private static final long serialVersionUID = 1763887410091234878L;

	@Override
	public void onDelete(Object entity, Serializable id, Object[] state,
			String[] propertyNames, Type[] types) {
		log.info("Chart Search Interceptor onDelete");
		if (isIndexable(entity)) {
			removeList.add(entity);
		}
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
		return false;
	}

	@Override
	public boolean onLoad(Object entity, Serializable id, Object[] state,
			String[] propertyNames, Type[] types) {
		log.info("Chart Search Interceptor onLoad");
		if (isIndexable(entity)) {
			Obs obs = ((Obs) entity);
			loadList.add(entity);
		}

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
		return false;
	}

	@Override
	public void postFlush(Iterator iterator) {
	}

	/**
	 * @see org.hibernate.EmptyInterceptor#afterTransactionCompletion(org.hibernate.Transaction)
	 */
	@Override
	public void afterTransactionCompletion(Transaction tx) {
		try {
			if (tx.wasCommitted()) {
				for (Object object : loadList) {
					if (isVoided(object))
						continue;
					addList.add(object);
				}
				for (Object object : addList) {
					Solr.addToIndex(((OpenmrsObject)object).getId());
				}
				for (Object object : removeList) {
					Solr.removeFromIndex(((OpenmrsObject)object).getId());
				}
			}
		} finally {
			addList.clear();
			removeList.clear();
			loadList.clear();
		}
	}

	private boolean isIndexable(Object entity) {
		return (entity instanceof Obs);
	}

	private boolean isVoided(Object entity) {
		Obs obs = ((Obs) entity);
		return (obs.isVoided());
	}	

	
}
