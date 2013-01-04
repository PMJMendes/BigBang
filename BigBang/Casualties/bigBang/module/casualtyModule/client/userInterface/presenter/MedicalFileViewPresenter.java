package bigBang.module.casualtyModule.client.userInterface.presenter;

import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.UIObject;
import com.google.gwt.user.client.ui.Widget;

import bigBang.definitions.client.BigBangConstants;
import bigBang.definitions.client.dataAccess.SubCasualtyDataBroker;
import bigBang.definitions.shared.BigBangProcess;
import bigBang.definitions.shared.HistoryItemStub;
import bigBang.definitions.shared.MedicalFile;
import bigBang.definitions.shared.SubCasualty;
import bigBang.library.client.HasEditableValue;
import bigBang.library.client.HasParameters;
import bigBang.library.client.HasValueSelectables;
import bigBang.library.client.dataAccess.DataBrokerManager;
import bigBang.library.client.event.ActionInvokedEventHandler;
import bigBang.library.client.userInterface.presenter.ViewPresenter;
import bigBang.module.casualtyModule.client.dataAccess.MedicalFileBroker;
import bigBang.module.casualtyModule.client.userInterface.view.MedicalFileView;

public class MedicalFileViewPresenter implements ViewPresenter{

	public enum Action{
		SAVE,
		CANCEL,
		SEND_MESSAGE,
		RECEIVE_MESSAGE,
		CLOSE, EDIT, BACK
	}
	
	public interface Display{
		HasEditableValue<MedicalFile> getForm();
		HasValue<SubCasualty> getOwnerForm();
		void registerActionHandler(ActionInvokedEventHandler<Action> handler);
		Widget asWidget();
		void allowEdit(boolean b);
		void setSaveMode(boolean b);
		void lockToolbar();
		void allowSendMessage(boolean hasPermission);
		void allowReceiveMessage(boolean hasPermission);
		void allowClose(boolean hasPermission);
		void clear();
		void setOwner(String id);
		HasValueSelectables<BigBangProcess> getSubProcessList();
		HasValueSelectables<HistoryItemStub> getHistoryList();
	}
	
	Display view;
	private SubCasualtyDataBroker subCasualtyBroker;
	private MedicalFileBroker broker;
	
	public MedicalFileViewPresenter(MedicalFileView view) {
		setView((UIObject)view);
		this.subCasualtyBroker = (SubCasualtyDataBroker) DataBrokerManager.staticGetBroker(BigBangConstants.EntityIds.SUB_CASUALTY);
		
	}

	@Override
	public void setView(UIObject view) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void go(HasWidgets container) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setParameters(HasParameters parameterHolder) {
		// TODO Auto-generated method stub
		
	}

}
