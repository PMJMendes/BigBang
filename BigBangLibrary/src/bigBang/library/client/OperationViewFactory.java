package bigBang.library.client;

import bigBang.library.client.userInterface.view.OperationView;
import bigBang.library.client.userInterface.view.View;

public interface OperationViewFactory {
	
	public OperationView getViewInstance(String operationId);

}
