package bigBang.library.client.dataAccess;

import bigBang.library.interfaces.FileService;

public class FileTypifiedListBroker extends BigBangTypifiedListBroker {

	public static class Util {
		private static FileTypifiedListBroker instance;
		
		public static FileTypifiedListBroker getInstance(){
			if(instance == null) {
				instance = new FileTypifiedListBroker();
			}
			return instance;
		}
	}
	
	public FileTypifiedListBroker(){
		super();
		service = FileService.Util.getInstance();
	}

}
