package bigBang.library.client.userInterface.view;

import java.util.ArrayList;

import bigBang.library.client.FormField;
import bigBang.library.client.Validatable;

import com.google.gwt.dom.client.Style.Float;
import com.google.gwt.dom.client.Style.FontStyle;
import com.google.gwt.dom.client.Style.FontWeight;
import com.google.gwt.dom.client.Style.Position;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.AbstractImagePrototype;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class FormView extends View implements Validatable {

	protected AbsolutePanel mainWrapper;
	protected VerticalPanel panel;
	protected FormViewSection currentSection = null;
	protected ArrayList<FormViewSection> sections;

	private boolean isReadOnly;


	public FormView(){
		sections = new ArrayList<FormViewSection>();
		mainWrapper = new AbsolutePanel();
		mainWrapper.setSize("100%", "100%");
		
		ScrollPanel wrapper = new ScrollPanel();
		wrapper.getElement().getStyle().setProperty("overflowX", "hidden");
		this.panel = new VerticalPanel();
		this.panel.setSize("100%", "100%");
		this.panel.setStyleName("formPanel");
		wrapper.setSize("100%", "100%");
		wrapper.add(this.panel);
		wrapper.setStyleName("formPanel");

		mainWrapper.add(wrapper, 0, 0);
		initWidget(mainWrapper);
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
		int height = 30;
		HorizontalPanel submitWrapper = new HorizontalPanel();
		submitWrapper.setSize("100%", "0px");
		SimplePanel filler = new SimplePanel();
		filler.setSize("100%", "0px");
		submitWrapper.add(filler);
		submitWrapper.setCellWidth(filler, "100%");
		submitWrapper.setCellHeight(filler, "0px");
				
		submit.setHeight(height + "px");
		submit.getElement().getStyle().setRight(20, Unit.PX);
		submit.getElement().getStyle().setPosition(Position.ABSOLUTE);
		submitWrapper.add(submit);
		
		mainWrapper.add(submitWrapper, 0, 0);//, , (mainWrapper.getOffsetHeight() - height));
		submit.getElement().getStyle().setFloat(Float.RIGHT);
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

	/*public void addDisclosureSection(String title, Widget widget){
		DisclosurePanel p = new DisclosurePanel();
		p.setSize("100%", "100%");
		SimplePanel wrapper = new SimplePanel();

		p.setHeader(getSectionHeader(title));

		SimplePanel contentWrapper = new SimplePanel();

		wrapper.setWidth("100%");
		contentWrapper.setWidget(widget);
		p.setContent(contentWrapper);

		p.getContent().getElement().getStyle().setPaddingLeft(10, Unit.PX);
		p.getContent().getElement().getStyle().setPaddingTop(10, Unit.PX);
		p.getContent().getElement().getStyle().setPaddingBottom(10, Unit.PX);
		wrapper.add(p);
		this.currentSection = wrapper;
		this.panel.add(wrapper);
	}*/

	public void addRuler(){
		SimplePanel p = new SimplePanel();
		p.setSize("100%", "20px");
		//p.setStyleName("formSection");
		this.panel.add(p);
	}

	public void addWidget(Widget w) {
		this.currentSection.addWidget(w);
	}
	
	//Disables all the input fields in the form
	public void setReadOnly(boolean readOnly) {
		this.isReadOnly = readOnly;
		for(FormViewSection s : sections){
			for(FormField<?> f : s.getFields())
				f.setReadOnly(!readOnly);
		}
	}

	public boolean isReadOnly(){
		return this.isReadOnly;
	}

	public <T> boolean validate() {
		return validate(true);
	}

	public <T> boolean validate(boolean showErrors) {
		boolean hasErrors = false;
		for(FormViewSection s : sections){
			s.clearErrorMessages();
			boolean sectionHasErrors = false;
			for(FormField<?> f : s.getFields()){
				boolean fieldHasErrors = !f.validate();
				hasErrors = fieldHasErrors ? hasErrors : true;
				sectionHasErrors = true;
				if(fieldHasErrors)
					s.addErrorMessage(f.getErrorMessage());
			}
			s.showErrorMessages(sectionHasErrors && showErrors);
		}
		return !hasErrors;
	}

	public void addFormField(FormField<?> field) {
		currentSection.addFormField(field);
	}

	public Widget getNonScrollableContent(){
		return this.panel;
	}
}

