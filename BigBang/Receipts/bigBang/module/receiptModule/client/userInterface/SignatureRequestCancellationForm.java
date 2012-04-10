package bigBang.module.receiptModule.client.userInterface;

import bigBang.definitions.shared.BigBangConstants;
import bigBang.definitions.shared.SignatureRequest;
import bigBang.definitions.shared.SignatureRequest.Cancellation;
import bigBang.library.client.userInterface.ExpandableListBoxFormField;
import bigBang.library.client.userInterface.view.FormView;

public class SignatureRequestCancellationForm extends FormView<SignatureRequest.Cancellation>{

	private ExpandableListBoxFormField motive;
	
	public SignatureRequestCancellationForm() {
		
		addSection("Cancelamento de Pedido de Assinatura");
		motive = new ExpandableListBoxFormField(BigBangConstants.TypifiedListIds.INFO_REQUEST_CANCEL_MOTIVES, "Motivo do cancelamento");
		addFormField(motive);
		
	}

	@Override
	public Cancellation getInfo() {
		Cancellation cancel = value;
		cancel.motiveId = motive.getValue();
		return cancel;
	}

	@Override
	public void setInfo(Cancellation info) {
		motive.setValue(info.motiveId);
	}
	
}
