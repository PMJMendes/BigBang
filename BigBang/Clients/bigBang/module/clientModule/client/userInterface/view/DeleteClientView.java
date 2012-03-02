package bigBang.module.clientModule.client.userInterface.view;

import org.gwt.mosaic.ui.client.MessageBox;
import org.gwt.mosaic.ui.client.MessageBox.ConfirmationCallback;

import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.VerticalPanel;

import bigBang.definitions.client.response.ResponseHandler;
import bigBang.library.client.userInterface.BigBangOperationsToolBar;
import bigBang.library.client.userInterface.view.View;

public abstract class DeleteClientView extends View {

	protected DeleteClientForm form;
	
	public DeleteClientView(){
		VerticalPanel wrapper = new VerticalPanel();
		
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
				onDeleteButtonPressed();
			}
		});

		wrapper.add(toolbar);
		wrapper.setCellHeight(toolbar, "21px");
		
		form = new DeleteClientForm();
		form.setSize("100%", "100%");
		
		wrapper.add(form);
		wrapper.setCellHeight(form, "100%");
		
		initWidget(wrapper);
	}
	
	public abstract void onDeleteButtonPressed();
	
	public void confirmDelete(final ResponseHandler<Boolean> handler){
		MessageBox.confirm("Confirmar Eliminação de Cliente", "O cliente seleccionado será eliminado. Tem certeza que pretende prosseguir?", new ConfirmationCallback() {
			
			@Override
			public void onResult(boolean result) {
				handler.onResponse(result);
			}
		});
	}
}
