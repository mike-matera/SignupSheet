package com.fatboycentral.client.event;

import com.google.gwt.event.shared.EventHandler;

public interface AppEventHandler extends EventHandler {
	public void onStateChange(AppEvent e);
}
