package com.fatboycentral.client.event;

import java.util.List;
import java.util.Map;

import com.fatboycentral.shared.SignupData;
import com.fatboycentral.shared.SignupData.Database;
import com.fatboycentral.shared.SignupData.JobStats;
import com.google.gwt.event.shared.GwtEvent;

public class AppEvent extends GwtEvent<AppEventHandler> {

	private static Type<AppEventHandler> HTYPE;

	public static Type<AppEventHandler> getType() {
		return HTYPE != null ? HTYPE : (HTYPE = new Type<AppEventHandler>());
	}
	
	public enum ViewState {EDIT, EMAIL, SIGNUPS, ORPHANS, NOCHANGE};

	public String HistoryToken = null;
	public Database Database = null;
	public boolean Redraw = false;
	public SignupData.DataStoreEntry Remove = null;
	public SignupData.DataStoreEntry Add = null;
	public boolean Reparse = false;
	public ViewState State = ViewState.NOCHANGE; 
	public List<SignupData.SchemaEntry> SchemaUpdate = null;
	public boolean SchemaCancel = false;
	public Map<String,JobStats> Statistics = null;
	
	@Override
	public com.google.gwt.event.shared.GwtEvent.Type<AppEventHandler> getAssociatedType() {
		return getType();
	}

	@Override
	protected void dispatch(AppEventHandler handler) {
		handler.onStateChange(this);
	}
}
