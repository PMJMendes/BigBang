package bigBang.module.clientModule.client;

import bigBang.library.client.userInterface.view.View;

public class ClientViewPresenterFactory {
	
	public static class Util {
		protected static ClientViewPresenterFactory instance;
		public static ClientViewPresenterFactory getInstance(){
			if(instance == null){
				instance = new ClientViewPresenterFactory();
			}
			return instance;
		}
	}

	public ClientViewPresenterFactory() {
		
	}
	
	public View getViewPresenter(String operationId){
		return null;
	}
	
	
}
