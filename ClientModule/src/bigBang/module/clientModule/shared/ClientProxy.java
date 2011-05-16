package bigBang.module.clientModule.shared;

import bigBang.module.clientModule.server.Client;

import com.google.web.bindery.requestfactory.shared.EntityProxy;
import com.google.web.bindery.requestfactory.shared.EntityProxyId;
import com.google.web.bindery.requestfactory.shared.ProxyFor;

@ProxyFor(Client.class)
public interface ClientProxy extends EntityProxy {

	String getId();
	
	String getName();
	
	String getTaxNumber();
	
	void setId(String id);
	
	void setName(String name);
	
	void setTaxNumber(String taxNumber);
	
	EntityProxyId<ClientProxy> stableId();

}
