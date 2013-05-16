package bigBang.module.casualtyModule.client.userInterface.view;

import bigBang.definitions.shared.TotalLossFile;
import bigBang.library.client.userInterface.view.SendMessageView;
import bigBang.module.casualtyModule.client.userInterface.form.TotalLossFileForm;

public class TotalLossFileSendMessageView extends SendMessageView<TotalLossFile>{

	public TotalLossFileSendMessageView() {
		super(new TotalLossFileForm());
	}

}
