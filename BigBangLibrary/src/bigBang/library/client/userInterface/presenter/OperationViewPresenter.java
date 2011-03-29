package bigBang.library.client.userInterface.presenter;

import com.google.gwt.user.client.ui.HasWidgets;

import bigBang.library.client.Operation;
import bigBang.library.shared.Permission;

public interface OperationViewPresenter extends ViewPresenter {

	public void setOperation(Operation o);
	
	public Operation getOperation();
	
	public void goCompact(HasWidgets container);
	
	public String setTargetEntity(String id);

	public void setOperationPermission(boolean hasPermissionForOperation);
}
