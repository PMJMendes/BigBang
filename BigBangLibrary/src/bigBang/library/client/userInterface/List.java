package bigBang.library.client.userInterface;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.ListIterator;

import bigBang.library.client.HasValueSelectables;
import bigBang.library.client.Selectable;
import bigBang.library.client.ValueSelectable;
import bigBang.library.client.event.SelectedStateChangedEvent;
import bigBang.library.client.event.SelectedStateChangedEventHandler;
import bigBang.library.client.event.SelectionChangedEvent;
import bigBang.library.client.event.SelectionChangedEventHandler;
import bigBang.library.client.userInterface.view.View;

import com.google.gwt.dom.client.Style.FontStyle;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.DoubleClickEvent;
import com.google.gwt.event.dom.client.DoubleClickHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.UIObject;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class List<T> extends View implements HasValueSelectables<T>, java.util.List<ListEntry<T>> {

	protected java.util.List<ListEntry<T>> entries;
	protected boolean multipleSelection = false;
	protected boolean selectableEntriesEnabled = true;

	//UI
	protected HasWidgets headerContainer;
	protected VerticalPanel listPanel;
	protected Label footerLabel;
	protected HasWidgets footer;
	protected ScrollPanel scrollPanel;
	protected SelectedStateChangedEventHandler entrySelectionHandler;
	protected DoubleClickHandler cellDoubleClickHandler;
	private boolean selectionChangeHandlerInitialized;

	public List(){
		this.entries = new ArrayList<ListEntry<T>>();

		VerticalPanel mainWrapper = new VerticalPanel();
		mainWrapper.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		mainWrapper.setSize("100%", "100%");
		mainWrapper.setStyleName("emptyContainer");

		headerContainer = new SimplePanel();
		((UIObject) headerContainer).setSize("100%", "100%");
		((UIObject) headerContainer).getElement().getStyle().setBackgroundImage("url(images/listHeaderBackground1.png)");
		this.setHeaderWidget(null);

		mainWrapper.add((Widget) headerContainer);
		this.setHeaderWidget(null);

		AbsolutePanel scrollPanelWrapper = new AbsolutePanel();
		scrollPanelWrapper.setSize("100%", "100%");

		scrollPanel = new ScrollPanel();
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
		footerLabel.getElement().getStyle().setMargin(2, Unit.PX);
		((UIObject) footer).setSize("100%", "100%");
		((UIObject) footer).getElement().getStyle().setBackgroundColor("#CCC");
		((UIObject) footer).getElement().getStyle().setProperty("borderTop", "1px solid #BBB");
		mainWrapper.add((Widget) footer);

		cellDoubleClickHandler = new DoubleClickHandler() {
			
			@SuppressWarnings("unchecked")
			@Override
			public void onDoubleClick(DoubleClickEvent event) {
				if(entries.contains(event.getSource()))
					onCellDoubleClicked((ListEntry<T>) event.getSource());
			}
		};
		
		clear();

		initWidget(mainWrapper);

		disableTextSelection(true);
	}


	//Listing Methods

	protected void render() {
		this.listPanel.clear();
		for(ListEntry<T> e : this.entries)
			this.listPanel.add(e);			
	}

	protected HandlerRegistration bindEntry(ListEntry<T> e){
		e.addHandler(this.cellDoubleClickHandler, DoubleClickEvent.getType());
		return e.addHandler(this.entrySelectionHandler, SelectedStateChangedEvent.TYPE);
	}

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
	
	protected void setSelectableEntries(boolean selectable) {
		this.selectableEntriesEnabled = selectable;
		for(ListEntry<T> e : this.entries)
			e.setSelectable(selectable);
	}

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

	public void setHeaderWidget(Widget w) {
		this.headerContainer.clear();
		if (w != null)
			this.headerContainer.add(w);
	}

	public void setFooterWidget(Widget w) {
		this.footer.clear();
		((UIObject) this.footer).setHeight("");
		if (w != null)
			this.footer.add(w);
	}

	public void setFooterText(String text) {
		this.footer.clear();
		((UIObject) this.footer).setHeight("20px");
		this.footerLabel.setText(text);
		this.footer.add(this.footerLabel);
	}

	protected void updateFooterText() {
	};

	protected void onSizeChanged() {
	}

	public void setCheckable(boolean checkable) {
		for (ListEntry<T> entry : this.entries)
			entry.setCheckable(checkable);
	}

	public ArrayList<ListEntry<T>> getCheckedEntries() {
		ArrayList<ListEntry<T>> result = new ArrayList<ListEntry<T>>();
		for (ListEntry<T> e : this.entries) {
			if (e.isChecked())
				result.add(e);
		}
		return result;
	}

	private void selectableStateChanged(Selectable source) {
		if(multipleSelection){
			//TODO throw
		}else{
			for(ListEntry<T> e : entries){
				if(source != e)
					e.setSelected(false, false);
			}
		}
		fireEvent(new SelectionChangedEvent(this.getSelected()));
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
			fireEvent(new SelectionChangedEvent(this.getSelected()));	
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
			this.listPanel.add(e);
			onSizeChanged();
		}
		return result;
	}

	@Override
	public void add(int index, ListEntry<T> element) {
		bindEntry(element);
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
				e.setSelectable(this.selectableEntriesEnabled);
			}
			render();
		}
		onSizeChanged();
		return result;
	}

	@Override
	public void clear() {
		this.entrySelectionHandler = new SelectedStateChangedEventHandler() {

			@Override
			public void onSelectedStateChanged(SelectedStateChangedEvent event) {
				selectableStateChanged((Selectable) event.getSource());
			}
		};
		fireEvent(new SelectionChangedEvent(getSelected()));
		boolean hadElements = !entries.isEmpty();
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
			if(((Selectable) o).isSelected())
				selectNext();
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
	
	public void onCellDoubleClicked(ListEntry<T> entry) {
	}
}
