package bigBang.library.client.dataAccess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import bigBang.definitions.client.dataAccess.DataBroker;
import bigBang.definitions.client.dataAccess.DataBrokerClient;
import bigBang.definitions.client.response.ResponseHandler;
import bigBang.definitions.shared.BigBangConstants;
import bigBang.definitions.shared.BigBangProcess;
import bigBang.library.client.BigBangAsyncCallback;
import bigBang.library.interfaces.BigBangProcessService;
import bigBang.library.interfaces.BigBangProcessServiceAsync;

public class SubProcessesBrokerImpl extends DataBroker<BigBangProcess>
		implements SubProcessesBroker {

	private BigBangProcessServiceAsync service;
	private Map<String, Collection<SubProcessesBrokerClient>> subProcessClients;
	
	public SubProcessesBrokerImpl(){
		this.service = BigBangProcessService.Util.getInstance();
		this.subProcessClients = new HashMap<String, Collection<SubProcessesBrokerClient>>();
		this.dataElementId = BigBangConstants.EntityIds.PROCESS;
	}
	
	@Override
	public void getSubProcesses(final String ownerId,
			final ResponseHandler<Collection<BigBangProcess>> handler) {
		service.getSubProcesses(ownerId, new BigBangAsyncCallback<BigBangProcess[]>() {

			@Override
			public void onSuccess(BigBangProcess[] result) {
				Collection<BigBangProcess> subProcesses = new ArrayList<BigBangProcess>();
				for(int i = 0; i < result.length; i++) {
					subProcesses.add(result[i]);
				}
				incrementDataVersion();
				for(SubProcessesBrokerClient client : subProcessClients.get(ownerId)){
					client.setSubProcesses(subProcesses);
					client.setDataVersionNumber(getDataElementId(), getCurrentDataVersion());
				}
				handler.onResponse(subProcesses);
			}
			
			@Override
			public void onFailure(Throwable caught) {
				handler.onError(new String[]{
					new String("Could not get the sub processes")	
				});
				super.onFailure(caught);
			}
		});
	}

	@Override
	public void getSubProcess(String id, final ResponseHandler<BigBangProcess> handler) {
		service.getProcess(id, new BigBangAsyncCallback<BigBangProcess>() {

			@Override
			public void onSuccess(BigBangProcess result) {
				handler.onResponse(result);
			}

			@Override
			public void onFailure(Throwable caught) {
				handler.onError(new String[]{
					new String("Could not get the sub process")	
				});
				super.onFailure(caught);
			}
		});
	}
	
	@Override
	public void registerClient(String ownerId, DataBrokerClient<BigBangProcess> client) {
		ownerId = ownerId.toLowerCase();
		if(!subProcessClients.containsKey(ownerId)){
			subProcessClients.put(ownerId, new ArrayList<SubProcessesBrokerClient>());
		}
		List<SubProcessesBrokerClient> clients = (List<SubProcessesBrokerClient>) subProcessClients.get(ownerId);
		clients.add((SubProcessesBrokerClient) client);
	}

	@Override
	public void unregisterClient(String ownerId, DataBrokerClient<BigBangProcess> client) {
		ownerId = ownerId.toLowerCase();
		if(!subProcessClients.containsKey(ownerId)){
			return;
		}
		List<SubProcessesBrokerClient> clients = (List<SubProcessesBrokerClient>) subProcessClients.get(ownerId);
		clients.remove(client);
	}

	@Override
	public void requireDataRefresh() {
		return;
	}

	@Override
	public void notifyItemCreation(String ownerId) {
		// TODO Auto-generated method stub

	}

	@Override
	public void notifyItemDeletion(String ownerId) {
		// TODO Auto-generated method stub

	}

	@Override
	public void notifyItemUpdate(String ownerId) {
		// TODO Auto-generated method stub

	}

}
