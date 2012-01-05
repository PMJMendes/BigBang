package bigBang.library.client.userInterface;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.ListIterator;

import bigBang.library.client.Checkable;
import bigBang.library.client.HasCheckables;
import bigBang.library.client.HasValueSelectables;
import bigBang.library.client.Selectable;
import bigBang.library.client.ValueSelectable;
import bigBang.library.client.event.CheckedSelectionChangedEvent;
import bigBang.library.client.event.CheckedSelectionChangedEventHandler;
import bigBang.library.client.event.CheckedStateChangedEvent;
import bigBang.library.client.event.CheckedStateChangedEventHandler;
import bigBang.library.client.event.SelectedStateChangedEvent;
import bigBang.library.client.event.SelectedStateChangedEventHandler;
import bigBang.library.client.event.SelectionChangedEvent;
import bigBang.library.client.event.SelectionChangedEventHandler;
import bigBang.library.client.userInterface.view.View;

import com.allen_sauer.gwt.dnd.client.PickupDragController;
import com.google.gwt.dom.client.Style.FontStyle;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.DoubleClickEvent;
import com.google.gwt.event.dom.client.DoubleClickHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.UIObject;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * 
 * @author Premium-Minds (Francisco Cabrita)
 *
 * Implements a list, able to hold entries inside a scrollable panel 
 * @param <T> The type of the value held by the list entries
 */
public class List<T> extends View implements HasValueSelectables<T>, java.util.List<ListEntry<T>>, HasCheckables {

	protected java.util.List<ListEntry<T>> entries;
	protected boolean multipleSelection = false;
	protected boolean selectableEntriesEnabled = true;
	protected boolean checkable;

	//UI
	protected VerticalPanel mainWrapper;
	protected HasWidgets headerContainer;
	protected VerticalPanel listPanel;
	protected Label footerLabel;
	protected HasWidgets footer;
	protected ScrollPanel scrollPanel;
	protected AbsolutePanel scrollPanelWrapper;
	protected SelectedStateChangedEventHandler entrySelectionHandler;
	protected DoubleClickHandler cellDoubleClickHandler;
	protected CheckedStateChangedEventHandler cellCheckedHandler;
	protected boolean selectionChangeHandlerInitialized;
	protected PickupDragController dragController;
	protected boolean draggable = false;
	protected Widget loadingWidget;

	/**
	 * The list constructor
	 */
	public List(){
		this.entries = new ArrayList<ListEntry<T>>();
		dragController = new PickupDragController(RootPanel.get(), false);

		mainWrapper = new VerticalPanel();
		initWidget(mainWrapper);
		mainWrapper.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		mainWrapper.setSize("100%", "100%");
		mainWrapper.setStyleName("emptyContainer");

		headerContainer = new SimplePanel();
		((UIObject) headerContainer).setSize("100%", "100%");
		((UIObject) headerContainer).getElement().getStyle().setBackgroundImage("url(images/listHeaderBackground1.png)");
		this.setHeaderWidget(null);

		mainWrapper.add((Widget) headerContainer);
		this.setHeaderWidget(null);

		scrollPanelWrapper = new AbsolutePanel();
		scrollPanelWrapper.setSize("100%", "100%");

		scrollPanel = new ScrollPanel();
		scrollPanel.setSize("100%", "100%");

		VerticalPanel listWrapper = new VerticalPanel();
		listWrapper.setSize("100%", "100%");
		this.listPanel = new VerticalPanel();
		listWrapper.add(this.listPanel);
		scrollPanel.add(listWrapper);
		this.listPanel.setSize("100%", "100%");
		this.listPanel.setStyleName("emptyContainer");

		AbsolutePanel shadowBottom = new AbsolutePanel();
		shadowBottom.setSize("100%", "3px");
		shadowBottom.getElement().getStyle().setBackgroundImage("url(images/listBottomShadow1.png)");
		listWrapper.add(shadowBottom);

		scrollPanel.getElement().getStyle().setProperty("overflowY", "scroll");
		scrollPanel.getElement().getStyle().setProperty("overflowX", "hidden");

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
		mainWrapper.add((Widget) footer);
		footerLabel.getElement().getStyle().setMargin(2, Unit.PX);
		((UIObject) footer).setSize("100%", "100%");
		((UIObject) footer).getElement().getStyle().setBackgroundColor("#CCC");
		((UIObject) footer).getElement().getStyle().setProperty("borderTop", "1px solid #BBB");

		cellDoubleClickHandler = new DoubleClickHandler() {

			@SuppressWarnings("unchecked")
			@Override
			public void onDoubleClick(DoubleClickEvent event) {
				if(entries.contains(event.getSource()))
					onCellDoubleClicked((ListEntry<T>) event.getSource());
			}
		};
		
		cellCheckedHandler = new CheckedStateChangedEventHandler() {
			
			@Override
			public void onCheckedStateChanged(CheckedStateChangedEvent event) {
				if(entries.contains(event.getSource()))
					checkedSelectionChanged((Checkable) event.getSource());
			}
		};

		this.entrySelectionHandler = new SelectedStateChangedEventHandler() {

			@Override
			public void onSelectedStateChanged(SelectedStateChangedEvent event) {
				selectableStateChanged((Selectable) event.getSource());
			}
		};

		clear();
		disableTextSelection(true);
	}


