package bigBang.module.mainModule.client;

import java.util.Collection;
import java.util.HashMap;

import bigBang.library.client.Operation;
import bigBang.library.client.Process;

public class ProcessManager {

	private HashMap <String, Process> processes;
	private HashMap <String, Operation> operations;
	
	public ProcessManager(){
		
	}
	
	public void manageProcess(Process process) throws Exception{
		if(this.processes == null)
			this.processes = new HashMap <String, Process> ();
		
		if(this.processes.containsKey(process.getId()))
			throw new Exception("The process ID is already assigned to a managed process");
		
		this.processes.put(process.getId(), process);
		this.manageProcessOperations(process.getOperations());
	}
	
	private void manageProcessOperations(HashMap <String, Operation> operations) {
		if(this.operations == null)
			this.operations = new HashMap <String, Operation> ();
		
		this.operations.putAll(operations);
	}
	
	public Process getProcess(String id) {
		Process process = this.processes.get(id);
		process.init();
		return process;
	}

	public Operation getOperation(String id) {
		Operation operation = this.operations.get(id);
		operation.init();
		return this.operations.get(id);
	}
	
	public Process getProcessForOperation(Operation operation){
		return this.processes.get(operation.getOwnerProcessId());
	}
	
	public Process getProcessForOperation(String id) {
		for(Process p : this.processes.values()){
			if(p.getOperations().containsKey(id));
				return p;
		}
		return null;
	}

	public Collection<Process> getProcesses() {
		return processes.values();
	}
}
