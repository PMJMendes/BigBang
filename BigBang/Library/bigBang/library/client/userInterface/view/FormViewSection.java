package bigBang.library.client.userInterface.view;

import java.util.ArrayList;
import java.util.Iterator;

import bigBang.library.client.FormField;

import com.google.gwt.dom.client.Style.Display;
import com.google.gwt.dom.client.Style.Float;
import com.google.gwt.dom.client.Style.FontWeight;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.dom.client.Style.VerticalAlign;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class FormViewSection extends View {

	protected static final String DEFAULT_FIELD_HEIGHT = "45px";
	
	protected ArrayList<FormField<?>> fields;
	protected Label headerLabel;
	protected HasWidgets content;
	protected HasWidgets currentContainer;
	protected Widget header;
	protected boolean readOnly = false;

	public FormViewSection(String title){
		fields = new ArrayList<FormField<?>>();
		
		VerticalPanel wrapper = new VerticalPanel();
		initWidget(wrapper);
		wrapper.setSize("100%", "100%");
		wrapper.getElement().getStyle().setMarginBottom(5, Unit.PX);

		//Verificar as larguras e a margem de baixo da secção
		
		if(title != null){
			wrapper.add(getSectionHeader(title));
		}

		FlowPanel p = new FlowPanel();
		p.setWidth("100%");

		p.setStyleName("formSection");
		wrapper.add(p);

		content = p;
		currentContainer = new FlowPanel();
		content.add((Widget) currentContainer);
		((Widget)content).getElement().getStyle().setProperty("minHeight", "50px");
		
//		mainWrapper.add
	}
	
	@Override
	protected void initializeView() {
		return;
	}
	
	public ArrayList<FormField<?>> getFields(){
		return this.fields;
	}

	protected Widget getSectionHeader(String text) {
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
				wrapper.getElement().getStyle().setDisplay(Display.INLINE_BLOCK);
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
		wrapper.setWidth("220px");
		for(int i = 0; i < group.length; i++) {
			group[i].setHeight(DEFAULT_FIELD_HEIGHT);
			registerFormField(group[i]);
			wrapper.add(group[i]);
		}
		if(inline) {
			wrapper.getElement().getStyle().setDisplay(Display.INLINE_BLOCK);
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
		Iterator<FormField<?>> iterator = this.fields.listIterator();
		while(iterator.hasNext()){
			FormField<?> field = iterator.next();
			field.removeFromParent();
			iterator.remove();
		}
	}

	public void clear(){
		this.fields.clear();
		this.content.clear();
	}

	public void addWidget(Widget w, boolean inline) {
		if(inline){
			w.getElement().getStyle().setDisplay(Display.INLINE_BLOCK);
		}
		w.getElement().getStyle().setVerticalAlign(VerticalAlign.TOP);

		content.add(w);
	}
	
	public void addWidget(Widget w){
		addWidget(w, false);
	}

	public void setContent(Widget content) {
		this.content.clear();
		this.content.add(content);
	}

	public Widget getHeader() {
		return this.header;
	}
	
	public void showHeader(boolean show){
		this.header.setVisible(show);
	}

	public HasWidgets getContentWrapper() {
		return this.content;
	}
	
	public void setReadOnly(boolean readOnly){
		for(FormField<?> f : getFields()){
			f.setReadOnly(readOnly);
		}
		this.readOnly = readOnly;
	}
	
	public void focus(){
		for(FormField<?> field : fields){
			if(!field.isReadOnly()){
				field.focus();
				break;
			}
		}
	}
	
	public void addLineBreak(){
		HTML lineBreak = new HTML();
		lineBreak.setHeight("0px");
		addWidget(lineBreak);
	}

}
