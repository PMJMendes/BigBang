package bigBang.module.generalSystemModule.server;

import java.util.ArrayList;

import javax.management.relation.Role;

import bigBang.module.generalSystemModule.interfaces.UserService;
import bigBang.module.generalSystemModule.shared.CostCenter;
import bigBang.module.generalSystemModule.shared.User;
import bigBang.module.generalSystemModule.shared.UserRole;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

public class UserServiceImpl extends RemoteServiceServlet implements UserService {
	
	private static final long serialVersionUID = 1L;

	private ArrayList<User> tempUsers = new ArrayList<User>(); //TODO ERASE
	private ArrayList<UserRole> tempRoles = new ArrayList<UserRole>(); //TODO ERASE
	
	public UserServiceImpl(){
		super();
		
		for(int i = 0; i < 4; i++){
			UserRole r = new UserRole();
			r.id = "id" + i;
			r.name = "role" + i;
			tempRoles.add(r);
		}
		
		for(int i = 0; i < 20; i++){
			User user = new User();
			user.id = "id" + i;
			user.name = "name" + i;
			user.email = "email" + i + "@bigbang.com";
			user.username = "username" + i;
			user.roleId = i%5 == 0 ? null : i+"";
			user.costCenterId = "CostCenter" + i; 
			this.tempUsers.add(user);
		}
	}
	
	@Override
	public User[] getUsers() {
		return (User[]) tempUsers.toArray(new User[tempUsers.size()]);
	}

	@Override
	public User getUser(String id) {
		for(User u : this.tempUsers)
			if(u.id.equals(id))
				return u;
		return null;
	}

	@Override
	public String saveUser(User user) {
		for(User u : this.tempUsers){
			if(u.id.equals(user.id)){
				u = user;
				return "";
			}
		}
		return null;
	}

	@Override
	public String addUser(User user) {
		this.tempUsers.add(user);
		return "";
	}

	@Override
	public String deleteUser(User user) {
		for(User u : this.tempUsers){
			if(u.id.equals(user.id)){
				this.tempUsers.remove(u);
				return "";
			}
		}
		return null;
	}

	@Override
	public User[] getUsersForCostCenterAssignment() {
		ArrayList<User> result = new ArrayList<User>();
		for(User u : this.tempUsers){
			if(u.costCenterId == null){
				result.add(u);
			}
		}
		return (User[]) result.toArray();
	}
	
	public UserRole[] getUserRoles(){
		return (UserRole[]) this.tempRoles.toArray();
	}

}
