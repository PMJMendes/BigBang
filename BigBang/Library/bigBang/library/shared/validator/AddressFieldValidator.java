package bigBang.library.shared.validator;

import bigBang.definitions.shared.Address;
import bigBang.library.client.FieldValidator;

public class AddressFieldValidator implements FieldValidator<Address> {

	@Override
	public boolean isValid(Address value) {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public String getErrorMessage() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isMandatory() {
		// TODO Auto-generated method stub
		return false;
	}

}
