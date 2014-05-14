package com.fatboycentral.shared;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import org.antlr.runtime.ANTLRStringStream;
import org.antlr.runtime.CommonTokenStream;
import org.antlr.runtime.RecognitionException;
import org.antlr.runtime.tree.CommonTreeNodeStream;
import org.antlr.runtime.tree.Tree;

import com.fatboycentral.shared.SignupData.DataStoreEntry;
import com.fatboycentral.shared.SignupData.JobStats;
import com.fatboycentral.shared.SignupData.SchemaEntry;
import com.fatboycentral.shared.SignupData.SchemaJob;
import com.fatboycentral.shared.SignupData.SignupEntry;
import com.fatboycentral.shared.parser.SignupRuntime;
import com.fatboycentral.shared.parser.SingupListLexer;
import com.fatboycentral.shared.parser.SingupListParser;
import com.fatboycentral.shared.parser.SingupListParser.sheet_return;

public class SignupDatabase {

	protected LinkedList<SignupEntry> signups = new LinkedList<SignupEntry>();
	protected List<SchemaJob> jobs = new ArrayList<SchemaJob>();
	protected List<DataStoreEntry> entries = new LinkedList<DataStoreEntry>();
	protected boolean orphansPresent = false;
	protected HashMap<String, HashMap<String,SignupEntry>> fastMap = new HashMap<String, HashMap<String,SignupEntry>>();
	
	protected HashMap<String, JobStats> stats = new HashMap<String, JobStats>();
	
	private String schemaText = ""; 

	public SignupDatabase() {
	}

	public void addJob(String j, String d) {
		SchemaJob sj = new SchemaJob();
		sj.job = j;
		sj.description = d;
		jobs.add(sj);
		stats.put(sj.job, new JobStats());
	}

	public SchemaJob[] getJobs() {
		return jobs.toArray(new SchemaJob[] {});
	}
	
	public DataStoreEntry[] getEntries() {
		return entries.toArray(new DataStoreEntry[] {});
	}

	public SignupEntry[] getSignups() {
		return signups.toArray(new SignupEntry[] {});
	}

	public boolean hasPrimaryJob(String job) {
		for (SchemaJob j : jobs) {
			if (j.job.equals(job)) {
				return true;
			}
		}
		return false;
	}
	
	public String getJobDescription(String job) {
		for (SchemaJob j : jobs) {
			if (j.job.equals(job)) {
				return j.description;
			}
		}
		return "";
	}

	public void add(SignupEntry e) {
		// Add to DB
		signups.add(e);

		// Keep stats
		if (stats.containsKey(e.shift.job)) {
			JobStats s = stats.get(e.shift.job);
			s.total += e.slots;
		}
			
		// Index the fastMap
		if (!fastMap.containsKey(e.shift.job)) {
			fastMap.put(e.shift.job, new HashMap<String,SignupEntry>());
		}
		HashMap<String,SignupEntry> ent = fastMap.get(e.shift.job);
		ent.put(e.shift.jid, e);
	}

	public void clear(boolean all) {
		// Clear the Database
		for (SignupEntry e : signups) {
			e.entries.clear();
		}
		signups.clear();
		jobs.clear();

		if (all) {
			entries.clear();
		}

		// Kill the caches
		fastMap.clear();

		orphansPresent = false;
	}

	public String getLoadedSchema() {
		return schemaText;
	}

	protected void loadDbEntries(DataStoreEntry new_entry) {
		List<DataStoreEntry> l = new ArrayList<DataStoreEntry>(1);
		l.add(new_entry);
		loadDbEntries(l);
	}

	public void loadDbEntries(List<DataStoreEntry> new_entries) {
		//List<DataStoreEntry> orphans = orphanProvider.getList();
		for (DataStoreEntry e : new_entries) {
			if (fastMap.containsKey(e.shift.job) && fastMap.get(e.shift.job).containsKey(e.shift.jid)) {
				SignupEntry el = fastMap.get(e.shift.job).get(e.shift.jid);
				el.entries.add(e);

				// Keep stats
				if (stats.containsKey(e.shift.job)) {
					JobStats s = stats.get(e.shift.job);
					s.taken++;
				}
				
				/* XXX: Fix me or nix orphans!
			}else{
				if (SignupSheet.coordinatorRole != null) {
					// Suppress orphans not in this role
					String role = e.shift.job.split(":")[0];
					if (SignupSheet.coordinatorRole.equals(role)) {
						// Orphan!
						orphans.add(e);
						orphansPresent = true;
					}
				}
				 */
			}
		}
	}

	public void execute(List<SchemaEntry> ent) throws RecognitionException {
		for (SchemaEntry e : ent) {
			execute(e);
		}
	}

	public void execute(SchemaEntry ent) throws RecognitionException {
		SingupListLexer lex = new SingupListLexer(new ANTLRStringStream(ent.data));
		CommonTokenStream tokens = new CommonTokenStream(lex);
		SingupListParser parser = new SingupListParser(tokens);

		sheet_return tree = parser.sheet();
		if (tree == null) {
			throw new IllegalArgumentException("Parse Failure!");
		}

		//System.out.println(((CommonTree) tree.getTree()).toStringTree());
		
		if (parser.first_error != null) {
			throw new IllegalArgumentException(formatError(parser.first_error, parser.error_message, tokens));
		}
		if (lex.first_error != null) {
			throw new IllegalArgumentException(formatError(lex.first_error, lex.error_message, tokens));
		}
		CommonTreeNodeStream nodes = new CommonTreeNodeStream((Tree)tree.getTree());
		nodes.setTokenStream(tokens);
		SignupRuntime walker = new SignupRuntime(nodes); // created from TP.g

		walker.execute(this);
	}

	protected String formatError(RecognitionException e, String message, CommonTokenStream tokens) {
		StringBuilder sb = new StringBuilder();
		sb.append("Whoa! There was a parse error:\n\n");
		if (e.token != null) {
			sb.append("I didn't understand \"" + e.token.getText() + "\" on line " + e.line);
		}else{
			sb.append("Something's wrong on line " + e.line + ".");
		}
		sb.append("\n\nThe raw error is:\n\n");		
		sb.append(message);
		return sb.toString();
	}

	public boolean hasOrphans() {
		return orphansPresent;
	}
	
	public HashMap<String, HashMap<String,SignupEntry>> getFastMap() {
		return fastMap;
	}

}
