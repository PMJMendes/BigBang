package bigBang.module.casualtyModule.client.userInterface.view;

import bigBang.definitions.shared.TotalLossFile;
import bigBang.library.client.userInterface.view.ConversationView;
import bigBang.module.casualtyModule.client.userInterface.form.TotalLossFileForm;

public class TotalLossFileConversationView extends ConversationView<TotalLossFile>{

	public TotalLossFileConversationView() {
		super(new TotalLossFileForm());
	}

}
