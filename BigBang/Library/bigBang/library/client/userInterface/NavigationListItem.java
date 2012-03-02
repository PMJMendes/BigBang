package bigBang.library.client.userInterface;

import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.Widget;

/**
 * A class that implements a navigation item to be used in navigation list items
 * 
 * @author Francisco Cabrita @ Premium Minds Lda.
 */
public class NavigationListItem implements HasValue<Object>{

	protected NavigationListItem[] children;
	protected Widget view;
	protected String description;
	protected Object value;

	protected HandlerManager handlerManager;

	/**
	 * The constructor
	 * @param description The description for this item
	 * @param children The children to which one can navigate from this item
	 */
	public NavigationListItem(String description, Object value, NavigationListItem[] children){
		setDescription(description);
		setChildren(children);
		setValue(value);
	}

	/**
	 * Sets the description for the item
	 * @param description The item's description
	 */
	public void setDescription(String description){
		this.description = description;
	}

	/**
	 * Sets the children for the item
	 * @param children The item's children
	 */
	public void setChildren(NavigationListItem[] children){
		if(children == null)
			children = new NavigationListItem[0];
		this.children = children;
	}

	/**
	 * Sets the view to be shown for the item. It will override the default view.
	 * @param view The item's view
	 */
	public void setSpecialView(Widget view){
		this.view = view;
	}

	/**
	 * Removes the item's special view from its parent and dissociates it from the item
	 */
	public void removeSpecialView(){
		if(this.hasSpecialView()){
			this.view.removeFromParent();
			this.view = null;
		}
	}

	/**
	 * Returns whether or not the item has a defined special view
	 * @return true if the item has a special view, false otherwise
	 */
	public boolean hasSpecialView(){
		return this.view != null;
	}

	/**
	 * Gets the view to be shown for the item
	 * @return The item view
	 */
	public Widget getSpecialView(){
		return view;
	}

	/**
	 * Gets the item description
	 * @return The item description
	 */
	public String getDescription(){
		return this.description;
	}

	/**
	 * Gets the item's children, to which on can navigate
	 * @return The item children
	 */
	public NavigationListItem[] getChildren(){
		return children;
	}

	@Override
	public HandlerRegistration addValueChangeHandler(
			ValueChangeHandler<Object> handler) {
		if(handlerManager == null) {
			handlerManager = new HandlerManager(this);
		}
		return handlerManager.addHandler(ValueChangeEvent.getType(), handler);
	}

	@Override
	public void fireEvent(GwtEvent<?> event) {
		if(handlerManager != null)
			handlerManager.fireEvent(event);
	}

	@Override
	public Object getValue() {
		return value;
	}

	@Override
	public void setValue(Object value) {
		setValue(value, false);
	}

	@Override
	public void setValue(Object value, boolean fireEvents) {
		this.value = value;
		if(fireEvents)
			ValueChangeEvent.fire(this, value);
	}

}
