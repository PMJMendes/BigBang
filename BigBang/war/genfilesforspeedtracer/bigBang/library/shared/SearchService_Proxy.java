package bigBang.library.shared;

import com.google.gwt.user.client.rpc.impl.RemoteServiceProxy;
import com.google.gwt.user.client.rpc.impl.ClientSerializationStreamWriter;
import com.google.gwt.user.client.rpc.SerializationStreamWriter;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.impl.RequestCallbackAdapter.ResponseReader;
import com.google.gwt.user.client.rpc.SerializationException;
import com.google.gwt.core.client.impl.Impl;
import com.google.gwt.user.client.rpc.impl.RpcStatsContext;

public class SearchService_Proxy extends RemoteServiceProxy implements bigBang.library.shared.SearchServiceAsync {
  private static final String REMOTE_SERVICE_INTERFACE_NAME = "bigBang.library.shared.SearchService";
  private static final String SERIALIZATION_POLICY ="D813EF97A45227358C1C1C1970F8557B";
  private static final bigBang.library.shared.SearchService_TypeSerializer SERIALIZER = new bigBang.library.shared.SearchService_TypeSerializer();
  
  public SearchService_Proxy() {
    super(GWT.getModuleBaseURL(),
      "SearchService", 
      SERIALIZATION_POLICY, 
      SERIALIZER);
  }
  
  public void search(java.util.HashMap filters, java.lang.String[] requiredFields, java.lang.String searchTerms, com.google.gwt.user.client.rpc.AsyncCallback callback) {
    RpcStatsContext statsContext = new RpcStatsContext();
    boolean toss = statsContext.isStatsAvailable() && statsContext.stats(statsContext.timeStat("SearchService_Proxy.search", "begin"));
    SerializationStreamWriter streamWriter = createStreamWriter();
    // createStreamWriter() prepared the stream
    try {
      streamWriter.writeString(REMOTE_SERVICE_INTERFACE_NAME);
      streamWriter.writeString("search");
      streamWriter.writeInt(3);
      streamWriter.writeString("java.util.HashMap/962170901");
      streamWriter.writeString("[Ljava.lang.String;/2600011424");
      streamWriter.writeString("java.lang.String/2004016611");
      streamWriter.writeObject(filters);
      streamWriter.writeObject(requiredFields);
      streamWriter.writeString(searchTerms);
      String payload = streamWriter.toString();
      toss = statsContext.isStatsAvailable() && statsContext.stats(statsContext.timeStat("SearchService_Proxy.search",  "requestSerialized"));
      doInvoke(ResponseReader.OBJECT, "SearchService_Proxy.search", statsContext, payload, callback);
    } catch (SerializationException ex) {
      callback.onFailure(ex);
    }
  }
}
