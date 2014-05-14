package com.fatboycentral.server;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.LinkedList;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fatboycentral.shared.SignupData.DataStoreEntry;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.SortDirection;

public class BackupServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	
	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) {
		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();

		Key datekey = KeyFactory.createKey("BackupKeys", new Date().getTime());
		
		Query q = new Query(SignupServiceImpl.SIGNUP_KIND);
		for (Entity result : datastore.prepare(q).asIterable()) {
			Entity ne = new Entity("bk_Signup", datekey);
			ne.setPropertiesFrom(result);
			datastore.put(ne);
		}

		q = new Query(SignupServiceImpl.SCHEMA_KIND);
		for (Entity result : datastore.prepare(q).asIterable()) {
			Entity ne = new Entity("bk_Schema", datekey);
			ne.setPropertiesFrom(result);		
			datastore.put(ne);
		}
		
	}

	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		doGet(request,response);
	}

}
