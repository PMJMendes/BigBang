package bigBang.library.shared.userInterface.view;

import java.util.ArrayList;

import bigBang.library.shared.userInterface.SearchPreviewListEntry;

import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.StackPanel;
import com.google.gwt.user.client.ui.Widget;

public class PreviewPanel<T> extends View implements HasValue<T>  {

	private StackPanel stackPanel;
	private AbsolutePanel individualSection;
	private AbsolutePanel multipleSection;
	
	private boolean valueChangeHandlerInitialized;
	
	private T value;
	
	public PreviewPanel(){
		this.stackPanel = new StackPanel();
		this.stackPanel.setSize("100%", "100%");

		initWidget(this.stackPanel);
	}

	public void showMultipleSection(boolean show) {
		if((show && this.multipleSection != null) || (!show && this.multipleSection == null))
			return;
		if(show && this.multipleSection == null){
			this.multipleSection = new AbsolutePanel();
			this.multipleSection.setSize("100%", "100%");
			this.stackPanel.add(this.multipleSection, "Selection");
			return;
		}
		if(!show && this.multipleSection != null){
			this.stackPanel.remove(this.multipleSection);
			this.multipleSection.removeFromParent();
			this.multipleSection = null;
		}		
	}
	
	public void setMultipleSectionHeaderText(String text) {
		showMultipleSection(true);
		stackPanel.setStackText(this.stackPanel.getWidgetIndex(this.multipleSection), text);
	}

	public void showIndividualSection(boolean show) {
		if((show && this.individualSection != null) || (!show && this.individualSection == null))
			return;
		if(show && this.individualSection == null){
			this.individualSection = new AbsolutePanel();
			this.individualSection.setSize("100%", "100%");			
			this.stackPanel.add(this.individualSection, "Individual");
			SimplePanel shadowTop = new SimplePanel();
			shadowTop.setSize("100%", "3px");
			shadowTop.getElement().getStyle().setBackgroundImage("url(images/listTopShadow1.png)");
			this.individualSection.add(shadowTop, 0, 0);
			return;
		}
		if(!show && this.individualSection != null){
			this.stackPanel.remove(this.individualSection);
			this.individualSection.removeFromParent();
			this.individualSection = null;
		}
	}

	public void setIndividualSectionHeaderText(String text) {
		showIndividualSection(true);
		stackPanel.setStackText(this.stackPanel.getWidgetIndex(this.individualSection), text);
	}
	
	public void setIndividualSectionContent(Widget w) {
		showIndividualSection(true);
		this.individualSection.clear();
		this.individualSection.add(w, 0, 0);
		SimplePanel shadowTop = new SimplePanel();
		shadowTop.setSize("100%", "3px");
		shadowTop.getElement().getStyle().setBackgroundImage("url(images/listTopShadow1.png)");
		this.individualSection.add(shadowTop, 0, 0);
	}

	public void setMultipleSectionEntries(
			ArrayList<SearchPreviewListEntry> entries) {
		// TODO Auto-generated method stub
		
	}

	public void setMultipleSectionEntry(SearchPreviewListEntry entry) {
		// TODO Auto-generated method stub
		
	}

	public HandlerRegistration addValueChangeHandler(
			ValueChangeHandler<T> handler) {
		if (!valueChangeHandlerInitialized)
			valueChangeHandlerInitialized = true;

		return addHandler(handler, ValueChangeEvent.getType());
	}

	public T getValue() {
		return value;
	}

	public void setValue(T value) {
		setValue(value, true);
	}

	public void setValue(T value, boolean fireEvents) {
		this.value = value;
		if(fireEvents)
			ValueChangeEvent.fire(this, this.value);
	}
	
	public PreviewPanel<T> getInstance(){
		return new PreviewPanel<T>();
	}
}
