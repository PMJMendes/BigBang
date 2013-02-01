package bigBang.library.client.userInterface.reports;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

import bigBang.definitions.client.BigBangConstants;
import bigBang.definitions.shared.ReportParam;
import bigBang.library.client.FormField;
import bigBang.library.client.HasParameters;
import bigBang.library.client.userInterface.CheckBoxFormField;
import bigBang.library.client.userInterface.DatePickerFormField;
import bigBang.library.client.userInterface.ExpandableListBoxFormField;
import bigBang.library.client.userInterface.MutableSelectionFormFieldFactory;
import bigBang.library.client.userInterface.TextBoxFormField;
import bigBang.library.client.userInterface.view.View;

public class ParamReportPanel extends View {

	private FormField<?>[] fields;
	private VerticalPanel wrapper;
	protected Button generateReport;
	protected Button clearButton;

	public ParamReportPanel() {
		wrapper = new VerticalPanel();
		initWidget(wrapper);
		wrapper.setSize("100%", "100%");
		wrapper.setVerticalAlignment(HasVerticalAlignment.ALIGN_TOP);
		wrapper.getElement().getStyle().setBackgroundColor("#DDDDDD");
		generateReport = new Button("Gerar relatório");
		
		clearButton = new Button("Limpar", new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				clearParameters();
			}
		});
	}

	@Override
	protected void initializeView() {
		return;
	}

	public void setAvailableParameters(ReportParam[] parameters) {

		fields = new FormField<?>[parameters.length];

		for(int i = 0; i<parameters.length; i++){
			switch(parameters[i].type){
			case BOOLEAN:
				fields[i] = new CheckBoxFormField(parameters[i].label);
				break;
			case DATE:
				fields[i] = new DatePickerFormField(parameters[i].label);
				break;
			case LIST:
				fields[i] = new ExpandableListBoxFormField(BigBangConstants.TypifiedListIds.FIELD_VALUES+"/"+parameters[i].id, parameters[i].label);
				break;
			case NUMERIC:
				fields[i] = new TextBoxFormField(parameters[i].label);
				fields[i].setFieldWidth("150px");
				break;
			case REFERENCE:
				HasParameters param = new HasParameters();
				param.setParameter("name", parameters[i].label);
				fields[i] = MutableSelectionFormFieldFactory.getFormField(parameters[i].refersToId, param);
				break;
			case TEXT:
				fields[i] = new TextBoxFormField(parameters[i].label);
				break;
			}
			wrapper.add(fields[i]);
		}
		
		HorizontalPanel buttonWrapper = new HorizontalPanel();
		buttonWrapper.setSpacing(5);
		buttonWrapper.add(clearButton);
		buttonWrapper.add(generateReport);
		
		wrapper.add(buttonWrapper);
		wrapper.setCellHeight(buttonWrapper, "100%");
		wrapper.setCellHorizontalAlignment(buttonWrapper, HasHorizontalAlignment.ALIGN_RIGHT);
	}

	public String[] getParameters() {

		String[] toReturn = new String[fields.length];

		for(int i = 0; i<fields.length; i++){
			if(fields[i] instanceof DatePickerFormField){
				toReturn[i] = ((DatePickerFormField)fields[i]).getStringValue();
			}else if(fields[i] instanceof CheckBoxFormField){
				toReturn[i] = ((CheckBoxFormField)fields[i]).getValue() ? "1" : "0";
			}else
				toReturn[i] = (String) fields[i].getValue();
		}


		return toReturn;

	}

	public HasClickHandlers getButton(){
		return generateReport;
	}

	protected void clearParameters(){
		if(fields != null) {
			for(FormField<?> field : fields){
				field.setValue(null);
			}
		}
	}

}
