package bigBang.library.client.userInterface.view;

import java.util.ArrayList;

import bigBang.library.client.FormField;
import bigBang.library.client.HasEditableValue;
import bigBang.library.client.Validatable;

import com.google.gwt.dom.client.Style.Float;
import com.google.gwt.dom.client.Style.FontStyle;
import com.google.gwt.dom.client.Style.FontWeight;
import com.google.gwt.dom.client.Style.Position;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.AttachEvent;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.AbstractImagePrototype;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public abstract class FormView<T> extends View implements Validatable, HasEditableValue<T> {

	protected AbsolutePanel mainWrapper;
	protected VerticalPanel panel;
	protected FormViewSection currentSection = null;
	protected ArrayList<FormViewSection> sections;

	protected HorizontalPanel topButtonWrapper;
	protected HorizontalPanel topToolbar;
	protected ScrollPanel scrollWrapper;

	private boolean isReadOnly;
	
	protected T value;
	private boolean valueChangeHandlerInitialized;


	public FormView(){
		sections = new ArrayList<FormViewSection>();
		mainWrapper = new AbsolutePanel();
		initWidget(mainWrapper);
		mainWrapper.setSize("100%", "100%");
		
		ScrollPanel wrapper = new ScrollPanel();
		this.scrollWrapper = wrapper;
		wrapper.getElement().getStyle().setProperty("overflowX", "hidden");
		this.panel = new VerticalPanel();
		this.panel.setSize("100%", "100%");
		this.panel.setStyleName("formPanel");
		wrapper.setSize("100%", "100%");
		wrapper.add(this.panel);
		wrapper.setStyleName("formPanel");

		mainWrapper.add(wrapper, 0, 0);
		
		topToolbar = new HorizontalPanel();
		topToolbar.setSize("100%", "0px");
		
		topButtonWrapper = new HorizontalPanel();
		topButtonWrapper.getElement().getStyle().setRight(20, Unit.PX);
		topButtonWrapper.getElement().getStyle().setPosition(Position.ABSOLUTE);
		topButtonWrapper.getElement().getStyle().setFloat(Float.RIGHT);
	
		//SimplePanel filler = new SimplePanel();
		////filler.setSize("100%", "0px");
		//topToolbar.add(filler);
		//topToolbar.setCellWidth(filler, "100%");
		//topToolbar.setCellHeight(filler, "0px");
		topToolbar.add(topButtonWrapper);
		
		mainWrapper.add(topToolbar, 0, 0);
	}

	public void addTitleSection(String title, String subTitle, AbstractImagePrototype icon){		
		VerticalPanel sectionContent = new VerticalPanel();

		if(title != null){
			Label titleLabel = new Label(title);
			//titleLabel.setStyleName("formTitle");
			titleLabel.getElement().getStyle().setFontSize(14, Unit.PX);
			titleLabel.getElement().getStyle().setFontWeight(FontWeight.BOLD);
			sectionContent.add(titleLabel);	
		}
		if(subTitle != null){
			Label subtitleLabel = new Label(subTitle);
			//subtitleLabel.setStyleName("formSubtitle");
			subtitleLabel.getElement().getStyle().setFontSize(12, Unit.PX);
			subtitleLabel.getElement().getStyle().setFontStyle(FontStyle.ITALIC);
			sectionContent.add(subtitleLabel);	
		}

		this.addSection((String)null);
		currentSection.setContent(sectionContent);
	}

	public void addFormWideWidget(Widget w){
		mainWrapper.add(w);
	}
	
	public void addSubmitButton(Button submit){
		addButton(submit);
		submit.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				validate();
				//TODO
			}
		});
	}
	
	public void addButton(final Button button) {
		button.setHeight("30px");
		topButtonWrapper.add(button);
	}
	
	public void addSection(String title){
		this.currentSection = new FormViewSection(title);
		this.panel.add(this.currentSection);
		this.sections.add(this.currentSection);
	}
	
	public void addSection(FormViewSection section){
		this.currentSection = section;
		this.panel.add(this.currentSection);
		this.sections.add(this.currentSection);
		//this.panel.setCellHeight(currentSection, minHeight);
	}

	public void addRuler(){
		SimplePanel p = new SimplePanel();
		p.setSize("100%", "20px");
		p.setStyleName("formSection");
		this.panel.add(p);
	}

	public void addWidget(Widget w) {
		this.currentSection.addWidget(w);
	}
	
	//Disables all the input fields in the form
	public void setReadOnly(boolean readOnly) {
		this.isReadOnly = readOnly;
		for(FormViewSection s : sections){
			for(FormField<?> f : s.getFields()){
				f.setReadOnly(readOnly);
				if(readOnly)
					f.setInvalid(false);
			}
		}
	}

	public boolean isReadOnly(){
		return this.isReadOnly;
	}

	public boolean validate() {
		return validate(true);
	}

	public boolean validate(boolean showErrors) {
		boolean hasErrors = false;
		for(FormViewSection s : sections){
			//s.clearErrorMessages();
			boolean sectionHasErrors = false;
			for(FormField<?> f : s.getFields()){
				boolean fieldHasErrors = !f.validate();
				hasErrors = !fieldHasErrors ? hasErrors : true;
				sectionHasErrors = !fieldHasErrors ? sectionHasErrors : true;
				if(fieldHasErrors)
					f.validate();//s.addErrorMessage(f.getErrorMessage());
			}
			//s.showErrorMessages(sectionHasErrors && showErrors);
		}
		return !hasErrors;
	}

	public void addFormField(FormField<?> field) {
		currentSection.addFormField(field);
	}
	
	public void addFormField(FormField<?> field, boolean inline) {
		currentSection.addFormField(field, inline);
	}
	
	public void addFormFieldGroup(FormField<?>[] group, boolean inline){
		currentSection.addFormFieldGroup(group, inline);
	}
	
	public void registerFormField(FormField<?> field) {
		currentSection.registerFormField(field);
	}

	public Widget getNonScrollableContent(){
		final VerticalPanel wrapper = new VerticalPanel();
		wrapper.setSize("100%", "100%");
		
		final AbsolutePanel absolutePanel = new AbsolutePanel();
		absolutePanel.setSize("100%", "100%");
		absolutePanel.add(this.panel, 0, 0);
		absolutePanel.add(this.topToolbar, 0, 0);

		wrapper.add(absolutePanel);

		wrapper.addAttachHandler(new AttachEvent.Handler() {
			
			@Override
			public void onAttachOrDetach(AttachEvent event) {
				if(event.isAttached()){
					absolutePanel.addAttachHandler(new AttachEvent.Handler() {
						
						@Override
						public void onAttachOrDetach(AttachEvent event) {
							wrapper.setCellHeight(absolutePanel, panel.getOffsetHeight() + "px");
						}
					});
				}
			}
		});

		return wrapper;
	}
	
	@Override
	public abstract T getInfo();
	
	@Override
	public abstract void setInfo(T info);
	
	public void clearInfo() {
		for(FormViewSection s : sections){
			for(FormField<?> f : s.getFields()){
				f.clear();
			}
		}
	}
	
	protected void clearValue(){
		return;
	}
	
	@Override
	public void revert(){
		this.setValue(this.value, false);
	}
	
	@Override
	public void commit(){
		this.setValue(this.getInfo(), false);
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
		if(value == null){
			clearInfo();
			clearValue();
		}else
			setInfo(value);
		if(this.value != value && fireEvents)
			ValueChangeEvent.fire(this, value);
		this.value = value;
	}
	
	public Widget getContentPanel(){
		return this.panel;
	}
	
	public void scrollToTop(){
		this.scrollWrapper.scrollToTop();
	}
	
	public void scrollToBottom(){
		this.scrollWrapper.scrollToBottom();
	}
	
	public void lock(boolean lock) {
		setReadOnly(true);
		topToolbar.setVisible(!lock);
	}
}

