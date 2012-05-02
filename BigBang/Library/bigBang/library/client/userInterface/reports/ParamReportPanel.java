package bigBang.library.client.userInterface.reports;

import com.google.gwt.user.client.ui.VerticalPanel;

import bigBang.definitions.shared.ReportParam;
import bigBang.library.client.FormField;
import bigBang.library.client.userInterface.CheckBoxFormField;
import bigBang.library.client.userInterface.DatePickerFormField;
import bigBang.library.client.userInterface.view.View;

public class ParamReportPanel extends View {
	
	private FormField<?>[] fields;
	private VerticalPanel wrapper;
	
	
	public ParamReportPanel() {
		wrapper = new VerticalPanel();
		initWidget(wrapper);
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
				
				break;
			case NUMERIC:
				
				break;
			case REFERENCE:
				
				break;
			case TEXT:
				
				break;
			}
			
			wrapper.add(fields[i]);
		}
		
	}

	public String[] getParameters() {
		// TODO Auto-generated method stub
		return null;
	}

	public void clearParameters() {
		// TODO Auto-generated method stub
		
	}

}
