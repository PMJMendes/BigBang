package bigBang.library.client.userInterface.form;

import bigBang.definitions.shared.BigBangConstants;
import bigBang.definitions.shared.ExternalInfoRequest.Closing;
import bigBang.library.client.userInterface.ExpandableListBoxFormField;
import bigBang.library.client.userInterface.view.FormView;

public class ExternalRequestClosingForm extends FormView<Closing> {

	protected ExpandableListBoxFormField motive;
	
	public ExternalRequestClosingForm(){
		motive = new ExpandableListBoxFormField(BigBangConstants.TypifiedListIds.EXTERNAL_INFO_REQUEST_CLOSING_MOTIVES, "Motivo");
		addSection("Encerramento de Pedido de Informação Externo");
		addFormField(motive);
		
		setValidator(new ExternalRequestClosingFormValidator(this));
	}
		
	
	@Override
	public Closing getInfo() {
		Closing result = getValue();
		if(result != null) {
			result.motiveId = motive.getValue();
		}
		return result;
	}

	@Override
	public void setInfo(Closing info) {
		if(info == null) {
			clearInfo();
		}else{
			motive.setValue(info.motiveId);
		}
	}

}
