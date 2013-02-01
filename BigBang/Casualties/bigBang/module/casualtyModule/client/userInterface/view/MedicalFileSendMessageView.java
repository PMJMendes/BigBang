package bigBang.module.casualtyModule.client.userInterface.view;

import bigBang.definitions.shared.MedicalFile;
import bigBang.library.client.userInterface.view.SendMessageView;
import bigBang.module.casualtyModule.client.userInterface.form.MedicalFileForm;

public class MedicalFileSendMessageView extends SendMessageView<MedicalFile>{

	public MedicalFileSendMessageView() {
		super(new MedicalFileForm());
	}
}
