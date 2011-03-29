package bigBang.library.client.userInterface;

import java.util.Collections;
import java.util.Comparator;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public abstract class FilterableList<T> extends SortableList<T> {

	protected TextBox textBoxFilter;
	
	public FilterableList() {
		super(null);
		
		VerticalPanel headerWrapper = new VerticalPanel();
		SimplePanel newHeaderContainer = new SimplePanel();
		newHeaderContainer.setWidth("100%");
		headerWrapper.setWidth("100%");
		headerWrapper.add(newHeaderContainer);
		
		final TextBox textBoxFilter = new TextBox();
		textBoxFilter.setWidth("100%");

		textBoxFilter.addKeyUpHandler(new KeyUpHandler() {
			
			@Override
			public void onKeyUp(KeyUpEvent event) {
				filterTextChanged(textBoxFilter.getValue());
			}
		});
		
		HorizontalPanel textFilterContainer = new HorizontalPanel();
		textFilterContainer.setWidth("100%");
		textFilterContainer.setSpacing(5);
		textFilterContainer.getElement().getStyle().setProperty("borderTop", "1px solid black");;
		textFilterContainer.add(new Label("Filtrar"));
		textFilterContainer.add(textBoxFilter);
		textFilterContainer.setCellWidth(textBoxFilter, "100%");
		headerWrapper.add(textFilterContainer);
		
		setHeaderWidget(headerWrapper);
		headerContainer = newHeaderContainer;
	}

	
	public void filterEntries() {
		for (ListEntry<T> entry : listEntries) {
			entry.setVisible(!filterOutListEntry(entry));
		}
	}
	
	public void filterTextChanged(String text){
		
	}

	public abstract boolean filterOutListEntry(ListEntry<T> entry);

}
