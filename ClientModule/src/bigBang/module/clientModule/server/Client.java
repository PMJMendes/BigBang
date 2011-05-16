package bigBang.module.clientModule.server;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
public class Client {

	public static List<Client> findAllClients(){
		List<Client> result =  new ArrayList<Client>();
		result.size();
		return result;
	}

	@Id
	private String id;
	
	@NotNull
	@Size(min = 1, max = 50)
	private String name;
	private String taxNumber;
	
	public String getId(){
		return id;
	}
	
	public String getName(){
		return this.name;
	}
	
	public String getTaxNumber(){
		return this.taxNumber;
	}
	
	public void persist(){
		
	}
	
	public void remove(){
		
	}
	
	public void setId(String id) {
		this.id = id;
	}
	
	public void setName(String name) {
		this.name = name;
	} 
	
	public void setTaxNumber(String taxNumber) {
		this.taxNumber = taxNumber;
	}
	
}
