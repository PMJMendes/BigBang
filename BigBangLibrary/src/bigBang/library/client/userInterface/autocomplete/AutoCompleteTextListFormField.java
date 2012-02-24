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
import com.google.gwt.user.client.ui.MultiWordSuggestOracle;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.SuggestBox;
import com.google.gwt.user.client.ui.SuggestOracle;
import com.google.gwt.user.client.ui.TextBox;

import bigBang.library.client.FormField;

public class AutoCompleteTextListFormField extends FormField<Collection<ListItem>> {

	List<String> itemsSelected = new ArrayList<String>();

	public AutoCompleteTextListFormField(){
		SimplePanel panel = new SimplePanel();
		initWidget(panel);
		final BulletList list = new BulletList();
		list.setStyleName("token-input-list-facebook");
		final ListItem item = new ListItem();
		item.setStyleName("token-input-input-token-facebook");
		final TextBox itemBox = new TextBox();
		itemBox.getElement().setAttribute("style", "outline-color: -moz-use-text-color; outline-style: none; outline-width: medium;");
		final SuggestBox box = new SuggestBox(getSuggestions(), itemBox);
		item.add(box);
		list.add(item);

		// this needs to be on the itemBox rather than box, or backspace will get executed twice
		itemBox.addKeyDownHandler(new KeyDownHandler() {
			public void onKeyDown(KeyDownEvent event) {
				if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
					// only allow manual entries with @ signs (assumed email addresses)
					if (itemBox.getValue().contains("@"))
						deselectItem(itemBox, list);
				}
				// handle backspace
				if (event.getNativeKeyCode() == KeyCodes.KEY_BACKSPACE) {
					if ("".equals(itemBox.getValue().trim())) {
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
		/* Div structure after a few elements have been added:
             <ul class="token-input-list-facebook">
                 <li class="token-input-token-facebook">
                     <p>What's New Scooby-Doo?</p>
                     <span class="token-input-delete-token-facebook">x</span>
                 </li>
                 <li class="token-input-token-facebook">
                     <p>Fear Factor</p>
                     <span class="token-input-delete-token-facebook">x</span>
                  </li>
                  <li class="token-input-input-token-facebook">
                      <input type="text" style="outline-color: -moz-use-text-color; outline-style: none; outline-width: medium;"/>
                  </li>
             </ul>
		 */
	}

	private void deselectItem(final TextBox itemBox, final BulletList list) {
        if (itemBox.getValue() != null && !"".equals(itemBox.getValue().trim())) {
            /** Change to the following structure:
             * <li class="token-input-token-facebook">
             * <p>What's New Scooby-Doo?</p>
             * <span class="token-input-delete-token-facebook">x</span>
             * </li>
             */

            final ListItem displayItem = new ListItem();
            displayItem.setStyleName("token-input-token-facebook");
            Paragraph p = new Paragraph(itemBox.getValue());

            displayItem.addClickHandler(new ClickHandler() {
                public void onClick(ClickEvent clickEvent) {
                    displayItem.addStyleName("token-input-selected-token-facebook");
                }
            });

            /** TODO: Figure out how to select item and allow deleting with backspace key
            displayItem.addKeyDownHandler(new KeyDownHandler() {
                public void onKeyDown(KeyDownEvent event) {
                    if (event.getNativeKeyCode() == KeyCodes.KEY_BACKSPACE) {
                        removeListItem(displayItem, list);
                    }
                }
            });
            displayItem.addBlurHandler(new BlurHandler() {
                public void onBlur(BlurEvent blurEvent) {
                    displayItem.removeStyleName("token-input-selected-token-facebook");
                }
            });
            */

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
		MultiWordSuggestOracle oracle = new MultiWordSuggestOracle(); //TODO
		oracle.add("Alef Arendsen");
        oracle.add("David Jencks");
        oracle.add("Alexey Belikov");
        oracle.add("Bryan Vial");
        oracle.add("Dror Bereznitsky");
        oracle.add("David Moskowitz");
        oracle.add("Oscar Chan");
        oracle.add("Sergey Sundukovskiy");
        oracle.add("John Newton");
        oracle.add("Chris Buzzetta");
        oracle.add("Peter Svensson");
        oracle.add("Riccardo Ferretti");
        oracle.add("Christian Parker");
        oracle.add("Ann (Jaksa) Skaehill");
        oracle.add("Justin Blue");
        oracle.add("Sean Dawson");
        oracle.add("Devaraj NS");
        oracle.add("Robert Gadd");
        oracle.add("Diego Campodonico");
        oracle.add("Bryan Field-Elliot");
        oracle.add("Scott Delap");
        oracle.add("Kevin Koster");
        oracle.add("Fernand Galiana");
        oracle.add("Christopher Shuler");
        oracle.add("Geir Magnusson Jr");
        oracle.add("Tyler Hansen");
        oracle.add("Olivier Lamy");
        oracle.add("J. Thomas Richardson");
        oracle.add("Russell Beattie");
        oracle.add("Martin Ouellet");
        oracle.add("Scott Ferguson");
        oracle.add("Guillaume Laforge");
        oracle.add("Eric Weidner");
        oracle.add("Troy McKinnon");
        oracle.add("Max Hays");
        oracle.add("Phillip Rhodes");
        oracle.add("Eugene Kulechov");
        oracle.add("Bob Johnson");
        oracle.add("Richard Tucker, PMP");
        oracle.add("Mats Henricson");
        oracle.add("Floyd Marinescu");
        oracle.add("Ed Burns");
        oracle.add("Michael Root");
        oracle.add("Dana Busch");
        oracle.add("Borislav Roussev");
        oracle.add("Harris Tsim");
		return oracle;
	}

	@Override
	public void setValue(Collection<ListItem> value, boolean fireEvents) {
		// TODO Auto-generated method stub
		super.setValue(value, fireEvents);
	}
	
	@Override
	public Collection<ListItem> getValue() {
		// TODO Auto-generated method stub
		return super.getValue();
	}
	
	protected void setSuggestions(){
		//TODO
	}

	@Override
	public void clear() {
		// TODO Auto-generated method stub

	}

	@Override
	public void setReadOnly(boolean readonly) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean isReadOnly() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void setLabelWidth(String width) {
		// TODO Auto-generated method stub

	}

}
