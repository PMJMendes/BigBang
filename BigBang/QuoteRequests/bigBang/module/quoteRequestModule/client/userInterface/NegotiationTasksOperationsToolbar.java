package bigBang.module.quoteRequestModule.client.userInterface;

import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.MenuItem;

import bigBang.library.client.userInterface.BigBangOperationsToolBar;

public abstract class NegotiationTasksOperationsToolbar extends BigBangOperationsToolBar {

	protected MenuItem grant, cancel, receiveResponse, repeat;
	
	public NegotiationTasksOperationsToolbar(){
		hideAll();
		
		grant = new MenuItem("Adjudicar", new Command() {
			
			@Override
			public void execute() {
				onGrant();
			}
		});
		addItem(grant);
		
		cancel = new MenuItem("Cancelar Negociação", new Command() {
			
			@Override
			public void execute() {
				onCancelRequest();
			}
		});
		addItem(cancel);
		
		receiveResponse = new MenuItem("Receber Resposta", new Command() {
			
			@Override
			public void execute() {
				onReceiveResponse();
			}
		});
		addItem(receiveResponse);
		
		repeat = new MenuItem("Repetir Pedido", new Command() {
			
			@Override
			public void execute() {
				onRepeat();
			}
		});
		addItem(repeat);
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
	public abstract void onCancelRequest();
	
	public abstract void onGrant();
	
	public abstract void onRepeat();
	
	public abstract void onReceiveResponse();
	
	public void allowGrant(boolean allow) {
		grant.setVisible(allow);
	}
	
	public void allowCancel(boolean allow) {
		cancel.setVisible(allow);
	}
	
	public void allowRepeat(boolean allow){
		repeat.setVisible(allow);
	}
	
	public void allowReceiveResponse(boolean allow) {
		receiveResponse.setVisible(allow);
	}
	
}
