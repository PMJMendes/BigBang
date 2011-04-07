package bigBang.library.server;

import bigBang.library.shared.Document;
import bigBang.library.shared.DocumentService;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

public class DocumentServiceImpl extends RemoteServiceServlet implements DocumentService {

	private static final long serialVersionUID = 1L;

	@Override
	public Document[] getDocuments(String entityTypeId, String entityId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Document createDocument(String entityTypeId, String entityId,
			Document document) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Document saveDocument(Document document) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void deleteDocument(String id) {
		// TODO Auto-generated method stub
		
	}
}
