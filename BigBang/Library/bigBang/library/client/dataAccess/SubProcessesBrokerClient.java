package bigBang.library.client.dataAccess;

import java.util.Collection;

import bigBang.definitions.client.dataAccess.DataBrokerClient;
import bigBang.definitions.shared.BigBangProcess;

public interface SubProcessesBrokerClient extends
		DataBrokerClient<BigBangProcess> {

	public void setSubProcesses(Collection<BigBangProcess> subProcesses);
	
	public void addSubProcess(BigBangProcess subProcess);
	
	public void updateSubProcess(BigBangProcess subProcess);
	
	public void deleteSubProcess(String id);
	
}
