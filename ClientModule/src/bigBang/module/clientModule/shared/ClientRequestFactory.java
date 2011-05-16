package bigBang.module.clientModule.shared;

import com.google.web.bindery.requestfactory.shared.RequestFactory;

public interface ClientRequestFactory extends RequestFactory {

	ClientRequest clientRequest();
	
}
