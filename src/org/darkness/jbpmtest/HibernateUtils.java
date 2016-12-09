package org.darkness.jbpmtest;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

/**
 * @author Darkness
 * 
 * QQ: 893951837 Email: darkness_sky@qq.com 
 * Blog: http://depravedAngel.javaeye.com/
 * 
 * Copyright (c) 2009 by Darkness
 * 
 * @date Apr 18, 2009 9:19:25 AM
 * @version 1.0
 */
public class HibernateUtils {
	
	private static SessionFactory factory;

	private HibernateUtils() {
	}

	static {
		Configuration cfg = new Configuration().configure();
		factory = cfg.buildSessionFactory();
	}

	public static SessionFactory getSessionFactory() {
		return factory;
	}

	public static Session getSession() {
		return factory.openSession();
	}

	public static void closeSession(Session session) {
		if (session != null) {
			if (session.isOpen()) {
				session.close();
			}
		}
	}

}
