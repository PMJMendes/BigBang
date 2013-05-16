package bigBang.module.casualtyModule.client.userInterface.view;

import bigBang.definitions.shared.MedicalFile;
import bigBang.library.client.userInterface.view.ConversationView;
import bigBang.module.casualtyModule.client.userInterface.form.MedicalFileForm;

public class MedicalFileConversationView extends ConversationView<MedicalFile>{

	public MedicalFileConversationView() {
		super(new MedicalFileForm());
	}
	
}
