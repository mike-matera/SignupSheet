package com.fatboycentral.client;

import com.fatboycentral.shared.SignupData.DataStoreEntry;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;

public class StatusBanner extends Composite implements HasClickHandlers {

	public enum Action {ADDED, REMOVED}; 

	private Label undo;
	
	// Make a permanent non-modal banner with an undo action
	public StatusBanner(Action a, DataStoreEntry e) {
		HorizontalPanel hp = new HorizontalPanel();
		initWidget(hp);
		hp.setSpacing(5);
		hp.setStylePrimaryName("undoBanner");

		String have;
		if (a == Action.ADDED) {
			have = "You have signed up for ";
		}else{
			have = "You have deleted ";
		}
		have += e.shift.job + ", " + e.shift.description;
		hp.add(new HTML(have));

		undo = new Label("Undo");
		undo.setStylePrimaryName("buttonLabel");
		hp.add(undo);
	}

	@Override
	public HandlerRegistration addClickHandler(ClickHandler handler) {
		return undo.addClickHandler(handler);
	}
}
