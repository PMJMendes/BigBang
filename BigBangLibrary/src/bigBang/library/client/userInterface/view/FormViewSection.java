package bigBang.library.client.userInterface.view;

import java.util.ArrayList;

import bigBang.library.client.FormField;

import com.google.gwt.dom.client.Style.Float;
import com.google.gwt.dom.client.Style.FontWeight;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.ui.DisclosurePanel;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class FormViewSection extends View {

	protected static final String DEFAULT_FIELD_HEIGHT = "45px";
	
	private ArrayList<FormField<?>> fields;
	private Label headerLabel;
	private HasWidgets content;
	private HasWidgets currentContainer;
	private Widget header;

	private DisclosurePanel errorMessagePanel;
	private ArrayList<String> errorMessages;

	public FormViewSection(String title){
		fields = new ArrayList<FormField<?>>();
		errorMessages = new ArrayList<String>();
		VerticalPanel wrapper = new VerticalPanel();
		initWidget(wrapper);
		wrapper.setSize("100%", "100%");
		wrapper.getElement().getStyle().setMarginBottom(5, Unit.PX);

		//Verificar as larguras e a margem de baixo da secção
		
		if(title != null){
			wrapper.add(getSectionHeader(title));
		}

		DisclosurePanel errorMessagesWrapper = new DisclosurePanel();
		wrapper.add(errorMessagesWrapper);
		errorMessagesWrapper.setOpen(false);
		errorMessagesWrapper.setAnimationEnabled(true);
		errorMessagesWrapper.setWidth("500px");
		this.errorMessagePanel = errorMessagesWrapper;
		
		wrapper.setCellHorizontalAlignment(this.errorMessagePanel, HasHorizontalAlignment.ALIGN_CENTER);

		FlowPanel p = new FlowPanel();
		p.setWidth("100%");

		//String minHeight = "30px";
		//p.setSize("100%", minHeight);
		p.setStyleName("formSection");
		wrapper.add(p);

		content = p;
		currentContainer = new FlowPanel();
		content.add((Widget) currentContainer);
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

	public void setHeaderText(String text){
		headerLabel.setText(text);
	}

	public String getHeaderText(){
		return headerLabel.getText();
	}

	public void addFormField(FormField<?> field, boolean inline) {
		if(field != null) {
			field.setHeight(DEFAULT_FIELD_HEIGHT);
			registerFormField(field);
			FlowPanel wrapper = new FlowPanel();
			addWidget(wrapper);
			if(inline){
				wrapper.getElement().getStyle().setFloat(Float.LEFT);
			}
			wrapper.add(field);
		}
	}
	
	public void addFormField(FormField<?> field) {
		this.addFormField(field, false);
	} 
	
	public void addFormFieldGroup(FormField<?>[] group, boolean inline){
		VerticalPanel wrapper = new VerticalPanel();
		wrapper.getElement().getStyle().setProperty("borderRight", "1px solid #BBB");
		for(int i = 0; i < group.length; i++) {
			group[i].setHeight(DEFAULT_FIELD_HEIGHT);
			registerFormField(group[i]);
			wrapper.add(group[i]);
		}
		if(inline) {
			wrapper.getElement().getStyle().setFloat(Float.LEFT);
		}
		addWidget(wrapper);
	}

	public void registerFormField(FormField<?> field) {
		this.fields.add(field);
	}
	
	public void unregisterFormField(FormField<?> field){
		field.removeFromParent();
		this.fields.remove(field);
	}
	
	public void unregisterAllFormFields(){
		for(FormField<?> f : this.fields){
			unregisterFormField(f);
		}
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

	public HasWidgets getContentWrapper() {
		return this.content;
	}

}
