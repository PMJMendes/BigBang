package bigBang.definitions.client.dataAccess;

import java.util.Collection;

import bigBang.definitions.client.response.ResponseHandler;
import bigBang.definitions.shared.Exercise;
import bigBang.definitions.shared.InsurancePolicy;
import bigBang.definitions.shared.InsurancePolicyStub;
import bigBang.definitions.shared.InsuredObject;
import bigBang.definitions.shared.Receipt;

public interface InsurancePolicyBroker extends DataBrokerInterface<InsurancePolicy> {
	
	public void getPolicy(String policyId, ResponseHandler<InsurancePolicy> handler);
	
	public void openPolicyResource(InsurancePolicy policy, ResponseHandler<InsurancePolicy> handler);

	public void commitPolicy(InsurancePolicy policy, ResponseHandler<InsurancePolicy> handler);
	
	public void closePolicyResource(String policyId, ResponseHandler<Void> handler);
	
	public void openCoverageDetailsPage(String policyId, String insuredObjectId, String exerciseId, ResponseHandler<InsurancePolicy.TableSection> handler);
	
	public void saveCoverageDetailsPage(String policyId, String insuredObjectId, String exerciseId, InsurancePolicy.TableSection data, ResponseHandler<InsurancePolicy.TableSection> handler);
	
	public void updatePolicy(InsurancePolicy policy, ResponseHandler<InsurancePolicy> handler);
	
	public void removePolicy(String policyId, ResponseHandler<String> handler);
	
	public void getClientPolicies(String clientid, ResponseHandler<Collection<InsurancePolicyStub>> policies);
	
	public void createReceipt(String policyId, Receipt receipt, ResponseHandler<Receipt> handler);

	//Insured Objects
	public void createInsuredObject(String policyId, InsuredObject object, ResponseHandler<InsuredObject> handler);
	
	public void updateInsuredObject(String policyId, InsuredObject object, ResponseHandler<InsuredObject> handler);
	
	public void removeInsuredObject(String policyId, InsuredObject object);
	
	//Exercises
	public void createExercise(String policyId, Exercise exercise, ResponseHandler<Exercise> handler);
	
	public void updateExercise(String policyId, Exercise exercise, ResponseHandler<Exercise> handler);
	
	public void removeExercise(String policyId, String exerciseId);

	
	public SearchDataBroker<InsurancePolicyStub> getSearchBroker();
	
}
