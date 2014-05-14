package com.fatboycentral.client;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.Locale;

import com.fatboycentral.client.event.AppEvent;
import com.fatboycentral.client.event.AppEvent.ViewState;
import com.fatboycentral.client.event.AppEventHandler;
import com.fatboycentral.shared.SignupDatabase;
import com.fatboycentral.shared.SignupData.DataStoreEntry;
import com.fatboycentral.shared.SignupData.Database;
import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.cell.client.TextCell;
import com.google.gwt.cell.client.TextInputCell;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.AttachEvent;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.event.shared.SimpleEventBus;
import com.google.gwt.http.client.URL;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.DataGrid;
import com.google.gwt.user.cellview.client.IdentityColumn;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.Random;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.DeckLayoutPanel;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.PopupPanel.PositionCallback;
import com.google.gwt.user.client.ui.RootLayoutPanel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class SignupApp implements EntryPoint, AppEventHandler, ValueChangeHandler<String> {

	public static final String pageTitle = "<span class='headerText'><h1>UnScruz Volunteer Signup Sheet 2014</h1>";

	public static final boolean usePopup = false;

	public static final DateTimeFormat dateCellFormat = DateTimeFormat.getFormat("EEEE hh:mm a");

	public static EventBus events = new SimpleEventBus();
	public static boolean adminMode = false;
	public static String coordinatorRole = null;

	public static int cookieId = 0;
	public static Date cookieExpire = null;

	DeckLayoutPanel body;
	JobsTable table;
	Selector links;

	DataGrid<DataStoreEntry> orphans;
	EmailList emails = new EmailList();

	AbsolutePanel header = new AbsolutePanel();
	static HTML header_text = new HTML();

	public static SignupServiceAsync service = GWT.create(SignupService.class);
	static StatusBanner showingUndo = null;
	static HorizontalPanel undoArea = null;

	static DockLayoutPanel top;
	static LoadingPopup loading;

	public static final int WEST_SIZE = 275;
	public static final int NORTH_SIZE = 75;
	public static final int SOUTH_SIZE = 25;
	public static final int BORDER_SIZE = 40;
	public static final int TABLE_MAX_SIZE = 1000;

	HandlerRegistration historyRegistration = null;

	public class CrashBox extends DialogBox {
		public CrashBox() {
			setText("Error");
			setAnimationEnabled(true);
			setGlassEnabled(true);
			setAutoHideEnabled(false);
			setAutoHideOnHistoryEventsEnabled(false);
			setWidget(new Label("You must reload the page now."));
			historyRegistration.removeHandler();
			top.setVisible(false);
		}
	};

	interface Resources extends DataGrid.Resources {
		@Source("DataGridCustomStyle.css")
		DataGrid.Style dataGridStyle();
	}

	interface Styles extends DataGrid.Style {
		Resources INSTANCE = GWT.create(Resources.class);
	}

	public static Resources TableResources = GWT.create(Resources.class);

	// Set a timer to periodically reload the DB so it doesn't get corrupt
	static Timer reloader = new Timer() {
		@Override
		public void run() {
			if (! JobCell.isEditing() && ! SignupApp.adminMode) {
				reloadDb();
			}
		}
	};

	public static class AddCallback implements AsyncCallback<DataStoreEntry> {
		private boolean showUndo;
		public AddCallback(boolean u) {
			showUndo = u;
		}

		@Override
		public void onFailure(Throwable caught) {
			Window.alert(caught.getMessage());
			JobCell.hidePopup();
		}

		@Override
		public void onSuccess(DataStoreEntry result) {
			AppEvent ev = new AppEvent();
			ev.Add = result;
			events.fireEvent(ev);
			if (showUndo) {
				setUndo(StatusBanner.Action.ADDED, result);
			}else{
				clearUndo();
			}
		}
	}

	public static class RemoveCallback implements AsyncCallback<DataStoreEntry> {
		private boolean showUndo;
		public RemoveCallback(boolean u) {
			showUndo = u;
		}

		@Override
		public void onFailure(Throwable caught) {
			Window.alert(caught.getMessage());
			JobCell.hidePopup();
		}

		@Override
		public void onSuccess(DataStoreEntry result) {
			AppEvent ev = new AppEvent();
			ev.Remove = result;
			events.fireEvent(ev);
			if (showUndo) {
				setUndo(StatusBanner.Action.REMOVED, result);
			}else{
				clearUndo();
			}
		}
	};

	private abstract class PasswordPopup extends PopupPanel {
		Button submit = new Button("Submit");
		TextBox pwbox = new TextBox();
		CheckBox cbox = new CheckBox("I already have a ticket.");

		public PasswordPopup() {
			super(false);
			VerticalPanel vp = new VerticalPanel();
			vp.add(new Label("What's the password?"));
			vp.add(pwbox);
			vp.add(cbox);
			submit.addClickHandler(new ClickHandler() {
				@Override
				public void onClick(ClickEvent event) {
					check();
				}
			});

			pwbox.addChangeHandler(new ChangeHandler() {
				@Override
				public void onChange(ChangeEvent event) {
					check();
				}
			});

			vp.add(submit);
			vp.setCellHorizontalAlignment(submit, HasHorizontalAlignment.ALIGN_RIGHT);
			setWidget(vp);

			addAttachHandler(new AttachEvent.Handler() {
				@Override
				public void onAttachOrDetach(AttachEvent event) {
					if (event.isAttached()) {
						Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
							@Override
							public void execute() {
								pwbox.setFocus(true);
							}
						});
					}
				}
			});
		}

		private void check() {
			if (! cbox.getValue()) {
				pwbox.setFocus(true);
				return;
			}
			if (! "wwed".equals(pwbox.getText())) {
				pwbox.setFocus(true);
				return;
			}
			success();
		}

		public abstract void success();
	}

	public void onModuleLoad() {
		TableResources.dataGridStyle().ensureInjected();

		cookieExpire = new Date();
		cookieExpire.setTime(cookieExpire.getTime() + 2592000000L); // Expires in 30 days

		String cookie_string = Cookies.getCookie("user");
		if (cookie_string == null) {
			cookie_string = Integer.toString(Random.nextInt());
		}
		// Set the new expire time...
		Cookies.setCookie("user", cookie_string, cookieExpire);
		cookieId = Integer.parseInt(cookie_string);

		if (usePopup) {
			final PasswordPopup pop = new PasswordPopup() {
				@Override
				public void success() {
					hide();
					secondaryModuleLoad();
				}
			};

			pop.setPopupPositionAndShow(new PositionCallback() {
				@Override
				public void setPosition(int offsetWidth, int offsetHeight) {
					int h = Window.getClientHeight();
					int w = Window.getClientWidth();
					h = (h - offsetHeight) / 2;
					w = (w - offsetWidth) / 2;
					pop.setPopupPosition(w, h);
				}
			});
		}else{
			secondaryModuleLoad();
		}

	}

	private void secondaryModuleLoad() {
		ClientDatabase db = ClientDatabase.get();

		top = new DockLayoutPanel(Unit.PX);
		top.setStylePrimaryName("Dock");
		RootLayoutPanel.get().add(top);

		header.setStylePrimaryName("Header");
		header.add(header_text, 20, NORTH_SIZE-50);

		/*
		Button b = new Button("onResize()");
		b.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				table.onResize();
			}
		});
		header.add(b,100,50);
		 */

		undoArea = new HorizontalPanel();
		undoArea.setStylePrimaryName("undoArea");
		header.add(undoArea,-1,-1);
		top.addNorth(header, NORTH_SIZE);

		loading = new LoadingPopup(undoArea);

		HTML footer = new HTML("", true);
		top.addSouth(footer, SOUTH_SIZE);
		footer.setStylePrimaryName("Footer");

		links = new Selector();
		events.addHandler(AppEvent.getType(), links);
		top.addWest(links, WEST_SIZE);

		body = new DeckLayoutPanel() {
			@Override
			public void onResize() {
				int w = RootLayoutPanel.get().getOffsetWidth() - WEST_SIZE - BORDER_SIZE;
				if (w > TABLE_MAX_SIZE) {
					setWidth("" + TABLE_MAX_SIZE + "px");
				}else{
					setWidth("" + w + "px");
				}
				super.onResize();
			}
		};
		body.setStylePrimaryName("CenterWidget");

		table = new JobsTable();
		orphans = new DataGrid<DataStoreEntry>(50);
		body.add(table);
		Widget editor = db.getEditor();
		editor.setSize("95%", "100%");
		body.add(emails);
		body.add(editor);
		body.add(orphans);
		top.add(body);
		body.showWidget(0);

		/*----------------------
		 * Orphans!
		 */
		// Create timeslot column.
		Column<DataStoreEntry,String> orphanId = new Column<DataStoreEntry,String>(new IDCell()) {
			@Override
			public String getValue(DataStoreEntry object) {
				return Long.toString(object.id);
			}
		};
		orphanId.setFieldUpdater(new FieldUpdater<DataStoreEntry,String>() {
			@Override
			public void update(int index, DataStoreEntry object, String value) {
				service.remove(object, new RemoveCallback(true));
			}
		});

		Column<DataStoreEntry,String> orphanJob = new Column<DataStoreEntry,String>(new TextInputCell()) {
			@Override
			public String getValue(DataStoreEntry object) {
				return object.shift.job;
			}
		};
		orphanJob.setFieldUpdater(new FieldUpdater<DataStoreEntry,String>() {
			@Override
			public void update(int index, DataStoreEntry object, String value) {
				object.shift.job = value;
				service.remove(object, new RemoveCallback(false));
				service.add(object, new AddCallback(true));
			}
		});

		Column<DataStoreEntry,String> orphanDesc = new Column<DataStoreEntry,String>(new TextInputCell()) {
			@Override
			public String getValue(DataStoreEntry object) {
				return object.shift.description;
			}
		};
		orphanDesc.setFieldUpdater(new FieldUpdater<DataStoreEntry,String>() {
			@Override
			public void update(int index, DataStoreEntry object, String value) {
				object.shift.description = value;
				service.remove(object, new RemoveCallback(false));
				service.add(object, new AddCallback(true));
			}
		});

		IdentityColumn<DataStoreEntry> orphanPerson = new IdentityColumn<DataStoreEntry>(new AbstractCell<DataStoreEntry>() {
			@Override
			public void render(com.google.gwt.cell.client.Cell.Context context, DataStoreEntry value, SafeHtmlBuilder sb) {
				sb.appendHtmlConstant(value.worker.name + " <i><a target='_blank' href='mailto:" + value.worker.email
						+ "'>" + value.worker.email + "</a></i>");
			}
		});

		Column<DataStoreEntry,String> orphanComment = new Column<DataStoreEntry,String>(new TextCell()) {
			@Override
			public String getValue(DataStoreEntry object) {
				return object.worker.comment;
			}
		};

		orphans.addColumn(orphanId, "ID");
		orphans.addColumn(orphanJob, "Job");
		orphans.addColumn(orphanDesc, "Slot");
		orphans.addColumn(orphanPerson, "Name/Email");
		orphans.addColumn(orphanComment, "Comment");

		db.addOrphanDisplay(orphans);

		historyRegistration = History.addValueChangeHandler(this);
		events.addHandler(AppEvent.getType(), this);
		events.addHandler(AppEvent.getType(), table);

		reloader.scheduleRepeating(600000);

		// Now that we've setup our listener, fire the initial history state.
		// Also... load the data
		if ("".equals(History.getToken()) ) {
			if (db.getJobs().length > 0) {
				History.newItem(db.getJobs()[0].job, false);
			}
		}
		History.fireCurrentHistoryState();

		// Get initial data...
		reloadDb();
	}

	public static void reloadDb() {
		showLoading();
		service.fetch(new AsyncCallback<Database>() {
			@Override
			public void onFailure(Throwable caught) {
				Window.alert("Error");
				hideLoading();
				reloader.cancel();
			}

			@Override
			public void onSuccess(Database result) {
				AppEvent ev = new AppEvent();
				ev.Database = result;
				events.fireEvent(ev);
				Widget preload = RootPanel.get("preload");
				if (preload.isVisible()) {
					preload.setVisible(false);
					header_text.setHTML(pageTitle);
				}
				hideLoading();
			}
		});
	}

	private static void clearUndo() {
		if (showingUndo != null) {
			undoArea.remove(showingUndo);
		}
	}

	private static void setUndo(final StatusBanner.Action action, final DataStoreEntry ent) {
		clearUndo();
		showingUndo = new StatusBanner(action, ent);
		showingUndo.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				if (action == StatusBanner.Action.ADDED) {
					service.remove(ent, new RemoveCallback(false));
				}else{
					service.add(ent, new AddCallback(false));
				}
			}
		});
		undoArea.add(showingUndo);
		undoArea.setCellHorizontalAlignment(showingUndo, HasHorizontalAlignment.ALIGN_CENTER);
		undoArea.setCellVerticalAlignment(showingUndo, HasVerticalAlignment.ALIGN_BOTTOM);
	}

	private String renderAdminLinks(String historyToken) {
		String rval;
		rval = "<br><a href='#role="
				+ xform(historyToken)
				+ "'>Admin Link</a>";
		return rval;
	}

	@Override
	public void onStateChange(AppEvent e) {
		ClientDatabase db = ClientDatabase.get();
		if (e.HistoryToken != null) {
			String desc = db.getJobDescription(e.HistoryToken);
			if (adminMode) {
				desc += renderAdminLinks(e.HistoryToken);
				/*
				desc += "<br><a href='#role="
						+ xform(e.HistoryToken)
						+ "'>Admin Link</a>";
				 */
			}
			table.setDescription(desc);
			db.setJobFilter(e.HistoryToken);
			deferredResize();
		}else if (e.Redraw) {
			// Check for forgers!
			if (coordinatorRole != null) {
				if (! ClientDatabase.get().hasPrimaryJob(coordinatorRole)) {
					final CrashBox fuckoff = new CrashBox();
					fuckoff.setPopupPositionAndShow(new PositionCallback() {
						@Override
						public void setPosition(int offsetWidth, int offsetHeight) {
							int w = (Window.getClientWidth() - offsetWidth) / 2;
							int h = (Window.getClientHeight() - offsetHeight) / 2;
							fuckoff.setPopupPosition(w, h);
						}
					});
					return;
				}
				if ("".equals(History.getToken())) {
					History.newItem(coordinatorRole, false);
				}
			}

			// For initial load
			if ("".equals(History.getToken())) {
				if (ClientDatabase.get().getJobs().length > 0) {
					String deftoken = ClientDatabase.get().getJobs()[0].job;
					History.newItem(deftoken);
				}
			}
			String desc = db.getJobDescription(History.getToken());
			if (adminMode) {
				desc += renderAdminLinks(History.getToken());
				/*
				desc += "<br><a href='#role="
						+ xform(History.getToken())
						+ "'>Admin Link</a>";
				 */
			}
			table.setDescription(desc);
			table.table.redraw();
			orphans.redraw();
			deferredResize();
		}else if (e.State != ViewState.NOCHANGE) {
			if (e.State == ViewState.SIGNUPS) {
				// Reload the database by parsing the text
				AppEvent new_event = new AppEvent();
				new_event.Reparse = true;
				events.fireEvent(new_event);
				body.showWidget(0);
			}else if (e.State == ViewState.EMAIL) {
				body.showWidget(1);
			}else if (e.State == ViewState.EDIT) {
				body.showWidget(2);
			}else{
				// Reload the database by parsing the text
				AppEvent new_event = new AppEvent();
				new_event.Reparse = true;
				events.fireEvent(new_event);
				body.showWidget(3);
			}
			deferredResize();
		}else if (e.SchemaUpdate != null) {
			body.showWidget(0);
			showLoading();
			service.putSchema(e.SchemaUpdate, coordinatorRole, new AsyncCallback<String> () {
				@Override
				public void onFailure(Throwable caught) {
					StringBuilder message = new StringBuilder();
					message.append("Oh Snap!\n");
					message.append("The program was not able to apply your changes because someone else made conflicting edits.\n\n");
					message.append("The signupsheet will reload. Your changes have been lost. Sorry.\n\n");
					message.append(caught.getMessage());
					Window.alert(message.toString());
					reloadDb();
					showLoading();
				}

				@Override
				public void onSuccess(String result) {
					reloadDb();
				}
			});
		}else if (e.SchemaCancel) {
			body.showWidget(0);
			reloadDb();
		}
	}

	@Override
	public void onValueChange(final ValueChangeEvent<String> event) {
		String token = event.getValue();
		if ("Forever".equals(token)) {
			adminMode = true;
			reloader.cancel();
			if (ClientDatabase.get().getJobs().length > 0) {
				String deftoken = ClientDatabase.get().getJobs()[0].job;
				History.newItem(deftoken, false);
				token = deftoken;
			}
			reloadDb();
		}else if (token.startsWith("role=")) {
			String role = token.substring(5);
			coordinatorRole = mrofx(role);
			reloader.cancel();
			if (ClientDatabase.get().getJobs().length > 0) {
				History.newItem(coordinatorRole, false);
				token = coordinatorRole;
			}
			reloadDb();
		}
		AppEvent e = new AppEvent();
		e.HistoryToken = token;
		events.fireEvent(e);
	}

	public static void unblockAccess() {
		// Clear any modal "loading" popups
		JobCell.hidePopup();
		hideLoading();
	}

	private static class LoadingPopup extends PopupPanel {
		public Widget parent;
		public LoadingPopup(Widget p) {
			super(false, true);
			parent = p;
			setStylePrimaryName("loadingPopup");
			setWidget(new HTML("Loading..."));
		}
	};

	public static void showLoading() {
		loading.setPopupPositionAndShow(new PopupPanel.PositionCallback() {
			public void setPosition(int offsetWidth, int offsetHeight) {
				int left;
				int top;
				if (loading.parent.isVisible()) {
					left = (loading.parent.getAbsoluteLeft() + loading.parent.getOffsetWidth() - offsetWidth)/2;
					top = (loading.parent.getAbsoluteTop() + loading.parent.getOffsetHeight() - offsetHeight);
				}else{
					left = Window.getClientWidth()/2;
					top = Window.getClientHeight()/2;
				}
				loading.setPopupPosition(left, top);
			}
		});
	}

	public static void hideLoading() {
		if (loading.isShowing()) {
			loading.hide();
		}
	}

	private void deferredResize() {
		Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
			@Override
			public void execute() {
				body.onResize();
			}
		});
	}

	private static byte[] __table = {
		32,48,49,50,51,52,53,54,55,56,57,65,66,67,68,69,70,71,72,73,74,75,76,77,78,79,80,81,82,83,84,85,86,87,88,89,90,97,98,99,100,101,102,103,104,105,106,107,108,109,110,111,112,113,114,115,116,117,118,119,120,121,122,
	};

	private static byte __lookup(byte c) {
		for (int i=0; i<__table.length; i++) {
			if (__table[i] == c) {
				return (byte) i;
			}
		}
		return 0;
	}

	public static String xform(String role) {
		try {
			byte[] role_bytes = role.getBytes("UTF-8");
			byte[] obfuscated_bytes = new byte[role_bytes.length];
			int seed = 13;

			for (int i=0; i<role_bytes.length; i++) {
				obfuscated_bytes[i] = __table[(__lookup(role_bytes[i]) + seed) % __table.length];
				seed++;
			}

			return URL.encodeQueryString(new String(obfuscated_bytes));
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "fuck";
		}
	}

	public static String mrofx(String obfuscated) {
		try {
			byte[] obfuscated_bytes = obfuscated.getBytes("UTF-8");
			byte[] role_bytes = new byte[obfuscated_bytes.length];
			int seed = 13;

			for (int i=0; i<obfuscated_bytes.length; i++) {
				role_bytes[i] = __table[(__lookup(obfuscated_bytes[i]) - seed + __table.length) % __table.length];
				seed++;
			}

			return new String(role_bytes);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "fuck";
		}
	}
}
