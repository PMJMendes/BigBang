package bigBang.library.client.userInterface.view;

import bigBang.definitions.client.dataAccess.SearchDataBroker;
import bigBang.definitions.shared.User;
import bigBang.library.client.HasCheckables;
import bigBang.library.client.HasEditableValue;
import bigBang.library.client.HasValueSelectables;
import bigBang.library.client.event.ActionInvokedEventHandler;
import bigBang.library.client.userInterface.BigBangOperationsToolBar;
import bigBang.library.client.userInterface.presenter.MassManagerTransferViewPresenter;
import bigBang.library.client.userInterface.presenter.MassManagerTransferViewPresenter.Action;

public abstract class MassManagerTransferView<T> extends View implements MassManagerTransferViewPresenter.Display<T> {

//	protected class CheckableSearchPanel extends SearchPanel<T>{
//
//		public CheckableSearchPanel(SearchDataBroker<T> broker) {
//			super(broker);
//			// TODO Auto-generated constructor stub
//		}
//		
//	}
//	
	protected ActionInvokedEventHandler<Action> actionHandler;
//	protected CheckableSearchPanel searchPanel;
//	protected SelectedList selectedList; 
//	protected NewManagerFormView managerForm;
	protected HasEditableValue<T> selectedProcessForm;
	protected BigBangOperationsToolBar toolbar;
	
	public MassManagerTransferView(){
		
	}
	
	@Override
	protected void initializeView() {
		return;
	}

	@Override
	public void setOperationFilter(String operationId) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public HasEditableValue<User> getNewManagerForm() {
		return null;
//		return this.managerForm;
	}

	@Override
	public HasEditableValue<T> getSelectedProcessForm() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public HasValueSelectables<T> getMainList() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public HasValueSelectables<T> getSelectedList() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public HasCheckables getCheckableMainList() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void allowTransfer(boolean allow) {
//		this.toolbar.allo
	}

	@Override
	public void registerActionHandler(ActionInvokedEventHandler<Action> handler) {
		// TODO Auto-generated method stub
		
	}

}
