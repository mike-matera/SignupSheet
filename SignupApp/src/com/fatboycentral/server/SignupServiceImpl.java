package com.fatboycentral.server;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import com.fatboycentral.client.SignupService;
import com.fatboycentral.server.diff_match_patch.Diff;
import com.fatboycentral.server.diff_match_patch.Patch;
import com.fatboycentral.shared.SignupData.DataStoreEntry;
import com.fatboycentral.shared.SignupData.Database;
import com.fatboycentral.shared.SignupData.SchemaEntry;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.SortDirection;
import com.google.appengine.api.datastore.Text;
import com.google.appengine.api.datastore.Transaction;

// XXX: FIXME: Make a full schema reload that deletes all previous entries!!!!!

/**
 * The server side implementation of the RPC service.
 */
@SuppressWarnings("serial")
public class SignupServiceImpl extends RemoteServiceServlet implements SignupService {

	public static final String ROOT_KIND = "SignupRoot";
	public static final String SIGNUP_KIND = "Signup";
	public static final String KEY_KIND = "KeyKind";
	public static final String SCHEMA_KIND = "Schema";
	public static final long ROOT_VERSION = 3L;

	/*
	public static Key checkRoot(DatastoreService datastore) {
		// Check that the root entity exists;
		Key rootkey = KeyFactory.createKey(ROOT_KIND, ROOT_VERSION);
		try {
			datastore.get(rootkey);
		} catch (EntityNotFoundException e) {
			datastore.put(new Entity(rootkey));
		}
		return rootkey;
	}
	 */

	private static Key signupEntityKey(DatastoreService datastore, DataStoreEntry e) {
		Key rval = KeyFactory.createKey(KEY_KIND, new String(e.shift.job + e.shift.jid).hashCode());
		try {
			datastore.get(rval);
		} catch (EntityNotFoundException ex) {
			datastore.put(new Entity(rval));
		}
		return rval;
	}

	private static Key schemaKey(DatastoreService datastore) {
		Key rval = KeyFactory.createKey(KEY_KIND, "RootKey");
		try {
			datastore.get(rval);
		} catch (EntityNotFoundException ex) {
			datastore.put(new Entity(rval));
		}
		return rval;
	}

	private static DataStoreEntry dataStoreEntry(Entity result) {
		DataStoreEntry e = new DataStoreEntry();
		e.id = result.getKey().getId();
		e.shift.jid = (String) result.getProperty("jid");
		e.shift.master = (String) result.getProperty("master");
		e.shift.job = (String) result.getProperty("job");
		e.shift.description = (String) result.getProperty("description");
		e.shift.instructions = ((Text) result.getProperty("instructions")).getValue();
		e.shift.adminOnly = (Boolean) result.getProperty("adminOnly");
		e.worker.name = (String) result.getProperty("name");
		e.worker.email = (String) result.getProperty("email");
		e.worker.url = (String) result.getProperty("url");
		e.worker.comment = (String) result.getProperty("comment");
		e.worker.cookie = (Long) result.getProperty("cookie");
		return e;
	}

	private static Entity dataStoreEntry(DatastoreService datastore, DataStoreEntry e) {
		// Overload safety... each signup should have a parent key so that I can do
		// transactions on them. There should be lots of parent keys so that I don't slow
		// down the database.
		Key txn_key = signupEntityKey(datastore, e);
		Entity ent = new Entity(SIGNUP_KIND, txn_key);
		dataStoreEntry(ent,e);
		return ent;
	}

	private static void dataStoreEntry(Entity ent, DataStoreEntry e) {
		ent.setProperty("jid", e.shift.jid);
		ent.setProperty("master", e.shift.master);
		ent.setProperty("job", e.shift.job);
		ent.setProperty("description", e.shift.description);
		ent.setProperty("instructions", new Text(e.shift.instructions));
		ent.setProperty("name", e.worker.name);
		ent.setProperty("email", e.worker.email);
		ent.setProperty("url", e.worker.url);
		ent.setProperty("comment", e.worker.comment);
		ent.setProperty("adminOnly", e.shift.adminOnly);
		ent.setProperty("cookie", e.worker.cookie);
	}

	private static SchemaEntry schemaEntry(Entity result) {
		SchemaEntry e = new SchemaEntry();
		e.key = result.getKey().getName();
		e.order = ((Long) result.getProperty("order")).intValue();
		e.data = ((Text) result.getProperty("text")).getValue();
		return e;
	}

	private static void schemaEntry(Entity ent, SchemaEntry e) {
		// Key must already be set...
		if (e.order >= 0) {
			ent.setProperty("order", new Integer(e.order));
		}
		ent.setProperty("text", new Text(e.data));
	}

