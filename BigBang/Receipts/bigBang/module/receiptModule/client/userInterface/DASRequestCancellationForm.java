package bigBang.module.receiptModule.client.userInterface;

import bigBang.definitions.shared.BigBangConstants;
import bigBang.definitions.shared.DASRequest;
import bigBang.definitions.shared.DASRequest.Cancellation;
import bigBang.library.client.userInterface.ExpandableListBoxFormField;
import bigBang.library.client.userInterface.view.FormView;

public class DASRequestCancellationForm extends FormView<DASRequest.Cancellation>{
	
	private ExpandableListBoxFormField motive;
	
	public DASRequestCancellationForm(){
		addSection("Cancelamento de Declaração de Ausência de Sinistro");
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
