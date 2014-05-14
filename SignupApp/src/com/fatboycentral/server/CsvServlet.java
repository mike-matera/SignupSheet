package com.fatboycentral.server;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.LinkedList;
import java.util.Map;
import java.util.HashMap;
import java.util.Map.Entry;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.antlr.runtime.RecognitionException;

import com.fatboycentral.shared.SignupData.SchemaJob;
import com.fatboycentral.shared.SignupData.SignupEntry;
import com.fatboycentral.shared.SignupDatabase;
import com.fatboycentral.shared.SignupData.DataStoreEntry;
import com.fatboycentral.shared.SignupData.Database;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Text;

public class CsvServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String escape(Object csv) {
		String rval;
		if (csv != null) {
			rval = csv.toString();
		}else{
			rval = "";
		}
		rval = rval.replace("\"", "\"\"");
		rval = '"' + rval + '"';
		return rval;
	}
	
	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) {
		response.setContentType("application/csv");
		response.setHeader("Content-disposition", "attachment; filename=SignupSheet.csv");
		PrintWriter out = null;
		try {
			out = response.getWriter();
			Database data = SignupServiceImpl.doFetch();
			SignupDatabase db = new SignupDatabase();
			db.execute(data.schema);
			db.loadDbEntries(data.entries);
			Map<String,HashMap<String,SignupEntry>> fastMap = db.getFastMap();

			int total=0, available=0;
			out.println("Role, Slot, People, Total, Needed");
			for (SignupEntry ent : db.getSignups()) {
				if (ent.shift.slots > 0) {
					SignupEntry signups = fastMap.get(ent.shift.job).get(ent.shift.jid);
					out.print(""  
							+ escape(ent.shift.job) + ","
							+ escape(ent.shift.jid) + ","
							);
					StringBuilder people = new StringBuilder();
					for (int i=0; i<signups.entries.size(); i++) {
						people.append(signups.entries.get(i).worker.name);
						if (i < signups.entries.size()-1) {
							people.append("\n");
						}
					}
					out.print(escape(people.toString()) + ",");
					int left = ent.shift.slots - signups.entries.size();
					out.print(escape("" + ent.shift.slots) + ",");
					out.print(escape("" + left));
					out.println();
					total+=ent.shift.slots;
					available+=left;
				}
			}
			out.println();
			out.println("Total staff, " + total);
			out.println("Available slots, " + available);
			
		} catch (IOException e) {
			e.printStackTrace();
		} catch (RecognitionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (out != null) {
				out.close();
			}
		}
	}

	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		doGet(request,response);
	}

}