	//Listing Methods

	/**
	 * Renders the current list state
	 */
	protected void render() {
		this.listPanel.clear();
		for(ListEntry<T> e : this.entries)
			this.listPanel.add(e);			
	}

	/**
	 * Binds an entry to the list.
	 * @param e The list entry
	 * @return the handler registration for a SelectedStateChangeEvent on the entry
	 */
	protected HandlerRegistration bindEntry(ListEntry<T> e){
		if(draggable)
			dragController.makeDraggable(e, e.getDragHandler());
		e.addHandler(this.cellDoubleClickHandler, DoubleClickEvent.getType());
		e.addHandler(this.cellCheckedHandler , CheckedStateChangedEvent.TYPE);
		return e.addHandler(this.entrySelectionHandler, SelectedStateChangedEvent.TYPE);
	}

	/**
	 * Selects the entry following the currently selected one.
	 * If no entries are selected, the first available entry is selected.
	 */
	public void selectNext() {
		if (this.entries.isEmpty())
			return;

		int size = this.entries.size();
		int selectedIndex = 0;
		for(int i = 0; i < size; i++){
			if(entries.get(i).isSelected())
				selectedIndex = i;
		}
		if (size >= selectedIndex)
			entries.get(selectedIndex).setSelected(true, true);
	}

	/**
	 * Selects the entry previous to the currently selected entry.
	 * If no entries are selected, the last available entry is selected.
	 */
	public void selectPrevious() {
		if (this.entries.isEmpty())
			return;

		int size = this.entries.size();
		int selectedIndex = 0;
		for(int i = 0; i < size; i++){
			if(entries.get(i).isSelected()){
				selectedIndex = i;
				break;
			}
		}
		if (size > 0)
			entries.get(selectedIndex).setSelected(true, true);
	}

	/**
	 * Sets whether or not the list entries can be selected.
	 * @param selectable If true, the entries can be selected
	 */
	public void setSelectableEntries(boolean selectable) {
		this.selectableEntriesEnabled = selectable;
		for(ListEntry<T> e : this.entries)
			e.setSelectable(selectable);
	}

	/**
	 * Selects all entries from the currently selected entry to the one at the given index
	 * @param index the index up to which entries will be selected 
	 */
	protected void selectTo(int index) {
		/*	if (this.selectedEntry == null) {
			select(index);
			return;
		}

		int selectedIndex = getSelectedEntryIndex();

		if (index < selectedIndex) {
			for (int i = index; i < selectedIndex; i++)
				addToSelection(i, false);
		} else if (index > selectedIndex) {
			for (int i = index; i > selectedIndex; i--)
				addToSelection(i, false);
		}*/ //TODO
	}

	/**
	 * Sets the widget that will be placed at the list's header placeholder.
	 * @param w the widget to place
	 */
	public void setHeaderWidget(Widget w) {
		this.headerContainer.clear();
		if (w != null)
			this.headerContainer.add(w);
	}

	/**
	 * Sets the widget that will be placed at the list's footer placeholder.
	 * @param w the widget to place
	 */
	public void setFooterWidget(Widget w) {
		this.footer.clear();
		((UIObject) this.footer).setHeight("");
		if (w != null)
			this.footer.add(w);
	}

	/**
	 * Sets the text to be placed in the footer placeholder.
	 * @param text the footer text
	 */
	public void setFooterText(String text) {
		this.footer.clear();
		((UIObject) this.footer).setHeight("20px");
		this.footerLabel.setText(text);
		this.footer.add(this.footerLabel);
	}

	/**
	 * Invoked when there is a change to the list's main state.
	 */
	protected void updateFooterText() {
	};

	/**
	 * Invoked when the list size is changed.
	 */
	protected void onSizeChanged() {
	}

	
	//Checkable functions
	
