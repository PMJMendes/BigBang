package bigBang.module.insurancePolicyModule.client.dataAccess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import bigBang.definitions.client.dataAccess.DataBroker;
import bigBang.definitions.client.dataAccess.DataBrokerClient;
import bigBang.definitions.client.dataAccess.ExerciseDataBroker;
import bigBang.definitions.client.dataAccess.ExpenseDataBroker;
import bigBang.definitions.client.dataAccess.InsurancePolicyBroker;
import bigBang.definitions.client.dataAccess.InsurancePolicyDataBrokerClient;
import bigBang.definitions.client.dataAccess.InsuredObjectDataBroker;
import bigBang.definitions.client.dataAccess.Search;
import bigBang.definitions.client.dataAccess.SearchDataBroker;
import bigBang.definitions.client.response.ResponseError;
import bigBang.definitions.client.response.ResponseHandler;
import bigBang.definitions.shared.BigBangConstants;
import bigBang.definitions.shared.DebitNote;
import bigBang.definitions.shared.Expense;
import bigBang.definitions.shared.InfoOrDocumentRequest;
import bigBang.definitions.shared.InsurancePolicyOLD.TableSection;
import bigBang.definitions.shared.ManagerTransfer;
import bigBang.definitions.shared.Negotiation;
import bigBang.definitions.shared.InsurancePolicy;
import bigBang.definitions.shared.InsurancePolicyStub;
import bigBang.definitions.shared.PolicyVoiding;
import bigBang.definitions.shared.Receipt;
import bigBang.definitions.shared.Remap;
import bigBang.definitions.shared.Remap.RemapId;
import bigBang.definitions.shared.SearchParameter;
import bigBang.definitions.shared.SearchResult;
import bigBang.definitions.shared.SortOrder;
import bigBang.definitions.shared.SortParameter;
import bigBang.library.client.BigBangAsyncCallback;
import bigBang.library.client.EventBus;
import bigBang.library.client.dataAccess.DataBrokerManager;
import bigBang.library.client.event.OperationWasExecutedEvent;
import bigBang.library.shared.CorruptedPadException;
import bigBang.module.insurancePolicyModule.interfaces.InsurancePolicyServiceOLD;
import bigBang.module.insurancePolicyModule.interfaces.InsurancePolicyService;
import bigBang.module.insurancePolicyModule.interfaces.InsurancePolicyServiceAsync;
import bigBang.module.insurancePolicyModule.shared.InsurancePolicySearchParameter;
import bigBang.module.insurancePolicyModule.shared.InsurancePolicySortParameter;

import com.google.gwt.core.client.GWT;

public class InsurancePolicyProcessBrokerImpl extends DataBroker<InsurancePolicy> implements InsurancePolicyBroker {

	protected InsurancePolicyServiceAsync service;
	protected SearchDataBroker<InsurancePolicyStub> searchBroker;
	protected InsuredObjectDataBroker insuredObjectsBroker;
	protected ExerciseDataBroker exerciseDataBroker;
	protected Map<String, String> policiesInScratchPad;
	public boolean requiresRefresh;

	public InsurancePolicyProcessBrokerImpl(){
		this(InsurancePolicyService.Util.getInstance());
	}

	public InsurancePolicyProcessBrokerImpl(InsurancePolicyServiceAsync service) {
		this.service = service;
		this.dataElementId = BigBangConstants.EntityIds.INSURANCE_POLICY;
		this.policiesInScratchPad = new HashMap<String, String>();
		this.searchBroker = new InsurancePolicySearchDataBroker(this.service);
	}

	@Override
	public void requireDataRefresh() {
		this.requiresRefresh = true;
	}

	@Override
	public void notifyItemCreation(String itemId) {
		this.getPolicy(itemId, new ResponseHandler<InsurancePolicy>() {

			@Override
			public void onResponse(InsurancePolicy response) {
				incrementDataVersion();
				for(DataBrokerClient<InsurancePolicy> bc : getClients()){
					((InsurancePolicyDataBrokerClient) bc).addInsurancePolicy(response);
					((InsurancePolicyDataBrokerClient) bc).setDataVersionNumber(BigBangConstants.EntityIds.INSURANCE_POLICY, getCurrentDataVersion());
				}
			}

			@Override
			public void onError(Collection<ResponseError> errors) {}
		});
	}

