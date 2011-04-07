package bigBang.library.interfaces;

import bigBang.library.shared.ZipCode;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface ZipCodeServiceAsync extends Service {

	void getZipCode(String code, AsyncCallback<ZipCode> callback);

}
