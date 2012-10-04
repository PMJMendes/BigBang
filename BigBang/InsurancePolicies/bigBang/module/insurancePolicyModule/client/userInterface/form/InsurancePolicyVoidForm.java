package bigBang.module.insurancePolicyModule.client.userInterface.form;

import bigBang.definitions.shared.BigBangConstants;
import bigBang.definitions.shared.PolicyVoiding;
import bigBang.library.client.userInterface.DatePickerFormField;
import bigBang.library.client.userInterface.ExpandableListBoxFormField;
import bigBang.library.client.userInterface.TextAreaFormField;
import bigBang.library.client.userInterface.view.FormView;

public class InsurancePolicyVoidForm extends FormView<PolicyVoiding> {

	private ExpandableListBoxFormField motive;
	private DatePickerFormField effectiveDate;
	private TextAreaFormField notes;
	
	public InsurancePolicyVoidForm(){
		motive = new ExpandableListBoxFormField(BigBangConstants.TypifiedListIds.INSURANCE_POLICY_VOID_MOTIVES, "Motivo");
		effectiveDate = new DatePickerFormField("Data Efectiva da Anulação");
		notes = new TextAreaFormField("Notas");
		
		addSection("Anulação de Apólice");
		addFormField(motive);
		addFormField(effectiveDate);
		addFormField(notes);
	}
	
	@Override
	public PolicyVoiding getInfo() {
		PolicyVoiding result = getValue();
		result.motiveId = motive.getValue();
		result.effectDate = effectiveDate.getStringValue();
		result.notes = notes.getValue();
		return result;
	}

	@Override
	public void setInfo(PolicyVoiding info) {
		if(info == null){
			setInfo(new PolicyVoiding());
		}else{
			motive.setValue(info.motiveId);
			effectiveDate.setValue(info.effectDate);
			notes.setValue(info.notes);
		}
	}

}
