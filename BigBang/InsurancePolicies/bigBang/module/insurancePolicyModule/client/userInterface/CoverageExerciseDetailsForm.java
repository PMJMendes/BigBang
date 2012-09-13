package bigBang.module.insurancePolicyModule.client.userInterface;

import bigBang.definitions.shared.FieldContainer;
import bigBang.definitions.shared.StructuredFieldContainer;
import bigBang.library.client.userInterface.view.FormView;

public class CoverageExerciseDetailsForm extends FormView<FieldContainer>{

	HeaderFieldsSection exerciseDetailsSection;
	TableFieldsSection tableFieldsSection;
	ExtraFieldsSection extraFieldsSection;

	public CoverageExerciseDetailsForm(String sectionName) {
		super();
		this.setWidth("100%");
		exerciseDetailsSection = new HeaderFieldsSection();
		exerciseDetailsSection.setSize("100%", "100%");
		addSection(exerciseDetailsSection);

		tableFieldsSection = new TableFieldsSection();
		addSection(tableFieldsSection);

		extraFieldsSection = new ExtraFieldsSection();
		extraFieldsSection.setSize("100%", "100%");
		addSection(extraFieldsSection);
	}

	@Override
	public FieldContainer getInfo() {
		FieldContainer result = value;
		result.headerFields = exerciseDetailsSection.getValue();
		result.extraFields = extraFieldsSection.getValue();
		result.columnFields = tableFieldsSection.getValue();
		return result;	
		}

	@Override
	public void setInfo(FieldContainer info) {
		value = info;
		exerciseDetailsSection.setValue(info.headerFields);
		extraFieldsSection.setValue(info.extraFields);
		tableFieldsSection.setValue(info.columnFields);
	}

	public void setExerciseDetailSectionName(String string) {
		exerciseDetailsSection.setHeaderText(string);
	}

	public void fillTable(StructuredFieldContainer.Coverage[] coverages, StructuredFieldContainer.ColumnHeader[] columns){
		tableFieldsSection.setHeaders(coverages, columns);
	}

	public void setCoveragesExtraFields(StructuredFieldContainer.Coverage[] coverages) {
		extraFieldsSection.setCoveragesExtraFields(coverages);
	}

	public void setExerciseDetailsVisible(boolean b) {
		exerciseDetailsSection.setVisible(b);
	}
	
	@Override
	public void setReadOnly(boolean readOnly) {
		if(exerciseDetailsSection != null){
			exerciseDetailsSection.setReadOnly(readOnly);
		}
		if(extraFieldsSection != null){
			extraFieldsSection.setReadOnly(readOnly);
		}
		if(tableFieldsSection != null){
			tableFieldsSection.setReadOnly(readOnly);
		}
	}

}
