package bigBang.library.client.userInterface.view;

import bigBang.library.client.BigBangAsyncCallback;
import bigBang.library.client.userInterface.FilterableList;
import bigBang.library.client.userInterface.ListHeader;
import bigBang.library.interfaces.SearchServiceAsync;
import bigBang.library.shared.NewSearchResult;
import bigBang.library.shared.SearchParameter;
import bigBang.library.shared.SearchResult;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.FocusEvent;
import com.google.gwt.event.dom.client.FocusHandler;
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
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * 
 * @author Premium-Minds (Francisco Cabrita)
 * 
 * Implements a List panel with remote search capabilities.
 * This list can be sorted and filtered.  
 */
public abstract class SearchPanel extends FilterableList<SearchResult> {

	private final String DEFAULT_TEXT = "Termos de pesquisa";
	protected final int DEFAULT_PAGE_SIZE = 50;

	private boolean liveSearch = false;
	private boolean hasSearchTerms;
	private Button searchButton;
	@SuppressWarnings("unused")
	private Widget filtersWidget;
	private HandlerRegistration liveSearchKeyUpHandlerRegistration;

	private SearchServiceAsync service;
	private String workspaceId;
	protected int pageSize = DEFAULT_PAGE_SIZE;
	protected int nextResultIndex = 0;
	protected int numberOfResults = 0;
	protected boolean requestedNextPage = false;

	/**
	 * The class constructor
	 * @param the search service to be used.
	 */
	public SearchPanel(SearchServiceAsync service) {
		super();

		super.liveSearch = false;
		this.service = service;

		this.searchButton = new Button("Pesquisar");
		this.searchButton.setSize("80px", "100%");
		this.setLiveSearch(this.liveSearch);

		textBoxFilter.addBlurHandler(new BlurHandler() {

			public void onBlur(BlurEvent event) {
				if (!hasSearchTerms) {
					setStandBy(true);
				}
			}
		});
		this.textBoxFilter.addFocusHandler(new FocusHandler() {

			public void onFocus(FocusEvent event) {
				if (!hasSearchTerms) {
					setStandBy(false);
					textBoxFilter.setText("");
				}
				searchButton.setEnabled(hasSearchTerms);
			}
		});
		this.textBoxFilter.addKeyUpHandler(new KeyUpHandler() {

			public void onKeyUp(KeyUpEvent event) {
				hasSearchTerms = !textBoxFilter.getText().equals("");
				searchButton.setEnabled(hasSearchTerms);
			}
		});
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

		final ScrollPanel scroll = this.getScrollable();
		scroll.addScrollHandler(new ScrollHandler() {

			@Override
			public void onScroll(ScrollEvent event) {
				if (((scroll.getMaximumVerticalScrollPosition() - scroll
						.getVerticalScrollPosition()) < 300) && !SearchPanel.this.requestedNextPage) {
					SearchPanel.this.fetchNextPage();
				}
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
		
		/*DisclosurePanel filtersWrapper = new DisclosurePanel();
		filtersWrapper.setSize("100%", "100%");
		Label headerLabel = new Label("Filtros");
		headerLabel.getElement().getStyle().setMarginLeft(10, Unit.PX);
		headerLabel.getElement().getStyle().setFontWeight(FontWeight.BOLD);
		VerticalPanel header = new VerticalPanel();
		header.setSize("100%", "100%");
		header.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		header.add(headerLabel);*/
		/*filtersWrapper.setHeader(header);
		filtersWrapper.getHeader().getElement().getStyle()
				.setBackgroundImage("url(images/listHeaderBackground1.png)");
		filtersWrapper.getHeader().getElement().getStyle()
				.setProperty("borderTop", "1px solid gray");
		filtersWrapper.getHeader().setHeight("20px");
		filtersWrapper.setOpen(false);
		filtersWrapper.setAnimationEnabled(true);
		filtersWrapper.setContent(new Label("Isto é um filtro"));
		filtersWrapper.getElement().getStyle().setBackgroundColor("#F6F6F6");

		this.filtersWidget = (Widget) filtersWrapper;

		headerWidgetWrapper.add(filtersWrapper);*/
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

	/**
	 * Shows a loading panel
	 * @param standBy shows a loading panel if true
	 */
	private void setStandBy(boolean standBy) {
		textBoxFilter.setText(DEFAULT_TEXT);
		searchButton.setEnabled(false);
	}

	@Override
	protected void updateFooterText() {
		int nEntries = this.numberOfResults;
		this.setFooterText((nEntries == 0 ? "Sem" : nEntries) + " resultados");
	}

	/**
	 * Queries the search service for elements that match a number of search parameters 
	 * @param parameters The search parameters to be matched
	 */
	public void doSearch(SearchParameter[] parameters) {

		try {
			if (workspaceId == null) {
				BigBangAsyncCallback<NewSearchResult> callback = new BigBangAsyncCallback<NewSearchResult>() {

					public void onSuccess(NewSearchResult result) {
						workspaceId = result.workspaceId;
						clear();
						nextResultIndex += result.results.length;
						SearchPanel.this.numberOfResults = result.totalCount;
						onResults(result.results);
						updateFooterText();
					}

				};
				this.service.openSearch(parameters, this.pageSize, callback);
			} else {
				BigBangAsyncCallback<NewSearchResult> callback = new BigBangAsyncCallback<NewSearchResult>() {

					public void onSuccess(NewSearchResult result) {
						clear();
						nextResultIndex += result.results.length;
						SearchPanel.this.numberOfResults = result.totalCount;
						onResults(result.results);
						updateFooterText();
					}

				};
				this.service.search(this.workspaceId, parameters, this.pageSize, callback);
			}
		} catch (Exception e) {
			GWT.log(e.getMessage());
		}

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
		if(this.nextResultIndex > this.numberOfResults)
			return;
		requestedNextPage = true;
		this.service.getResults(this.workspaceId, this.nextResultIndex, this.pageSize, new BigBangAsyncCallback<SearchResult[]>() {

			@Override
			public void onSuccess(SearchResult[] result) {
				nextResultIndex += result.length;
				onResults(result);
				updateFooterText();
				requestedNextPage = false;
			}
			
			@Override
			public void onFailure(Throwable caught) {
				requestedNextPage = false;
				super.onFailure(caught);
			}
		});
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

	/**
	 * Performs A search query to the class' defined search service
	 */
	public abstract void doSearch();

	/**
	 * This function is invoked when results are received after a query.
	 * @param results The array of results
	 */
	public abstract void onResults(SearchResult[] results);

}
