package com.fatboycentral.client;

import java.util.LinkedList;
import java.util.List;

import com.fatboycentral.client.SignupApp.AddCallback;
import com.fatboycentral.client.SignupApp.RemoveCallback;
import com.fatboycentral.client.event.AppEvent;
import com.fatboycentral.client.event.AppEventHandler;
import com.fatboycentral.shared.SignupDatabase;
import com.fatboycentral.shared.SignupData.DataStoreEntry;
import com.fatboycentral.shared.SignupData.SignupEntry;
import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.user.cellview.client.DataGrid;
import com.google.gwt.user.cellview.client.IdentityColumn;
import com.google.gwt.user.client.ui.DeckLayoutPanel;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.ProvidesResize;
import com.google.gwt.user.client.ui.RequiresResize;
import com.google.gwt.user.client.ui.ResizeComposite;

public class JobsTable extends ResizeComposite implements RequiresResize, ProvidesResize, AppEventHandler {

	private DeckLayoutPanel top;
	private FlowPanel panel; 
	protected DataGrid<SignupEntry> table;
	private HTML description = new HTML();

	public JobsTable() {
		table = new DataGrid<SignupEntry>(100, SignupApp.TableResources);
		table.setHeight("95%");
		// Create timeslot column.		
		IdentityColumn<SignupEntry> dateColumn = new IdentityColumn<SignupEntry>(new JobCell());
		dateColumn.setVerticalAlignment(HasVerticalAlignment.ALIGN_TOP);
		dateColumn.setFieldUpdater(new FieldUpdater<SignupEntry,SignupEntry>() {
			@Override
			public void update(int index, SignupEntry olds, SignupEntry news) {
				if (olds.entries.size() < news.entries.size()) {
					// Add extra entry
					DataStoreEntry new_ent = news.entries.get(news.entries.size()-1);
					SignupApp.service.add(new_ent, new AddCallback(true));
				}else{
					// Find gone entry and remove it
					List<DataStoreEntry> copy = new LinkedList<DataStoreEntry>();
					copy.addAll(olds.entries);
					copy.removeAll(news.entries);
					SignupApp.service.remove(copy.get(0), new RemoveCallback(true));
				}
			}
		});

		// Create address column.		
		IdentityColumn<SignupEntry> nameColumn = new IdentityColumn<SignupEntry>(new SignupCell());
		nameColumn.setVerticalAlignment(HasVerticalAlignment.ALIGN_TOP);
		// Add the columns.
		table.addColumn(dateColumn);
		table.addColumn(nameColumn);
		table.setColumnWidth(dateColumn, "35%");
		table.setColumnWidth(nameColumn, "65%");

		ClientDatabase.get().addDataDisplay(table);

		description.setStylePrimaryName("JobDescriptionPanel");
		
		panel = new FlowPanel();
		panel.add(description);
		panel.add(table);
		
		top = new DeckLayoutPanel();		
		top.setAnimationDuration(1000);
		top.setAnimationVertical(true);
		top.add(panel);
		top.add(new HTML(""));
		top.showWidget(1);
		initWidget(top);
	}

	public void setDescription(String html) {
		description.setHTML(html);
	}
	
	@Override
	public void onResize() {
		int w = getOffsetWidth();
		int h = getOffsetHeight();
		table.setPixelSize(w, h-description.getOffsetHeight());
		table.onResize();
		super.onResize();
	}

	@Override
	public void onStateChange(AppEvent e) {
		if (e.Redraw) {
			top.forceLayout();
			top.showWidget(0);
		}
	}
}
