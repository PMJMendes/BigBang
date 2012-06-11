package bigBang.library.client.userInterface.view;

import java.util.Collection;

import bigBang.definitions.client.dataAccess.Search;
import bigBang.definitions.client.dataAccess.SearchDataBroker;
import bigBang.definitions.client.response.ResponseError;
import bigBang.definitions.client.response.ResponseHandler;
import bigBang.definitions.shared.SearchParameter;
import bigBang.definitions.shared.SearchResult;
import bigBang.definitions.shared.SortParameter;
import bigBang.library.client.userInterface.FilterableList;
import bigBang.library.client.userInterface.ListEntry;
import bigBang.library.client.userInterface.ListHeader;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.event.dom.client.ScrollEvent;
import com.google.gwt.event.dom.client.ScrollHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * 
 * @author Premium-Minds (Francisco Cabrita)
 * 
 * Implements a List panel with remote search capabilities.
 * This list can be sorted and filtered.  
 */
public abstract class SearchPanel<T extends SearchResult> extends FilterableList<T> {

	protected final int DEFAULT_PAGE_SIZE = 50;

	private boolean liveSearch = false;
	private Button searchButton;
	@SuppressWarnings("unused")
	private Widget filtersWidget;
	private HandlerRegistration liveSearchKeyUpHandlerRegistration;

	protected SearchDataBroker<T> broker;
	protected String workspaceId;
	protected int pageSize = DEFAULT_PAGE_SIZE;
	protected int nextResultIndex = 0;
	protected int numberOfResults = 0;
	protected boolean requestedNextPage = false;
	protected String operationId = null;

