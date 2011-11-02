package bigBang.module.receiptModule.client.dataAccess;

import bigBang.definitions.client.dataAccess.SearchDataBroker;
import bigBang.definitions.shared.ReceiptStub;
import bigBang.library.client.dataAccess.SearchDataBrokerImpl;
import bigBang.library.interfaces.SearchServiceAsync;
import bigBang.module.receiptModule.interfaces.ReceiptService;

public class ReceiptSearchDataBroker extends SearchDataBrokerImpl<ReceiptStub> implements SearchDataBroker<ReceiptStub>{

	public ReceiptSearchDataBroker(){
		this(ReceiptService.Util.getInstance());
	}
	
	public ReceiptSearchDataBroker(SearchServiceAsync service) {
		super(service);
	}

}