	@Override
	public void notifyItemDeletion(String itemId) {
		incrementDataVersion();
		for(DataBrokerClient<InsurancePolicy> bc : getClients()){
			((InsurancePolicyDataBrokerClient) bc).removeInsurancePolicy(itemId);
			((InsurancePolicyDataBrokerClient) bc).setDataVersionNumber(BigBangConstants.EntityIds.INSURANCE_POLICY, getCurrentDataVersion());
		}
	}

	@Override
	public void notifyItemUpdate(String itemId) {
		this.getPolicy(itemId, new ResponseHandler<InsurancePolicy>() {

			@Override
			public void onResponse(InsurancePolicy response) {
				incrementDataVersion();
				for(DataBrokerClient<InsurancePolicy> bc : getClients()){
					((InsurancePolicyDataBrokerClient) bc).updateInsurancePolicy(response);
					((InsurancePolicyDataBrokerClient) bc).setDataVersionNumber(BigBangConstants.EntityIds.INSURANCE_POLICY, getCurrentDataVersion());
				}
			}

			@Override
			public void onError(Collection<ResponseError> errors) {
				return;
			}
		});
	}

	@Override
	public void getPolicy(String insurancePolicyId,
			final ResponseHandler<InsurancePolicy> handler) {
		final String policyId = getEffectiveId(insurancePolicyId);
		if(isTemp(policyId)){
		}else{
			this.service.getPolicy(policyId, new BigBangAsyncCallback<InsurancePolicy>() {

				@Override
				public void onResponseSuccess(InsurancePolicy result) {
					incrementDataVersion();
					for(DataBrokerClient<InsurancePolicy> bc : getClients()){
						((InsurancePolicyDataBrokerClient) bc).updateInsurancePolicy(result);
						((InsurancePolicyDataBrokerClient) bc).setDataVersionNumber(BigBangConstants.EntityIds.INSURANCE_POLICY, getCurrentDataVersion());
					}
					handler.onResponse(result);
					requiresRefresh = false;
				}

				@Override
				public void onResponseFailure(Throwable caught) {
					handler.onError(new String[]{
							new String("Could not get the requested policy")
					});
					super.onResponseFailure(caught);
				}
			});
		}
	}

	@Override
	public void updatePolicy(InsurancePolicy policy,
			final ResponseHandler<InsurancePolicy> handler) {
		final String policyId = getEffectiveId(policy.id);
		if(isTemp(policyId)){
			String tempPolicyId = policy.id;
			policy.id = policyId;
		}else{
			handler.onError(new String[]{
					new String("Could not save the policy header. The policy is not in scratch pad")
			});
		}
	}

	@Override
	public void removePolicy(String id, final ResponseHandler<String> handler) {
		final String policyId = getFinalMapping(id);
		this.service.deletePolicy(policyId, new BigBangAsyncCallback<Void>() {

			@Override
			public void onResponseSuccess(Void result) {
				String finalId = getFinalMapping(policyId);
				incrementDataVersion();
				for(DataBrokerClient<InsurancePolicy> bc : getClients()){
					((InsurancePolicyDataBrokerClient) bc).removeInsurancePolicy(finalId);
					((InsurancePolicyDataBrokerClient) bc).setDataVersionNumber(BigBangConstants.EntityIds.INSURANCE_POLICY, getCurrentDataVersion());
				}
				EventBus.getInstance().fireEvent(new OperationWasExecutedEvent(BigBangConstants.OperationIds.InsurancePolicyProcess.DELETE_POLICY, finalId));
				handler.onResponse(finalId);
			}

			@Override
			public void onResponseFailure(Throwable caught) {
				handler.onError(new String[]{
						new String("Could not remove the insurance policy")	
				});
				super.onResponseFailure(caught);
			}
		});
	}

	@Override
	public void getClientPolicies(String clientid,
			final ResponseHandler<Collection<InsurancePolicyStub>> policies) {
		InsurancePolicySearchParameter parameter = new InsurancePolicySearchParameter();
		parameter.ownerId = clientid;

		SearchParameter[] parameters = new SearchParameter[]{
				parameter
		};

		SortParameter sort = new InsurancePolicySortParameter(InsurancePolicySortParameter.SortableField.NUMBER, SortOrder.DESC);		
		SortParameter[] sorts = new SortParameter[]{
				sort
		};

		this.searchBroker.search(parameters, sorts, -1, new ResponseHandler<Search<InsurancePolicyStub>>() {

			@Override
			public void onResponse(Search<InsurancePolicyStub> response) {
				policies.onResponse(response.getResults());
			}

			@Override
			public void onError(Collection<ResponseError> errors) {
				policies.onError(new String[]{
						new String("Could not get the client policies")	
				});
			}
		}, true);
	}

