package com.fatboycentral.client;

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.cell.client.ValueUpdater;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;

public class IDCell extends AbstractCell<String> {

	public IDCell() {
		super("click", "keydown");
	}

	@Override
	public void render(com.google.gwt.cell.client.Cell.Context context, String value, SafeHtmlBuilder sb) {
		sb.appendHtmlConstant("value  <span class='buttonLabel'>delete</span>");
	}		

	@Override
	public void onBrowserEvent(Context context, final Element parent, String value, NativeEvent event, ValueUpdater<String> valueUpdater) {
		// Check that the value is not null.
		if (value == null) {
			return;
		}

		// Call the super handler, which handlers the enter key.
		super.onBrowserEvent(context, parent, value, event, valueUpdater);

		// Handle click events.
		if ("click".equals(event.getType())) {
			if (valueUpdater != null) {
				valueUpdater.update(value);
			}
		}
	}

}
