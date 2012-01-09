package bigBang.library.client.dataAccess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import bigBang.definitions.client.dataAccess.DataBroker;
import bigBang.definitions.client.dataAccess.DataBrokerClient;
import bigBang.definitions.client.response.ResponseError;
import bigBang.definitions.client.response.ResponseHandler;
import bigBang.definitions.shared.Contact;
import bigBang.library.client.BigBangAsyncCallback;
import bigBang.library.interfaces.ContactsService;
import bigBang.library.interfaces.ContactsServiceAsync;

public class BigBangContactsListBroker extends DataBroker<Contact> implements ContactsBroker {

	public static class Util {
		protected static BigBangContactsListBroker instance;
		public static ContactsBroker getInstance(){
			if(instance == null){
				instance = new BigBangContactsListBroker();
			}
			return instance;
		}
	}

	protected static final int NO_DATA_VERSION = 0;

	protected Map<String, Integer> dataVersions;
	protected Map<String, List<Contact>> contacts;
	protected Map<String, Boolean> dataRefreshRequirements;
	protected Map<String, List<ContactsBrokerClient>> clients;

	protected ContactsServiceAsync service;

	public BigBangContactsListBroker(){
		dataVersions = new HashMap<String, Integer>();
		contacts = new HashMap<String, List<Contact>>();
		dataRefreshRequirements = new HashMap<String, Boolean>();
		clients = new HashMap<String, List<ContactsBrokerClient>>();
		service = ContactsService.Util.getInstance();
	}

	@Override
	public int incrementDataVersion(String ownerId) {
		if(!clients.containsKey(ownerId)){
			throw new RuntimeException("The contacts for the owner with id " + ownerId + " are not being managed by this broker.");
		}
		int newDataVersion = new Integer(dataVersions.get(ownerId).intValue() + 1);
		dataVersions.put(ownerId, newDataVersion);
		return newDataVersion;
	}

	@Override
	public boolean checkClientDataVersions() {
		boolean result = true;
		for(String o : clients.keySet()){
			result &= checkClientDataVersions(o);
		}
		return result;
	}

	@Override
	public boolean checkClientDataVersions(String ownerId) {
		if(!clients.containsKey(ownerId)){
			throw new RuntimeException("The contacts for the owner with id " + ownerId + " are not being managed by this broker.");
		}
		for(ContactsBrokerClient c : clients.get(ownerId)) {
			if(dataVersions.get(ownerId) != c.getContactsDataVersionNumber(ownerId)){
				return false;
			}
		}
		return true;  
	}

	@Override
	public int getCurrentDataVersion(String ownerId) {
		if(!clients.containsKey(ownerId)){
			throw new RuntimeException("The contacts for the owner with id " + ownerId + " are not being managed by this broker.");
		}
		return dataVersions.get(ownerId);
	}

	@Override
	public void registerClient(final ContactsBrokerClient client, final String ownerId) {
		if(!clients.containsKey(ownerId)){
			List<ContactsBrokerClient> clientList = new ArrayList<ContactsBrokerClient>();
			clients.put(ownerId, clientList);
			contacts.put(ownerId, new ArrayList<Contact>());
			requireDataRefresh(ownerId);
			dataVersions.put(ownerId, NO_DATA_VERSION);	
			getContacts(ownerId, new ResponseHandler<List<Contact>>() {

				@Override
				public void onResponse(List<Contact> response) {
					clients.get(ownerId).add(client);
					updateClient(ownerId, client);
				}

				@Override
				public void onError(Collection<ResponseError> errors) {}
			});
		}else{
			clients.get(ownerId).add(client);
			updateClient(ownerId, client);
		}
	}

	@Override
	public void unregisterClient(ContactsBrokerClient client) {
		boolean managed = false;
		for(String ownerId : clients.keySet()){
			List<ContactsBrokerClient> clientList = clients.get(ownerId);
			if(clientList.contains(client)){
				managed = true;
				unregisterClient(client, ownerId);
			}
		}
	}