	@Override
	public void createReceipt(String id, Receipt receipt,
			final ResponseHandler<Receipt> handler) {
		String policyId = getFinalMapping(id);
		this.service.createReceipt(policyId, receipt, new BigBangAsyncCallback<Receipt>() {

			@Override
			public void onResponseSuccess(Receipt result) {
				EventBus.getInstance().fireEvent(new OperationWasExecutedEvent(BigBangConstants.OperationIds.InsurancePolicyProcess.CREATE_RECEIPT, result.id));
				handler.onResponse(result);
				DataBrokerManager.Util.getInstance().getBroker(BigBangConstants.EntityIds.RECEIPT).notifyItemCreation(result.id);
			}

			@Override
			public void onResponseFailure(Throwable caught) {
				handler.onError(new String[]{
						new String("Could not create receipt")	
				});
				super.onResponseFailure(caught);
			}

		});
	}

	@Override
	public SearchDataBroker<InsurancePolicyStub> getSearchBroker() {
		return this.searchBroker;
	}

	@Override
	public void openPolicyResource(final String policyId,
			final ResponseHandler<InsurancePolicy> handler) {

		//NEW POLICY
		if(policyId == null){
//			service.openPolicyScratchPad(policyId, new BigBangAsyncCallback<Remap[]>() {
//
//				@Override
//				public void onResponseSuccess(Remap[] result) {
//					//If new policy
//					if(result.length == 1 && result[0].remapIds.length == 1 && result[0].remapIds[0].oldId == null){
//						InsurancePolicy policy = new InsurancePolicy();
//						RemapId remapId = result[0].remapIds[0];
//						policy.id = remapId.newId;
//						policiesInScratchPad.put(policy.id, policy.id);
//						handler.onResponse(policy);
//					}
//				}
//
//				@Override
//				public void onResponseFailure(Throwable caught) {
//					if(caught instanceof CorruptedPadException){
//						onPadCorrupted(policyId);
//					}
//					handler.onError(new String[]{
//							new String("Could not open the new policy resource")
//					});
//					super.onResponseFailure(caught);
//				}
//
//			});
		}else if(!isTemp(policyId)){
			//EXISTING POLICY
//			service.openPolicyScratchPad(policyId, new BigBangAsyncCallback<Remap[]>() {
//
//				@Override
//				public void onResponseSuccess(Remap[] result) {
//					doRemapping(result);
//					getPolicy(policyId, handler);
//				}
//
//				@Override
//				public void onResponseFailure(Throwable caught) {
//					if(caught instanceof CorruptedPadException){
//						onPadCorrupted(policyId);
//					}
//					handler.onError(new String[]{
//							new String("Could not open the policy resource")
//					});
//					super.onResponseFailure(caught);
//				}
//
//			});
		}else{
			handler.onError(new String[]{
					new String("Cannot open a policy resource twice for the same policy")	
			});
		}
	}

	@Override
	public void commitPolicy(final InsurancePolicy policy, final ResponseHandler<InsurancePolicy> handler){
		final String policyId = getEffectiveId(policy.id);
//		this.service.commitPad(policyId, new BigBangAsyncCallback<Remap[]>() {
//
//			@Override
//			public void onResponseSuccess(Remap[] result) {
//				EventBus.getInstance().fireEvent(new OperationWasExecutedEvent(BigBangConstants.OperationIds.InsurancePolicyProcess.UPDATE_POLICY, policy.id));
//				String policyId = result[0].remapIds[0].newId;
//				doRemapping(result);
//				getPolicy(policyId, handler);
//			}
//
//			@Override
//			public void onResponseFailure(Throwable caught) {
//				if(caught instanceof CorruptedPadException){
//					onPadCorrupted(policyId);
//				}
//				handler.onError(new String[]{
//						new String("Could not commit the scratch pad")	
//				});
//				super.onResponseFailure(caught);
//			}
//
//		});
	}

