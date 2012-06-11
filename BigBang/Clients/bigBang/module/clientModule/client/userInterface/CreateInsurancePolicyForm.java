package bigBang.module.clientModule.client.userInterface;

import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;

import bigBang.definitions.shared.BigBangConstants;
import bigBang.library.client.FormField;
import bigBang.library.client.userInterface.ExpandableListBoxFormField;
import bigBang.library.client.userInterface.view.FormView;

public class CreateInsurancePolicyForm extends FormView<Void> {

	protected ExpandableListBoxFormField category;
	protected ExpandableListBoxFormField line;
	protected ExpandableListBoxFormField subLine;
	
	public CreateInsurancePolicyForm(){
		addSection("Criação de Apólice");
		
		category = new ExpandableListBoxFormField(BigBangConstants.EntityIds.CATEGORY, "Categoria");
		category.allowEdition(false);
		line = new ExpandableListBoxFormField("Ramo");
		line.allowEdition(false);
		subLine = new ExpandableListBoxFormField("Modalidade");
		subLine.allowEdition(false);
		
		FormField<?>[] group = new FormField<?>[]{
				category,
				line,
				subLine
		};
		addFormFieldGroup(group, false);

		category.addValueChangeHandler(new ValueChangeHandler<String>() {

			@Override
			public void onValueChange(ValueChangeEvent<String> event) {
				if(event.getValue() == null || event.getValue().isEmpty()){
					line.setListId(null, null);
				}else{
					line.setListId(BigBangConstants.EntityIds.LINE+"/"+event.getValue(), null);
				}
			}
		});
		line.addValueChangeHandler(new ValueChangeHandler<String>() {

			@Override
			public void onValueChange(ValueChangeEvent<String> event) {
				if(event.getValue() == null || event.getValue().isEmpty()){
					subLine.setListId(null, null);
				}else{
					subLine.setListId(BigBangConstants.EntityIds.SUB_LINE+"/"+event.getValue(), null);
				}
			}
		});
	}

	@Override
	public Void getInfo() {
		return null;
	}

	@Override
	public void setInfo(Void info) {
		clearInfo();
	}

	public String getCategory() {
		return category.getValue();
	}
	
	public String getLine(){
		return line.getValue();
	}
	
	public String getSubLine(){
		return subLine.getValue();
	}

}
