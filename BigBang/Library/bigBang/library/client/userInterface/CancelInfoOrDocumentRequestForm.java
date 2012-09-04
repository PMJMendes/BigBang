package bigBang.library.client.userInterface;

import bigBang.definitions.shared.BigBangConstants;
import bigBang.definitions.shared.InfoOrDocumentRequest.Cancellation;
import bigBang.library.client.userInterface.view.FormView;

public class CancelInfoOrDocumentRequestForm extends FormView<Cancellation> {

	private ExpandableListBoxFormField motive;

	public CancelInfoOrDocumentRequestForm(){
		motive = new ExpandableListBoxFormField(BigBangConstants.TypifiedListIds.INFO_REQUEST_CANCEL_MOTIVES, "Motivo");

		addSection("Cancelamento de Pedido de Informação");
		addFormField(motive);
	}

	@Override
	public Cancellation getInfo() {
		Cancellation result = getValue();
		if(result != null){
			result.motiveId = motive.getValue();
		}
		return result;
	}

	@Override
	public void setInfo(Cancellation info) {
		if(info == null) {
			clearInfo();
		}else{
			motive.setValue(info.motiveId);
		}
	}

}
