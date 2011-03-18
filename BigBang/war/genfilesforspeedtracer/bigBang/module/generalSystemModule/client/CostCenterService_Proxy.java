package bigBang.module.generalSystemModule.client;

import com.google.gwt.user.client.rpc.impl.RemoteServiceProxy;
import com.google.gwt.user.client.rpc.impl.ClientSerializationStreamWriter;
import com.google.gwt.user.client.rpc.SerializationStreamWriter;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.impl.RequestCallbackAdapter.ResponseReader;
import com.google.gwt.user.client.rpc.SerializationException;
import com.google.gwt.core.client.impl.Impl;
import com.google.gwt.user.client.rpc.impl.RpcStatsContext;

public class CostCenterService_Proxy extends RemoteServiceProxy implements bigBang.module.generalSystemModule.client.CostCenterServiceAsync {
  private static final String REMOTE_SERVICE_INTERFACE_NAME = "bigBang.module.generalSystemModule.client.CostCenterService";
  private static final String SERIALIZATION_POLICY ="015D3AF795EE9CC170E77609776D4025";
  private static final bigBang.module.generalSystemModule.client.CostCenterService_TypeSerializer SERIALIZER = new bigBang.module.generalSystemModule.client.CostCenterService_TypeSerializer();
  
  public CostCenterService_Proxy() {
    super(GWT.getModuleBaseURL(),
      "CostCenterService", 
      SERIALIZATION_POLICY, 
      SERIALIZER);
  }
  
  public void addMember(java.lang.String costCenterId, java.lang.String userId, com.google.gwt.user.client.rpc.AsyncCallback callback) {
    RpcStatsContext statsContext = new RpcStatsContext();
    boolean toss = statsContext.isStatsAvailable() && statsContext.stats(statsContext.timeStat("CostCenterService_Proxy.addMember", "begin"));
    SerializationStreamWriter streamWriter = createStreamWriter();
    // createStreamWriter() prepared the stream
    try {
      streamWriter.writeString(REMOTE_SERVICE_INTERFACE_NAME);
      streamWriter.writeString("addMember");
      streamWriter.writeInt(2);
      streamWriter.writeString("java.lang.String/2004016611");
      streamWriter.writeString("java.lang.String/2004016611");
      streamWriter.writeString(costCenterId);
      streamWriter.writeString(userId);
      String payload = streamWriter.toString();
      toss = statsContext.isStatsAvailable() && statsContext.stats(statsContext.timeStat("CostCenterService_Proxy.addMember",  "requestSerialized"));
      doInvoke(ResponseReader.STRING, "CostCenterService_Proxy.addMember", statsContext, payload, callback);
    } catch (SerializationException ex) {
      callback.onFailure(ex);
    }
  }
  
  public void createCostCenter(bigBang.module.generalSystemModule.shared.CostCenter costCenter, com.google.gwt.user.client.rpc.AsyncCallback callback) {
    RpcStatsContext statsContext = new RpcStatsContext();
    boolean toss = statsContext.isStatsAvailable() && statsContext.stats(statsContext.timeStat("CostCenterService_Proxy.createCostCenter", "begin"));
    SerializationStreamWriter streamWriter = createStreamWriter();
    // createStreamWriter() prepared the stream
    try {
      streamWriter.writeString(REMOTE_SERVICE_INTERFACE_NAME);
      streamWriter.writeString("createCostCenter");
      streamWriter.writeInt(1);
      streamWriter.writeString("bigBang.module.generalSystemModule.shared.CostCenter/3484916152");
      streamWriter.writeObject(costCenter);
      String payload = streamWriter.toString();
      toss = statsContext.isStatsAvailable() && statsContext.stats(statsContext.timeStat("CostCenterService_Proxy.createCostCenter",  "requestSerialized"));
      doInvoke(ResponseReader.STRING, "CostCenterService_Proxy.createCostCenter", statsContext, payload, callback);
    } catch (SerializationException ex) {
      callback.onFailure(ex);
    }
  }
  
  public void deleteCostCenter(java.lang.String id, com.google.gwt.user.client.rpc.AsyncCallback callback) {
    RpcStatsContext statsContext = new RpcStatsContext();
    boolean toss = statsContext.isStatsAvailable() && statsContext.stats(statsContext.timeStat("CostCenterService_Proxy.deleteCostCenter", "begin"));
    SerializationStreamWriter streamWriter = createStreamWriter();
    // createStreamWriter() prepared the stream
    try {
      streamWriter.writeString(REMOTE_SERVICE_INTERFACE_NAME);
      streamWriter.writeString("deleteCostCenter");
      streamWriter.writeInt(1);
      streamWriter.writeString("java.lang.String/2004016611");
      streamWriter.writeString(id);
      String payload = streamWriter.toString();
      toss = statsContext.isStatsAvailable() && statsContext.stats(statsContext.timeStat("CostCenterService_Proxy.deleteCostCenter",  "requestSerialized"));
      doInvoke(ResponseReader.STRING, "CostCenterService_Proxy.deleteCostCenter", statsContext, payload, callback);
    } catch (SerializationException ex) {
      callback.onFailure(ex);
    }
  }
  
