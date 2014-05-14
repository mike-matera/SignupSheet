package com.fatboycentral.client;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import org.antlr.runtime.ANTLRStringStream;
import org.antlr.runtime.CommonTokenStream;
import org.antlr.runtime.RecognitionException;
import org.antlr.runtime.tree.CommonTreeNodeStream;
import org.antlr.runtime.tree.Tree;

import com.fatboycentral.client.event.AppEventHandler;
import com.fatboycentral.client.event.AppEvent;
import com.fatboycentral.shared.SignupDatabase;
import com.fatboycentral.shared.SignupData.DataStoreEntry;
import com.fatboycentral.shared.SignupData.JobStats;
import com.fatboycentral.shared.SignupData.SchemaEntry;
import com.fatboycentral.shared.SignupData.SignupEntry;
import com.fatboycentral.shared.parser.SignupRuntime;
import com.fatboycentral.shared.parser.SingupListLexer;
import com.fatboycentral.shared.parser.SingupListParser;
import com.fatboycentral.shared.parser.SingupListParser.sheet_return;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.HasData;
import com.google.gwt.view.client.ListDataProvider;

public class ClientDatabase extends SignupDatabase implements AppEventHandler {

	private TextArea editor = new TextArea();
	private ListDataProvider<SignupEntry> dataProvider = new ListDataProvider<SignupEntry>();
	private ListDataProvider<DataStoreEntry> orphanProvider = new ListDataProvider<DataStoreEntry>();
	protected String lastJobFilter = "";

	/**
	 * The singleton instance of the database.
	 */
	private static ClientDatabase instance;
	public static ClientDatabase get() {
		if (instance == null) {
			instance = new ClientDatabase();
		}	
		return instance;
	}

	private ClientDatabase() {
		SignupApp.events.addHandler(AppEvent.getType(), this);
	}

	public void addDataDisplay(HasData<SignupEntry> display) {
		dataProvider.addDataDisplay(display);
	}


	public void addOrphanDisplay(HasData<DataStoreEntry> display) {
		orphanProvider.addDataDisplay(display);
	}

	public void setJobFilter(String job) {
		lastJobFilter = job;
		List<SignupEntry> data = dataProvider.getList();
		data.clear();
		for (SignupEntry s : signups) {
			if (s.shift.job.equals(lastJobFilter)) {
				data.add(s);
			}
		}
	}

	public Widget getEditor() {
		return editor;
	}

	private void appendEditorText(SchemaEntry ent) {
		StringBuilder bld = new StringBuilder();
		bld.append(editor.getText());
		bld.append(ent.data + "\n");
		editor.setText(bld.toString());
	}

	@Override
	public void clear(boolean all) {
		super.clear(all);
		
		// Kill providers
		dataProvider.getList().clear();
		orphanProvider.getList().clear();
	}
	
	@Override
	public void loadDbEntries(List<DataStoreEntry> new_entries) {
		super.loadDbEntries(new_entries);
		
		// Update statistics event
		AppEvent up = new AppEvent();
		up.Statistics = stats;
		SignupApp.events.fireEvent(up);
	}
	
	protected void execute(List<SchemaEntry> source, String filter, boolean append) throws RecognitionException {
		// Execute in proper order
		HashMap<String, List<SchemaEntry>> map = new HashMap<String, List<SchemaEntry>>();
		LinkedList<List<SchemaEntry>> list = new LinkedList<List<SchemaEntry>>();
		for (SchemaEntry ent : source) {
			String[] parts = ent.key.split(":");
			if (filter != null && !parts[0].equals(filter)) {
				continue;
			}
			if (map.containsKey(parts[0])) {
				if (parts.length == 1) {
					List<SchemaEntry> l = map.get(parts[0]);
					l.add(0,ent);
					list.add(l);
				}else{
					map.get(parts[0]).add(ent);
				}
			}else{
				LinkedList<SchemaEntry> l = new LinkedList<SchemaEntry>();
				l.add(ent);
				map.put(parts[0], l);
				if (parts.length == 1) {
					list.add(l);
				}
			}
		}

		for (List<SchemaEntry> ent : list) {
			for (SchemaEntry sub : ent) {
				execute(sub);
				if (append) {
					appendEditorText(sub);
				}
			}
		}
	}

