package com.endress.infoserve.test.pca.filter;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.NonUniqueObjectException;

import com.endress.infoserve.persistence.PersistenceContext;
import com.endress.infoserve.persistence.PersistenceException;
import com.endress.infoserve.test.pca.util.SessionContext;

public class SessionFilter implements Filter {

	private static final Log logger = LogFactory.getLog(SessionFilter.class);

	static boolean firstSession = true;

	// map probably not necessarily needed
	private static Map<String, String> sessMap = Collections.synchronizedMap(new HashMap<String, String>());

	public SessionFilter() {
		super();
	}

	// no comment any
	public void init(FilterConfig filterConfig) throws ServletException {
	}

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		HttpServletRequest httpRequest = (HttpServletRequest)request;
		String requestedUri = httpRequest.getRequestURI();
		String remoteUser = httpRequest.getRemoteUser();
		logger.debug(">>> Entry =" + requestedUri + " User=" + remoteUser);

		HttpSession session = httpRequest.getSession(true);

		SessionContext sc = (SessionContext)session.getAttribute(SessionContext.class.getName());
		if (sc == null) {
			try {
				sc = new SessionContext();

				if (remoteUser == null) {
					sc.initialize(SessionContext.HIBERNATE_CFG_XML);

					logger.debug("sessionContext and remoteUser is null");

					// if we do form login with WGT-Pages then we have to allow
					// DWR request without remoteUser, because after clicking
					// Login button a DWR request is started.
					if (requestedUri.endsWith("DWRCaller.event.dwr")) {
						logger.info("DWR request without sessionContext and remoteUser");
						chain.doFilter(request, response);
					}
					else {
						logger.error("### SessionContext without remoteUser!");
					}

					return;
				}

				sc.initByUser(remoteUser);
				session.setAttribute(SessionContext.class.getName(), sc);
			}
			catch (Exception e) {
				logger.error("Error initializing SessionContext in SessionFilter", e);
				throw new ServletException(e);
			}
		}

		if (sc != null) {
			// // keep the url parameters - only for non-DWR-calls
			// if (!httpRequest.getRequestURI().endsWith("DWRCaller.event.dwr")) {
			// sc.setParameterMap(httpRequest.getParameterMap());
			// }

			SessionContext.setInstance(sc);
			if (SessionContext.getPersistenceManager() != null) {
				synchronized (SessionContext.getPersistenceManager()) { // persman not threadsafe
					try {
						// sess.getPersistenceManager().getHibernateUtility().setConfigFileName(configFile)
						SessionContext.getPersistenceManager().attach();
					}
					catch (NonUniqueObjectException ex) {
						logger.error(ex.getEntityName() + " - " + ex.getIdentifier(), ex);
						throw ex;
					}

					try {
						chain.doFilter(request, response);
					}
					finally {
						try {
							SessionContext.getPersistenceManager().rollback();
						}
						catch (PersistenceException e) {
							logger.error("Error doing rollback in SessionFilter", e);
						}
						catch (Exception e) {
							logger.error("Error doing rollback in SessionFilter", e);
						}
						finally {
							PersistenceContext.getPersistenceManager().detach();
							SessionContext.setInstance(null);
						}
					}
				}
			}
		}
	}

	public void destroy() {
	}
}
