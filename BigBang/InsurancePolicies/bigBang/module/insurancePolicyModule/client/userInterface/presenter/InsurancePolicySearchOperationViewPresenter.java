package bigBang.module.insurancePolicyModule.client.userInterface.presenter;

import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.UIObject;
import com.google.gwt.user.client.ui.Widget;

import bigBang.definitions.client.dataAccess.InsurancePolicyBroker;
import bigBang.definitions.shared.BigBangConstants;
import bigBang.definitions.shared.InsurancePolicy;
import bigBang.library.client.BigBangAsyncCallback;
import bigBang.library.client.HasParameters;
import bigBang.library.client.dataAccess.DataBrokerManager;
import bigBang.library.client.userInterface.presenter.ViewPresenter;
import bigBang.module.insurancePolicyModule.interfaces.InsurancePolicyService;
import bigBang.module.insurancePolicyModule.interfaces.InsurancePolicyServiceAsync;

import com.google.gwt.core.client.GWT;



public class InsurancePolicySearchOperationViewPresenter implements ViewPresenter{

	public static class Services
	{
		public static final InsurancePolicyServiceAsync insurancePolicyService =
				GWT.create(InsurancePolicyService.class);
	}

	public static enum Action {
		ON_NEW_RESULTS

	}
	public interface Display {

		Widget asWidget();

		void setValue(InsurancePolicy result);

		void setReadOnly(boolean readOnly);
	}

	private InsurancePolicyBroker broker;
	private Display view;

	public InsurancePolicySearchOperationViewPresenter(Display view){
		this.broker = ((InsurancePolicyBroker)DataBrokerManager.Util.getInstance().getBroker(BigBangConstants.EntityIds.INSURANCE_POLICY));
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
		//		String sublineId = "BEBB58B5-CD95-4872-B72F-9EE90118938F";
		//		Services.insurancePolicyService.getEmptyPolicy(sublineId,"5936520C-307B-48BC-993E-A0B80001F71A", new BigBangAsyncCallback<InsurancePolicy>() {
		//
		//			@Override
		//			public void onResponseSuccess(InsurancePolicy result) {
		//				view.setValue(result);
		//				view.setReadOnly(false);
		//			}
		//		
		//		
		//		});

		Services.insurancePolicyService.getPolicy("8A0CCD8B-2107-4D7D-826C-A0B800162382", new BigBangAsyncCallback<InsurancePolicy>() {

			@Override
			public void onResponseSuccess(InsurancePolicy result) {
				view.setValue(result);			
				view.setReadOnly(true);
			}



		});

	}

}
