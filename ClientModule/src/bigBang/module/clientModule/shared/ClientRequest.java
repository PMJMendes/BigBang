package bigBang.module.clientModule.shared;

import java.util.List;

import bigBang.module.clientModule.server.Client;

import com.google.web.bindery.requestfactory.shared.InstanceRequest;
import com.google.web.bindery.requestfactory.shared.Request;
import com.google.web.bindery.requestfactory.shared.RequestContext;
import com.google.web.bindery.requestfactory.shared.Service;

@Service(Client.class)
public interface ClientRequest extends RequestContext {

	Request<List<ClientProxy>> findAllClients();
	
	Request<ClientProxy> findClient();
	
	InstanceRequest<ClientProxy, ClientProxy> persist();
	
	InstanceRequest<ClientProxy, Void> remove();
	
}