  public void getAvailableUsersForMembership(java.lang.String costCenterId, com.google.gwt.user.client.rpc.AsyncCallback callback) {
    RpcStatsContext statsContext = new RpcStatsContext();
    boolean toss = statsContext.isStatsAvailable() && statsContext.stats(statsContext.timeStat("CostCenterService_Proxy.getAvailableUsersForMembership", "begin"));
    SerializationStreamWriter streamWriter = createStreamWriter();
    // createStreamWriter() prepared the stream
    try {
      streamWriter.writeString(REMOTE_SERVICE_INTERFACE_NAME);
      streamWriter.writeString("getAvailableUsersForMembership");
      streamWriter.writeInt(1);
      streamWriter.writeString("java.lang.String/2004016611");
      streamWriter.writeString(costCenterId);
      String payload = streamWriter.toString();
      toss = statsContext.isStatsAvailable() && statsContext.stats(statsContext.timeStat("CostCenterService_Proxy.getAvailableUsersForMembership",  "requestSerialized"));
      doInvoke(ResponseReader.OBJECT, "CostCenterService_Proxy.getAvailableUsersForMembership", statsContext, payload, callback);
    } catch (SerializationException ex) {
      callback.onFailure(ex);
    }
  }
  
  public void getCostCenter(java.lang.String id, com.google.gwt.user.client.rpc.AsyncCallback callback) {
    RpcStatsContext statsContext = new RpcStatsContext();
    boolean toss = statsContext.isStatsAvailable() && statsContext.stats(statsContext.timeStat("CostCenterService_Proxy.getCostCenter", "begin"));
    SerializationStreamWriter streamWriter = createStreamWriter();
    // createStreamWriter() prepared the stream
    try {
      streamWriter.writeString(REMOTE_SERVICE_INTERFACE_NAME);
      streamWriter.writeString("getCostCenter");
      streamWriter.writeInt(1);
      streamWriter.writeString("java.lang.String/2004016611");
      streamWriter.writeString(id);
      String payload = streamWriter.toString();
      toss = statsContext.isStatsAvailable() && statsContext.stats(statsContext.timeStat("CostCenterService_Proxy.getCostCenter",  "requestSerialized"));
      doInvoke(ResponseReader.OBJECT, "CostCenterService_Proxy.getCostCenter", statsContext, payload, callback);
    } catch (SerializationException ex) {
      callback.onFailure(ex);
    }
  }
  
  public void getCostCenterList(com.google.gwt.user.client.rpc.AsyncCallback callback) {
    RpcStatsContext statsContext = new RpcStatsContext();
    boolean toss = statsContext.isStatsAvailable() && statsContext.stats(statsContext.timeStat("CostCenterService_Proxy.getCostCenterList", "begin"));
    SerializationStreamWriter streamWriter = createStreamWriter();
    // createStreamWriter() prepared the stream
    try {
      streamWriter.writeString(REMOTE_SERVICE_INTERFACE_NAME);
      streamWriter.writeString("getCostCenterList");
      streamWriter.writeInt(0);
      String payload = streamWriter.toString();
      toss = statsContext.isStatsAvailable() && statsContext.stats(statsContext.timeStat("CostCenterService_Proxy.getCostCenterList",  "requestSerialized"));
      doInvoke(ResponseReader.OBJECT, "CostCenterService_Proxy.getCostCenterList", statsContext, payload, callback);
    } catch (SerializationException ex) {
      callback.onFailure(ex);
    }
  }
  
  public void removeMember(java.lang.String costCenterId, java.lang.String[] memberIds, com.google.gwt.user.client.rpc.AsyncCallback callback) {
    RpcStatsContext statsContext = new RpcStatsContext();
    boolean toss = statsContext.isStatsAvailable() && statsContext.stats(statsContext.timeStat("CostCenterService_Proxy.removeMember", "begin"));
    SerializationStreamWriter streamWriter = createStreamWriter();
    // createStreamWriter() prepared the stream
    try {
      streamWriter.writeString(REMOTE_SERVICE_INTERFACE_NAME);
      streamWriter.writeString("removeMember");
      streamWriter.writeInt(2);
      streamWriter.writeString("java.lang.String/2004016611");
      streamWriter.writeString("[Ljava.lang.String;/2600011424");
      streamWriter.writeString(costCenterId);
      streamWriter.writeObject(memberIds);
      String payload = streamWriter.toString();
      toss = statsContext.isStatsAvailable() && statsContext.stats(statsContext.timeStat("CostCenterService_Proxy.removeMember",  "requestSerialized"));
      doInvoke(ResponseReader.STRING, "CostCenterService_Proxy.removeMember", statsContext, payload, callback);
    } catch (SerializationException ex) {
      callback.onFailure(ex);
    }
  }
  
  public void saveCostCenter(bigBang.module.generalSystemModule.shared.CostCenter costCenter, com.google.gwt.user.client.rpc.AsyncCallback callback) {
    RpcStatsContext statsContext = new RpcStatsContext();
    boolean toss = statsContext.isStatsAvailable() && statsContext.stats(statsContext.timeStat("CostCenterService_Proxy.saveCostCenter", "begin"));
    SerializationStreamWriter streamWriter = createStreamWriter();
    // createStreamWriter() prepared the stream
    try {
      streamWriter.writeString(REMOTE_SERVICE_INTERFACE_NAME);
      streamWriter.writeString("saveCostCenter");
      streamWriter.writeInt(1);
      streamWriter.writeString("bigBang.module.generalSystemModule.shared.CostCenter/3484916152");
      streamWriter.writeObject(costCenter);
      String payload = streamWriter.toString();
      toss = statsContext.isStatsAvailable() && statsContext.stats(statsContext.timeStat("CostCenterService_Proxy.saveCostCenter",  "requestSerialized"));
      doInvoke(ResponseReader.STRING, "CostCenterService_Proxy.saveCostCenter", statsContext, payload, callback);
    } catch (SerializationException ex) {
      callback.onFailure(ex);
    }
  }
}