	@Override
	public void unregisterClient(ContactsBrokerClient client, String ownerId) {
		if(!clients.containsKey(ownerId)){
			throw new RuntimeException("The contacts for the owner with id " + ownerId + " are not being managed by this broker.");
		}
		List<ContactsBrokerClient> clientList = clients.get(ownerId);
		clientList.remove(client);
		if(clientList.isEmpty()){
			dataVersions.remove(ownerId);
			contacts.remove(ownerId);
			dataRefreshRequirements.remove(ownerId);
			clients.remove(ownerId);
		}
	}

	@Override
	public Collection<DataBrokerClient<Contact>> getClients() {
		Collection<DataBrokerClient<Contact>> result = new ArrayList<DataBrokerClient<Contact>>();
		for(List<ContactsBrokerClient> clientList : clients.values()){
			result.addAll(clientList);
		}
		return result;
	}

	@Override
	public Collection<ContactsBrokerClient> getClients(String ownerId) {
		if(!clients.containsKey(ownerId)){
			throw new RuntimeException("The contacts for the owner with id " + ownerId + " are not being managed by this broker.");
		}
		return clients.get(ownerId);
	}

	@Override
	public void requireDataRefresh() {
		for(String ownerId : dataRefreshRequirements.keySet()){
			requireDataRefresh(ownerId);
		}
	}

	@Override
	public void requireDataRefresh(String ownerId) {
		if(!clients.containsKey(ownerId)){
			throw new RuntimeException("The contacts for the owner with id " + ownerId + " are not being managed by this broker.");
		}
		dataRefreshRequirements.put(ownerId, true);
	}

	protected boolean requiresDataRefresh(String ownerId){
		return this.dataRefreshRequirements.get(ownerId);
	}

	@Override
	public void getContact(final String ownerId, final String contactId,
			final ResponseHandler<Contact> handler) {
		if(!clients.containsKey(ownerId)){
			throw new RuntimeException("The contacts for the owner with id " + ownerId + " are not being managed by this broker.");
		}
		if(requiresDataRefresh(ownerId)){
			getContacts(ownerId, new ResponseHandler<List<Contact>>() {
				@Override
				public void onResponse(List<Contact> response) {
					getContact(ownerId, contactId, handler);
				}

				@Override
				public void onError(Collection<ResponseError> errors) {
					handler.onError(errors);
				}
			});
		}else{
			Collection<Contact> contactsCollection = contacts.get(ownerId);
			for(Contact c : contactsCollection) {
				if(c.id.equalsIgnoreCase(contactId)){
					handler.onResponse(c);
					return;
				}
			}
		}
	}

	@Override
	public void getContacts(final String ownerId, final ResponseHandler<List<Contact>> handler) {
		if(!clients.containsKey(ownerId)){
			throw new RuntimeException("The contacts for the owner with id " + ownerId + " are not being managed by this broker.");
		}
		if(requiresDataRefresh(ownerId)){
			service.getContacts(ownerId, new BigBangAsyncCallback<Contact[]>() {

				@Override
				public void onSuccess(Contact[] result) {
					List<Contact> contactsList = new ArrayList<Contact>();
					for(int i = 0; i < result.length; i++) {
						contactsList.add(result[i]);
					}
					contacts.put(ownerId, contactsList);
					incrementDataVersion(ownerId);
					updateClients(ownerId);
					handler.onResponse(contactsList);
				}
			});
		}else{
			handler.onResponse(contacts.get(ownerId));
		}
	}

	@Override
	public void addContact(String processId, String opId, final String ownerId, Contact contact,
			final ResponseHandler<Contact> handler) {
		if(!clients.containsKey(ownerId)){
			throw new RuntimeException("The contacts for the owner with id " + ownerId + " are not being managed by this broker.");
		}
//		service.createContact(processId, opId, contact, new BigBangAsyncCallback<Contact>() {
//
//			@Override
//			public void onSuccess(Contact result) {
//				List<Contact> contactsList = contacts.get(ownerId);
//				contactsList.add(result);
//				int currentVersion = dataVersions.get(ownerId);
//				int newVersion = incrementDataVersion(ownerId);
//				for(ContactsBrokerClient c : clients.get(ownerId)) {
//					if(c.getContactsDataVersionNumber(ownerId) != currentVersion) {
//						updateClient(ownerId, c);
//					}
//					c.addContact(ownerId, result);
//					c.setContactsDataVersionNumber(ownerId, newVersion);					
//				}
//				handler.onResponse(result);
//			}
//			@Override
//			public void onFailure(Throwable caught) {
//				handler.onError(new String[]{caught.getMessage()});
//				super.onFailure(caught);
//			}
//		});
	}

