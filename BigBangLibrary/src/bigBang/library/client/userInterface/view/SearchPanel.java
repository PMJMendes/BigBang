package bigBang.library.client.userInterface.view;

import java.util.ArrayList;
import java.util.Collection;

import bigBang.library.client.SearchResult;
import bigBang.library.client.userInterface.List;
import bigBang.library.client.userInterface.ListEntry;
import bigBang.library.client.userInterface.SearchPanelListEntry;
import bigBang.library.interfaces.SearchService;
import bigBang.library.interfaces.SearchServiceAsync;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.FontWeight;
import com.google.gwt.dom.client.Style.Unit;
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
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DisclosurePanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class SearchPanel<T> extends List<T> {

	private final String DEFAULT_TEXT = "Termos de pesquisa";
	
	private boolean multipleSelection = true;
	private boolean liveSearch = false;
	private boolean hasSearchTerms = false;

	private TextBox searchField;
	private Button searchButton;
	
	private Widget filtersWidget;
	
	private HandlerRegistration liveSearchKeyUpHandlerRegistration;
	
	private SearchServiceAsync service;

	public SearchPanel(){
		this(SearchService.Util.getInstance());
	}
	
	public SearchPanel(SearchServiceAsync service){
		super();
		
		this.service = service;
		
		this.searchField = new TextBox();
		this.searchField.setText(DEFAULT_TEXT);
		this.searchField.setWidth("100%");

		this.searchButton = new Button("Pesquisar");
		this.searchButton.setSize("80px", "22px");
		this.setLiveSearch(this.liveSearch);

		this.searchField.addBlurHandler(new BlurHandler() {

			public void onBlur(BlurEvent event) {
				if(!hasSearchTerms){
					setStandBy(true);
				}
			}
		});
		this.searchField.addFocusHandler(new FocusHandler() {

			public void onFocus(FocusEvent event) {
				if(!hasSearchTerms){
					setStandBy(false);
					searchField.setText("");
				}
				searchButton.setEnabled(hasSearchTerms);
			}
		});
		this.searchField.addKeyUpHandler(new KeyUpHandler() {
			
			public void onKeyUp(KeyUpEvent event) {
				hasSearchTerms = !searchField.getText().equals("");
				searchButton.setEnabled(hasSearchTerms);
			}
		});
		this.searchField.addKeyPressHandler(new KeyPressHandler() {
			
			public void onKeyPress(KeyPressEvent event) {
				if(event.getNativeEvent().getKeyCode() == KeyCodes.KEY_ENTER)
					doSearch();
			}
		});
		this.searchField.addHandler(new ValueChangeHandler<String>() {

			public void onValueChange(ValueChangeEvent<String> event) {
				doSearch();
			}
			
		}, ValueChangeEvent.getType());
		this.searchButton.addClickHandler(new ClickHandler() {
			
			public void onClick(ClickEvent event) {
				doSearch();
			}
		});
		
		HorizontalPanel searchFieldWrapper = new HorizontalPanel();
		searchFieldWrapper.setSize("100%", "40px");
		searchFieldWrapper.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		searchFieldWrapper.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		searchFieldWrapper.add(searchField);
		searchFieldWrapper.setCellWidth(searchField, "100%");
		searchFieldWrapper.add(searchButton);
		searchFieldWrapper.setCellWidth(searchButton, "100px");
		searchFieldWrapper.setSpacing(5);
		searchFieldWrapper.getElement().getStyle().setBackgroundImage("url(images/listHeaderBackground1.png)");
		
		VerticalPanel headerWidgetWrapper = new VerticalPanel();
		headerWidgetWrapper.setSize("100%", "100%");
		headerWidgetWrapper.add(searchFieldWrapper);
		
		DisclosurePanel filtersWrapper = new DisclosurePanel();
		filtersWrapper.setSize("100%", "100%");
		Label headerLabel = new Label("Filtros");
		headerLabel.getElement().getStyle().setMarginLeft(10, Unit.PX);
		headerLabel.getElement().getStyle().setFontWeight(FontWeight.BOLD);
		VerticalPanel header = new VerticalPanel();
		header.setSize("100%", "100%");
		header.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		header.add(headerLabel);
		filtersWrapper.setHeader(header);
		filtersWrapper.getHeader().getElement().getStyle().setBackgroundImage("url(images/listHeaderBackground1.png)");
		filtersWrapper.getHeader().getElement().getStyle().setProperty("borderTop", "1px solid gray");
		filtersWrapper.getHeader().setHeight("20px");
		filtersWrapper.setOpen(false);
		filtersWrapper.setAnimationEnabled(true);
		filtersWrapper.setContent(new Label("Isto é um filtro"));
		filtersWrapper.getElement().getStyle().setBackgroundColor("#F6F6F6");
		
		this.filtersWidget = (Widget)filtersWrapper;
		this.showFilters(false);
		
		headerWidgetWrapper.add(filtersWrapper);
		this.setHeaderWidget(headerWidgetWrapper);

		setResults(null);
	}

	public void setMultipleSelection(boolean multiple) {
		this.multipleSelection = multiple;
		for(ListEntry<T> e : this.entries)
			((SearchPanelListEntry<T>)e).setCheckable(multiple);
	}
	
	public void showFilters(boolean show) {
		this.filtersWidget.setVisible(show);
	}

	public void setLiveSearch(boolean liveSearch) {
		this.liveSearch = liveSearch;
		this.searchButton.setVisible(!liveSearch);
		this.searchButton.setEnabled(!liveSearch);

		if(!liveSearch){
			if(this.liveSearchKeyUpHandlerRegistration != null)
				this.liveSearchKeyUpHandlerRegistration.removeHandler();
			return;
		}

		if(this.liveSearchKeyUpHandlerRegistration != null)
			return;

		this.liveSearchKeyUpHandlerRegistration = this.searchField.addKeyUpHandler(new KeyUpHandler() {
			
			public void onKeyUp(KeyUpEvent event) {
				ValueChangeEvent.fire(searchField, searchField.getText());
			}
		});
	}

	private void setStandBy(boolean standBy){
		searchField.setText(DEFAULT_TEXT);
		searchButton.setEnabled(false);
	}
	
	@SuppressWarnings("unchecked")
	public void setResults(ArrayList <SearchPanelListEntry<String>> results){
		this.clear();
		Collection <?> col = results;
		clear();
		//addAll((Collection<ListEntry<T>>) col);		
		updateFooterText();
	}

	@Override
	protected void updateFooterText(){
		int nEntries = this.size();
		this.setFooterText((nEntries == 0 ? "Sem" : nEntries) + " resultados");
	}

	public void doSearch() {
		AsyncCallback<SearchResult[]> callback = new AsyncCallback<SearchResult[]>() {

			public void onFailure(Throwable caught) {
				GWT.log("Search service has failed");
			}

			public void onSuccess(SearchResult[] result) {
				clear();
				renderResults(result);
				updateFooterText();
			}
			
		};		
		
		try{
			this.service.search(null, null, this.searchField.getText(), callback);
		}catch(Exception e){
			GWT.log(e.getMessage());
		}

	}
	
	protected void renderResults(SearchResult[] results){
		for(int i = 0; i < results.length; i++)
			add(new SearchPanelListEntry(i+""));
	}

}
