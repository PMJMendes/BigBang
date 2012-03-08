package bigBang.module.insurancePolicyModule.client.dataAccess;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import com.google.gwt.core.client.GWT;

import bigBang.definitions.client.dataAccess.DataBroker;
import bigBang.definitions.client.dataAccess.DataBrokerClient;
import bigBang.definitions.client.dataAccess.InsuranceSubPolicyBroker;
import bigBang.definitions.client.dataAccess.InsuranceSubPolicyDataBrokerClient;
import bigBang.definitions.client.dataAccess.Search;
import bigBang.definitions.client.dataAccess.SearchDataBroker;
import bigBang.definitions.client.response.ResponseError;
import bigBang.definitions.client.response.ResponseHandler;
import bigBang.definitions.shared.BigBangConstants;
import bigBang.definitions.shared.BigBangPolicyValidationException;
import bigBang.definitions.shared.PolicyVoiding;
import bigBang.definitions.shared.Receipt;
import bigBang.definitions.shared.Remap;
import bigBang.definitions.shared.SortOrder;
import bigBang.definitions.shared.SubPolicy;
import bigBang.definitions.shared.SubPolicyStub;
import bigBang.definitions.shared.Remap.RemapId;
import bigBang.definitions.shared.SubPolicy.TableSection;
import bigBang.library.client.BigBangAsyncCallback;
import bigBang.library.client.EventBus;
import bigBang.library.client.dataAccess.DataBrokerManager;
import bigBang.library.client.event.OperationWasExecutedEvent;
import bigBang.library.shared.CorruptedPadException;
import bigBang.module.insurancePolicyModule.interfaces.SubPolicyService;
import bigBang.module.insurancePolicyModule.interfaces.SubPolicyServiceAsync;
import bigBang.module.insurancePolicyModule.shared.SubPolicySearchParameter;
import bigBang.module.insurancePolicyModule.shared.SubPolicySortParameter;

