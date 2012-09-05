package bigBang.module.insurancePolicyModule.client.userInterface;

import bigBang.definitions.shared.FieldContainer;
import bigBang.definitions.shared.FieldContainer.ColumnField;
import bigBang.definitions.shared.InsurancePolicy.ColumnHeader;
import bigBang.definitions.shared.InsurancePolicy.Coverage;
import bigBang.library.client.userInterface.view.FormView;

public class InsurancePolicyDetailsForm extends FormView<FieldContainer>{
	
	HeaderFieldsSection exerciseDetailsSection;
	TableFieldsSection tableFieldsSection;
	ExtraFieldsSection extraFieldsSection;
	
	public InsurancePolicyDetailsForm(String sectionName) {
		super();
		exerciseDetailsSection = new HeaderFieldsSection();
		addSection(exerciseDetailsSection);
		
		tableFieldsSection = new TableFieldsSection();
		addSection(tableFieldsSection);
		
		extraFieldsSection = new ExtraFieldsSection();
		addSection(extraFieldsSection);
		
	}
	
	@Override
	public FieldContainer getInfo() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setInfo(FieldContainer info) {
		exerciseDetailsSection.setValue(info.headerFields);
	}

	public void setExerciseDetailSectionName(String string) {
		exerciseDetailsSection.setHeaderText(string);
	}
	
	public void fillTable(Coverage[] coverages, ColumnHeader[] columns, ColumnField[] fields){
		tableFieldsSection.setHeaders(coverages, columns, fields);
	}
	
}
