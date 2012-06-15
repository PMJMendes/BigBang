package bigBang.library.client.userInterface.autocomplete;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.user.client.ui.MultiWordSuggestOracle;
import com.google.gwt.user.client.ui.SuggestBox;
import com.google.gwt.user.client.ui.SuggestOracle;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

import bigBang.library.client.FormField;

public class AutoCompleteTextListFormField extends FormField<Collection<String>> {

	protected MultiWordSuggestOracle oracle = new MultiWordSuggestOracle();
	protected List<String> itemsSelected = new ArrayList<String>();
	protected BulletList list;
	protected TextBox itemBox;
	public SuggestBox box;

	public AutoCompleteTextListFormField(String label) {
		this();
		this.label.setText(label);
	}

	public AutoCompleteTextListFormField(){
		super();
		VerticalPanel panel = new VerticalPanel();
		initWidget(panel);

		panel.add(this.label);

		list = new BulletList();
		list.setStyleName("token-input-list-facebook");
		final ListItem item = new ListItem();
		item.setStyleName("token-input-input-token-facebook");
		itemBox = new TextBox();
		itemBox.getElement().setAttribute("style", "outline-color: -moz-use-text-color; outline-style: none; outline-width: medium;");
		itemBox.setWidth("100%");
		box = new SuggestBox(getSuggestions(), itemBox);
		item.add(box);
		list.add(item);

		// this needs to be on the itemBox rather than box, or backspace will get executed twice
		itemBox.addKeyDownHandler(new KeyDownHandler() {
			public void onKeyDown(KeyDownEvent event) {
				if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
					// only allow manual entries with @ signs (assumed email addresses)
					//					if (itemBox.getValue().contains("@"))
					//						deselectItem(itemBox, list);
				}
				// handle backspace
				if (event.getNativeKeyCode() == KeyCodes.KEY_BACKSPACE) {
					if ("".equals(itemBox.getValue().trim()) && (list.getWidgetCount() - 2) >= 0) {
						ListItem li = (ListItem) list.getWidget(list.getWidgetCount() - 2);
						Paragraph p = (Paragraph) li.getWidget(0);
						if (itemsSelected.contains(p.getText())) {
							itemsSelected.remove(p.getText());
							GWT.log("Removing selected item '" + p.getText() + "'", null);
							GWT.log("Remaining: " + itemsSelected, null);
						}
						list.remove(li);
						itemBox.setFocus(true);
					}
				}
			}
		});

		box.addSelectionHandler(new SelectionHandler<SuggestOracle.Suggestion>() {
			public void onSelection(SelectionEvent<SuggestOracle.Suggestion> selectionEvent) {
				deselectItem(itemBox, list);
			}
		});

		
		panel.add(list);

		panel.addDomHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				box.setFocus(true);
			}
		}, ClickEvent.getType());
		box.setFocus(true);
	}

	private void deselectItem(final TextBox itemBox, final BulletList list) {
		if (itemBox.getValue() != null && !"".equals(itemBox.getValue().trim())) {
			final ListItem displayItem = new ListItem();
			displayItem.setStyleName("token-input-token-facebook");
			Paragraph p = new Paragraph(itemBox.getValue());

			displayItem.addClickHandler(new ClickHandler() {
				public void onClick(ClickEvent clickEvent) {
					displayItem.addStyleName("token-input-selected-token-facebook");
				}
			});


			Span span = new Span("x");
			span.addClickHandler(new ClickHandler() {
				public void onClick(ClickEvent clickEvent) {
					removeListItem(displayItem, list);
				}
			});

			displayItem.add(p);
			displayItem.add(span);
			// hold the original value of the item selected

			GWT.log("Adding selected item '" + itemBox.getValue() + "'", null);
			itemsSelected.add(itemBox.getValue());
			GWT.log("Total: " + itemsSelected, null);

			list.insert(displayItem, list.getWidgetCount() - 1);
			itemBox.setValue("");
			itemBox.setFocus(true);
		}
	}

	private void removeListItem(ListItem displayItem, BulletList list) {
		GWT.log("Removing: " + displayItem.getWidget(0).getElement().getInnerHTML(), null);
		itemsSelected.remove(displayItem.getWidget(0).getElement().getInnerHTML());
		list.remove(displayItem);
	}

	protected MultiWordSuggestOracle getSuggestions(){
		return oracle;
	}

	@Override
	public void setValue(Collection<String> value, boolean fireEvents) {
		if(value == null){
			this.box.setValue("");
			this.itemBox.setValue("");
			this.itemsSelected.clear();
		}else{
			for(String s : value){
				this.itemBox.setValue(this.itemBox.getValue() + " " + s);
			}
		}
		if(fireEvents)
			ValueChangeEvent.fire(this, value);	}

	@Override
	public Collection<String> getValue() {
		return this.itemsSelected;
	}

	public void setSuggestions(Collection<String> suggestions){
		if(suggestions == null) {
			oracle.clear();
		}else{
			for(String s : suggestions){
				oracle.add(s);
			}
		}
	}

	@Override
	public void clear() {
		this.box.setValue(null);
	}

	@Override
	public void setReadOnly(boolean readonly) {
		this.itemBox.setReadOnly(readonly);
	}

	@Override
	public boolean isReadOnly() {
		return this.itemBox.isReadOnly();
	}

	@Override
	public void setLabelWidth(String width) {
		// TODO Auto-generated method stub

	}


	@Override
	public void focus() {
		itemBox.setFocus(true);
	}

}
