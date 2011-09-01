package bigBang.module.clientModule.client.userInterface.view;

import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.VerticalPanel;

import bigBang.library.client.userInterface.BigBangOperationsToolBar;
import bigBang.library.client.userInterface.view.View;

public abstract class ClientManagerTransferView extends View {

	protected TransferClientManagerForm form;

	public ClientManagerTransferView(){
		VerticalPanel wrapper = new VerticalPanel();
		wrapper.setSize("100%", "100%");

		BigBangOperationsToolBar toolbar = new BigBangOperationsToolBar() {

			@Override
			public void onSaveRequest() {}

			@Override
			public void onEditRequest() {}

			@Override
			public void onCancelRequest() {}
		};
		toolbar.hideAll();
		toolbar.addItem("Eliminar", new Command() {

			@Override
			public void execute() {
				onTransferButtonPressed();
			}
		});

		wrapper.add(toolbar);
		wrapper.setCellHeight(toolbar, "21px");

		form = new TransferClientManagerForm();
		form.setSize("100%", "100%");

		wrapper.add(form);
		wrapper.setCellHeight(form, "100%");

		initWidget(wrapper);
	}
	
	public abstract void onTransferButtonPressed();

}
