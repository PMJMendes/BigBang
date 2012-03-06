package bigBang.module.insurancePolicyModule.client.userInterface;

import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.MenuItem;

import bigBang.library.client.userInterface.BigBangOperationsToolBar;

public abstract class SubPolicyOperationsToolbar extends BigBangOperationsToolBar {

	//ADMIN
	protected MenuItem deleteItem;
	
	public SubPolicyOperationsToolbar(){
		//ADMIN
		deleteItem = new MenuItem("Eliminar", new Command() {
			
			@Override
			public void execute() {
				onDelete();
			}
		});
		addItem(SUB_MENU.ADMIN, deleteItem);
	}

	public void allowDelete(boolean allow){
		this.deleteItem.setEnabled(allow);
	}

	public abstract void onDelete();
	
	public abstract void onIncludeInsuredObject();
	
	public abstract void onIncludeInsuredObjectFromClient();
	
	public abstract void onCreateInsuredObject();
	
	public abstract void onCreateInsuredObjectFromClient();
	
	public abstract void onExcludeInsuredObject();
	
	public abstract void onPerformCalculations();
	
	public abstract void onValidate();
	
	public abstract void onTransferToPolicy();
	
	public abstract void onCreateInfoOrDocumentRequest();
	
	public abstract void onCreateReceipt();
	
	public abstract void onVoid();

}