	public List<SchemaEntry> parse(String schema) throws RecognitionException {
		SingupListLexer lex = new SingupListLexer(new ANTLRStringStream(schema));
		CommonTokenStream tokens = new CommonTokenStream(lex);
		SingupListParser parser = new SingupListParser(tokens);

		sheet_return tree = parser.sheet();

		if (parser.first_error != null) {
			throw new IllegalArgumentException(formatError(parser.first_error, parser.error_message, tokens));
		}		
		if (lex.first_error != null) {
			throw new IllegalArgumentException(formatError(lex.first_error, lex.error_message, tokens));
		}
		if (tree == null) {
			throw new IllegalArgumentException("Parse Failure!");
		}

		CommonTreeNodeStream nodes = new CommonTreeNodeStream((Tree)tree.getTree());
		SignupRuntime walker = new SignupRuntime(nodes); // created from TP.g

		return walker.split();
	}

	public List<SchemaEntry> parse() throws RecognitionException {
		List<SchemaEntry> rval = parse(editor.getText());
		// If this is a coordinator role, validate 
		if (SignupApp.coordinatorRole != null) {
			for (SchemaEntry ent : rval) {
				if (! ent.key.startsWith(SignupApp.coordinatorRole)) {
					throw new IllegalArgumentException("You cannot create the signup: " + ent.key 
							+ ".\n You can only only create signups that start with " + SignupApp.coordinatorRole);
				}
				if (ent.key.equals(SignupApp.coordinatorRole)) {
					// Don't let the coordinator reassign the order
					ent.order = -1;
				}
			}
		}
		return rval;
	}

	@Override
	public void onStateChange(AppEvent ev) {
		boolean redraw = false;
		if (ev.Database != null) {

			// Full Reload
			clear(true);
			editor.setText("");

			try {
				execute(ev.Database.schema, SignupApp.coordinatorRole, (SignupApp.adminMode || SignupApp.coordinatorRole != null)); 
			} catch (Exception e) {
				Window.alert(e.getMessage());
			}

			loadDbEntries(ev.Database.entries);
			entries.addAll(ev.Database.entries);

			// Reset the jobs filter to update the table
			setJobFilter(lastJobFilter); 
			redraw = true;

		} else if (ev.Reparse) {

			// Reparse text!
			try {
				List<SchemaEntry> news = parse();				
				clear(false);
				execute(news, null, false);
				loadDbEntries(entries);

				// Reset the jobs filter to update the table
				setJobFilter(lastJobFilter); 
			}catch (Exception e) {
				Window.alert(e.getMessage());
			}
			redraw = true;

		} else if (ev.Add != null) {
			// Update (partial, server) reload.
			entries.add(ev.Add);
			loadDbEntries(ev.Add);
			redraw = true;

		} else if (ev.Remove != null) {
			// Remove the entry in ev.Remove
			if (entries.remove(ev.Remove)) {
				try {
					fastMap.get(ev.Remove.shift.job).get(ev.Remove.shift.jid).entries.remove(ev.Remove);
				} catch (Exception e) {
					// Just in case some entry isn't there. 
				}
			}
			
			List<DataStoreEntry> orphans = orphanProvider.getList();
			orphans.remove(ev.Remove);
			if (orphans.size() == 0) {
				orphansPresent = false;
			}
			// Keep stats
			if (stats.containsKey(ev.Remove.shift.job)) {
				JobStats s = stats.get(ev.Remove.shift.job);
				s.taken--;
			}
			redraw = true;
		}

		if (redraw) {
			// Database is updated... better redraw
			AppEvent up = new AppEvent();
			up.Redraw = true;
			SignupApp.events.fireEvent(up);
			// Unblock the user if she's blocked
			SignupApp.unblockAccess();
		}
	}

}
