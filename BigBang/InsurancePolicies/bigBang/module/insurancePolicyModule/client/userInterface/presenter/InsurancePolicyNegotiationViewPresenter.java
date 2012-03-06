package bigBang.module.insurancePolicyModule.client.userInterface.presenter;

import java.util.Collection;

import bigBang.definitions.client.dataAccess.InsurancePolicyBroker;
import bigBang.definitions.client.dataAccess.NegotiationBroker;
import bigBang.definitions.client.response.ResponseError;
import bigBang.definitions.client.response.ResponseHandler;
import bigBang.definitions.shared.BigBangConstants;
import bigBang.definitions.shared.Negotiation;
import bigBang.definitions.shared.Negotiation.Deletion;
import bigBang.library.client.HasParameters;
import bigBang.library.client.dataAccess.DataBrokerManager;
import bigBang.module.quoteRequestModule.client.userInterface.presenter.NegotiationViewPresenter;

public class InsurancePolicyNegotiationViewPresenter extends NegotiationViewPresenter{

	private InsurancePolicyBroker insurancePolicyBroker;
	
	
	public InsurancePolicyNegotiationViewPresenter(Display view) {
		super(view);
		insurancePolicyBroker = (InsurancePolicyBroker) DataBrokerManager.staticGetBroker(BigBangConstants.EntityIds.INSURANCE_POLICY);
		
	}
	
	@Override
	public void setParameters(HasParameters parameterHolder){
		super.setParameters(parameterHolder);
	}

	@Override
	protected void createNegotiation(Negotiation negotiation) {
		
		//METER O OWNERTYPEID
		insurancePolicyBroker.createNegotiation(negotiation, new ResponseHandler<Negotiation>() {
			
			@Override
			public void onResponse(Negotiation response) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onError(Collection<ResponseError> errors) {
				// TODO Auto-generated method stub
				
			}
		});
	}
	
}
