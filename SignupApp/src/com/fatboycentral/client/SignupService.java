package com.fatboycentral.client;

import java.util.List;

import com.fatboycentral.shared.SignupData.DataStoreEntry;
import com.fatboycentral.shared.SignupData.Database;
import com.fatboycentral.shared.SignupData.SchemaEntry;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 * The client side stub for the RPC service.
 */
@RemoteServiceRelativePath("action")
public interface SignupService extends RemoteService {
	
	Database fetch();
	DataStoreEntry add(DataStoreEntry ent) throws IllegalArgumentException;
	DataStoreEntry remove(DataStoreEntry ent) throws IllegalArgumentException;
	String putSchema(List<SchemaEntry> updates, String replace) throws IllegalArgumentException;
}
