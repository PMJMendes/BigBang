package bigBang.module.insurancePolicyModule.client.userInterface.presenter;

import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.UIObject;
import com.google.gwt.user.client.ui.Widget;

import bigBang.definitions.client.dataAccess.InsurancePolicyBroker;
import bigBang.definitions.client.dataAccess.InsuredObjectDataBroker;
import bigBang.definitions.shared.BigBangConstants;
import bigBang.library.client.HasParameters;
import bigBang.library.client.dataAccess.DataBrokerManager;
import bigBang.library.client.userInterface.presenter.ViewPresenter;

public class InsurancePolicySearchOperationViewPresenter implements ViewPresenter{


	public static enum Action {
		ON_NEW_RESULTS

	}
	public interface Display {

		Widget asWidget();

	}

	private InsurancePolicyBroker broker;
	private InsuredObjectDataBroker insuredObjectBroker;
	private Display view;
	
	public InsurancePolicySearchOperationViewPresenter(Display view){
		this.broker = ((InsurancePolicyBroker)DataBrokerManager.Util.getInstance().getBroker(BigBangConstants.EntityIds.INSURANCE_POLICY));
		this.insuredObjectBroker = ((InsuredObjectDataBroker)DataBrokerManager.Util.getInstance().getBroker(BigBangConstants.EntityIds.POLICY_INSURED_OBJECT));
		setView((UIObject) view);
	}


	@Override
	public void setView(UIObject view) {
		this.view = (Display) view;
	}

	@Override
	public void go(HasWidgets container) {
		bind();
		container.clear();
		container.add(this.view.asWidget());
	}

	private void bind() {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void setParameters(HasParameters parameterHolder) {
		// TODO Auto-generated method stub

	}

}
