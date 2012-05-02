package bigBang.library.client.userInterface.reports;

import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.VerticalPanel;

import bigBang.definitions.shared.BigBangConstants;
import bigBang.definitions.shared.ReportParam;
import bigBang.library.client.FormField;
import bigBang.library.client.userInterface.CheckBoxFormField;
import bigBang.library.client.userInterface.DatePickerFormField;
import bigBang.library.client.userInterface.ExpandableListBoxFormField;
import bigBang.library.client.userInterface.TextBoxFormField;
import bigBang.library.client.userInterface.view.View;

public class ParamReportPanel extends View {
	
	private FormField<?>[] fields;
	private VerticalPanel wrapper;
	protected Button generateReport;

	
	public ParamReportPanel() {
		wrapper = new VerticalPanel();
		initWidget(wrapper);
		wrapper.setSize("100%", "100%");
		wrapper.setVerticalAlignment(HasVerticalAlignment.ALIGN_TOP);
		wrapper.getElement().getStyle().setBackgroundColor("#DDDDDD");
		generateReport = new Button("Gerar relatório");
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
				fields[i] = new ExpandableListBoxFormField(parameters[i].label,   BigBangConstants.TypifiedListIds.FIELD_VALUES+"/"+parameters[i].id);
				break;
			case NUMERIC:
				fields[i] = new TextBoxFormField(parameters[i].label);
				fields[i].setFieldWidth("150px");
				break;
			case REFERENCE:
				fields[i] = new ExpandableListBoxFormField(parameters[i].label, parameters[i].refersToId);
				break;
			case TEXT:
				fields[i] = new TextBoxFormField(parameters[i].label);
				break;
			}
			
			wrapper.add(fields[i]);
			wrapper.add(generateReport);
			wrapper.setCellHeight(generateReport, "100%");
			wrapper.setCellHorizontalAlignment(generateReport, HasHorizontalAlignment.ALIGN_RIGHT);
		}
		
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

	public void clearParameters() {
		for(FormField<?> field : fields){
			field.setValue(null);
		}
	}
	
	public HasClickHandlers getButton(){
		return generateReport;
	}

}
