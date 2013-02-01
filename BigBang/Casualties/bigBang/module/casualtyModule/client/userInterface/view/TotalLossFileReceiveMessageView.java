package bigBang.module.casualtyModule.client.userInterface.view;

import bigBang.definitions.shared.TotalLossFile;
import bigBang.library.client.userInterface.view.ReceiveMessageView;
import bigBang.module.casualtyModule.client.userInterface.form.TotalLossFileForm;

public class TotalLossFileReceiveMessageView extends ReceiveMessageView<TotalLossFile>{

	public TotalLossFileReceiveMessageView() {
		super(new TotalLossFileForm());	}

}