public class InsuranceSubPolicyBrokerImpl extends DataBroker<SubPolicy>
implements InsuranceSubPolicyBroker {

	protected SubPolicyServiceAsync service;
	protected SearchDataBroker<SubPolicyStub> searchBroker;
	protected Map<String, String> subPoliciesInScratchPad;

	public InsuranceSubPolicyBrokerImpl() {
		this.dataElementId = BigBangConstants.EntityIds.INSURANCE_SUB_POLICY;
		this.service = SubPolicyService.Util.getInstance();
		this.subPoliciesInScratchPad = new HashMap<String, String>();
		this.searchBroker = new InsuranceSubPolicySearchDataBroker();
	}

	@Override
	public void requireDataRefresh() {
		return; //No cache usage, so no need to signal refresh
	}

	@Override
	public void notifyItemCreation(String itemId) {
		this.getSubPolicy(itemId, new ResponseHandler<SubPolicy>() {

			@Override
			public void onResponse(SubPolicy response) {
				incrementDataVersion();
				for(DataBrokerClient<SubPolicy> bc : getClients()) {
					((InsuranceSubPolicyDataBrokerClient) bc).addInsuranceSubPolicy(response);
					((InsuranceSubPolicyDataBrokerClient) bc).setDataVersionNumber(getDataElementId(), getCurrentDataVersion());
				}
			}

			@Override
			public void onError(Collection<ResponseError> errors) {}
		});
	}

	@Override
	public void notifyItemDeletion(String itemId) {
		incrementDataVersion();
		for(DataBrokerClient<SubPolicy> bc : getClients()) {
			((InsuranceSubPolicyDataBrokerClient) bc).removeInsuranceSubPolicy(itemId);
			((InsuranceSubPolicyDataBrokerClient) bc).setDataVersionNumber(getDataElementId(), getCurrentDataVersion());
		}
	}

	@Override
	public void notifyItemUpdate(String itemId) {
		this.getSubPolicy(itemId, new ResponseHandler<SubPolicy>() {

			@Override
			public void onResponse(SubPolicy response) {
				incrementDataVersion();
				for(DataBrokerClient<SubPolicy> bc : getClients()) {
					((InsuranceSubPolicyDataBrokerClient) bc).updateInsuranceSubPolicy(response);
					((InsuranceSubPolicyDataBrokerClient) bc).setDataVersionNumber(getDataElementId(), getCurrentDataVersion());
				}
			}

			@Override
			public void onError(Collection<ResponseError> errors) {}
		});
	}

	@Override
	public boolean isTemp(String subPolicyId) {
		return this.subPoliciesInScratchPad.containsKey(subPolicyId) || this.subPoliciesInScratchPad.containsValue(subPolicyId);
	}

	@Override
	public void getSubPolicy(String insuranceSubPolicyId,
			final ResponseHandler<SubPolicy> handler) {
		final String subPolicyId = getEffectiveId(insuranceSubPolicyId);
		if(isTemp(subPolicyId)){
			this.service.getSubPolicyInPad(subPolicyId, new BigBangAsyncCallback<SubPolicy>() {

				@Override
				public void onResponseSuccess(SubPolicy result) {
					result.id = getFinalMapping(result.id);
					incrementDataVersion();
					for(DataBrokerClient<SubPolicy> bc : getClients()){
						((InsuranceSubPolicyDataBrokerClient) bc).updateInsuranceSubPolicy(result);
						((InsuranceSubPolicyDataBrokerClient) bc).setDataVersionNumber(BigBangConstants.EntityIds.INSURANCE_SUB_POLICY, getCurrentDataVersion());
					}
					handler.onResponse(result);
				}

				@Override
				public void onResponseFailure(Throwable caught) {
					if(caught instanceof CorruptedPadException){
						onPadCorrupted(subPolicyId);
					}
					handler.onError(new String[]{
							new String("Could not get the subpolicy")
					});
					super.onResponseFailure(caught);
				}
			});
		}else{
			this.service.getSubPolicy(subPolicyId, new BigBangAsyncCallback<SubPolicy>() {

				@Override
				public void onResponseSuccess(SubPolicy result) {
					incrementDataVersion();
					for(DataBrokerClient<SubPolicy> bc : getClients()){
						((InsuranceSubPolicyDataBrokerClient) bc).updateInsuranceSubPolicy(result);
						((InsuranceSubPolicyDataBrokerClient) bc).setDataVersionNumber(BigBangConstants.EntityIds.INSURANCE_SUB_POLICY, getCurrentDataVersion());
					}
					handler.onResponse(result);
				}

				@Override
				public void onResponseFailure(Throwable caught) {
					handler.onError(new String[]{
							new String("Could not get the requested subpolicy")
					});
					super.onResponseFailure(caught);
				}
			});
		}
	}

	@Override
	public void openSubPolicyResource(final String subPolicyId,
			final ResponseHandler<SubPolicy> handler) {
		//NEW SUB POLICY
		if(subPolicyId == null){
			service.openSubPolicyScratchPad(subPolicyId, new BigBangAsyncCallback<Remap[]>() {

				@Override
				public void onResponseSuccess(Remap[] result) {
					//If new policy
					if(result.length == 1 && result[0].remapIds.length == 1 && result[0].remapIds[0].oldId == null){
						SubPolicy subPolicy = new SubPolicy();
						RemapId remapId = result[0].remapIds[0];
						subPolicy.id = remapId.newId;
						subPoliciesInScratchPad.put("new", subPolicy.id);
						handler.onResponse(subPolicy);
					}
				}

				@Override
				public void onResponseFailure(Throwable caught) {
					if(caught instanceof CorruptedPadException){
						onPadCorrupted(subPolicyId);
					}
					handler.onError(new String[]{
							new String("Could not open the new subpolicy resource")
					});
					super.onResponseFailure(caught);
				}

			});
		}else if(!isTemp(subPolicyId)){
			//EXISTING SUB POLICY
			service.openSubPolicyScratchPad(subPolicyId, new BigBangAsyncCallback<Remap[]>() {

				@Override
				public void onResponseSuccess(Remap[] result) {
					doRemapping(result);
					getSubPolicy(subPolicyId, handler);
				}

				@Override
				public void onResponseFailure(Throwable caught) {
					if(caught instanceof CorruptedPadException){
						onPadCorrupted(subPolicyId);
					}
					handler.onError(new String[]{
							new String("Could not open the subpolicy resource")
					});
					super.onResponseFailure(caught);
				}

			});
		}else{
			handler.onError(new String[]{
					new String("Cannot open a subpolicy resource twice for the same subpolicy")	
			});
		}
	}
	
	@Override
	public void getSubPolicyDefinition(SubPolicy subPolicy, final ResponseHandler<SubPolicy> handler){
		String id = subPolicy.id;
		if(isTemp(id)) {
			service.initSubPolicyInPad(subPolicy, new BigBangAsyncCallback<SubPolicy>() {

				@Override
				public void onResponseSuccess(SubPolicy result) {
					handler.onResponse(result);
				}
				
				@Override
				public void onResponseFailure(Throwable caught) {
					handler.onError(new String[]{
							new String("Could not get the sub policy definition")
					});
					super.onResponseFailure(caught);
				}
			});
		}else{
			handler.onError(new String[]{
				new String("The sub policy is not in scratch pad")	
			});
		}
	}

	@Override
	public void commitSubPolicy(SubPolicy subPolicy,
			final ResponseHandler<SubPolicy> handler) {
		final String subPolicyId = getEffectiveId(subPolicy.id);
		this.service.commitPad(subPolicyId, new BigBangAsyncCallback<Remap[]>() {

			@Override
			public void onResponseSuccess(Remap[] result) {
				String subPolicyId = result[0].remapIds[0].newId;
				doRemapping(result);
				getSubPolicy(subPolicyId, handler);
				EventBus.getInstance().fireEvent(new OperationWasExecutedEvent(BigBangConstants.OperationIds.InsuranceSubPolicyProcess.EDIT_SUB_POLICY, subPolicyId));
			}

			@Override
			public void onResponseFailure(Throwable caught) {
				if(caught instanceof CorruptedPadException){
					onPadCorrupted(subPolicyId);
				}
				handler.onError(new String[]{
						new String("Could not commit the scratch pad")	
				});
				super.onResponseFailure(caught);
			}

		});
	}

	@Override
	public void closeSubPolicyResource(String subPolicyId,
			final ResponseHandler<Void> handler) {
		subPolicyId = getEffectiveId(subPolicyId);
		if(isTemp(subPolicyId)){
			service.discardPad(subPolicyId, new BigBangAsyncCallback<Remap[]>() {

				@Override
				public void onResponseSuccess(Remap[] result) {
					doRemapping(result);
					handler.onResponse(null);
				}

				@Override
				public void onResponseFailure(Throwable caught) {
					handler.onError(new String[]{
							new String("Could not close the subpolicy resource")	
					});
					super.onResponseFailure(caught);
				}
			});
		}else{
			handler.onError(new String[]{
					new String("Cannot close an unopened subpolicy resource")
			});
		}
	}

	@Override
	public void openCoverageDetailsPage(String insuranceSubPolicyId,
			String insuredObjectId, String exerciseId,
			final ResponseHandler<TableSection> handler) {
		final String subPolicyId = getEffectiveId(insuranceSubPolicyId);
		if(isTemp(subPolicyId)){
			service.getPageForEdit(subPolicyId, insuredObjectId, exerciseId, new BigBangAsyncCallback<SubPolicy.TableSection>() {

				@Override
				public void onResponseSuccess(TableSection result) {
					handler.onResponse(result);
				}

				@Override
				public void onResponseFailure(Throwable caught) {
					if(caught instanceof CorruptedPadException){
						onPadCorrupted(subPolicyId);
					}
					handler.onError(new String[]{
							new String("Could not get the requested page for edit")
					});
					super.onResponseFailure(caught);
				}
			});
		}else{
			service.getPage(subPolicyId, insuredObjectId, exerciseId, new BigBangAsyncCallback<SubPolicy.TableSection>() {

				@Override
				public void onResponseSuccess(TableSection result) {
					handler.onResponse(result);
				}

				@Override
				public void onResponseFailure(Throwable caught) {
					handler.onError(new String[]{
							new String("Could not get the requested page")
					});
					super.onResponseFailure(caught);
				}
			});
		}
	}

	@Override
	public void saveCoverageDetailsPage(String insuranceSubPolicyId,
			String insuredObjectId, String exerciseId, TableSection data,
			final ResponseHandler<TableSection> handler) {
		final String subPolicyId = getEffectiveId(insuranceSubPolicyId);
		if(isTemp(subPolicyId)){
			this.service.savePage(data, new BigBangAsyncCallback<SubPolicy.TableSection>() {

				@Override
				public void onResponseSuccess(TableSection result) {
					handler.onResponse(result);
				}

				@Override
				public void onResponseFailure(Throwable caught) {
					if(caught instanceof CorruptedPadException){
						onPadCorrupted(subPolicyId);
					}
					handler.onError(new String[]{
							new String("Coulg not save the page")	
					});
					super.onResponseFailure(caught);
				}
			});
		}else{
			handler.onError(new String[]{
					new String("Could not save the page on an unopened subpolicy resource")
			});
		}
	}

	@Override
	public void updateSubPolicy(SubPolicy subPolicy,
			final ResponseHandler<SubPolicy> handler) {
		final String subPolicyId = getEffectiveId(subPolicy.id);
		if(isTemp(subPolicyId)){
			String tempSubPolicyId = subPolicy.id;
			subPolicy.id = subPolicyId;
			service.updateHeader(subPolicy, new BigBangAsyncCallback<SubPolicy>() {

				@Override
				public void onResponseSuccess(SubPolicy result) {
					result.id = getFinalMapping(result.id);
					incrementDataVersion();
					for(DataBrokerClient<SubPolicy> bc : getClients()){
						((InsuranceSubPolicyDataBrokerClient) bc).updateInsuranceSubPolicy(result);
						((InsuranceSubPolicyDataBrokerClient) bc).setDataVersionNumber(BigBangConstants.EntityIds.INSURANCE_SUB_POLICY, getCurrentDataVersion());
					}
					handler.onResponse(result);
				}

				@Override
				public void onResponseFailure(Throwable caught) {
					if(caught instanceof CorruptedPadException){
						onPadCorrupted(subPolicyId);
					}
					handler.onError(new String[]{
							new String("Could not save subpolicy header")	
					});
					super.onResponseFailure(caught);
				}

			});
			subPolicy.id = tempSubPolicyId;
		}else{
			handler.onError(new String[]{
					new String("Could not save the subpolicy header. The policy is not in scratch pad")
			});
		}
	}

	@Override
	public void removeSubPolicy(String id, String reason,
			final ResponseHandler<String> handler) {
		final String subPolicyId = getFinalMapping(id);
		this.service.deleteSubPolicy(subPolicyId, reason, new BigBangAsyncCallback<Void>() {

			@Override
			public void onResponseSuccess(Void result) {
				String finalId = getFinalMapping(subPolicyId);
				incrementDataVersion();
				for(DataBrokerClient<SubPolicy> bc : getClients()){
					((InsuranceSubPolicyDataBrokerClient) bc).removeInsuranceSubPolicy(finalId);
					((InsuranceSubPolicyDataBrokerClient) bc).setDataVersionNumber(BigBangConstants.EntityIds.INSURANCE_SUB_POLICY, getCurrentDataVersion());
				}
				handler.onResponse(finalId);
				EventBus.getInstance().fireEvent(new OperationWasExecutedEvent(BigBangConstants.OperationIds.InsuranceSubPolicyProcess.DELETE_SUB_POLICY, subPolicyId));
			}

			@Override
			public void onResponseFailure(Throwable caught) {
				handler.onError(new String[]{
						new String("Could not remove the insurance subpolicy")	
				});
				super.onResponseFailure(caught);
			}
		});
	}

	@Override
	public void createReceipt(String id, Receipt receipt,
			final ResponseHandler<Receipt> handler) {
		final String subPolicyId = getFinalMapping(id);
		this.service.createReceipt(subPolicyId, receipt, new BigBangAsyncCallback<Receipt>() {

			@Override
			public void onResponseSuccess(Receipt result) {
				handler.onResponse(result);
				DataBrokerManager.Util.getInstance().getBroker(BigBangConstants.EntityIds.RECEIPT).notifyItemCreation(result.id);
				EventBus.getInstance().fireEvent(new OperationWasExecutedEvent(BigBangConstants.OperationIds.InsuranceSubPolicyProcess.CREATE_RECEIPT, subPolicyId));
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
	public void remapItemId(String oldId, String newId, boolean newInScratchPad) {
		if(newInScratchPad){
			this.subPoliciesInScratchPad.put(oldId, newId);
		}else{
			this.subPoliciesInScratchPad.remove(newId);
		}
		incrementDataVersion();
		for(DataBrokerClient<SubPolicy> bc : getClients()){
			((InsuranceSubPolicyDataBrokerClient) bc).remapItemId(oldId, newId);
			((InsuranceSubPolicyDataBrokerClient) bc).setDataVersionNumber(BigBangConstants.EntityIds.INSURANCE_SUB_POLICY, getCurrentDataVersion());
		}
	}

	@Override
	public SearchDataBroker<SubPolicyStub> getSearchBroker() {
		return this.searchBroker;
	}

	@Override
	public void discardTemp(String subPolicyId) {
		this.subPoliciesInScratchPad.remove(subPolicyId);
		String finalId = getFinalMapping(subPolicyId);
		this.subPoliciesInScratchPad.remove(finalId);
	}

	@Override
	public void getPage(
			String insuranceSubPolicyId,
			String insuredObjectId,
			String exerciseId,
			final ResponseHandler<TableSection> handler) {
		final String subPolicyId = getEffectiveId(insuranceSubPolicyId);
		if(isTemp(subPolicyId)){
			service.getPageForEdit(subPolicyId, insuredObjectId, exerciseId, new BigBangAsyncCallback<SubPolicy.TableSection>() {

				@Override
				public void onResponseSuccess(TableSection result) {
					handler.onResponse(result);
				}

				@Override
				public void onResponseFailure(Throwable caught) {
					if(caught instanceof CorruptedPadException){
						onPadCorrupted(subPolicyId);
					}
					handler.onError(new String[]{
							new String("Could not get the requested page")
					});
					super.onResponseFailure(caught);
				}
			});
		}else{
			service.getPage(subPolicyId, insuredObjectId, exerciseId, new BigBangAsyncCallback<SubPolicy.TableSection>() {

				@Override
				public void onResponseSuccess(TableSection result) {
					handler.onResponse(result);
				}

				@Override
				public void onResponseFailure(Throwable caught) {
					handler.onError(new String[]{
							new String("Could not get the requested page")
					});
					super.onResponseFailure(caught);
				}
			});
		}
	}

	@Override
	public void validateSubPolicy(final String subPolicyId,
			final ResponseHandler<Void> handler)
					throws BigBangPolicyValidationException {
		service.validateSubPolicy(subPolicyId, new BigBangAsyncCallback<Void>() {

			@Override
			public void onResponseSuccess(Void result) {
				handler.onResponse(null);
				EventBus.getInstance().fireEvent(new OperationWasExecutedEvent(BigBangConstants.OperationIds.InsuranceSubPolicyProcess.VALIDATE, subPolicyId));
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
	public void executeDetailedCalculations(String subPolicyId,
			final ResponseHandler<SubPolicy> handler) {
		service.performCalculations(subPolicyId, new BigBangAsyncCallback<SubPolicy>() {

			@Override
			public void onResponseSuccess(SubPolicy result) {
				handler.onResponse(result);
				EventBus.getInstance().fireEvent(new OperationWasExecutedEvent(BigBangConstants.OperationIds.InsuranceSubPolicyProcess.PERFORM_CALCULATIONS, result.id));
			}
			
			@Override
			public void onResponseFailure(Throwable caught) {
				handler.onError(new String[]{
						new String("Could not perform detailed calculations")
				});
				super.onResponseFailure(caught);
			}
		});
	}

	@Override
	public void voidSubPolicy(PolicyVoiding voiding,
			final ResponseHandler<SubPolicy> handler) {
		service.voidSubPolicy(voiding, new BigBangAsyncCallback<SubPolicy>() {

			@Override
			public void onResponseSuccess(SubPolicy result) {
				handler.onResponse(result);
				EventBus.getInstance().fireEvent(new OperationWasExecutedEvent(BigBangConstants.OperationIds.InsuranceSubPolicyProcess.VOID, result.id));
			}
			
			@Override
			public void onResponseFailure(Throwable caught) {
				handler.onError(new String[]{
						new String("Could not void the subpolicy")
				});
				super.onResponseFailure(caught);
			}
		});
	}
	
	@Override
	public void getSubPoliciesForPolicy(String ownerId,
			final ResponseHandler<Collection<SubPolicyStub>> responseHandler) {
		SubPolicySearchParameter parameter = new SubPolicySearchParameter();
		parameter.ownerId = ownerId;
		SubPolicySearchParameter[] parameters = new SubPolicySearchParameter[]{
				parameter
		};
		
		SubPolicySortParameter sort = new SubPolicySortParameter();
		sort.order = SortOrder.DESC;
		sort.field = SubPolicySortParameter.SortableField.NUMBER;
		SubPolicySortParameter[] sorts = new SubPolicySortParameter[]{
			sort	
		};
		
		getSearchBroker().search(parameters, sorts, -1, new ResponseHandler<Search<SubPolicyStub>>() {
			
			@Override
			public void onResponse(Search<SubPolicyStub> response) {
				responseHandler.onResponse(response.getResults());
			}
			
			@Override
			public void onError(Collection<ResponseError> errors) {
				responseHandler.onError(new String[]{
						new String("Could not get the subpolicies")
				});
			}
		});
	}

	@Override
	public String getEffectiveId(String id) {
		if(this.subPoliciesInScratchPad.containsKey(id)){
			return this.subPoliciesInScratchPad.get(id);
		}
		return id;
	}

	@Override
	public String getFinalMapping(String tempId) {
		for(String key : this.subPoliciesInScratchPad.keySet()){
			if(this.subPoliciesInScratchPad.get(key).equalsIgnoreCase(tempId)){
				return key;
			}
		}
		return tempId;
	}

	protected void doRemapping(Remap[] remappings){
		for(int i = 0; i < remappings.length; i++) {
			Remap remap = remappings[i];

			//SUBPOLICIES
			if(remap.typeId.equalsIgnoreCase(BigBangConstants.EntityIds.INSURANCE_SUB_POLICY)){
				for(int j = 0; j < remap.remapIds.length; j++){
					RemapId remapId = remap.remapIds[j];
					remapItemId(remapId.oldId, remapId.newId, remapId.newIdIsInPad);
				}

//			//OBJECTS TODO
//			}else if(remap.typeId.equalsIgnoreCase(BigBangConstants.EntityIds.POLICY_INSURED_OBJECT)){
//				for(int j = 0; j < remap.remapIds.length; j++){
//					RemapId remapId = remap.remapIds[j];
//					this.insuredObjectsBroker.remapItemId(remapId.oldId, remapId.newId, remapId.newIdIsInPad);
//				}
//
//				//EXERCISES
//			}else if(remap.typeId.equalsIgnoreCase(BigBangConstants.EntityIds.POLICY_EXERCISE)){
//				for(int j = 0; j < remap.remapIds.length; j++){
//					RemapId remapId = remap.remapIds[j];
//					this.exerciseDataBroker.remapItemId(remapId.oldId, remapId.newId, remapId.newIdIsInPad);
//				}
			}else{
				GWT.log("Could not remap item id for typeId = " + remap.typeId);
			}
		}
	}
	
	protected void onPadCorrupted(String subPolicyId){
		closeSubPolicyResource(subPolicyId, new ResponseHandler<Void>() {

			@Override
			public void onResponse(Void response) {}

			@Override
			public void onError(Collection<ResponseError> errors) {}
		});
	}

}
