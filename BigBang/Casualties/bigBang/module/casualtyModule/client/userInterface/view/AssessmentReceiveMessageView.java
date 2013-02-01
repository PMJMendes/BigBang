package bigBang.module.casualtyModule.client.userInterface.view;

import bigBang.definitions.shared.Assessment;
import bigBang.library.client.userInterface.view.ReceiveMessageView;
import bigBang.module.casualtyModule.client.userInterface.form.AssessmentForm;

public class AssessmentReceiveMessageView extends ReceiveMessageView<Assessment>{

	public AssessmentReceiveMessageView( ) {
		super(new AssessmentForm());
	}

}
