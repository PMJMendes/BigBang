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
import bigBang.definitions.shared.BigBangConstants;
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
		service = ContactsService.Util.getInstance();
		this.dataElementId = BigBangConstants.EntityIds.CONTACT;
		dataVersions = new HashMap<String, Integer>();
		contacts = new HashMap<String, List<Contact>>();
		dataRefreshRequirements = new HashMap<String, Boolean>();
		clients = new HashMap<String, List<ContactsBrokerClient>>();
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
		}
		clients.get(ownerId).add(client);
		updateClient(ownerId, client);
	}

	@Override
	public void unregisterClient(ContactsBrokerClient client) {
		for(String ownerId : clients.keySet()){
			List<ContactsBrokerClient> clientList = clients.get(ownerId);
			if(clientList.contains(client)){
				unregisterClient(client, ownerId);
				continue;
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
		if(!dataRefreshRequirements.containsKey(ownerId)){
			throw new RuntimeException("The contacts for the owner with id " + ownerId + " are not being managed by this broker.");
		}
		return this.dataRefreshRequirements.get(ownerId);
	}

	@Override
	public void getContact(final String contactId,
			final ResponseHandler<Contact> handler) {
		for(Collection<Contact> collection : this.contacts.values()){
			for(Contact contact : collection){
				if(contactId.equalsIgnoreCase(contact.id)){
					handler.onResponse(contact);
					return;
				}
			}
		}
		handler.onError(new String[]{
				new String("Cannot get the requested contact")	
		});
	}

	@Override
	public void refreshContactsForOwner(String ownerId,
			final ResponseHandler<Void> handler) {
		getContacts(ownerId, new ResponseHandler<List<Contact>>() {

			@Override
			public void onResponse(List<Contact> response) {
				handler.onResponse(null);
			}

			@Override
			public void onError(Collection<ResponseError> errors) {
				handler.onError(new String[]{
						new String("Could not refersh contacts list")
				});
			}
		});
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

				@Override
				public void onFailure(Throwable caught) {
					handler.onError(new String[]{
							new String("Cannot get the contacts for the specified owner")	
					});
					super.onFailure(caught);
				}
			});
		}else{
			handler.onResponse(contacts.get(ownerId));
		}
	}

	@Override
	public void addContact(Contact contact,
			final ResponseHandler<Contact> handler) {
		if(!clients.containsKey(contact.ownerId)){
			throw new RuntimeException("The contacts for the owner with id " + contact.ownerId + " are not being managed by this broker.");
		}
		service.createContact(contact, new BigBangAsyncCallback<Contact>() {

			@Override
			public void onSuccess(Contact result) {
				List<Contact> contactsList = contacts.get(result.ownerId);
				contactsList.add(result);
				incrementDataVersion(result.ownerId);
				for(ContactsBrokerClient c : clients.get(result.ownerId)) {
					c.addContact(result.ownerId, result);
					c.setContactsDataVersionNumber(result.ownerId, getCurrentDataVersion(result.ownerId));					
				}
				handler.onResponse(result);
			}
			@Override
			public void onFailure(Throwable caught) {
				handler.onError(new String[]{
						new String("Could not create the contact")
				});
				super.onFailure(caught);
			}
		});
	}

	@Override
	public void updateContact(Contact contact, final ResponseHandler<Contact> handler) {
		if(!clients.containsKey(contact.ownerId)){
			throw new RuntimeException("The contacts for the owner with id " + contact.ownerId + " are not being managed by this broker.");
		}
		service.saveContact(contact, new BigBangAsyncCallback<Contact>() {
			@Override
			public void onSuccess(Contact result) {
				String ownerId = result.ownerId;
				List<Contact> contactsList = contacts.get(ownerId);
				for(Contact contact : contactsList){
					if(contact.id.equalsIgnoreCase(result.id)){
						int index = contactsList.indexOf(contact);
						contactsList.remove(index);
						contactsList.add(index, contact);
						break;
					}
				}
				incrementDataVersion(ownerId);
				for(ContactsBrokerClient c : clients.get(ownerId)) {
					c.updateContact(ownerId, result);
					c.setContactsDataVersionNumber(ownerId, getCurrentDataVersion(ownerId));					
				}
				handler.onResponse(result);
			}
			@Override
			public void onFailure(Throwable caught) {
				handler.onError(new String[]{
						new String("Could not updade the contact")
				});
				super.onFailure(caught);
			}
		});
	}

	@Override
	public void removeContact(final String contactId,
			final ResponseHandler<Void> handler) {
		service.deleteContact(contactId, new BigBangAsyncCallback<Void>() {

			@Override
			public void onSuccess(Void v) {
				String ownerId = null;
				for(Collection<Contact> collection : contacts.values()){
					for(Contact contact : collection) {
						if(contact.id.equalsIgnoreCase(contactId)){
							ownerId = contact.ownerId;
							collection.remove(contact);
							break;
						}
					}
				}
				incrementDataVersion();
				for(ContactsBrokerClient c : clients.get(ownerId)) {
					c.removeContact(ownerId, contactId);
					c.setContactsDataVersionNumber(ownerId, getCurrentDataVersion(ownerId));					
				}
				handler.onResponse(null);
			}
			@Override
			public void onFailure(Throwable caught) {
				handler.onError(new String[]{
						new String("Could not delete the contact")
				});
				super.onFailure(caught);
			}
		});
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
