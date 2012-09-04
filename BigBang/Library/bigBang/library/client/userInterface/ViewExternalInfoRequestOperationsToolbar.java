package bigBang.library.client.userInterface;

import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.MenuItem;

public abstract class ViewExternalInfoRequestOperationsToolbar extends
		BigBangOperationsToolBar {

	protected MenuItem replyRequest, continueRequest, closeRequest;
	
	public ViewExternalInfoRequestOperationsToolbar(){
		hideAll();
		
		replyRequest = new MenuItem("Responder", new Command() {
			
			@Override
			public void execute() {
				onReply();
			}
		});
		addItem(replyRequest);
		
		continueRequest = new MenuItem("Continuar Pedido", new Command() {
			
			@Override
			public void execute() {
				onContinue();
			}
		});
		addItem(continueRequest);
		
		addSeparator();
		
		closeRequest = new MenuItem("Encerrar Pedido", new Command() {
			
			@Override
			public void execute() {
				onClose();
			}
		});
		addItem(closeRequest);
	}
	
	public void allowReply(boolean allow){
		this.replyRequest.setEnabled(allow);
	}
	
	public void allowContinue(boolean allow){
		this.continueRequest.setEnabled(allow);
	}
	
	public void allowClose(boolean allow){
		this.closeRequest.setEnabled(allow);
	}
	
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
	
	public abstract void onReply();
	
	public abstract void onContinue();
	
	public abstract void onClose();

}