	/**
	 * Sets whether or not the list entries have checkable capabilities.
	 * @param checkable If true, the list entries acquire checkable capabilities.
	 */
	public void setCheckable(boolean checkable) {
		this.checkable = checkable;
		for (ListEntry<T> entry : this.entries)
			entry.setCheckable(checkable);
	}

	@Override
	public Collection<Checkable> getChecked() {
		ArrayList<Checkable> result = new ArrayList<Checkable>();
		for (ListEntry<T> e : this.entries) {
			if (e.isChecked())
				result.add(e);
		}
		return result;
	}
	
	@Override
	public HandlerRegistration addCheckedSelectionChangedEventHandler(
			CheckedSelectionChangedEventHandler handler) {
		return addHandler(handler, CheckedSelectionChangedEvent.TYPE);
	}
	
	protected void checkedSelectionChanged(Checkable c){
		fireEvent(new CheckedSelectionChangedEvent(getChecked(), c));
	}

	protected void selectableStateChanged(Selectable source) {
		if(multipleSelection){
			//TODO throw
		}else{
			for(ListEntry<T> e : entries){
				if(source != e){
					e.setSelected(false, false);
				}
			}
		}
		
		if(!((UIObject) source).isVisible()){
			this.scrollPanel.ensureVisible((UIObject) source);
		}
		selectionChangedEventFireBypass(new SelectionChangedEvent(this.getSelected()));
	}

	//HasSelectables Methods

	@Override
	public Collection<ValueSelectable<T>> getSelected() {
		Collection<ValueSelectable<T>> result = new ArrayList<ValueSelectable<T>>();
		for(ListEntry<T> s : this.entries) {
			if(s.isSelected())
				result.add(s);
		}		
		return result;
	}

	/*@Override
	public void setSelected(Collection<ListEntry<T>> selectables,
			boolean selected) {
		setSelected(selectables, selected, true);
	}

	@Override
	public void setSelected(Collection<ListEntry<T>> selectables,
			boolean selected, boolean fireEvents) {
		if(!entries.containsAll(selectables))
			throw new RuntimeException("Some selectables are not part of the list");

		boolean hasChanges = false;
		for(Selectable s : selectables) {
			if(s.isSelected() != selected){
				hasChanges = true;
				s.setSelected(selected);
			}
			if(this.multipleSelection == false)
				break;
		}
		if(hasChanges && fireEvents)
			fireEvent(new SelectionChangedEvent(new ArrayList<Selectable>(entries)));
	}

	@Override
	public void setSelected(ListEntry<T> selectable, boolean selected) {
		setSelected(selectable, selected, true);
	}

	@Override
	public void setSelected(ListEntry<T> selectable, boolean selected,
			boolean fireEvents) {
		if(!entries.contains(selectable))
			throw new RuntimeException("Some selectables are not part of the list");
		if(!multipleSelection)
			clearSelection();
		if(selectable.isSelected() != selected){
			selectable.setSelected(selected);
			if(fireEvents)
				fireEvent(new SelectionChangedEvent(new ArrayList<Selectable>(entries)));
		}

	}*/

	@Override
	public void clearSelection() {
		boolean hasChanges = false;
		Collection<ValueSelectable<T>> selected = this.getSelected();
		for(ValueSelectable<T> s : selected){
			hasChanges |= s.isSelected();
			s.setSelected(false, true);
		}
		if(hasChanges && selectionChangeHandlerInitialized)
			selectionChangedEventFireBypass(new SelectionChangedEvent(this.getSelected()));	
	}

	@Override
	public void setMultipleSelectability(boolean multi) {
		this.multipleSelection = multi;
	}

	@Override
	public boolean isMultipleSelectabilityActive() {
		return this.multipleSelection;
	}

	@Override
	public HandlerRegistration addSelectionChangedEventHandler(
			SelectionChangedEventHandler handler) {
		if (!selectionChangeHandlerInitialized)
			selectionChangeHandlerInitialized = true;

		return addHandler(handler, SelectionChangedEvent.TYPE);
	}


	//LISTING

	@Override
	public boolean add(ListEntry<T> e) {
		boolean result = entries.add(e);
		if(result){
			this.bindEntry(e);
			e.setSelectable(this.selectableEntriesEnabled);
			e.setCheckable(checkable);
			this.listPanel.add(e);
			onSizeChanged();
		}
		return result;
	}

	@Override
	public void add(int index, ListEntry<T> element) {
		bindEntry(element);
		element.setCheckable(checkable);
		element.setSelectable(this.selectableEntriesEnabled);
		entries.add(index, element);
		onSizeChanged();
		render();
	}

