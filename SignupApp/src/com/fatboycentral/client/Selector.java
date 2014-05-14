package com.fatboycentral.client;

import java.util.HashMap;
import java.util.Map;

import com.fatboycentral.client.event.AppEvent;
import com.fatboycentral.client.event.AppEvent.ViewState;
import com.fatboycentral.client.event.AppEventHandler;
import com.fatboycentral.shared.SignupDatabase;
import com.fatboycentral.shared.SignupData.JobStats;
import com.fatboycentral.shared.SignupData.SchemaJob;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.DeckLayoutPanel;
import com.google.gwt.user.client.ui.DeckPanel;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.ResizeComposite;
import com.google.gwt.user.client.ui.ResizeLayoutPanel;
import com.google.gwt.user.client.ui.ScrollPanel;

public class Selector extends ResizeComposite implements AppEventHandler {

	DeckLayoutPanel top = new DeckLayoutPanel();
	FlowPanel main_panel = new FlowPanel(); 
	FlowPanel admin_panel = new FlowPanel(); 
	FlowPanel jobs_panel = new FlowPanel();
	FlowPanel email_panel = new FlowPanel();
	ListBox control_list = new ListBox();
	ScrollPanel scroller = new ScrollPanel();
	ResizeLayoutPanel scrollcontainer = new ResizeLayoutPanel();
	
	DeckPanel deck = new DeckPanel();
	int showingWidget = 0;

	HashMap<String,HTML> map = new HashMap<String,HTML>();
	HashMap<String,JobStats> stats = new HashMap<String,JobStats>();
	
	HTML selected = null;
	String selection = new String();

