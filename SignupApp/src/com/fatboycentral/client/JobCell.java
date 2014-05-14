package com.fatboycentral.client;

import java.util.ArrayList;
import java.util.List;

import com.fatboycentral.shared.SignupData.DataStoreEntry;
import com.fatboycentral.shared.SignupData.Person;
import com.fatboycentral.shared.SignupData.SignupEntry;
import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.cell.client.ValueUpdater;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DecoratedPopupPanel;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

public class JobCell extends AbstractCell<SignupEntry> {

	public static final String emailPattern = "\\s*^[a-zA-Z0-9._%+\\-]+@[a-zA-Z0-9.\\-_]+\\.(?:[a-zA-Z]{2,6})\\s*$";
	public static final String urlPattern = "^https?://.*";
	public static final String namePattern = "\\S+";

	private static SignupDialogBox pop = null;

	public static void hidePopup() {
		if (pop != null && pop.isShowing()) {
			pop.saveDefs();
			pop.hide();
		}
	}

	public static boolean isEditing() {
		return (pop != null && pop.isShowing());
	}

	public JobCell() {
		super("click", "keydown");
	}

	@Override
	public boolean isEditing(com.google.gwt.cell.client.Cell.Context context, Element parent, SignupEntry value) {
		return (pop != null && pop.isShowing());
	}

	private boolean isSignedUp(SignupEntry value) {
		for (DataStoreEntry e : value.entries) {
			if (e.worker.cookie == SignupApp.cookieId) {
				return true;
			}
		}
		return false;
	}

	private List<DataStoreEntry> userSignups(SignupEntry value) {
		ArrayList<DataStoreEntry> rval = new ArrayList<DataStoreEntry>(8);
		for (DataStoreEntry e : value.entries) {
			if (e.worker.cookie == SignupApp.cookieId) {
				rval.add(e);
			}
		}
		return rval;
	}

	@Override
	public void render(com.google.gwt.cell.client.Cell.Context context, SignupEntry value, SafeHtmlBuilder sb) {
		if (value == null) {
			return;
		}

		if (value.slots == 0) {
			sb.appendHtmlConstant("<h4>" + value.shift.description + "</h4>");
		}else{
			int indent = 0;
			if (! "".equals(value.shift.master)) {
				indent = 1;
			}
			sb.appendHtmlConstant("<span style='margin-left:" + indent + "em'><b>" + value.shift.description + "</b>");

			sb.appendHtmlConstant("&nbsp;&nbsp;&nbsp;<span class=\"buttonLabel\">");
			if (SignupApp.adminMode || SignupApp.coordinatorRole != null) {
				sb.appendHtmlConstant("(un)Signup!");
			}else{
				// Has this user signed up for this job?
				if (isSignedUp(value)) {
					sb.appendHtmlConstant("(un)Signup!");
				}else{
					if (! value.shift.adminOnly) {
						if (value.entries.size() < value.slots) {
							sb.appendHtmlConstant("Signup!");
						}
					}
				}
			}
			sb.appendHtmlConstant("</span>");

			if (! "".equals(value.shift.instructions)) {
				sb.appendHtmlConstant("<br>" + value.shift.instructions);
			}
			sb.appendHtmlConstant("</span>");
		}
	}

	@Override
	public void onBrowserEvent(Context context, final Element parent, SignupEntry value, NativeEvent event, ValueUpdater<SignupEntry> valueUpdater) {
		// Check that the value is not null.
		if (value == null || value.slots == 0) {
			return;
		}

		if (value.shift.adminOnly && (!SignupApp.adminMode && SignupApp.coordinatorRole == null)) {
			return;
		}

		// Call the super handler, which handlers the enter key.
		super.onBrowserEvent(context, parent, value, event, valueUpdater);

		// Handle click events.
		if ("click".equals(event.getType())) {

			List<DataStoreEntry> can_remove = userSignups(value);

			if (can_remove.size() == 0 && (value.entries.size() >= value.slots && !SignupApp.adminMode && SignupApp.coordinatorRole == null)) {
				return;
			}

			if (SignupApp.adminMode || SignupApp.coordinatorRole != null) {
				can_remove.clear();
				for (DataStoreEntry e : value.entries) {
					can_remove.add(e);
				}
			}

			/*
			DataStoreEntry existing_signup = isSignedUp(value);
			if (existing_signup != null) {
				SignupEntry newval = new SignupEntry(value);
				newval.entries.remove(existing_signup);
				valueUpdater.update(newval);
				return;
			}
			*/

			pop = new SignupDialogBox(value, valueUpdater, can_remove);
			pop.setPopupPositionAndShow(new PopupPanel.PositionCallback() {
				public void setPosition(int offsetWidth, int offsetHeight) {
					int left = (Window.getClientWidth() - pop.getOffsetWidth()) / 2;
					int top = (Window.getClientHeight() - pop.getOffsetHeight()) / 2;
					pop.setPopupPosition(left, top);
				}
			});
		}
	}

	public static class SignupDialogBox extends DialogBox {

