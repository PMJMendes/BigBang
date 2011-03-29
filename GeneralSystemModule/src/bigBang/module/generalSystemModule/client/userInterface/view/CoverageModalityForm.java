package bigBang.module.generalSystemModule.client.userInterface.view;

import bigBang.library.client.FieldValidator;
import bigBang.library.client.userInterface.TextBoxFormField;
import bigBang.library.client.userInterface.view.FormView;
import bigBang.module.generalSystemModule.shared.CoverageModality;

public class CoverageModalityForm extends FormView {

	private CoverageModality info;
	
	private TextBoxFormField name;
	
	public CoverageModalityForm(){
		super();
		addSection("Informação Geral");
		name = new TextBoxFormField("Nome", new FieldValidator<String>() {
			
			private String errorMessage;
			
			@Override
			public boolean isValid(String value) {
				boolean result = value.length() > 0;
				if(!result)
					errorMessage = "Por favor, introduza o nome do ramo.";
				return result;
			}
			
			@Override
			public boolean isMandatory() {
				return true;
			}
			
			@Override
			public String getErrorMessage() {
				return errorMessage;
			}
		});
		
		addFormField(name);
	}
	
	@Override
	public Object getInfo() {
		info.name = name.getValue();
		return info;
	}

	@Override
	public void setInfo(Object info) {
		if(info == null){
			this.info = new CoverageModality();
			clearInfo();
			return;
		}
		this.info = (CoverageModality)info;
		name.setValue(this.info.name);
	}

	@Override
	public void clearInfo() {
		name.setValue("");
		getInfo();
	}

}