	public Selector() {
		initWidget(top);

		main_panel.setStylePrimaryName("SelectorPanel");
		
		//
		// Admin controls
		//
		HTML view = new HTML("Admin View");
		view.setStylePrimaryName("SelectorTitle");
		admin_panel.add(view);

		control_list.setWidth("100%");
		control_list.addItem("Signups");
		control_list.addItem("Email");
		control_list.addItem("Editor");
		control_list.addItem("Orphans");
		control_list.setItemSelected(showingWidget, true);
		control_list.addChangeHandler(new ChangeHandler() {
			@Override
			public void onChange(ChangeEvent event) {
				if (showingWidget == 1 && control_list.getSelectedIndex() != 1) {
					// Test parse. Don't let the user push a bad parse!
					try {
						ClientDatabase.get().parse();
					} catch (Exception e) {
						Window.alert(e.getMessage());
						control_list.setSelectedIndex(1);
						return;
					}
				}
				AppEvent e = new AppEvent();
				if (control_list.isItemSelected(0)) {
					e.State = ViewState.SIGNUPS;
					showingWidget = 0;
				}else if (control_list.isItemSelected(1)) {
					e.State = ViewState.EMAIL;
					showingWidget = 1;
				}else if (control_list.isItemSelected(2)) {
					e.State = ViewState.EDIT;
					showingWidget = 2;
				}else{
					e.State = ViewState.ORPHANS;
					showingWidget = 3;
				}
				deck.showWidget(showingWidget);
				SignupApp.events.fireEvent(e);
			}

		});
		admin_panel.setStylePrimaryName("SelectorContents");
		admin_panel.add(control_list);		
		admin_panel.setVisible(false);
		main_panel.add(admin_panel);
		
		//
		// Email controls
		//
		FlowPanel emailopts = new FlowPanel();

		HTML actions = new HTML("Email Actions");
		actions.setStylePrimaryName("SelectorTitle");
		emailopts.add(actions);

		HTML submit = new HTML("(none)");
		submit.setStylePrimaryName("Selector");
		emailopts.add(submit);

		// 
		// Editor Controls
		//
		FlowPanel editor = new FlowPanel();

		// Submit 
		HTML schema_actions = new HTML("Schema Actions");
		schema_actions.setStylePrimaryName("SelectorTitle");
		editor.add(schema_actions);

		submit = new HTML("Submit");
		submit.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				try { 	
					AppEvent ev = new AppEvent();
					ClientDatabase db = ClientDatabase.get();
					if (db.hasOrphans()) {
						Window.alert("You cannot submit this change because there are orphans!");
						return;
					}
					ev.SchemaUpdate = db.parse();
					StringBuilder sb = new StringBuilder();
					sb.append("You are about to submit a new signup sheet to the server!\n");
					sb.append("Please make sure you:\n\n");
					sb.append("  1. Have tested your changes by going back to the signup view and looking at them.\n");
					sb.append("  2. Have fixed any orphans your changes created.\n");
					sb.append("  3. Are authorized to make this change.\n\n");
					if (! Window.confirm(sb.toString())) {
						return;
					}
					showingWidget = 0;
					SignupApp.events.fireEvent(ev);					
				} catch (Exception e) {
					Window.alert("You cannot submit a signup sheet with errors!");
				}
			}
		});
		submit.setStylePrimaryName("Selector");
		editor.add(submit);

		// Cancel
		HTML cancel = new HTML("Cancel");
		cancel.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				showingWidget = 0;
				AppEvent ev = new AppEvent();
				ev.SchemaCancel = true;
				SignupApp.events.fireEvent(ev);				
			}
		});
		cancel.setStylePrimaryName("Selector");
		editor.add(cancel);
		
		//
		// Orphan controls
		//
		FlowPanel oopts = new FlowPanel();

		actions = new HTML("Orphans Actions");
		actions.setStylePrimaryName("SelectorTitle");
		oopts.add(actions);

		submit = new HTML("(none)");
		submit.setStylePrimaryName("Selector");
		oopts.add(submit);

		//
		// Signups links
		//
		FlowPanel signups = new FlowPanel();

		view = new HTML("Jobs");
		view.setStylePrimaryName("SelectorTitle");
		signups.add(view);

		scrollcontainer.setSize("100%", "100%");
		scroller.add(jobs_panel);
		scrollcontainer.add(scroller);
		signups.add(scrollcontainer);

		deck.add(signups);
		deck.add(emailopts);
		deck.add(editor);
		deck.add(oopts);

		//
		// Setup the Deck
		//
		deck.setStylePrimaryName("SelectorContents");
		deck.setSize("100%", "100%");
		main_panel.add(deck);
		top.add(main_panel);
		top.add(new HTML());
		top.showWidget(1);
		top.setAnimationDuration(1000);
		top.setAnimationVertical(false);
	}

	private void render() {

		if (SignupApp.adminMode || SignupApp.coordinatorRole != null) {
			admin_panel.setVisible(true);
		}else{
			admin_panel.setVisible(false);
		}
		
		jobs_panel.clear();
		map.clear();
		FlowPanel f = null; 
		ClientDatabase db = ClientDatabase.get();
		for (final SchemaJob job : db.getJobs()) {			
			String[] parts = job.job.split(":");

			HTML h = new HTML();
			h.setStylePrimaryName("Selector");			
			String jobtitle = parts[0];
			if (parts.length == 2) {
				h.addStyleDependentName("SubSelect");
				jobtitle = parts[1];
			}
			
			if (stats.containsKey(job.job)) {
				JobStats s = stats.get(job.job);
				int available = s.total - s.taken;
				h.setHTML("<b>" + jobtitle + "</b>&nbsp;(" + available + ")");
			}else{
				h.setHTML(jobtitle);
			}
			
			h.addClickHandler(new ClickHandler() {
				@Override
				public void onClick(ClickEvent event) {				
					History.newItem(job.job);
				}
			});
			map.put(job.job, h);

			if (parts.length == 1) {
				if (f != null) {
					jobs_panel.add(f);
				}
				f = new FlowPanel();
			}
			f.add(h);
		}
		if (f != null) {
			jobs_panel.add(f);
		}

		// XXX: For debugging only....
		/*
		HTML h = new HTML("(become an admin)");
		h.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				History.newItem("FnFadmin");
			}
		});
		h.setStylePrimaryName("Selector");
		jobs_panel.add(h);
		*/
		
		control_list.setSelectedIndex(showingWidget);
		deck.showWidget(showingWidget);
		top.forceLayout();
		top.showWidget(0);

		Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
			@Override
			public void execute() {
				onResize();
			}
		});
	}

	@Override
	public void onStateChange(AppEvent e) {
		if (e.HistoryToken != null) {
			selection = e.HistoryToken;
			setSelection();
		}else if (e.Redraw) {
			render();
			setSelection();
		}else if (e.Statistics != null) {
			stats.clear();
			stats.putAll(e.Statistics);
		}
	}

	private void setSelection() {
		if (map.containsKey(selection)) {
			if (selected != null) {
				selected.removeStyleDependentName("selected");
			}
			selected = map.get(selection);
			selected.addStyleDependentName("selected");
		}
	}

	@Override
	public void onResize() {
		int h = getOffsetHeight() - (scroller.getAbsoluteTop() - getAbsoluteTop()) - 10;
		int w = getOffsetWidth();
		deck.setPixelSize(w-20, h);
		scroller.setPixelSize(w-20, h-10);
		super.onResize();
	}
}