		private static String defName = "";
		private static String defAddress = "";
		private static String defUrl = "";
		private static String defComment = "";

		private TextBox nameBox = new TextBox();
		private TextBox addrBox = new TextBox();
		private TextBox urlBox = new TextBox();
		private TextArea comment = new TextArea();
		protected Label okay = new Label("Signup!");
		protected Label cancel = new Label("Cancel");

		public SignupDialogBox(final SignupEntry value, final ValueUpdater<SignupEntry> valueUpdater, List<DataStoreEntry> removers) {
			setText("Enter Your Details!");
			setAnimationEnabled(true);
			setGlassEnabled(true);

			VerticalPanel vp = new VerticalPanel();
			setWidget(vp);

			getDefs();

			vp.add(new Label("Name"));
			nameBox.setText(defName);
			nameBox.setWidth("95%");
			vp.add(nameBox);

			vp.add(new Label("Email Address (Confidential!)"));
			addrBox.setText(defAddress);
			addrBox.setWidth("95%");
			vp.add(addrBox);

			/*
			vp.add(new Label("URL"));
			urlBox.setText(defUrl);
			urlBox.setWidth("95%");
			vp.add(urlBox);
			*/

			vp.add(new Label("Comment"));
			comment.setText(defComment);
			comment.setWidth("95%");
			vp.add(comment);

			HorizontalPanel hp = new HorizontalPanel();
			hp.setSpacing(5);
			okay.setStylePrimaryName("buttonLabel");
			cancel.setStylePrimaryName("buttonLabel");

			okay.addClickHandler(new ClickHandler() {
				@Override
				public void onClick(ClickEvent event) {
					if (! nameBox.isEnabled()) {
						return;
					}

					Person p = new Person();
					p.name = nameBox.getText();
					p.email = addrBox.getText();
					p.url = urlBox.getText();
					p.comment = comment.getText();
					p.cookie = SignupApp.cookieId;

					if ("".equals(p.name)) {
						Window.alert("The name field is required!");
						return;
					}

					boolean valid_email = p.email.matches(emailPattern);
					//boolean valid_url = p.url.matches(urlPattern);

					boolean proceed = valid_email; //& valid_url;

					if (!proceed) {
						StringBuilder bldr = new StringBuilder();
						bldr.append("Please click Cancel and give us more information. If you don't want to click Okay\n\n"
								+ "Errors:\n");
						if (! valid_email) {
							bldr.append("  - Invalid email address\n");
						}
						/*
						if (! valid_url) {
							bldr.append("  - Invalid URL\n");
						}
						*/
						proceed = Window.confirm(bldr.toString());
					}

					if (!proceed) {
						return;
					}

					nameBox.setEnabled(false);
					addrBox.setEnabled(false);
					urlBox.setEnabled(false);
					comment.setEnabled(false);

					SignupEntry newval = new SignupEntry(value, p);
					valueUpdater.update(newval);
				}
			});

			cancel.addClickHandler(new ClickHandler() {
				@Override
				public void onClick(ClickEvent event) {
					if (! nameBox.isEnabled()) {
						return;
					}
					hide();
				}
			});

			hp.add(cancel);
			hp.add(new HTML("&nbsp;&nbsp;"));
			hp.add(okay);
			vp.add(hp);
			vp.setCellHorizontalAlignment(hp, HasHorizontalAlignment.ALIGN_RIGHT);

			if (removers.size() > 0) {
				vp.add(new HTML("<br>Already Signed Up:"));
				for (final DataStoreEntry e : removers) {
					hp = new HorizontalPanel();
					hp.setWidth("100%");
					hp.setSpacing(5);
					hp.add(new Label(e.worker.name));
					Label remove = new Label("remove");
					remove.setStylePrimaryName("buttonLabel");
					remove.addClickHandler(new ClickHandler() {
						@Override
						public void onClick(ClickEvent event) {
							SignupEntry newval = new SignupEntry(value);
							newval.entries.remove(e);
							valueUpdater.update(newval);
							hide();
						}
					});
					hp.add(remove);
					hp.setCellHorizontalAlignment(remove, HasHorizontalAlignment.ALIGN_RIGHT);
					vp.add(hp);
				}
			}
		}

		private void getDefs() {
			defName = Cookies.getCookie("def_name");
			if (defName == null) defName = "";

			defAddress = Cookies.getCookie("def_address");
			if (defAddress == null) defAddress = "";

			defUrl = Cookies.getCookie("def_url");
			if (defUrl == null) defUrl = "";

			defComment = Cookies.getCookie("def_comment");
			if (defComment == null) defComment = "";
		}

		private void saveDefs() {
			defName = nameBox.getText();
			defAddress = addrBox.getText();
			defUrl = urlBox.getText();
			defComment = comment.getText();
			Cookies.setCookie("def_name", defName, SignupApp.cookieExpire);
			Cookies.setCookie("def_address", defAddress, SignupApp.cookieExpire);
			Cookies.setCookie("def_url", defUrl, SignupApp.cookieExpire);
			Cookies.setCookie("def_comment", defComment, SignupApp.cookieExpire);
		}

	};
}
