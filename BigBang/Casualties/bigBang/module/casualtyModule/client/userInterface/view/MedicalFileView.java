package bigBang.module.casualtyModule.client.userInterface.view;

import com.google.gwt.user.client.ui.HasValue;

import bigBang.definitions.shared.BigBangProcess;
import bigBang.definitions.shared.HistoryItemStub;
import bigBang.definitions.shared.MedicalFile;
import bigBang.definitions.shared.SubCasualty;
import bigBang.library.client.HasEditableValue;
import bigBang.library.client.HasValueSelectables;
import bigBang.library.client.event.ActionInvokedEventHandler;
import bigBang.library.client.userInterface.view.View;
import bigBang.module.casualtyModule.client.userInterface.presenter.MedicalFileViewPresenter;
import bigBang.module.casualtyModule.client.userInterface.presenter.MedicalFileViewPresenter.Action;

public class MedicalFileView extends View implements MedicalFileViewPresenter.Display{

	@Override
	protected void initializeView() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public HasEditableValue<MedicalFile> getForm() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public HasValue<SubCasualty> getOwnerForm() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void registerActionHandler(ActionInvokedEventHandler<Action> handler) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void allowEdit(boolean b) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setSaveMode(boolean b) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void lockToolbar() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void allowSendMessage(boolean hasPermission) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void allowReceiveMessage(boolean hasPermission) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void allowClose(boolean hasPermission) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void clear() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setOwner(String id) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public HasValueSelectables<BigBangProcess> getSubProcessList() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public HasValueSelectables<HistoryItemStub> getHistoryList() {
		// TODO Auto-generated method stub
		return null;
	}

}
