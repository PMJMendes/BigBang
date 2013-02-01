package bigBang.library.client.userInterface.view;

import bigBang.library.client.userInterface.FileImportFormViewSection;

public abstract class GeneralTasksView extends View {

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
		
		FileImportFormViewSection section = new FileImportFormViewSection(getItemType());
		form.addSection(section);
		
		form.setReadOnly(false);
	}
	
	public abstract String getItemType();

	@Override
	protected void initializeView() {
		return;
	}

}
