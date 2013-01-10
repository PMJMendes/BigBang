package bigBang.module.casualtyModule.client.userInterface.form;

import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.user.client.ui.Button;

import bigBang.definitions.client.BigBangConstants;
import bigBang.definitions.shared.MedicalFile;
import bigBang.definitions.shared.MedicalFile.MedicalDetail;
import bigBang.library.client.userInterface.DatePickerFormField;
import bigBang.library.client.userInterface.ExpandableListBoxFormField;
import bigBang.library.client.userInterface.NumericTextBoxFormField;
import bigBang.library.client.userInterface.TextBoxFormField;
import bigBang.library.client.userInterface.view.CollapsibleFormViewSection;

public class MedicalDetailItemSection extends CollapsibleFormViewSection{

	
	Button removeButton;
	
	ExpandableListBoxFormField disabilityType;
	DatePickerFormField startDate;
	TextBoxFormField disabilityLocation;
	NumericTextBoxFormField disabilityPercent;
	DatePickerFormField endDate;
	NumericTextBoxFormField benefits;

	private MedicalDetail currentItem;
	
	
	public MedicalDetailItemSection() {
		super("");
	}
	
	public MedicalDetailItemSection(MedicalFile.MedicalDetail medicalDetail) {
		super("");

		startDate = new DatePickerFormField("Data de início");
		disabilityType = new ExpandableListBoxFormField(BigBangConstants.TypifiedListIds.DISABILITY_TYPE, "Tipo de baixa");
		disabilityLocation = new TextBoxFormField("Local da baixa");
		disabilityPercent = new NumericTextBoxFormField("Percentagem de incapacidade", false);
		disabilityPercent.setUnitsLabel("%");
		endDate = new DatePickerFormField("Data de fim");
		benefits = new NumericTextBoxFormField("Valor da indemnização", true);
		benefits.setUnitsLabel("€");
		
		removeButton = new Button("Remover");

		addFormField(startDate);
		addFormField(disabilityType);
		addFormField(disabilityLocation);
		addFormField(disabilityPercent);
		addFormField(endDate);
		addFormField(benefits);

		addWidget(removeButton);
		
	}

	public HasClickHandlers getRemoveButton() {
		return removeButton;
	}

	@Override
	public void setReadOnly(boolean readOnly) {
		super.setReadOnly(readOnly);
		removeButton.setVisible(!readOnly);
	}

	public MedicalFile.MedicalDetail getItem() {
		
		MedicalFile.MedicalDetail detail = this.currentItem;
		
		if(detail != null){
			detail.startDate = startDate.getStringValue();
			detail.disabilityTypeId = disabilityType.getValue();
			detail.endDate = endDate.getStringValue();
			detail.percentDisability = disabilityPercent.getValue() != null ? disabilityPercent.getValue().intValue() : null;
			detail.place = disabilityLocation.getValue();
			detail.benefits = benefits.getValue();
		}
		
		return detail;
		
	}
	
	public void setItem(MedicalFile.MedicalDetail item){
		
		currentItem = item;
		
		if(item != null){
			this.headerLabel.setText("Detalhe");
			
			startDate.setValue(item.startDate);
			disabilityType.setValue(item.disabilityTypeId);
			endDate.setValue(item.endDate);
			disabilityPercent.setValue(item.percentDisability != null ? item.percentDisability.doubleValue() : null);
			disabilityLocation.setValue(item.place);
			benefits.setValue(item.benefits);
		}
	}

	
	
}
