package bigBang.module.casualtyModule.client.userInterface.form;

import bigBang.definitions.client.BigBangConstants;
import bigBang.definitions.shared.TotalLossFile;
import bigBang.library.client.userInterface.ExpandableListBoxFormField;
import bigBang.library.client.userInterface.NumericTextBoxFormField;
import bigBang.library.client.userInterface.TextAreaFormField;
import bigBang.library.client.userInterface.view.FormView;

public class TotalLossFileForm extends FormView<TotalLossFile>{

	ExpandableListBoxFormField salvageType;
	NumericTextBoxFormField capital, deductible, settlement, salvageValue;
	TextAreaFormField salvageBuyer;
	
	public TotalLossFileForm() {
		
		addSection("Informação Geral");
		
		salvageType = new ExpandableListBoxFormField(BigBangConstants.TypifiedListIds.SALVAGE_TYPE, "Posse do Salvado");	
		capital = new NumericTextBoxFormField("Capital", true);
		capital.setUnitsLabel("€");
		deductible = new NumericTextBoxFormField("Franquia", true);
		deductible.setUnitsLabel("€");
		settlement = new NumericTextBoxFormField("Indemnização", true);
		settlement.setUnitsLabel("€");
		salvageValue = new NumericTextBoxFormField("Valor do Salvado", true);
		salvageValue.setUnitsLabel("€");
		salvageBuyer = new TextAreaFormField("Comprador do salvado");
		
		addFormField(salvageType);
		addFormField(capital, true);
		addFormField(deductible, true);
		addFormField(settlement, true);
		addFormField(salvageValue, true);
		addLineBreak();
		addFormField(salvageBuyer);
		
		setValidator(new TotalLossFileFormValidator(this));
	}
	
	
	@Override
	public TotalLossFile getInfo() {

		TotalLossFile toReturn = value;
		
		toReturn.salvageTypeId = salvageType.getValue();
		toReturn.capital = capital.getValue();
		toReturn.deductible = deductible.getValue();
		toReturn.settlement = settlement.getValue();
		toReturn.salvageValue = salvageValue.getValue();
		toReturn.salvageBuyer = salvageBuyer.getValue();
		
		return toReturn;
		
	}

	@Override
	public void setInfo(TotalLossFile info) {

		salvageType.setValue(info.salvageTypeId);
		capital.setValue(info.capital);
		deductible.setValue(info.deductible);
		settlement.setValue(info.settlement);
		salvageValue.setValue(info.salvageValue);
		salvageBuyer.setValue(info.salvageBuyer);
		
	}
}



