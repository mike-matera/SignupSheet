package com.fatboycentral.client;

import com.fatboycentral.shared.SignupData.DataStoreEntry;
import com.fatboycentral.shared.SignupData.SignupEntry;
import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;

public class SignupCell extends AbstractCell<SignupEntry> {

	public SignupCell() {
	}

	@Override
	public void render(com.google.gwt.cell.client.Cell.Context context, SignupEntry value, SafeHtmlBuilder sb) {
		if (value == null) {
			return;
		}
		for (int i=0; i<value.slots; i++) {
			if (i < value.entries.size()) {
				DataStoreEntry e = value.entries.get(i);
				sb.appendHtmlConstant("<div>");

				/*
				if (! "".equals(e.worker.url)) {
					sb.appendHtmlConstant(" <a target='_blank' href='" + e.worker.url + "'>");
				}
				*/
				sb.appendHtmlConstant(e.worker.name);

				/*
				if (! "".equals(e.worker.url)) {
					sb.appendHtmlConstant("</a>");
				}
				if (! "".equals(e.worker.email)) {
					sb.appendHtmlConstant(" <i><a target='_blank' href='mailto:" + e.worker.email + "'>" + e.worker.email + "</a></i>");
				}
				*/
				if (! "".equals(e.worker.comment)) {
					sb.appendHtmlConstant(" -- <b>"+ e.worker.comment + "</b>");
				}
				sb.appendHtmlConstant("</div>");
			}else{
				sb.appendHtmlConstant("<div>--</div>");
			}
		}
	}

}
