package bigBang.module.casualtyModule.client.userInterface.form;

import bigBang.definitions.client.BigBangConstants;
import bigBang.definitions.shared.MedicalFile.MedicalDetail;
import bigBang.library.client.Session;
import bigBang.library.client.userInterface.DatePickerFormField;
import bigBang.library.client.userInterface.ExpandableListBoxFormField;
import bigBang.library.client.userInterface.NumericTextBoxFormField;
import bigBang.library.client.userInterface.TextBoxFormField;
import bigBang.library.client.userInterface.view.FormView;

import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.user.client.ui.Button;

public class MedicalDetailForm extends FormView<MedicalDetail>{

	Button removeButton;

	ExpandableListBoxFormField disabilityType;
	DatePickerFormField startDate;
	TextBoxFormField disabilityLocation;
	NumericTextBoxFormField disabilityPercent;
	DatePickerFormField endDate;
	NumericTextBoxFormField benefits;

	public MedicalDetailForm() {

		startDate = new DatePickerFormField("Data de início");
		disabilityType = new ExpandableListBoxFormField(BigBangConstants.TypifiedListIds.DISABILITY_TYPE, "Tipo de baixa");
		disabilityLocation = new TextBoxFormField("Local da baixa");
		disabilityPercent = new NumericTextBoxFormField("Percentagem de incapacidade", false);
		disabilityPercent.setUnitsLabel("%");
		endDate = new DatePickerFormField("Data de fim");
		benefits = new NumericTextBoxFormField("Valor da indemnização", true);
		benefits.setUnitsLabel(Session.getCurrency());

		removeButton = new Button("Remover");

		addSection("");
		currentSection.getHeader().setHeight("2px");
		
		addFormField(startDate, true);
		addFormField(disabilityType, true);
		addFormField(disabilityLocation, true);
		addLineBreak();
		addFormField(disabilityPercent, true);
		addFormField(endDate, true);
		addFormField(benefits, true);

		addWidget(removeButton);
		
		setValidator(new MedicalDetailFormValidator(this));

	}

	public HasClickHandlers getRemoveButton() {
		return removeButton;
	}

	@Override
	public void setReadOnly(boolean readOnly) {
		if(removeButton != null){
			removeButton.setVisible(!readOnly);
			super.setReadOnly(readOnly);
		}
	}


	@Override
	public MedicalDetail getInfo() {
		MedicalDetail toReturn = value;
		
		toReturn.benefits = benefits.getValue();
		toReturn.disabilityTypeId = disabilityType.getValue();
		toReturn.endDate = endDate.getStringValue();
		toReturn.startDate = startDate.getStringValue();
		toReturn.percentDisability = disabilityPercent.getValue() != null ? disabilityPercent.getValue().intValue() : null;
		toReturn.place = disabilityLocation.getValue();
		
		
		return toReturn;
	}

	@Override
	public void setInfo(MedicalDetail info) {
				
		benefits.setValue(info.benefits);
		disabilityType.setValue(info.disabilityTypeId);
		endDate.setValue(info.endDate);
		startDate.setValue(info.startDate);
		disabilityPercent.setValue(info.percentDisability != null ? info.percentDisability.doubleValue() : null);
		disabilityLocation.setValue(info.place);
		
	}



}
