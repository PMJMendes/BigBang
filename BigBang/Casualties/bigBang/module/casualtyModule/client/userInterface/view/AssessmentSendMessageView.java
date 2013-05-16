package bigBang.module.casualtyModule.client.userInterface.view;

import bigBang.definitions.shared.Assessment;
import bigBang.library.client.userInterface.view.SendMessageView;
import bigBang.module.casualtyModule.client.userInterface.form.AssessmentForm;

public class AssessmentSendMessageView extends SendMessageView<Assessment>{

	public AssessmentSendMessageView() {
		super(new AssessmentForm());
	}

}
