package bigBang.module.quoteRequestModule.client.dataAccess;

import bigBang.library.client.dataAccess.BigBangTypifiedListBroker;
import bigBang.module.quoteRequestModule.interfaces.QuoteRequestService;

public class QuoteRequestTypifiedListBroker extends BigBangTypifiedListBroker {

	public static class Util {
		private static QuoteRequestTypifiedListBroker instance;
		
		public static QuoteRequestTypifiedListBroker getInstance(){
			if(instance == null) {
				instance = new QuoteRequestTypifiedListBroker();
			}
			return instance;
		}
	}
	
	public QuoteRequestTypifiedListBroker(){
		super();
		service = QuoteRequestService.Util.getInstance();
	}
	
}
