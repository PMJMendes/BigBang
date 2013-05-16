package bigBang.module.casualtyModule.client.userInterface.view;

import bigBang.definitions.shared.Assessment;
import bigBang.library.client.userInterface.view.ConversationView;
import bigBang.module.casualtyModule.client.userInterface.form.AssessmentForm;

public class AssessmentConversationView extends ConversationView<Assessment>{

	public AssessmentConversationView() {
		super(new AssessmentForm());
	}

}