	@Override
	public void closePolicyResource(String policyId, final ResponseHandler<Void> handler) {
		policyId = getEffectiveId(policyId);
		if(isTemp(policyId)){
//			service.discardPad(policyId, new BigBangAsyncCallback<Remap[]>() {
//
//				@Override
//				public void onResponseSuccess(Remap[] result) {
//					doRemapping(result);
//					handler.onResponse(null);
//				}
//
//				@Override
//				public void onResponseFailure(Throwable caught) {
//					handler.onError(new String[]{
//							new String("Could not close the policy resource")	
//					});
//					super.onResponseFailure(caught);
//				}
//			});
		}else{
			handler.onError(new String[]{
					new String("Cannot close an unopened policy resource")
			});
		}
	}

	@Override
	public void openCoverageDetailsPage(String insurancePolicyId,
			String insuredObjectId, String exerciseId,
			final ResponseHandler<TableSection> handler) {
		final String policyId = getEffectiveId(insurancePolicyId);
		if(isTemp(policyId)){
//			service.getPageForEdit(policyId, insuredObjectId, exerciseId, new BigBangAsyncCallback<TableSection>() {
//
//				@Override
//				public void onResponseSuccess(TableSection result) {
//					handler.onResponse(result);
//				}
//
//				@Override
//				public void onResponseFailure(Throwable caught) {
//					if(caught instanceof CorruptedPadException){
//						onPadCorrupted(policyId);
//					}
//					handler.onError(new String[]{
//							new String("Could not get the requested page for edit")
//					});
//					super.onResponseFailure(caught);
//				}
//			});
		}else{
//			service.getPage(policyId, insuredObjectId, exerciseId, new BigBangAsyncCallback<TableSection>() {
//
//				@Override
//				public void onResponseSuccess(TableSection result) {
//					handler.onResponse(result);
//				}
//
//				@Override
//				public void onResponseFailure(Throwable caught) {
//					handler.onError(new String[]{
//							new String("Could not get the requested page")
//					});
//					super.onResponseFailure(caught);
//				}
//			});
		}
	}

	@Override
	public void saveCoverageDetailsPage(String insurancePolicyId,
			String insuredObjectId, String exerciseId, TableSection data,
			final ResponseHandler<TableSection> handler) {
		final String policyId = getEffectiveId(insurancePolicyId);
		if(isTemp(policyId)){
		}else{
			handler.onError(new String[]{
					new String("Could not save the page on an unopened policy resource")
			});
		}
	}

	@Override
	public void initPolicy(InsurancePolicy policy,
			final ResponseHandler<InsurancePolicy> handler) {
		policy.id = getEffectiveId(policy.id);
	}
	
	@Override
	public boolean isTemp(String policyId) {
		return this.policiesInScratchPad.containsKey(policyId) || this.policiesInScratchPad.containsValue(policyId);
	}

	protected void doRemapping(Remap[] remappings){
		for(int i = 0; i < remappings.length; i++) {
			Remap remap = remappings[i];

			//POLICIES
			if(remap.typeId.equalsIgnoreCase(BigBangConstants.EntityIds.INSURANCE_POLICY)){
				for(int j = 0; j < remap.remapIds.length; j++){
					RemapId remapId = remap.remapIds[j];
					remapItemId(remapId.oldId, remapId.newId, remapId.newIdIsInPad);
				}

				//OBJECTS
			}else if(remap.typeId.equalsIgnoreCase(BigBangConstants.EntityIds.POLICY_INSURED_OBJECT)){
				for(int j = 0; j < remap.remapIds.length; j++){
					RemapId remapId = remap.remapIds[j];
					this.insuredObjectsBroker.remapItemId(remapId.oldId, remapId.newId, remapId.newIdIsInPad);
				}

				//EXERCISES
			}else if(remap.typeId.equalsIgnoreCase(BigBangConstants.EntityIds.POLICY_EXERCISE)){
				for(int j = 0; j < remap.remapIds.length; j++){
					RemapId remapId = remap.remapIds[j];
					this.exerciseDataBroker.remapItemId(remapId.oldId, remapId.newId, remapId.newIdIsInPad);
				}
			}else{
				GWT.log("Could not remap item id for typeId = " + remap.typeId);
			}
		}
	}

