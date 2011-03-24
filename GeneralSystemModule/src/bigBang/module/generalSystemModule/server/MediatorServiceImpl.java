package bigBang.module.generalSystemModule.server;

import bigBang.library.shared.Address;
import bigBang.module.generalSystemModule.interfaces.MediatorService;
import bigBang.module.generalSystemModule.shared.ComissionProfile;
import bigBang.module.generalSystemModule.shared.Mediator;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

public class MediatorServiceImpl extends RemoteServiceServlet implements MediatorService {

	private static final long serialVersionUID = 1L;

	@Override
	public Mediator[] getMediators() {
		Mediator[] result = new Mediator[1];
		result[0] = new Mediator();
		result[0].name = "name1";
		result[0].taxNumber = "12345678";
		result[0].NIB = "12314564456545654";
		result[0].ISPNumber = "4156";
		result[0].id = "id1";
		result[0].address = new Address();
		result[0].comissionProfile = new ComissionProfile();
		result[0].comissionProfile.id = "idcp1";
		result[0].comissionProfile.name = "namecp1";
		
		return result;
	}

	@Override
	public Mediator getMediator(String id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String saveMediator(Mediator mediator) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Mediator createMediator(Mediator mediator) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String deleteMediator(String id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ComissionProfile[] getComissionProfiles() {
		ComissionProfile cp = new ComissionProfile();
		cp.id = "idcp1";
		cp.name = "namecp1";
		return new ComissionProfile[]{cp};
	}
}
