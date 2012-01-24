package bigBang.library.client.userInterface;

import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

import bigBang.library.client.FormField;

public class TypifiedTextFormField extends FormField<String>{

	private ExpandableListBoxFormField labels = new ExpandableListBoxFormField("Etiqueta");
	private TextBoxFormField labelText = new TextBoxFormField();
	private TextBoxFormField subject = new TextBoxFormField("Assunto");
	private RichTextAreaFormField textBody = new RichTextAreaFormField();
	
	private VerticalPanel wrapper = new VerticalPanel();
	private HorizontalPanel labelPanel = new HorizontalPanel();
	
	
	public TypifiedTextFormField(){
		
		initWidget(wrapper);
		
		labelPanel.setWidth("100%");
		labelPanel.add(labels);
		labelPanel.add(labelText);
		labelText.setWidth("100%");
		labelText.setFieldWidth("100%");
		labelPanel.setCellWidth(labelText, "100%");
		labelPanel.setCellVerticalAlignment(labelText, HasVerticalAlignment.ALIGN_BOTTOM);
		wrapper.add(labelPanel);
		
		subject.setWidth("100%");
		subject.setFieldWidth("100%");
		wrapper.add(subject);
		
		textBody.setLabelWidth("0px");
		textBody.setFieldWidth("100%");
		wrapper.add(textBody);
	}
	
	@Override
	public void clear() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setReadOnly(boolean readonly) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean isReadOnly() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void setLabelWidth(String width) {
		// TODO Auto-generated method stub
		
	}
}
