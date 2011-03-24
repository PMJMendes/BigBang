package bigBang.library.client.userInterface.view;

import java.util.ArrayList;

import bigBang.library.client.FormField;

import com.google.gwt.dom.client.Style.FontWeight;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.ui.DisclosurePanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class FormViewSection extends View {
	
	private ArrayList<FormField<?>> fields;
	private Label headerLabel;
	private HasWidgets content;
	private Widget header;
	
	private DisclosurePanel errorMessagePanel;
	private ArrayList<String> errorMessages;
	
	public FormViewSection(String title){
		fields = new ArrayList<FormField<?>>();
		errorMessages = new ArrayList<String>();
		VerticalPanel wrapper = new VerticalPanel();
		wrapper.setSize("100%", "100%");
		
		if(title != null){
			wrapper.add(getSectionHeader(title));
		}
		
		wrapper.add(getErrorMessagesPanel());
		wrapper.setCellHorizontalAlignment(this.errorMessagePanel, HasHorizontalAlignment.ALIGN_CENTER);
		
		VerticalPanel p = new VerticalPanel();
		p.setWidth("100%");
		
		//String minHeight = "30px";
		//p.setSize("100%", minHeight);
		p.setStyleName("formSection");
		p.setSpacing(10);
		wrapper.add(p);
		
		content = p;

		initWidget(wrapper);
	}
	
	public ArrayList<FormField<?>> getFields(){
		return this.fields;
	}
	
	private Widget getSectionHeader(String text) {
		VerticalPanel headerWrapper = new VerticalPanel();
		headerWrapper.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		headerWrapper.setWidth("100%");
		headerWrapper.setStylePrimaryName("formDisclosureSectionHeader");
		Label titleLabel = new Label(text);
		this.headerLabel = titleLabel;
		titleLabel.getElement().getStyle().setColor("#FFFFFF");
		titleLabel.getElement().getStyle().setFontWeight(FontWeight.BOLD);
		headerWrapper.add(titleLabel);
		headerWrapper.getElement().getStyle().setPaddingLeft(10, Unit.PX);
		headerWrapper.setHeight("22px");
		this.header = headerWrapper;
		return headerWrapper;
	}
	
	private Widget getErrorMessagesPanel(){
		DisclosurePanel wrapper = new DisclosurePanel();
		wrapper.setOpen(false);
		wrapper.setAnimationEnabled(true);
		wrapper.setWidth("500px");
		this.errorMessagePanel = wrapper;
		return wrapper;
	}
	
	
	public void setHeaderText(String text){
		headerLabel.setText(text);
	}
	
	public String getHeaderText(){
		return headerLabel.getText();
	}

	public void addFormField(FormField<?> field) {
		this.fields.add(field);
		HorizontalPanel wrapper = new HorizontalPanel();
		wrapper.add(field);
		addWidget(wrapper);
	}
	
	public void clear(){
		this.fields.clear();
		this.errorMessagePanel.clear();
		this.content.clear();
		this.errorMessages.clear();
	}
	
	public void addWidget(Widget w){
		content.add(w);
	}

	public void setContent(Widget content) {
		this.content.clear();
		this.content.add(content);
	}
	
	public void clearErrorMessages(){
		clearErrorMessages(true);
	}
	
	public void clearErrorMessages(boolean hide) {
		errorMessagePanel.clear();
		errorMessages.clear();
		showErrorMessages(!hide);
	}
	
	public void addErrorMessage(String message, boolean show) {
		showErrorMessages(show);
	}
	
	public void addErrorMessage(String message) {
		addErrorMessage(message, true);
		this.errorMessages.add(message);
	}
	
	public void showErrorMessages(boolean show) {
		errorMessagePanel.clear();
		if(errorMessages.size() == 0){
			errorMessages.clear();
			errorMessagePanel.setOpen(false);
			return;
		}
		VerticalPanel messagesWrapper = new VerticalPanel();
		messagesWrapper.setSize("100%", "100%");
		messagesWrapper.getElement().getStyle().setBorderColor("red");
		messagesWrapper.getElement().getStyle().setBackgroundColor("#ffd497");
		messagesWrapper.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		for(String m : errorMessages){
			messagesWrapper.add(new Label(m));
		}
		this.errorMessagePanel.setContent(messagesWrapper);
		this.errorMessagePanel.setOpen(show);
	}

	public Widget getHeader() {
		return this.header;
	}

}
