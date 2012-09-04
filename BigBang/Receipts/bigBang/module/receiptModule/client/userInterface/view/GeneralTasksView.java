package bigBang.module.receiptModule.client.userInterface.view;

import bigBang.library.client.userInterface.FileImportFormViewSection;
import bigBang.library.client.userInterface.view.FormView;
import bigBang.library.client.userInterface.view.View;

public class GeneralTasksView extends View {

	public GeneralTasksView(){
		FormView<Void> form = new FormView<Void>() {
			
			@Override
			public void setInfo(Void info) {
				return;
			}
			
			@Override
			public Void getInfo() {
				return null;
			}
		};
		initWidget(form);
		form.setSize("100%", "100%");
		
		FileImportFormViewSection section = new FileImportFormViewSection("recibo");
		form.addSection(section);
		
		form.setReadOnly(false);
	}
	
	@Override
	protected void initializeView() {
		return;
	}

}
