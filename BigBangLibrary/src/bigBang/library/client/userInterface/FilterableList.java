package bigBang.library.client.userInterface;

import java.util.HashMap;
import java.util.Map;

import bigBang.library.client.ListFilter;
import bigBang.library.client.resources.Resources;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.event.logical.shared.CloseEvent;
import com.google.gwt.event.logical.shared.CloseHandler;
import com.google.gwt.event.logical.shared.OpenEvent;
import com.google.gwt.event.logical.shared.OpenHandler;
import com.google.gwt.user.client.ui.DisclosurePanel;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.UIObject;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * @author Premium-Minds (Francisco Cabrita)
 *
 * A class that implements a list with filtering capabilities.
 * @param <T> The type of value held by the list entries
 */
public class FilterableList<T> extends SortableList<T> {

	protected TextBox textBoxFilter;
	protected HasWidgets filtersContainer;
	protected HorizontalPanel searchFieldContainer;
	protected Map<String, ListFilter<?>> filters;
	protected boolean liveSearch = true;

	/**
	 * The FilterableList constructor
	 */
	public FilterableList() {
		super();

		this.filters = new HashMap<String, ListFilter<?>>();
		Resources resources = GWT.create(Resources.class);		
		
		final VerticalPanel headerWrapper = new VerticalPanel();

		SimplePanel newHeaderContainer = new SimplePanel();
		newHeaderContainer.setWidth("100%");
		headerWrapper.setWidth("100%");
		headerWrapper.add(newHeaderContainer);

		textBoxFilter = new TextBox();
		textBoxFilter.setWidth("100%");

		textBoxFilter.addKeyUpHandler(new KeyUpHandler() {

			@Override
			public void onKeyUp(KeyUpEvent event) {
				if(FilterableList.this.liveSearch)
					onFilterTextChanged(textBoxFilter.getValue());
			}
		});

		DisclosurePanel filterContainer = new DisclosurePanel();
		filterContainer.getElement().getStyle().setBackgroundColor("#DDD");
		filterContainer.setAnimationEnabled(true);
		filterContainer.setSize("100%", "100%");
		filterContainer.getElement().getStyle().setProperty("borderTop", "1px solid gray");
		
		HorizontalPanel filterHeaderWrapper = new HorizontalPanel();
		filterHeaderWrapper.setSize("100%", "100%");
		Image filterHeaderImage = new Image(resources.arrowDown());
		filterHeaderImage.getElement().getStyle().setMarginLeft(5, Unit.PX);
		filterHeaderWrapper.add(filterHeaderImage);
		filterHeaderWrapper.setCellWidth(filterHeaderImage, "20px");
		filterHeaderWrapper.add(new Label("Filtros"));
		filterContainer.setHeader(filterHeaderWrapper);
		
		filtersContainer = filterContainer;
		
		this.searchFieldContainer = new HorizontalPanel();
		searchFieldContainer.setSize("100%", "100%");
		searchFieldContainer.setSpacing(5);
		searchFieldContainer.add(textBoxFilter);
	
		headerWrapper.add(searchFieldContainer);
		headerWrapper.add(filterContainer);

		filterContainer.addOpenHandler(new OpenHandler<DisclosurePanel>() {
			
			@Override
			public void onOpen(OpenEvent<DisclosurePanel> event) {
				((DisclosurePanel) filtersContainer).getContent().setHeight(scrollPanel.getOffsetHeight() + "px");
			}
		});
		
		filterContainer.addCloseHandler(new CloseHandler<DisclosurePanel>() {
			
			@Override
			public void onClose(CloseEvent<DisclosurePanel> event) {
				headerWrapper.setCellHeight((Widget) filtersContainer, "auto");
			}
		});
		
		filterContainer.setContent(new SimplePanel());
		
		setHeaderWidget(headerWrapper);
		headerContainer = newHeaderContainer;
	}

	/**
	 * Matches the list entries to the defined filters and hides/shows them accordingly
	 */
	public void filterEntries() {
		for (ListEntry<T> entry : this.entries) {
			entry.setVisible(!filterOutListEntry(entry));
		}
	}
	
	/**
	 * Invoked when the freetext filter value is changed
	 * @param text the current freetext value
	 */
	protected void onFilterTextChanged(String text){
		sortListEntries();
		filterEntries();
	}

	/**
	 * Filters a specific list entry. If it does not meet the filters criteria, the entry is hidden 
	 * @param entry The entry to which the filters are applied
	 * @return true if the entry does not match the filters
	 */
	protected boolean filterOutListEntry(ListEntry<T> entry) {
		String text = entry.getText().toUpperCase();
		String title = entry.getTitle().toUpperCase();
		String token = textBoxFilter.getValue().toUpperCase();
		return !((text != null && text.contains(token)) || (title != null && title.contains(token)));
	}
	
	/**
	 * Clears all the filters values for the current list
	 */
	public void clearFilters(){
		textBoxFilter.setValue("");
		for(ListFilter<?> f : this.filters.values()) {
			f.clearValue();
		}
		filterEntries();
	}
	
	/**
	 * shows or hides the text field
	 * @param show if true, shows the text field
	 */
	public void showSearchField(boolean show) {
		((UIObject) this.searchFieldContainer).setVisible(show);
	}
	
	/**
	 * shows or hides the filter disclosure panel
	 * @param show if true, shows the filter disclosure panel
	 */
	public void showFilterField(boolean show) {
		((UIObject) this.filtersContainer).setVisible(show);
	}

	/**
	 * returns the panel wrapping the filters
	 * @return
	 */
	protected HasWidgets getFiltersPanel(){
		VerticalPanel panel = new VerticalPanel();
		panel.setSize("100%", "100%");
		
		return panel;
	}
	
	/**
	 * Adds a filter to the list
	 * @param filter the filter to add
	 */
	public void addFilter(ListFilter<?> filter) {
		this.filters.put(filter.getName(), filter);
	}
	
	public void removeFilter(ListFilter<?> filter){
		this.filters.remove(filter);
	}
	
	/**
	 * @return The map with the current list's filters
	 */
	public Map<String, ListFilter<?>> getFilters(){
		return this.filters;
	}
	
	/**
	 * Sets whether or not the list is filtered in real time
	 * @param liveSearch if true, live search is turned on
	 */
	public void setLiveSearch(boolean liveSearch){
		this.liveSearch = liveSearch;
	}
}
