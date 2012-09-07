package bigBang.module.insurancePolicyModule.client.userInterface;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.ui.Label;

import bigBang.definitions.shared.InsurancePolicyStub;
import bigBang.library.client.userInterface.ListEntry;

public class PolicySelectButton extends ListEntry<InsurancePolicyStub>{

	protected Label numberLabel;
	protected Label label;


	public PolicySelectButton(InsurancePolicyStub value) {
		super(value);
		setHeight("40px");
	}

	public <I extends Object> void setInfo(I info) {
		InsurancePolicyStub value = (InsurancePolicyStub)info;
		if(value == null){
			numberLabel.setText("");
			label.setText("");
			return;
		}
		if(value.id == null){
			if(numberLabel == null){
				numberLabel = getFormatedLabel();
				numberLabel.getElement().getStyle().setFontSize(14, Unit.PX);
				numberLabel.setWordWrap(false);
				label = getFormatedLabel();
				label.getElement().getStyle().setFontSize(11, Unit.PX);
				label.getElement().getStyle().setProperty("whiteSpace", "");
				label.setHeight("1.2em");
			}
			
			numberLabel.setText(value.number == null ? "Nova Apólice" : "#" + value.number);
			label.setText("(Todas as unidades de risco)");
		}
	}
}
