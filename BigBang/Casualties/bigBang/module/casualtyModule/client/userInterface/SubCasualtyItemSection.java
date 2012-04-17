package bigBang.module.casualtyModule.client.userInterface;

import bigBang.definitions.shared.SubCasualty;
import bigBang.definitions.shared.SubCasualty.SubCasualtyItem;
import bigBang.library.client.userInterface.ExpandableListBoxFormField;
import bigBang.library.client.userInterface.TextBoxFormField;
import bigBang.library.client.userInterface.view.CollapsibleFormViewSection;

public class SubCasualtyItemSection extends CollapsibleFormViewSection {

	protected SubCasualty.SubCasualtyItem currentItem;
	
	protected ExpandableListBoxFormField insuredObject;
	protected ExpandableListBoxFormField coverage;
	protected ExpandableListBoxFormField damageType;
	protected TextBoxFormField damages;
	protected TextBoxFormField settlement;
	
	public SubCasualtyItemSection(SubCasualtyItem item) {
		super("");
		setItem(item);
	}
	
	public void setItem(SubCasualtyItem item){
		this.currentItem = item;
		if(item != null) {
			this.headerLabel.setText("Detalhe");
			
			insuredObject.setValue(item.insuredObjectId);
			coverage.setValue(item.coverageId);
			damageType.setValue(item.damageTypeId);
			damages.setValue(item.damages);
			settlement.setValue(item.settlement);
		}
	}
	
	public SubCasualtyItem getItem(){
		SubCasualtyItem result = this.currentItem;
		
		if(result != null) {
			result.insuredObjectId = insuredObject.getValue();
			result.coverageId = coverage.getValue();
			result.damageTypeId = damageType.getValue();
			result.damages = damages.getValue();
			result.settlement = settlement.getValue();
		}
		
		return result;
	}
}
