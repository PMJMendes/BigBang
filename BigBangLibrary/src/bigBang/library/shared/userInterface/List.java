package bigBang.library.shared.userInterface;

import java.util.ArrayList;
import java.util.Collection;

import bigBang.library.shared.userInterface.view.View;

import com.gargoylesoftware.htmlunit.javascript.host.KeyboardEvent;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.FontStyle;
import com.google.gwt.dom.client.Style.FontWeight;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyEvent;
import com.google.gwt.event.logical.shared.HasValueChangeHandlers;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Focusable;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.KeyboardListener;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.UIObject;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class List<T> extends View implements HasValue<T>,
HasValueChangeHandlers<T> {

	private ListEntry<T> selectedEntry;
	private ArrayList<ListEntry<T>> selectedListEntries;
	private ArrayList<ListEntry<T>> listEntries;
	
	private boolean isMultipleSelect = false;

	private Label headerLabel;
	private HasWidgets header;
	private VerticalPanel listPanel;
	private Label footerLabel;
	private HasWidgets footer;

	private boolean valueChangeHandlerInitialized = false;

	public List(){
		this.listEntries = new ArrayList<ListEntry<T>>();
		this.selectedListEntries = new ArrayList<ListEntry<T>>();
		VerticalPanel mainWrapper = new VerticalPanel();
		mainWrapper.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		mainWrapper.setSize("100%", "100%");
		mainWrapper.setStyleName("emptyContainer");
		
		header = new SimplePanel();
		((UIObject) header).setSize("100%", "100%");
		((UIObject) header).getElement().getStyle().setBackgroundImage("url(images/listHeaderBackground1.png)");
		this.setHeaderWidget(null);

		this.headerLabel = new Label();
		mainWrapper.add((Widget) header);
		this.setHeaderWidget(null);

		AbsolutePanel scrollPanelWrapper = new AbsolutePanel();
		scrollPanelWrapper.setSize("100%", "100%");

		final ScrollPanel scrollPanel = new ScrollPanel();
		scrollPanel.setSize("100%", "100%");

		VerticalPanel listWrapper = new VerticalPanel();
		listWrapper.setSize("100%", "100%");
		this.listPanel = new VerticalPanel();
		this.listPanel.setSize("100%", "100%");
		listWrapper.add(this.listPanel);

		scrollPanel.add(listWrapper);

		AbsolutePanel shadowBottom = new AbsolutePanel();
		shadowBottom.setSize("100%", "3px");
		shadowBottom.getElement().getStyle().setBackgroundImage("url(images/listBottomShadow1.png)");
		listWrapper.add(shadowBottom);

		scrollPanel.getElement().setAttribute("overflow-y", "scroll");
		scrollPanel.getElement().setAttribute("overflow-x", "hidden");

		scrollPanelWrapper.add(scrollPanel, 0 , 0);

		AbsolutePanel shadowTop = new AbsolutePanel();
		shadowTop.setSize("100%", "3px");
		shadowTop.getElement().getStyle().setBackgroundImage("url(images/listTopShadow1.png)");
		scrollPanelWrapper.add(shadowTop, 0, 0);

		mainWrapper.add(scrollPanelWrapper);
		mainWrapper.setCellHeight(scrollPanelWrapper, "100%");

		this.footer = new SimplePanel();
		this.setFooterWidget(null);

		this.footerLabel = new Label();
		footerLabel.getElement().getStyle().setFontStyle(FontStyle.ITALIC);
		footer = new SimplePanel();
		footerLabel.getElement().getStyle().setMargin(2, Unit.PX);
		((UIObject) footer).setSize("100%", "100%");
		((UIObject) footer).getElement().getStyle().setBackgroundColor("#CCC");
		((UIObject) footer).getElement().getStyle().setProperty("borderTop", "1px solid #BBB");
		mainWrapper.add((Widget) footer);

		initWidget(mainWrapper);
		
		disableTextSelection(true);
	}
	@Override
	public void onBrowserEvent(Event event){
		GWT.log("event");
		super.onBrowserEvent(event);
		
		int type = DOM.eventGetType(event);
		if(type != Event.ONKEYPRESS)
			return;
		GWT.log("keypress");
		if(event.getKeyCode() == KeyCodes.KEY_DOWN)
			selectNext();
		if(event.getKeyCode() == KeyCodes.KEY_UP)
			selectPrevious();
	}
	
	public void setMultipleSelectEnabled(boolean enabled) {
		this.isMultipleSelect = enabled;
	}

	public boolean isMultipleSelectionEnabled() {
		return this.isMultipleSelect;
	}
	
	public HandlerRegistration addValueChangeHandler(
			ValueChangeHandler<T> handler) {

		if (!valueChangeHandlerInitialized)
			valueChangeHandlerInitialized = true;

		return addHandler(handler, ValueChangeEvent.getType());
	}

	public T getValue() {
		return selectedEntry == null ? null : selectedEntry.getValue();
	}

	public void setValue(T value) {
		setValue(value, true);
	}

	public void setValue(T value, boolean fireEvents) {
		List<T> list = this;

		for(ListEntry<T> e : listEntries) {
			if(e.getValue().equals(value)){
				if(e.equals(selectedEntry))
					return;
				this.selectedEntry = e;
				if(fireEvents && valueChangeHandlerInitialized){
					ValueChangeEvent.fire(list, e.getValue());
				}
				return;
			}
		}
	}

	public void select(ListEntry<T> entry){
		entry.setSelected(true);
		if(this.selectedEntry == entry)
			return;
		if(this.selectedEntry != null)
			this.selectedEntry.setSelected(false);
		this.selectedListEntries.add(entry);
		this.setValue(entry.getValue(), true);
	}

	public void select(int index) {
		ListEntry<T> entry = this.listEntries.get(index);
		select(entry);
	}

	public void selectNext(){
		if(this.selectedEntry == null && this.listEntries.isEmpty())
			return;
		if(this.selectedEntry == null){
			this.select(0);
			return;
		}
		
		int selectedIndex = this.listEntries.indexOf(this.selectedEntry);
		if(this.listEntries.size() >= selectedIndex)
			select(selectedIndex + 1);
	}

	public void selectPrevious(){
		if(this.selectedEntry == null && this.listEntries.isEmpty())
			return;
		if(this.selectedEntry == null){
			this.select(0);
			return;
		}
		
		int selectedIndex = this.listEntries.indexOf(this.selectedEntry);
		if(this.listEntries.size() > 0)
			select(selectedIndex - 1);
	}

	public void setEntries(Collection<T> entries) {
		clear();
		for(T t : entries) {
			ListEntry<T> listEntry = new ListEntry<T>(t);
			this.listEntries.add(listEntry);
		}
		onSizeChanged();
		render();
	}

	public void setListEntries(Collection<ListEntry<T>> entries) {
		this.clear();
		this.addListEntries(entries);
	}

	public void clear(){
		for(ListEntry<?> e : this.listEntries){
			e.removeFromParent();
			this.selectedEntry = null;
			this.setValue(null);
		}
		this.selectedListEntries.clear();
		this.listEntries.clear();
		onSizeChanged();
	}

	public void addListEntries(Collection <ListEntry<T>> listEntries) {
		if(listEntries == null)
			return;
		for(ListEntry<T> e : listEntries)
			addListEntry(e);
	}

	public void addListEntry(final ListEntry<T> e) {
		if(e == null)
			return;

		this.listEntries.add(e);
		this.listPanel.add(e);
		e.addDomHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {
				if(isMultipleSelect && event.isControlKeyDown())
					addToSelection(e, true);
				else if(isMultipleSelect && event.isShiftKeyDown()){
					clearMultipleSelection();
					selectTo(e);
				}else{
					select(e);
					clearMultipleSelection();
				}
			}
		}, ClickEvent.getType());
		onSizeChanged();
	}

	protected void clearMultipleSelection() {
		for(ListEntry<?> entry : this.listEntries){
			if(entry != selectedEntry){
				if(entry.isSelected())
					entry.setSelected(false);
			}
		}
		this.selectedListEntries.clear();
		this.selectedListEntries.add(this.selectedEntry);
	}

	protected void selectTo(ListEntry<T> e) {
		selectTo(this.listEntries.indexOf(e));
	}
	
	protected void selectTo(int index) {
		if(this.selectedEntry == null){
			select(index);
			return;
		}

		int selectedIndex = getSelectedEntryIndex();
		
		if(index < selectedIndex){
			for(int i = index; i < selectedIndex; i++)
				addToSelection(i, false);
		}else if(index > selectedIndex){
			for(int i = index; i > selectedIndex; i--)
				addToSelection(i, false);
		}	
	}

	protected void addToSelection(ListEntry<T> e, boolean swapSelectedEntry) {
		if(this.selectedListEntries.contains(e))
			return;
		this.selectedListEntries.add(e);
		e.setSelected(true);
		if(swapSelectedEntry)
			this.selectedEntry = e;
	}
	
	protected void addToSelection(int index, boolean swapSelectedEntry) {
		addToSelection(this.listEntries.get(index), swapSelectedEntry);
	}
	
	protected void removeFromSelection(ListEntry<T> e) {
		if(!inSelection(e) || this.selectedEntry == null || selectedListEntries.size() == 1)
			return;
	
		this.selectedListEntries.remove(e);
		if(this.selectedEntry == e){
			this.selectedEntry = this.selectedListEntries.get(0);
		}
	}
	
	protected void removeFromSelection(int index) {
		removeFromSelection(this.listEntries.get(index));
	}
	
	protected boolean inSelection(ListEntry<?> entry){
		return this.selectedListEntries.contains(entry);
	}
	
	protected boolean inSelection(int index){
		return inSelection(this.listEntries.get(index));
	}

	public ListEntry<T> getSelectedEntry(){
		return this.selectedEntry;
	}

	public int getSelectedEntryIndex(){
		if(this.selectedEntry == null)
			return -1;
		return this.listEntries.indexOf(this.selectedEntry);
	}
	
	public ArrayList<ListEntry<T>> getMultipleSelectionEntries() {
		return this.selectedListEntries;
	}	
	
	public int[] getMultipleSelectionEntriesIndexes() {
		int nSelectedEntries = this.selectedListEntries.size();
		int[] result = new int[nSelectedEntries];
		for(int i = 0; i < nSelectedEntries; i++)
			result[i] = this.listEntries.indexOf(this.selectedListEntries.get(i));
		return result;
	}

	public Collection <ListEntry<T>> getListEntries(){
		return this.listEntries;
	}

	public void removeListEntry(ListEntry<T> entry){
		int index = this.listEntries.indexOf(entry);
		removeListEntry(index);
	}

	public void removeListEntry(int index){
		final ListEntry<T> entry = this.listEntries.get(index);
		if(entry == this.selectedEntry && index < (this.listEntries.size() - 1))
			this.select(index + 1);
		else if(entry == this.selectedEntry && index > 0)
			this.select(index - 1);
		else {
			setValue(null);
			this.selectedEntry = null;
		}
		entry.removeFromParent();
		this.selectedListEntries.remove(entry);
		this.listEntries.remove(index);
		onSizeChanged();
	}

	public void render(){
		this.listPanel.clear();
		for(ListEntry<T> e : this.listEntries) {
			addListEntry(e);
		}
	}

	public void setHeaderWidget(Widget w){
		this.header.clear();
		if(w != null)
			this.header.add(w);
	}

	public void setHeaderText(String text){
		this.header.clear();
		HorizontalPanel headerWrapper = new HorizontalPanel();
		headerWrapper.setSize("100%", "40px");
		headerWrapper.getElement().getStyle().setBackgroundImage("url(images/listHeaderBackground1.png)");
		headerWrapper.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		headerWrapper.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		headerWrapper.add(headerLabel);
		headerLabel.setText(text);
		headerLabel.getElement().getStyle().setFontSize(14, Unit.PX);
		headerLabel.getElement().getStyle().setFontWeight(FontWeight.BOLD);
		this.setHeaderWidget(headerWrapper);
	}

	public void setFooterWidget(Widget w){
		this.footer.clear();
		((UIObject) this.footer).setHeight("");
		if(w != null)
			this.footer.add(w);
	}

	public void setFooterText(String text){
		this.footer.clear();
		((UIObject) this.footer).setHeight("20px");
		this.footerLabel.setText(text);
		this.footer.add(this.footerLabel);
	}
	
	protected void updateFooterText(){};

	public int size() {
		return this.listEntries.size();
	}

	protected void onSizeChanged(){}

	public void setCheckable(boolean checkable) {
		for(ListEntry<T> entry : this.listEntries)
			entry.setCheckable(checkable);
	}
}
