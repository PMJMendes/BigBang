package bigBang.module.casualtyModule.client.userInterface.view;

import bigBang.definitions.shared.MedicalFile;
import bigBang.library.client.userInterface.view.ReceiveMessageView;
import bigBang.module.casualtyModule.client.userInterface.form.MedicalFileForm;

public class MedicalFileReceiveMessageView extends ReceiveMessageView<MedicalFile>{
	
	public MedicalFileReceiveMessageView(){
		super(new MedicalFileForm());
	}

}
