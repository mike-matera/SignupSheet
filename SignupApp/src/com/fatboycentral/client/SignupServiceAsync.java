package com.fatboycentral.client;

import java.util.List;

import com.fatboycentral.shared.SignupData.DataStoreEntry;
import com.fatboycentral.shared.SignupData.Database;
import com.fatboycentral.shared.SignupData.SchemaEntry;
import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * The async counterpart of <code>GreetingService</code>.
 */
public interface SignupServiceAsync {
	void fetch(AsyncCallback<Database> cb);
	void add(DataStoreEntry ent, AsyncCallback<DataStoreEntry> cb);
	void remove(DataStoreEntry ent, AsyncCallback<DataStoreEntry> cb);
	void putSchema(List<SchemaEntry> updates, String replace, AsyncCallback<String> cb);
}
