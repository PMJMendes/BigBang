package bigBang.library.client.userInterface;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.validation.constraints.NotNull;

import org.gwt.mosaic.ui.client.util.ButtonHelper.ButtonLabelType;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.AttachEvent;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.AbstractImagePrototype;
import com.google.gwt.user.client.ui.CellPanel;
import com.google.gwt.user.client.ui.DecoratedPopupPanel;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.ToggleButton;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

import bigBang.library.client.resources.Resources;
import bigBang.library.client.userInterface.view.View;

/**
 * A Toolbar that shows popups when an item is selected
 *  
 * @author Francisco Cabrita @ Premium Minds Lda.
 */
public class PopupBar extends View {

	/**
	 * An item to be placed in the popup bar
	 */
	public static class Item extends ToggleButton {

		protected String text;
		protected Widget content;

		/**
		 * The constructor
		 * @param text The item text
		 * @param content The content to be shown in the popup
		 */
		public Item(String text, Widget content){
			this.text = text;
			this.content = content;
			this.setText(text);
			this.setHTML(getLabelHTML(text));
			this.setSize("100%", "30px");
		}

		private String getLabelHTML(String text){			
			Resources r = GWT.create(Resources.class);

			final HTML html = new HTML(text, false);
			final Image img = new Image(r.arrowUpExpand());

			VerticalPanel vpanel = new VerticalPanel();
			vpanel.setSize("100%", "100%");
			vpanel.setStyleName("dockItemButton");

			vpanel.add(img);
			vpanel.add(html);
			vpanel.setCellHorizontalAlignment(html,
					HasHorizontalAlignment.ALIGN_CENTER);
			vpanel.setCellHorizontalAlignment(img,
					HasHorizontalAlignment.ALIGN_CENTER);
			return vpanel.getElement().getString();
		}

		/**
		 * Gets the content to be shown in the popup
		 * @return The content to be shown
		 */
		protected Widget getContent(){
			return content;
		}
	}

	/**
	 * The possible positions where the popups will appear relative to the bar
	 */
	public static enum PopupPosition{
		NORTH,
		SOUTH,
		EAST,
		WEST
	}

	public static enum BarOrientation {
		HORIZONTAL,
		VERTICAL
	}

	protected ArrayList<Item> items;
	protected Map<Item, Collection<HandlerRegistration>> handlers;

	//UI
	protected CellPanel itemsWrapper;
	protected PopupPanel popup;
	protected PopupPosition popupPosition;
	protected BarOrientation orientation ;

	/**
	 * The constructor
	 */
	public PopupBar(){
		this(BarOrientation.VERTICAL, PopupPosition.WEST);
	}

	/**
	 * The constructor
	 * @param popupPosition the position of the popups relatively to the bar
	 */
	public PopupBar(@NotNull BarOrientation orientation, @NotNull PopupPosition popupPosition){
		this.popupPosition = popupPosition;
		this.orientation = orientation;

		this.items = new ArrayList<PopupBar.Item>();
		this.handlers = new HashMap<PopupBar.Item, Collection<HandlerRegistration>>();

		this.popup = new PopupPanel(true);

		SimplePanel wrapper = new SimplePanel();
		wrapper.setSize("100%", "100%");

		switch(orientation){
		case VERTICAL:
			break;
		case HORIZONTAL:
			break;
		default:
			throw new RuntimeException("The popup bar has no defined orientation.");
		}

		itemsWrapper = new VerticalPanel();
		itemsWrapper.setHeight("100%");
		wrapper.add(itemsWrapper);

		setPopupPosition(popupPosition);

		initWidget(wrapper);
	}

	/**
	 * Adds a new Item to the bar
	 * @param item the item to add
	 */
	public void addItem(final PopupBar.Item item) {
		if(this.items.contains(item))
			return;

		this.items.add(item);
		ArrayList<HandlerRegistration> handlersList = new ArrayList<HandlerRegistration>();
		this.handlers.put(item, handlersList);
		itemsWrapper.add(item);

		HandlerRegistration attachHandlerRegistration = item.getContent().addHandler(new AttachEvent.Handler() {

			@Override
			public void onAttachOrDetach(AttachEvent event) {
				if(event.isAttached()){
					PopupBar.this.popup.setPopupPosition(getNextPopupLeft(item), getNextPopupTop(item));
				}else{
					PopupBar.this.deselectItems();
				}
			}
		}, AttachEvent.getType());
		handlersList.add(attachHandlerRegistration);

		HandlerRegistration valueChangeHandlerRegistration = item.addValueChangeHandler(new ValueChangeHandler<Boolean>() {

			@Override
			public void onValueChange(ValueChangeEvent<Boolean> event) {
				if(event.getValue()){
					PopupBar.this.deselectItems();
					PopupBar.this.selectItem(item);
				}else{
					//TODO
				}
			}
		});
		handlersList.add(valueChangeHandlerRegistration);
	}

	/**
	 * Gets the left position where the popup will be placed, next to an item
	 * @param item the item relative to which the popup will be placed
	 * @return the left position of the popup
	 */
	protected int getNextPopupLeft(PopupBar.Item item){
		if((item.getAbsoluteLeft() + item.getContent().getOffsetWidth() + 10) > (this.getElement().getAbsoluteLeft() + this.getElement().getOffsetWidth())){
			return this.getElement().getAbsoluteLeft() + this.getElement().getOffsetWidth() - item.getContent().getOffsetWidth() - 10;
		}
		return item.getAbsoluteLeft();
	}

	/**
	 * Gets the top position where the popup will be placed, next to an item
	 * @param item the item relative to which the popup will be placed
	 * @return the top position of the popup
	 */
	protected int getNextPopupTop(PopupBar.Item item){
		return item.getAbsoluteTop() - item.getContent().getOffsetHeight() - 10;
	}

	/**
	 * Removes an item from the bar
	 * @param item the item to be removed
	 */
	public boolean removeItem(PopupBar.Item item) {
		if(!this.items.remove(item))
			return false;
		this.itemsWrapper.remove(item);
		Collection<HandlerRegistration> handlersList = this.handlers.get(item);

		for(HandlerRegistration r : handlersList) {
			r.removeHandler();
		}

		this.handlers.remove(item);
		return true;
	}

	/**
	 * Sets the position where the popups will appear relative to the bar
	 * @param popupPosition the new position
	 */
	public void setPopupPosition(PopupPosition popupPosition){
		this.popupPosition = popupPosition;
		if(this.popup.isAttached()){
			//TODO WHEN ITS SHOWING
		}
	}

	/**
	 * Selects an item in the bar and shows the corresponding popup
	 * @param item the item to be selected
	 */
	public void selectItem(PopupBar.Item item) {
		deselectItems();
		item.setValue(true);
		showItemPopup(item);
	}

	/**
	 * Deselects the currently selected item from the bar and hides its popup
	 */
	public void deselectItems() {
		for(PopupBar.Item i : this.items)
			i.setValue(false);
				this.popup.hide(true);
	}

	/**
	 * Shows the popup this the item content
	 * @param item The item for which the content will be shown in the popup 
	 */
	protected void showItemPopup(PopupBar.Item item) {
		popup.clear();
		popup.add(item.getContent());
		popup.show();
	}

}
