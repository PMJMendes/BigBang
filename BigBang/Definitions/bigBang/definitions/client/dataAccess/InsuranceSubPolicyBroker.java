package bigBang.definitions.client.dataAccess;

import java.util.Collection;

import bigBang.definitions.client.response.ResponseHandler;
import bigBang.definitions.shared.PolicyVoiding;
import bigBang.definitions.shared.Receipt;
import bigBang.definitions.shared.SubPolicy;
import bigBang.definitions.shared.SubPolicy.TableSection;
import bigBang.definitions.shared.SubPolicyStub;

public interface InsuranceSubPolicyBroker extends
		DataBrokerInterface<SubPolicy> {

	public boolean isTemp(String subPolicyId);
	
	public void getSubPolicy(String subPolicyId, ResponseHandler<SubPolicy> handler);

	public void openSubPolicyResource(String subPolicyId, ResponseHandler<SubPolicy> handler);
	
	public void getSubPolicyDefinition(SubPolicy subPolicy, ResponseHandler<SubPolicy> handler);

	public void commitSubPolicy(SubPolicy subPolicy, ResponseHandler<SubPolicy> handler);

	public void closeSubPolicyResource(String subPolicyId, ResponseHandler<Void> handler);

	public void openCoverageDetailsPage(String subPolicyId, String insuredObjectId, String exerciseId, ResponseHandler<SubPolicy.TableSection> handler);

	public void saveCoverageDetailsPage(String subPolicyId, String insuredObjectId, String exerciseId, SubPolicy.TableSection data, ResponseHandler<SubPolicy.TableSection> handler);

	public void updateSubPolicy(SubPolicy subPolicy, ResponseHandler<SubPolicy> handler);

	public void removeSubPolicy(String subPolicyId, ResponseHandler<String> handler);
	
	public void createReceipt(String subPolicyId, Receipt receipt, ResponseHandler<Receipt> handler);

	public void remapItemId(String oldId, String newId, boolean inScratchPad);
	
	public SearchDataBroker<SubPolicyStub> getSearchBroker();

	public void discardTemp(String subPolicyId);
	
	public void getPage(String subPolicyId, String insuredObjectId, String exerciseId, ResponseHandler<TableSection> handler);

	void validateSubPolicy(String subPolicyId, ResponseHandler<Void> handler) throws bigBang.definitions.shared.BigBangPolicyValidationException;
	
	void executeDetailedCalculations(String subPolicyId, ResponseHandler<SubPolicy> handler);
	
	void voidSubPolicy(PolicyVoiding voiding, ResponseHandler<SubPolicy> responseHandler);
	
	public String getEffectiveId(String ownerId);

	public String getFinalMapping(String ownerId);

	public void getSubPoliciesForPolicy(String ownerId,
			ResponseHandler<Collection<SubPolicyStub>> responseHandler);

}
