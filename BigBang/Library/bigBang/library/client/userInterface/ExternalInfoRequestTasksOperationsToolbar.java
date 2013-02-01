package bigBang.library.client.userInterface;

import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.MenuItem;

public abstract class ExternalInfoRequestTasksOperationsToolbar extends
		BigBangOperationsToolBar {

	protected MenuItem sendResponse, continueRequest, close, goToExternalInfo;
	
	public ExternalInfoRequestTasksOperationsToolbar(){
		hideAll();
		
		sendResponse = new MenuItem("Enviar Resposta", new Command() {
			
			@Override
			public void execute() {
				onSendResponse();
			}
		});
		addItem(sendResponse);
		
		continueRequest = new MenuItem("Continuar Processo", new Command() {
			
			@Override
			public void execute() {
				onContinue();
			}
		});
		addItem(continueRequest);
		
		close = new MenuItem("Encerrar Processo", new Command() {
			
			@Override
			public void execute() {
				onClose();
			}
		});
		addItem(close);
		
		goToExternalInfo = new MenuItem("Navegar para Processo", new Command() {
			
			@Override
			public void execute() {
				onGoToRequest();
			}
		});
		addItem(goToExternalInfo);
	}
	
	protected abstract void onGoToRequest();

	@Override
	public void onEditRequest() {
		return;
	}

	@Override
	public void onSaveRequest() {
		return;
	}

	@Override
	public void onCancelRequest() {
		return;
	}
	
	public abstract void onSendResponse();
	
	public abstract void onContinue();
	
	public abstract void onClose();
	
	public void allowSendResponse(boolean allow) {
		sendResponse.setVisible(allow);
	}
	
	public void allowContinue(boolean allow) {
		continueRequest.setVisible(allow);
	}
	
	public void allowClose(boolean allow) {
		close.setVisible(allow);
	}

	public void setGoToProcessVisible() {
		goToExternalInfo.setVisible(true);
	}

}
