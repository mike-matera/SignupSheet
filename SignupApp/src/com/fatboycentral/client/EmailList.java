package com.fatboycentral.client;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.fatboycentral.client.event.AppEvent;
import com.fatboycentral.client.event.AppEventHandler;
import com.fatboycentral.shared.SignupDatabase;
import com.fatboycentral.shared.SignupData.DataStoreEntry;
import com.fatboycentral.shared.SignupData.Person;
import com.fatboycentral.shared.SignupData.SignupEntry;
import com.google.gwt.user.client.ui.ResizeComposite;
import com.google.gwt.user.client.ui.SimpleLayoutPanel;
import com.google.gwt.user.client.ui.TextArea;

public class EmailList extends ResizeComposite implements AppEventHandler {

	SimpleLayoutPanel panel = new SimpleLayoutPanel();
	TextArea list = new TextArea();
	
	public EmailList() {
		initWidget(panel);		
		panel.add(list);
		//list.setStylePrimaryName("");
	}
	
	@Override
	public void onResize() {
		list.setSize("90%", "90%");
	}
	
	@Override
	public void onLoad() {
		SignupApp.events.addHandler(AppEvent.getType(), this);
	}

	@Override
	public void onStateChange(AppEvent e) {
		if (e.State == AppEvent.ViewState.EMAIL) {
			if (! SignupApp.adminMode && SignupApp.coordinatorRole == null) {
				list.setText("Illegal Access");
				return;
			}
			
			list.setText("loading...");
			
			ClientDatabase db = ClientDatabase.get();

			Map<Person,Person> signups = new HashMap<Person,Person>(600);
			Map<String, HashMap<String, SignupEntry>> fastMap = db.getFastMap();
			
			if (SignupApp.coordinatorRole == null) {
				for (Entry<String, HashMap<String, SignupEntry>> ent : fastMap.entrySet()) {
					for (Entry<String, SignupEntry> signup : ent.getValue().entrySet()) {
						for (DataStoreEntry dse : signup.getValue().entries) {
							signups.put(dse.worker, dse.worker);
						}
					}
				}
			}else{				
				if (! fastMap.containsKey(SignupApp.coordinatorRole)) {
					list.setText("Invalid Coordinator Role!");
					return;
				}
				for (Entry<String, SignupEntry> signup : fastMap.get(SignupApp.coordinatorRole).entrySet()) {
					for (DataStoreEntry dse : signup.getValue().entries) {
						signups.put(dse.worker, dse.worker);
					}
				}
			}

			StringBuilder good = new StringBuilder();
			StringBuilder bad = new StringBuilder();
			for (Person p : signups.values()) {
				if (p.email.matches(JobCell.emailPattern)) {
					good.append("\"" + p.name + "\" <" + p.email + ">\n");
				}else{
					bad.append("\"" + p.name + "\" <" + p.email + ">\n");
				}
			}

			StringBuilder all = new StringBuilder();
			all.append("People with missing or invalid email addresses:\n\n");
			all.append(bad.toString());
			all.append("\nPeople with valid email addresses:\n\n");
			all.append(good.toString());
			
			list.setText(all.toString());
		}
	}
}