	@Override
	public void updateContact(String processId, String opId, final String ownerId, Contact contact,
			final ResponseHandler<Contact> handler) {
		if(!clients.containsKey(ownerId)){
			throw new RuntimeException("The contacts for the owner with id " + ownerId + " are not being managed by this broker.");
		}
//		service.saveContact(processId, opId, contact, new BigBangAsyncCallback<Contact>() {
//			@Override
//			public void onSuccess(Contact result) {
//				List<Contact> contactsList = contacts.get(ownerId);
//				contactsList.add(result);
//				int currentVersion = dataVersions.get(ownerId);
//				int newVersion = incrementDataVersion(ownerId);
//				for(ContactsBrokerClient c : clients.get(ownerId)) {
//					if(c.getContactsDataVersionNumber(ownerId) != currentVersion) {
//						updateClient(ownerId, c);
//					}
//					c.updateContact(ownerId, result);
//					c.setContactsDataVersionNumber(ownerId, newVersion);					
//				}
//				handler.onResponse(result);
//			}
//			@Override
//			public void onFailure(Throwable caught) {
//				handler.onError(new String[]{caught.getMessage()});
//				super.onFailure(caught);
//			}
//		});
	}

	@Override
	public void removeContact(String processId, String opId, final String ownerId, String contactId,
			final ResponseHandler<Contact> handler) {
		if(!clients.containsKey(ownerId)){
			throw new RuntimeException("The contacts for the owner with id " + ownerId + " are not being managed by this broker.");
		}
		Contact tempContact = null;
		for(Contact c : contacts.get(ownerId)){
			if(c.id.equalsIgnoreCase(contactId)){
				tempContact = c;
				break;
			}
		}
		final Contact contact = tempContact;
//		service.deleteContact(processId, opId, contact, new BigBangAsyncCallback<Void>() {
//
//			@Override
//			public void onSuccess(Void v) {
//				List<Contact> contactsList = contacts.get(ownerId);
//				contactsList.add(contact);
//				int currentVersion = dataVersions.get(ownerId);
//				int newVersion = incrementDataVersion(ownerId);
//				for(ContactsBrokerClient c : clients.get(ownerId)) {
//					if(c.getContactsDataVersionNumber(ownerId) != currentVersion) {
//						updateClient(ownerId, c);
//					}
//					c.updateContact(ownerId, contact);
//					c.setContactsDataVersionNumber(ownerId, newVersion);					
//				}
//				handler.onResponse(contact);
//			}
//			@Override
//			public void onFailure(Throwable caught) {
//				handler.onError(new String[]{caught.getMessage()});
//				super.onFailure(caught);
//			}
//		});
	}

	/**
	 * Updates the clients to the latest version of the info
	 * @param ownerId The id of the owner
	 */
	protected void updateClients(String ownerId) {
		List<ContactsBrokerClient> clientList = clients.get(ownerId);
		int currentVersion = dataVersions.get(ownerId);
		for(ContactsBrokerClient c : clientList){
			if(c.getContactsDataVersionNumber(ownerId) != currentVersion){
				updateClient(ownerId, c);
			}
		}
	}

	/**
	 * Updates a given client to the latest version of the contact information
	 * @param ownerId The owner of the contacts
	 * @param client The client to be updated
	 */
	protected void updateClient(String ownerId, ContactsBrokerClient client) {
		int currentVersion = dataVersions.get(ownerId);
		client.setContacts(ownerId, contacts.get(ownerId));
		client.setContactsDataVersionNumber(ownerId, currentVersion);
	}

	@Override
	public void notifyItemCreation(String itemId) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void notifyItemDeletion(String itemId) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void notifyItemUpdate(String itemId) {
		// TODO Auto-generated method stub
		
	}

}