	private static Entity schemaEntry(SchemaEntry e, Key schema_key) {
		// Create key
		Entity ent = new Entity(KeyFactory.createKey(schema_key, SCHEMA_KIND, e.key));
		ent.setProperty("order", new Integer(e.order));
		ent.setProperty("text", new Text(e.data));
		return ent;
	}

	public Database fetch() {
		return doFetch();
	}

	public static Database doFetch() {
		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();

		Database rval = new Database();
		rval.entries = new LinkedList<DataStoreEntry>();
		rval.schema = new LinkedList<SchemaEntry>();

		Query q = new Query(SIGNUP_KIND);
		for (Entity result : datastore.prepare(q).asIterable()) {
			rval.entries.add(dataStoreEntry(result));
		}
		q = new Query(SCHEMA_KIND);
		q.addSort("order", SortDirection.ASCENDING);
		for (Entity result : datastore.prepare(q).asIterable()) {
			rval.schema.add(schemaEntry(result));
		}
		return rval;
	}

	public DataStoreEntry add(DataStoreEntry e) throws IllegalArgumentException {
		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
		Transaction txn = datastore.beginTransaction();
		Entity signup = null;

		try {
			// Create Signup
			signup = dataStoreEntry(datastore, e);

			// Now validate signup count...
			Query q = new Query(SIGNUP_KIND, signup.getParent());
			FetchOptions limit = FetchOptions.Builder.withLimit(100);
			if (e.shift.slots <= datastore.prepare(q).countEntities(limit)) {
				txn.commit();
				throw new IllegalArgumentException("Oh no: Someone got there first!");
			}

			// There are available slots!
			datastore.put(signup);

			txn.commit();

		} finally {
			if (txn.isActive()) {
				txn.rollback();
				throw new IllegalArgumentException("Whoa! The Datastore is too busy. Please try again.");
			}
		}

		if (signup != null) {
			// Log must happen outside of the transaction.
			Entity log = new Entity("Log");
			log.setProperty("date", new Date());
			log.setProperty("action", "ADD");
			log.setProperty("id", signup.getKey().getId());
			log.setPropertiesFrom(signup);
			datastore.put(log);
		}

		return dataStoreEntry(signup);
	}

	public DataStoreEntry remove(DataStoreEntry e) throws IllegalArgumentException {
		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
		Transaction txn = datastore.beginTransaction();

		Entity todie = null;

		try {
			// Fetch
			Key key = KeyFactory.createKey(signupEntityKey(datastore, e), SIGNUP_KIND, e.id);
			try {
				todie = datastore.get(key);
			} catch (EntityNotFoundException ex) {
				// System.out.println("Shit!");
				// Not really an error here...
				txn.commit();
				return e;
			}

			// Remove
			datastore.delete(key);
			txn.commit();

		} finally {
			if (txn.isActive()) {
				txn.rollback();
				throw new IllegalArgumentException("Whoa! The Datastore is too busy. Please try again.");
			}
		}

		// Log
		if (todie != null) {
			Entity log = new Entity("Log");
			log.setProperty("date", new Date());
			log.setProperty("action", "DELETE");
			log.setProperty("id", todie.getKey().getId());
			log.setPropertiesFrom(todie);
			datastore.put(log);
		}
		return e;
	}

	public String putSchema(List<SchemaEntry> updates, String replace) {
		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
		Key schema_key = schemaKey(datastore);
		Transaction txn = datastore.beginTransaction();

		Database rval = new Database();
		rval.entries = new LinkedList<DataStoreEntry>();
		rval.schema = new LinkedList<SchemaEntry>();

		try {

			// Kill the replace list
			for (Entity e : datastore.prepare(new Query(SCHEMA_KIND, schema_key)).asIterable()) {
				if (replace != null) {
					String schemakey = e.getKey().getName();
					if (schemakey.startsWith(replace)) {
						datastore.delete(e.getKey());
					}
				}else{
					datastore.delete(e.getKey());
				}
			}

			for (SchemaEntry update : updates) {

				// Fetch
				Entity schEnt = null;
				try {
					schEnt = datastore.get(KeyFactory.createKey(schema_key, SCHEMA_KIND, update.key));
					schemaEntry(schEnt, update); // modify
				} catch (EntityNotFoundException e) {
					schEnt = schemaEntry(update, schema_key); // create
				}

				// Put
				datastore.put(schEnt);

			}

			txn.commit();

		} finally {
			if (txn.isActive()) {
				txn.rollback();
				throw new IllegalArgumentException("TRANSACTION FAILURE!");
			}
		}

		for (SchemaEntry update : updates) {
			Entity log = new Entity("Log");
			log.setProperty("date", new Date());
			log.setProperty("action", "SCHEMA");
			log.setProperty("title", update.key);
			log.setProperty("text", new Text(update.data));
			datastore.put(log);
		}

		return "okay!";
	}
}