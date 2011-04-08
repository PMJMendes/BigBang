package bigBang.library.client.userInterface;

import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.UIObject;
import com.google.gwt.user.client.ui.VerticalPanel;

public class FilterableList<T> extends SortableList<T> {

	protected TextBox textBoxFilter;
	protected HasWidgets filtersContainer;
	
	public FilterableList() {
		super();
		
		VerticalPanel headerWrapper = new VerticalPanel();
		SimplePanel newHeaderContainer = new SimplePanel();
		newHeaderContainer.setWidth("100%");
		headerWrapper.setWidth("100%");
		headerWrapper.add(newHeaderContainer);
		
		textBoxFilter = new TextBox();
		textBoxFilter.setWidth("100%");

		textBoxFilter.addKeyUpHandler(new KeyUpHandler() {
			
			@Override
			public void onKeyUp(KeyUpEvent event) {
				onFilterTextChanged(textBoxFilter.getValue());
			}
		});
		
		HorizontalPanel textFilterContainer = new HorizontalPanel();
		textFilterContainer.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		textFilterContainer.setWidth("100%");
		textFilterContainer.setSpacing(5);
		textFilterContainer.getElement().getStyle().setProperty("borderTop", "1px solid gray");
		textFilterContainer.add(new Label("Filtrar"));
		textFilterContainer.add(textBoxFilter);
		textFilterContainer.setCellWidth(textBoxFilter, "100%");
		filtersContainer = textFilterContainer;
		headerWrapper.add(textFilterContainer);
		
		setHeaderWidget(headerWrapper);
		headerContainer = newHeaderContainer;
	}

	
	public void filterEntries() {
		for (ListEntry<T> entry : this.entries) {
			entry.setVisible(!filterOutListEntry(entry));
		}
	}
	
	public void onFilterTextChanged(String text){
		sortListEntries();
		filterEntries();
	}

	public boolean filterOutListEntry(ListEntry<T> entry) {
		String text = entry.getText().toUpperCase();
		String title = entry.getTitle().toUpperCase();
		String token = textBoxFilter.getValue().toUpperCase();
		return !((text != null && text.contains(token)) || (title != null && title.contains(token)));
	}
	
	public void clearFilters(){
		textBoxFilter.setValue("");
		filterEntries();
	}
	
	public void showFilterField(boolean show) {
		((UIObject) this.filtersContainer).setVisible(show);
	}

}
