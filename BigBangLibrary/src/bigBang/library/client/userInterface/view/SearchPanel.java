package bigBang.library.client.userInterface.view;

import bigBang.library.client.userInterface.FilterableList;
import bigBang.library.interfaces.SearchService;
import bigBang.library.interfaces.SearchServiceAsync;
import bigBang.library.shared.SearchResult;

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
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public abstract class SearchPanel extends FilterableList<SearchResult> {

	private final String DEFAULT_TEXT = "Termos de pesquisa";
	
	private boolean liveSearch = false;
	private boolean hasSearchTerms;
	private Button searchButton;
	@SuppressWarnings("unused")
	private Widget filtersWidget;
	private HandlerRegistration liveSearchKeyUpHandlerRegistration;
	
	private SearchServiceAsync service;

	public SearchPanel(){
		this(SearchService.Util.getInstance());
	}
	
	public SearchPanel(SearchServiceAsync service){
		super();
		
		this.service = service;
	
		this.searchButton = new Button("Pesquisar");
		this.searchButton.setSize("80px", "22px");
		this.setLiveSearch(this.liveSearch);

		textBoxFilter.addBlurHandler(new BlurHandler() {

			public void onBlur(BlurEvent event) {
				if(!hasSearchTerms){
					setStandBy(true);
				}
			}
		});
		this.textBoxFilter.addFocusHandler(new FocusHandler() {

			public void onFocus(FocusEvent event) {
				if(!hasSearchTerms){
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
				if(event.getNativeEvent().getKeyCode() == KeyCodes.KEY_ENTER)
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
		
		HorizontalPanel searchFieldWrapper = new HorizontalPanel();
		searchFieldWrapper.setSize("100%", "40px");
		searchFieldWrapper.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		searchFieldWrapper.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		searchFieldWrapper.add(textBoxFilter);
		searchFieldWrapper.setCellWidth(textBoxFilter, "100%");
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
		
		headerWidgetWrapper.add(filtersWrapper);
		this.setHeaderWidget(headerWidgetWrapper);
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

		this.liveSearchKeyUpHandlerRegistration = this.textBoxFilter.addKeyUpHandler(new KeyUpHandler() {
			
			public void onKeyUp(KeyUpEvent event) {
				ValueChangeEvent.fire(textBoxFilter, textBoxFilter.getText());
			}
		});
	}

	private void setStandBy(boolean standBy){
		textBoxFilter.setText(DEFAULT_TEXT);
		searchButton.setEnabled(false);
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
				onResults(result);
				updateFooterText();
			}
			
		};		
		
		try{
			this.service.search(null, null, 0, callback);
		}catch(Exception e){
			GWT.log(e.getMessage());
		}

	}
	
	public abstract void onResults(SearchResult[] results);

}
