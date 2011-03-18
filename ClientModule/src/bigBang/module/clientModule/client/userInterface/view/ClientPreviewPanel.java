package bigBang.module.clientModule.client.userInterface.view;

import bigBang.library.client.userInterface.view.PreviewPanel;
import bigBang.module.clientModule.shared.ClientProcess;

public class ClientPreviewPanel extends PreviewPanel<ClientProcess> {

	private ClientFormView form;
	
	public ClientPreviewPanel(){
		super();
		showIndividualSection(true);
		this.form = new ClientFormView();
		this.form.setSize("100%", "100%");
		setIndividualSectionContent(form);
		
		showMultipleSection(true);
	}
	
	@Override
	public void setValue(ClientProcess value, boolean fireEvents) {
		super.setValue(value, fireEvents);
		render();
	}
	
	public void render(){
		this.form.showProcess(getValue());
	}

}