	@Override
	public void validatePolicy(final String policyId, final ResponseHandler<Void> handler) {
		service.validatePolicy(policyId, new BigBangAsyncCallback<Void>() {

			@Override
			public void onResponseSuccess(Void result) {
				EventBus.getInstance().fireEvent(new OperationWasExecutedEvent(BigBangConstants.OperationIds.InsurancePolicyProcess.VALIDATE_POLICY, policyId));
				handler.onResponse(null);
			}

			@Override
			public void onResponseFailure(Throwable caught) {
				handler.onError(new String[]{
						caught.getMessage()	
				});
				super.onResponseFailure(caught);
			}
		});
	}
	
	@Override
	public void executeDetailedCalculations(final String policyId, final ResponseHandler<InsurancePolicy> handler){
//		service.performCalculations(policyId, new BigBangAsyncCallback<Policy2>() {
//
//			@Override
//			public void onResponseSuccess(Policy2 result) {
//				EventBus.getInstance().fireEvent(new OperationWasExecutedEvent(BigBangConstants.OperationIds.InsurancePolicyProcess.EXECUTE_DETAILED_CALCULATIONS, policyId));
//				handler.onResponse(result);
//			}
//			
//			@Override
//			public void onResponseFailure(Throwable caught) {
//				handler.onError(new String[]{
//						new String("Could not perform detailed calculations")
//				});
//				super.onResponseFailure(caught);
//			}
//		}); TODO
	}

	@Override
	public void voidPolicy(PolicyVoiding voiding, final ResponseHandler<InsurancePolicy> handler) {
//		service.voidPolicy(voiding, new BigBangAsyncCallback<Policy2>() {
//
//			@Override
//			public void onResponseSuccess(Policy2 result) {
//				EventBus.getInstance().fireEvent(new OperationWasExecutedEvent(BigBangConstants.OperationIds.InsurancePolicyProcess.VOID_POLICY, result.id));
//				handler.onResponse(result);
//			}
//			
//			@Override
//			public void onResponseFailure(Throwable caught) {
//				handler.onError(new String[]{
//						new String("Could not void the policy")
//				});
//				super.onResponseFailure(caught);
//			}
//		}); TODO
	}
	
	@Override
	public void issueDebitNote(final String policyId, DebitNote note, final ResponseHandler<Void> handler) {
		service.createDebitNote(policyId, note, new BigBangAsyncCallback<Void>() {

			@Override
			public void onResponseSuccess(Void result) {
				EventBus.getInstance().fireEvent(new OperationWasExecutedEvent(BigBangConstants.OperationIds.InsurancePolicyProcess.CREATE_DEBIT_NOTE, policyId));
				handler.onResponse(null);
			}
			
			@Override
			public void onResponseFailure(Throwable caught) {
				handler.onError(new String[]{
					new String("Could not issue debit note")	
				});
				super.onResponseFailure(caught);
			}
		});
	}
	
	@Override
	public void remapItemId(String oldId, String newId, boolean newInScratchPad) {
		if(newInScratchPad){
			this.policiesInScratchPad.put(oldId, newId);
		}else if(newId == null){
			discardTemp(oldId);
		}else {
			this.policiesInScratchPad.remove(newId);
		}
		incrementDataVersion();
		for(DataBrokerClient<InsurancePolicy> bc : getClients()){
			((InsurancePolicyDataBrokerClient) bc).remapItemId(oldId, newId);
			((InsurancePolicyDataBrokerClient) bc).setDataVersionNumber(BigBangConstants.EntityIds.INSURANCE_POLICY, getCurrentDataVersion());
		}
	}

	@Override
	public void discardTemp(String policyId) {
		this.policiesInScratchPad.remove(policyId);
		String finalId = getFinalMapping(policyId);
		this.policiesInScratchPad.remove(finalId);
	}

	public String getEffectiveId(String id){
		if(this.policiesInScratchPad.containsKey(id)){
			return this.policiesInScratchPad.get(id);
		}
		return id;
	}

	public String getFinalMapping(String tempId){
		for(String key : this.policiesInScratchPad.keySet()){
			if(this.policiesInScratchPad.get(key).equalsIgnoreCase(tempId)){
				return key;
			}
		}
		return tempId;
	}

