package bigBang.module.insurancePolicyModule.client.userInterface;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;

import bigBang.definitions.shared.SubPolicyStub;
import bigBang.library.client.userInterface.ListEntry;

public class SubPolicySelectButton extends ListEntry<SubPolicyStub>{

	protected Label numberLabel;
	protected Label label;


	public SubPolicySelectButton(SubPolicyStub value){
		super(value);
		setHeight("40px");
	}

	public <I extends Object> void setInfo(I info) {
		SubPolicyStub value = (SubPolicyStub) info;

		if(value == null){
			numberLabel.setText("");
			label.setText("");
			return;
		}
		if(numberLabel == null){
			numberLabel = getFormatedLabel();
			numberLabel.getElement().getStyle().setFontSize(14, Unit.PX);
			numberLabel.setWordWrap(false);
			label = getFormatedLabel();
			label.getElement().getStyle().setFontSize(11, Unit.PX);
			label.getElement().getStyle().setProperty("whiteSpace", "");
			label.setHeight("1.2em");
		}
		if(value.id != null){

			numberLabel.setText(value.id.equalsIgnoreCase("new") ? "Nova Adesão" : "Adesão #" + value.number);
			label.setText("(Todas as unidades de risco)");

			VerticalPanel panel = new VerticalPanel();
			panel.add(numberLabel);
			panel.add(label);

			setWidget(panel);
		}
		else{
			numberLabel.setText("");
			label.setText("");
		}
	}
}


