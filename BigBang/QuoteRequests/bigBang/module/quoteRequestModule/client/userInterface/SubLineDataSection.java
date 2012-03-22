package bigBang.module.quoteRequestModule.client.userInterface;

import bigBang.definitions.shared.QuoteRequestObject.SubLineData;
import bigBang.library.client.userInterface.view.CollapsibleFormViewSection;

public class SubLineDataSection extends CollapsibleFormViewSection {

	public SubLineDataSection(SubLineData data) {
		super("");
		setSubLineData(data);
	}

	public void setSubLineData(SubLineData data){
		setHeaderText(data.headerText);
		//TODO
	}
	
	public SubLineData getSubLineData(){
		//TODO
		return null;
	}
	
}
