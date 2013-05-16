package bigBang.module.quoteRequestModule.client.userInterface.form;

import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;

import bigBang.definitions.client.BigBangConstants;
import bigBang.library.client.FormField;
import bigBang.library.client.userInterface.ExpandableListBoxFormField;
import bigBang.library.client.userInterface.view.FormView;

public class ChooseSublineForm extends FormView<String>{

	protected ExpandableListBoxFormField category;
	protected ExpandableListBoxFormField line;
	protected ExpandableListBoxFormField subLine;

	public ChooseSublineForm() {
		addSection("Modalidade");

		category = new ExpandableListBoxFormField(BigBangConstants.EntityIds.CATEGORY, "Categoria");
		category.setMandatory(true);
		category.allowEdition(false);
		line = new ExpandableListBoxFormField("Ramo");
		line.setMandatory(true);
		line.allowEdition(false);
		subLine = new ExpandableListBoxFormField("Modalidade");
		subLine.setMandatory(true);
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
		
		setValidator(new ChooseSublineFormValidator(this));
	}

	@Override
	public String getInfo() {
		return subLine.getValue();
	}

	@Override
	public void setInfo(String info) {
		subLine.setValue(info);
	}

}
