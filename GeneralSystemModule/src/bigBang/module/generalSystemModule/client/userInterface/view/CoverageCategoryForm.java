package bigBang.module.generalSystemModule.client.userInterface.view;

import bigBang.library.client.FieldValidator;
import bigBang.library.client.userInterface.TextBoxFormField;
import bigBang.library.client.userInterface.view.FormView;
import bigBang.module.generalSystemModule.shared.CoverageCategory;

public class CoverageCategoryForm extends FormView {

	private CoverageCategory info;
	
	private TextBoxFormField name;
	
	public CoverageCategoryForm(){
		super();
		addSection("Informação Geral");
		name = new TextBoxFormField("Nome", new FieldValidator<String>() {
			
			private String errorMessage;
			
			@Override
			public boolean isValid(String value) {
				boolean result = value.length() > 0;
				if(!result)
					errorMessage = "Por favor, introduza o nome da categoria.";
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
			this.info = new CoverageCategory();
			clearInfo();
			return;
		}
		this.info = (CoverageCategory)info;
		name.setValue(this.info.name);
	}

	@Override
	public void clearInfo() {
		name.setValue("");
		getInfo();
	}

}
