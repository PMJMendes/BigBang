package bigBang.module.insurancePolicyModule.client.userInterface;


import bigBang.definitions.shared.FieldContainer;
import bigBang.definitions.shared.StructuredFieldContainer;
import bigBang.definitions.shared.StructuredFieldContainer.Coverage;
import bigBang.library.client.userInterface.view.FormView;
import bigBang.library.client.userInterface.view.FormViewSection;

public class CoverageExerciseDetailsForm extends FormView<FieldContainer>{

	HeaderFieldsSection exerciseDetailsSection;
	CoverageFieldsGrid table;
	ExtraFieldsSection extraFieldsSection;
	FormViewSection coveragesSection;

	public CoverageExerciseDetailsForm(String sectionName) {
		super();
		this.setWidth("100%");
		exerciseDetailsSection = new HeaderFieldsSection();
		exerciseDetailsSection.setSize("100%", "100%");
		addSection(exerciseDetailsSection);
		coveragesSection = new FormViewSection("Coberturas");

		table = new CoverageFieldsGrid(){

			@Override
			public void enableExtraFields(int index, boolean enable) {
				extraFieldsSection.enableFields(index, enable);
			}
		};
		addSection(coveragesSection);
		coveragesSection.addWidget(table);

		extraFieldsSection = new ExtraFieldsSection();
		extraFieldsSection.setSize("100%", "100%");
		addSection(extraFieldsSection);


	}

	@Override
	public void setValue(FieldContainer value) {
		super.setValue(value);
		if(value == null){
			exerciseDetailsSection.setValue(null);
			extraFieldsSection.setValue(null);
			table.setValue(null);
			coveragesSection.setVisible(false);
		}
		else{
			coveragesSection.setVisible(value.columnFields.length > 0);
		}
	}

	@Override
	public FieldContainer getInfo() {
		if(value == null){
			return new FieldContainer();
		}
		FieldContainer result = value;
		result.headerFields = exerciseDetailsSection.getValue();
		result.extraFields = extraFieldsSection.getValue();
		result.columnFields = table.getValue();
		coveragesSection.setVisible(true);
		return result;
	}

	@Override
	public void setInfo(FieldContainer info) {
		exerciseDetailsSection.setValue(info.headerFields);
		extraFieldsSection.setValue(info.extraFields);
		table.setValue(info.columnFields);
	}

	public void setExerciseDetailSectionName(String string) {
		exerciseDetailsSection.setHeaderText(string);
	}

	public void setHeaders(StructuredFieldContainer.Coverage[] coverages, StructuredFieldContainer.ColumnHeader[] columns){
		table.setHeaders(coverages, columns);
		coveragesSection.setVisible(!(coverages == null || coverages.length == 0));
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
		if(table != null){
			table.setReadOnly(readOnly);
		}
	}

	public void setExerciseHeader(String header) {
		exerciseDetailsSection.setHeaderText(header);
	}

	public Coverage[] getPresentCoverages() {
		return table.getPresentCoverages();	
	}

}