	@Override
	public void getPage(String insurancePolicyId, String insuredObjectId,
			String exerciseId, final ResponseHandler<TableSection> handler) {
		final String policyId = getEffectiveId(insurancePolicyId);
		if(isTemp(policyId)){
//			service.getPageForEdit(policyId, insuredObjectId, exerciseId, new BigBangAsyncCallback<TableSection>() {
//
//				@Override
//				public void onResponseSuccess(TableSection result) {
//					handler.onResponse(result);
//				}
//
//				@Override
//				public void onResponseFailure(Throwable caught) {
//					if(caught instanceof CorruptedPadException){
//						onPadCorrupted(policyId);
//					}
//					handler.onError(new String[]{
//							new String("Could not get the requested page")
//					});
//					super.onResponseFailure(caught);
//				}
//			});
		}else{
//			service.getPage(policyId, insuredObjectId, exerciseId, new BigBangAsyncCallback<TableSection>() {
//
//				@Override
//				public void onResponseSuccess(TableSection result) {
//					handler.onResponse(result);
//				}
//
//				@Override
//				public void onResponseFailure(Throwable caught) {
//					handler.onError(new String[]{
//							new String("Could not get the requested page")
//					});
//					super.onResponseFailure(caught);
//				}
//			});
		}
	}

	private void onPadCorrupted(String policyId){
		closePolicyResource(policyId, new ResponseHandler<Void>() {

			@Override
			public void onResponse(Void response) {}

			@Override
			public void onError(Collection<ResponseError> errors) {}
		});
	}

	@Override
	public void createNegotiation(Negotiation negotiation, final ResponseHandler<Negotiation> handler) {
		service.createNegotiation(negotiation, new BigBangAsyncCallback<Negotiation>() {

			@Override
			public void onResponseSuccess(Negotiation result) {
				EventBus.getInstance().fireEvent(new OperationWasExecutedEvent(BigBangConstants.OperationIds.InsurancePolicyProcess.CREATE_NEGOTIATION, result.id));
				handler.onResponse(result);
				DataBrokerManager.Util.getInstance().getBroker(BigBangConstants.EntityIds.NEGOTIATION).notifyItemCreation(result.id);
			}
			
			@Override
			public void onFailure(Throwable caught) {
				handler.onError(new String[]{
						new String("Could not save the negotiation")
				});
				super.onResponseFailure(caught);
			}
		});
		
	}
	
	@Override
	public void transferToClient(String policyId, String newClientId, final ResponseHandler<InsurancePolicy> handler){
//		service.transferToClient(policyId, newClientId, new BigBangAsyncCallback<Policy2>() {
//
//			@Override
//			public void onResponseSuccess(Policy2 result) {
//				incrementDataVersion();
//				for(DataBrokerClient<Policy2> bc : getClients()){
//					((InsurancePolicyDataBrokerClient) bc).updateInsurancePolicy(result);
//					((InsurancePolicyDataBrokerClient) bc).setDataVersionNumber(BigBangConstants.EntityIds.INSURANCE_POLICY, getCurrentDataVersion());
//				}
//				EventBus.getInstance().fireEvent(new OperationWasExecutedEvent(BigBangConstants.OperationIds.InsurancePolicyProcess.TRANSFER_TO_CLIENT, result.id));
//				handler.onResponse(result);				
//			}
//		
//			@Override
//			public void onResponseFailure(Throwable caught) {
//				handler.onError(new String[]{
//						new String("Could not transfer to client")
//				});
//				super.onResponseFailure(caught);
//			}
//		
//		
//		}); TODO
	}
	
	@Override
	public void createManagerTransfer(String[] objectIds, String newManagerId, final ResponseHandler<Void> handler) {
		ManagerTransfer transfer = new ManagerTransfer();
		transfer.dataObjectIds = objectIds;
		transfer.newManagerId = newManagerId;

		if(objectIds.length == 1) {
			service.createManagerTransfer(transfer, new BigBangAsyncCallback<ManagerTransfer>() {

				@Override
				public void onResponseSuccess(ManagerTransfer result) {
					EventBus.getInstance().fireEvent(new OperationWasExecutedEvent(BigBangConstants.OperationIds.InsurancePolicyProcess.TRANSFER_MANAGER, result.id));
					handler.onResponse(null);
				}
				
				@Override
				public void onResponseFailure(Throwable caught) {
					handler.onError(new String[]{
							new String("Could not transfer the process")
					});
					super.onResponseFailure(caught);
				}
			});
		}else if(objectIds.length > 1){
			service.createManagerTransfer(transfer, new BigBangAsyncCallback<ManagerTransfer>() {

				@Override
				public void onResponseSuccess(ManagerTransfer result) {
					EventBus.getInstance().fireEvent(new OperationWasExecutedEvent(BigBangConstants.OperationIds.InsurancePolicyProcess.TRANSFER_MANAGER, result.id));
					handler.onResponse(null);
				}
				
				@Override
				public void onResponseFailure(Throwable caught) {
					handler.onError(new String[]{
							new String("Could not transfer the processes")
					});
					super.onResponseFailure(caught);
				}
			});
		}else{
			handler.onError(new String[]{
				new String("Cannot transfer 0 processes")	
			});
		}
	}
	
