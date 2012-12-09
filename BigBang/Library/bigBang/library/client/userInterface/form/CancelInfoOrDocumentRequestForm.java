package bigBang.library.client.userInterface.form;

import bigBang.definitions.shared.BigBangConstants;
import bigBang.definitions.shared.InfoOrDocumentRequest.Cancellation;
import bigBang.library.client.userInterface.ExpandableListBoxFormField;
import bigBang.library.client.userInterface.view.FormView;

public class CancelInfoOrDocumentRequestForm extends FormView<Cancellation> {

	protected ExpandableListBoxFormField motive;

	public CancelInfoOrDocumentRequestForm(){
		motive = new ExpandableListBoxFormField(BigBangConstants.TypifiedListIds.INFO_REQUEST_CANCEL_MOTIVES, "Motivo");

		addSection("Cancelamento de pedido de resposta a mensagem");
		addFormField(motive);
		
		setValidator(new CancelInfoOrDocumentRequestFormValidator(this));
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