	/**
	 * The class constructor
	 * @param the search service to be used.
	 */
	public SearchPanel(SearchDataBroker<T> broker) {
		super();

		super.liveSearch = false;
		this.broker = broker;
		
		this.searchButton = new Button("Pesquisar");
		this.searchButton.setSize("80px", "100%");
		this.setLiveSearch(this.liveSearch);

		this.textBoxFilter.addKeyPressHandler(new KeyPressHandler() {

			public void onKeyPress(KeyPressEvent event) {
				if (event.getNativeEvent().getKeyCode() == KeyCodes.KEY_ENTER)
					doSearch();
			}
		});
		this.textBoxFilter.addHandler(new ValueChangeHandler<String>() {

			public void onValueChange(ValueChangeEvent<String> event) {
				doSearch();
			}

		}, ValueChangeEvent.getType());
		this.searchButton.addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {
				doSearch();
			}
		});

		this.getScrollable().addScrollHandler(new ScrollHandler() {

			@Override
			public void onScroll(ScrollEvent event) {
				checkScrollPosition();
			}
		});

		ListHeader header = new ListHeader();
		
		Widget textFieldOldParent = textBoxFilter.getParent();
		
		HorizontalPanel searchFieldWrapper = new HorizontalPanel();
		searchFieldWrapper.setSize("100%", "40px");
		searchFieldWrapper
				.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		searchFieldWrapper
				.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		searchFieldWrapper.add(textBoxFilter);
		searchFieldWrapper.setCellWidth(textBoxFilter, "100%");
		searchFieldWrapper.add(searchButton);
		searchFieldWrapper.setCellWidth(searchButton, "100px");
		searchFieldWrapper.setSpacing(5);
		searchFieldWrapper.getElement().getStyle()
				.setBackgroundImage("url(images/listHeaderBackground1.png)");

		VerticalPanel headerWidgetWrapper = new VerticalPanel();
		headerWidgetWrapper.setSize("100%", "100%");
		headerWidgetWrapper.add(searchFieldWrapper);
		
		header.setWidget(headerWidgetWrapper);
		textFieldOldParent.removeFromParent();
		
		this.setHeaderWidget(headerWidgetWrapper);
	}
	
	/**
	 * Sets live search on or off
	 * @param liveSearch If true, live search is turned on
	 */
	public void setLiveSearch(boolean liveSearch) {
		this.liveSearch = liveSearch;
		this.searchButton.setVisible(!liveSearch);
		this.searchButton.setEnabled(!liveSearch);

		if (!liveSearch) {
			if (this.liveSearchKeyUpHandlerRegistration != null)
				this.liveSearchKeyUpHandlerRegistration.removeHandler();
			return;
		}

		if (this.liveSearchKeyUpHandlerRegistration != null)
			return;

		this.liveSearchKeyUpHandlerRegistration = this.textBoxFilter
				.addKeyUpHandler(new KeyUpHandler() {

					public void onKeyUp(KeyUpEvent event) {
						ValueChangeEvent.fire(textBoxFilter,
								textBoxFilter.getText());
					}
				});
	}

	@Override
	protected void updateFooterText() {
		int nEntries = this.numberOfResults;
		this.setFooterText((nEntries == 0 ? "Sem" : nEntries) + " resultados");
	}
	
	/**
	 * Sets the id of the Operation for which the results should be open to execution 
	 * @param operationId The id of the operation
	 */
	public void setOperationId(String operationId) {
		this.operationId = operationId;
	}
	
	/**
	 * Queries the search service for elements that match a number of search parameters 
	 * @param parameters The search parameters to be matched
	 */
	public void doSearch(SearchParameter[] parameters, SortParameter[] sorts) {

		try {
			if (workspaceId == null) {
				ResponseHandler<Search<T>> handler = new ResponseHandler<Search<T>>() {

					@Override
					public void onResponse(Search<T> result) {
						workspaceId = result.getWorkspaceId();
						clear();
						scrollPanel.scrollToTop();
						requestedNextPage = false;
						nextResultIndex = result.getResults().size();
						SearchPanel.this.numberOfResults = result.getTotalResultsCount();
						onResults(result.getResults());
						updateFooterText();
					}

					@Override
					public void onError(Collection<ResponseError> errors) {}
				};
				if(this.operationId == null){
					this.broker.search(parameters, sorts, this.pageSize, handler);
				}else{
					this.broker.searchOpenForOperation(this.operationId, parameters, sorts, this.pageSize, handler);
				}
			} else {
				this.broker.disposeSearch(this.workspaceId);
				ResponseHandler<Search<T>> handler = new ResponseHandler<Search<T>>() {

					@Override
					public void onResponse(Search<T> result) {
						clear();
						scrollPanel.scrollToTop();
						requestedNextPage = false;
						nextResultIndex = result.getResults().size();
						SearchPanel.this.numberOfResults = result.getTotalResultsCount();
						onResults(result.getResults());
						updateFooterText();
					}

					@Override
					public void onError(Collection<ResponseError> errors) {}
				};
				if(this.operationId == null){
					this.broker.search(parameters, sorts, this.pageSize, handler);
				}else{
					this.broker.searchOpenForOperation(this.operationId, parameters, sorts, this.pageSize, handler);
				}
			}
		} catch (RuntimeException e) {
			GWT.log(e.getMessage());
		}

		this.filtersContainer.setOpen(false);
	}

	/**
	 * Defines how many entries are fetched at a time
	 * @param size The number of entries to be fetched
	 */
	public void setPageSize(int size) {
		this.pageSize = size;
		this.workspaceId = null;
	}

	/**
	 * Queries the search service for the next page of results
	 */
	protected void fetchNextPage() {
		if(!hasResultsLeft()){
			return;
		}
		requestedNextPage = true;
		this.broker.getResults(this.workspaceId, this.nextResultIndex, this.pageSize, new ResponseHandler<Search<T>>() {
			
			@Override
			public void onResponse(Search<T> result) {
				nextResultIndex += result.getResults().size();
				onResults(result.getResults());
				updateFooterText();
				requestedNextPage = false;
			}
			
			@Override
			public void onError(Collection<ResponseError> errors) {
				requestedNextPage = false;
			}
		});
	}
	
	protected boolean hasResultsLeft(){
		return this.nextResultIndex <= this.numberOfResults;
	}

	/**
	 * Returns the currently defined page size
	 * @return The current page size
	 */
	public int getPageSize() {
		return this.pageSize;
	}
	
	/**
	 * Returns the text inserted into the free text field
	 * @return the free text string
	 */
	protected String getFreeText(){
		return this.textBoxFilter.getText();
	}
	
	@Override
	public ListEntry<T> remove(int index) {
		ListEntry<T> result = super.remove(index);
		checkScrollPosition();
		return result;		
	}
	
	@Override
	public boolean remove(Object o) {
		boolean result = super.remove(o);
		checkScrollPosition();
		return result;
	}
	
	@Override
	public boolean removeAll(Collection<?> c) {
		boolean result = super.removeAll(c);
		checkScrollPosition();
		return result;
	}
	
	protected void checkScrollPosition(){
		if (((this.getScrollable().getMaximumVerticalScrollPosition() - this.getScrollable()
				.getVerticalScrollPosition()) < 300) && !SearchPanel.this.requestedNextPage) {
			SearchPanel.this.fetchNextPage();
		}
	}
	
	public SearchDataBroker<T> getSearchBroker(){
		return this.broker;
	}
	
	public String getWorkspaceId(){
		return workspaceId;
	}
	
	/**
	 * Performs A search query to the class' defined search service
	 */
	public abstract void doSearch();

	/**
	 * This function is invoked when results are received after a query.
	 * @param results The array of results
	 */
	public abstract void onResults(Collection<T> results);

}
