package bigBang.library.client.userInterface.presenter;

import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.Widget;

import bigBang.definitions.shared.ClientStub;
import bigBang.definitions.shared.InsurancePolicyStub;
import bigBang.definitions.shared.ManagerTransfer;
import bigBang.definitions.shared.SearchResult;
import bigBang.definitions.shared.BigBangConstants.EntityIds;
import bigBang.library.client.EventBus;
import bigBang.library.client.HasValueSelectables;
import bigBang.library.client.event.SelectionChangedEvent;
import bigBang.library.client.event.SelectionChangedEventHandler;
import bigBang.library.client.userInterface.ListEntry;
import bigBang.library.client.userInterface.view.ManagerTransferForm;
import bigBang.library.client.userInterface.view.View;
import bigBang.library.interfaces.Service;

public abstract class ManagerTransferViewPresenter implements ViewPresenter{

	private ManagerTransfer transfer;
	private Display view;
	private boolean bound = false;
	
	public static enum BarStatus{
		ACCEPT_REJECT, 
		CANCEL, 
		NONE
	}

	public interface Display{

		public ManagerTransferForm getForm();
		public void setToolBarState(BarStatus status);
		public HasValueSelectables<SearchResult> getList();
		public void clearList();
		public void addObject(ListEntry<SearchResult> object);
		Widget asWidget();

	}

	public void setManagerTransfer(ManagerTransfer transfer){

		this.transfer = transfer;
		view.getForm().clearInfo();
		view.clearList();
		view.getForm().setValue(this.transfer);
		chooseToolBarState();
		
		fillList();


	}


	private void chooseToolBarState() {
		// TODO COMPARISON OF USER ID WITH NEW MANAGER
		view.setToolBarState(BarStatus.ACCEPT_REJECT);
	}


	public void go(HasWidgets container){
		bind();
		bound = true;
		container.clear();
		container.add(this.view.asWidget());
	}

	@Override
	public void bind(){
		if(bound){
			return;
		}
		view.getList().addSelectionChangedEventHandler(new SelectionChangedEventHandler(){

			@Override
			public void onSelectionChanged(SelectionChangedEvent event) {
				// TODO ONSELECTIONCHANGED nos objectos
				System.out.println("MUDEI DE OBJECTO");
				
			}
			
		});
		

	}

	@Override
	public void setService(Service service) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setEventBus(EventBus eventBus) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setView(View view) {

		this.view = (Display)view;

	}


	@Override
	public void registerEventHandlers(EventBus eventBus) {
		// TODO Auto-generated method stub

	}

	private void fillList(){


		if(transfer.objectTypeId.compareTo(EntityIds.CLIENT) == 0){

			view.getForm().setObjectType("Clientes");
			for(int i = 0; i<transfer.objectStubs.length; i++){
				ClientStub client = (ClientStub)transfer.objectStubs[i];
				ListEntry<SearchResult> temp = new ListEntry<SearchResult>(client);
				temp.setHeight("40px");
				temp.setTitle(client.name);
				temp.setText(client.clientNumber);
				view.addObject(temp);
			}

		}else if(transfer.objectTypeId.compareTo(EntityIds.POLICY_INSURED_OBJECT) == 0){

			view.getForm().setObjectType("Apólices");
			for(int i = 0; i<transfer.objectStubs.length; i++){
				InsurancePolicyStub policy = (InsurancePolicyStub)transfer.objectStubs[i];
				ListEntry<SearchResult> temp = new ListEntry<SearchResult>(policy);
				temp.setHeight("40px");
				temp.setTitle(policy.number);
				temp.setText(policy.categoryName+" / " +policy.lineName + " / "+policy.subLineName);
				view.addObject(temp);
			}

		}
	}
}


