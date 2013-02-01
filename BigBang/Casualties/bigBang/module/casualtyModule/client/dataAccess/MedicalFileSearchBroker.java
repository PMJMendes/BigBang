package bigBang.module.casualtyModule.client.dataAccess;

import bigBang.definitions.client.dataAccess.SearchDataBroker;
import bigBang.definitions.shared.MedicalFileStub;
import bigBang.library.client.dataAccess.SearchDataBrokerImpl;
import bigBang.library.interfaces.SearchServiceAsync;
import bigBang.module.casualtyModule.interfaces.MedicalFileService;

public class MedicalFileSearchBroker extends SearchDataBrokerImpl<MedicalFileStub> implements SearchDataBroker<MedicalFileStub>{

	public MedicalFileSearchBroker(){
		this(MedicalFileService.Util.getInstance());
	}
	
	public MedicalFileSearchBroker(SearchServiceAsync service){
		super(service);
	}
}