	@Override
	public void getInsurancePoliciesWithNumber(String label, final ResponseHandler<Collection<InsurancePolicyStub>> handler){
		service.getExactResults(label, new BigBangAsyncCallback<SearchResult[]>() {

			@Override
			public void onResponseSuccess(SearchResult[] result) {
				ArrayList<InsurancePolicyStub> stubs = new ArrayList<InsurancePolicyStub>();
				
				for(int i = 0; i<result.length; i++){
					stubs.add((InsurancePolicyStub)result[i]);
				}
				handler.onResponse(stubs);
			}
			
			@Override
			public void onResponseFailure(Throwable caught) {
				handler.onError(new String[]{
						new String("Could not find exact results")
				});
				super.onResponseFailure(caught);
			}
		
		
		});
	}
	
	@Override
	public void createExpense(Expense expense, final ResponseHandler<Expense> handler){
		service.createExpense(expense, new BigBangAsyncCallback<Expense>() {

			@Override
			public void onResponseSuccess(Expense result) {
				EventBus.getInstance().fireEvent(new OperationWasExecutedEvent(BigBangConstants.OperationIds.InsurancePolicyProcess.CREATE_HEALTH_EXPENSE, result.id));

				handler.onResponse(result);
				((ExpenseDataBroker) DataBrokerManager.staticGetBroker(BigBangConstants.EntityIds.EXPENSE)).notifyItemCreation(result.id);
			}

			@Override
			public void onResponseFailure(Throwable caught) {
				handler.onError(new String[]{
						new String("Could not create the new Expense")	
				});
				super.onResponseFailure(caught);
			}
		});
	}

	@Override
	public void createCompanyInfoRequest(InfoOrDocumentRequest request,
			final ResponseHandler<InfoOrDocumentRequest> responseHandler) {
		service.createInfoOrDocumentRequest(request, new BigBangAsyncCallback<InfoOrDocumentRequest>() {

			@Override
			public void onResponseSuccess(InfoOrDocumentRequest result) {
				EventBus.getInstance().fireEvent(new OperationWasExecutedEvent(BigBangConstants.OperationIds.InsurancePolicyProcess.CREATE_COMPANY_INFO_REQUEST, result.id));

				responseHandler.onResponse(result);
			}
			
			@Override
			public void onResponseFailure(Throwable caught) {
				responseHandler.onError(new String[]{
					new String("Could not create de info request")	
				});
				super.onResponseFailure(caught);
			}
		});
	}

	@Override
	public void createClientInfoRequest(InfoOrDocumentRequest request,
			final ResponseHandler<InfoOrDocumentRequest> responseHandler) {
		service.createInfoOrDocumentRequest(request, new BigBangAsyncCallback<InfoOrDocumentRequest>() {

			@Override
			public void onResponseSuccess(InfoOrDocumentRequest result) {
				EventBus.getInstance().fireEvent(new OperationWasExecutedEvent(BigBangConstants.OperationIds.InsurancePolicyProcess.CREATE_CLIENT_INFO_REQUEST, result.id));

				responseHandler.onResponse(result);
			}
			
			@Override
			public void onResponseFailure(Throwable caught) {
				responseHandler.onError(new String[]{
					new String("Could not create de info request")	
				});
				super.onResponseFailure(caught);
			}
		});
	}

	@Override
	public boolean isNewPolicy(String policyId) {
		return isTemp(policyId) && this.policiesInScratchPad.containsKey(policyId) && this.policiesInScratchPad.containsValue(policyId);
	}

}