	@Override
	public boolean addAll(Collection<? extends ListEntry<T>> c) {
		boolean result = entries.addAll(c);
		if(result){
			for(ListEntry<T> l : this.entries){
				bindEntry(l);
				l.setCheckable(checkable);
				l.setSelectable(this.selectableEntriesEnabled);
				this.listPanel.add(l);
			}
		}
		onSizeChanged();
		return result;
	}

	@Override
	public boolean addAll(int index, Collection<? extends ListEntry<T>> c) {
		boolean result = this.entries.addAll(index, c);
		if(result){
			for(ListEntry<T> e : c){
				bindEntry(e);
				e.setCheckable(checkable);
				e.setSelectable(this.selectableEntriesEnabled);
			}
			render();
		}
		onSizeChanged();
		return result;
	}

	@Override
	public void clear() {
		boolean hadElements = !entries.isEmpty();
		this.clearSelection();
		this.listPanel.clear();
		this.entries.clear();
		if(hadElements)
			onSizeChanged();
	}

	@Override
	public boolean contains(Object o) {
		return entries.contains(o);
	}

	@Override
	public boolean containsAll(Collection<?> c) {
		return entries.containsAll(c);
	}

	@Override
	public ListEntry<T> get(int index) {
		return entries.get(index);
	}

	@Override
	public int indexOf(Object o) {
		return entries.indexOf(o);
	}

	@Override
	public boolean isEmpty() {
		return entries.isEmpty();
	}

	@Override
	public Iterator<ListEntry<T>> iterator() {
		return entries.iterator();
	}

	@Override
	public int lastIndexOf(Object o) {
		return entries.lastIndexOf(o);
	}

	@Override
	public ListIterator<ListEntry<T>> listIterator() {
		return entries.listIterator();
	}

	@Override
	public ListIterator<ListEntry<T>> listIterator(int index) {
		return entries.listIterator(index);
	}

	@Override
	public boolean remove(Object o) {
		ListEntry<?> entry = (ListEntry<?>) o;
		boolean result = this.entries.remove(o);
		if(result){
			if(o instanceof Selectable && ((Selectable) o).isSelected()){
				((Selectable) o).setSelected(false, true);
				//selectNext();
			}
			entry.removeFromParent();
		}
		onSizeChanged();
		return result;
	}

	@Override
	public ListEntry<T> remove(int index) {
		ListEntry<T> result = this.entries.remove(index);
		if(result.isSelected())
			selectNext();
		result.removeFromParent();
		onSizeChanged();
		return result;
	}

	@Override
	public boolean removeAll(Collection<?> c) {
		boolean result = this.entries.removeAll(c);
		if(result){
			for(Object e : c)
				((ListEntry<?>)e).removeFromParent();
		}
		onSizeChanged();
		return result;
	}

	@Override
	public boolean retainAll(Collection<?> c) {
		boolean result = this.entries.retainAll(c);
		if(result)
			render();
		return result;
	}

	@Override
	public ListEntry<T> set(int index, ListEntry<T> element) {
		ListEntry<T> result = this.entries.set(index, element);
		result.removeFromParent();
		this.listPanel.insert(element, index + 1);
		onSizeChanged();
		return result;
	}

	@Override
	public int size() {
		return this.entries.size();
	}

	@Override
	public java.util.List<ListEntry<T>> subList(int fromIndex, int toIndex) {
		return this.entries.subList(fromIndex, toIndex);
	}

	@Override
	public Object[] toArray() {
		return this.entries.toArray();
	}

	@Override
	public <U extends Object> U[] toArray(U[] a) {
		return this.entries.toArray(a);
	}

	public ScrollPanel getScrollable(){
		return scrollPanel;
	}

	public VerticalPanel getListContent(){
		return this.listPanel;
	}

	protected void onCellDoubleClicked(ListEntry<T> entry) {
	}

	protected void selectionChangedEventFireBypass(SelectionChangedEvent e){
		fireEvent(e);
	}

	public void makeCellsDraggable(boolean draggable) {
		dragController.unregisterDropControllers();
		
		if(draggable) {
			for(ListEntry<T> e : this.entries) {
				dragController.makeDraggable(e);
			}
		}

		this.draggable = draggable;
	}
	
	public void showLoading(boolean show){
		if(show){
			if(this.loadingWidget == null) {
				VerticalPanel wrapper = new VerticalPanel();
				wrapper.setSize("100%", "100%");
				wrapper.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
				wrapper.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
				wrapper.add(new Label("A Carregar..."));
				this.loadingWidget = wrapper;
				this.scrollPanelWrapper.add(this.loadingWidget, 0, 0);
			}
		}else{
			if(this.loadingWidget != null) {
				this.loadingWidget.removeFromParent();
			}
		}
	}

}
