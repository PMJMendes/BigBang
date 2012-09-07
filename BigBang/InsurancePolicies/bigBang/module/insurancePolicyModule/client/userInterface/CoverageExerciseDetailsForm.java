package bigBang.module.insurancePolicyModule.client.userInterface;

import bigBang.definitions.shared.FieldContainer;
import bigBang.definitions.shared.FieldContainer.ColumnField;
import bigBang.definitions.shared.FieldContainer.ExtraField;
import bigBang.definitions.shared.InsurancePolicy.ColumnHeader;
import bigBang.definitions.shared.InsurancePolicy.Coverage;
import bigBang.library.client.userInterface.view.FormView;

public class CoverageExerciseDetailsForm extends FormView<FieldContainer>{

	HeaderFieldsSection exerciseDetailsSection;
	TableFieldsSection tableFieldsSection;
	ExtraFieldsSection extraFieldsSection;
	private Coverage[] coverages;

	public CoverageExerciseDetailsForm(String sectionName) {
		super();
		exerciseDetailsSection = new HeaderFieldsSection();
		addSection(exerciseDetailsSection);

		tableFieldsSection = new TableFieldsSection();
		addSection(tableFieldsSection);

		extraFieldsSection = new ExtraFieldsSection();
		addWidget(extraFieldsSection);

	}

	@Override
	public FieldContainer getInfo() {
		return value; //TODO GET VALUES;	
		}

	@Override
	public void setInfo(FieldContainer info) {
		value = info;
		exerciseDetailsSection.setValue(value.headerFields);
	}

	public void setExerciseDetailSectionName(String string) {
		exerciseDetailsSection.setHeaderText(string);
	}

	public void fillTable(Coverage[] coverages, ColumnHeader[] columns, ColumnField[] fields){
		tableFieldsSection.setHeaders(coverages, columns);
		this.coverages = coverages;
		tableFieldsSection.setTableData(fields);
	}

	public void setExtraFields(ExtraField[] fields){
		extraFieldsSection.setValue(fields);
	}

}
