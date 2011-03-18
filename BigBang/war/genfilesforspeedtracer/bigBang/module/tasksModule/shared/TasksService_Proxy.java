package bigBang.module.tasksModule.shared;

import com.google.gwt.user.client.rpc.impl.RemoteServiceProxy;
import com.google.gwt.user.client.rpc.impl.ClientSerializationStreamWriter;
import com.google.gwt.user.client.rpc.SerializationStreamWriter;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.impl.RequestCallbackAdapter.ResponseReader;
import com.google.gwt.user.client.rpc.SerializationException;
import com.google.gwt.core.client.impl.Impl;
import com.google.gwt.user.client.rpc.impl.RpcStatsContext;

public class TasksService_Proxy extends RemoteServiceProxy implements bigBang.module.tasksModule.shared.TasksServiceAsync {
  private static final String REMOTE_SERVICE_INTERFACE_NAME = "bigBang.module.tasksModule.shared.TasksService";
  private static final String SERIALIZATION_POLICY ="D43AF8E7C7621361AA64B20B86843BDC";
  private static final bigBang.module.tasksModule.shared.TasksService_TypeSerializer SERIALIZER = new bigBang.module.tasksModule.shared.TasksService_TypeSerializer();
  
  public TasksService_Proxy() {
    super(GWT.getModuleBaseURL(),
      "TasksService", 
      SERIALIZATION_POLICY, 
      SERIALIZER);
  }
  
  public void getTasks(com.google.gwt.user.client.rpc.AsyncCallback callback) {
    RpcStatsContext statsContext = new RpcStatsContext();
    boolean toss = statsContext.isStatsAvailable() && statsContext.stats(statsContext.timeStat("TasksService_Proxy.getTasks", "begin"));
    SerializationStreamWriter streamWriter = createStreamWriter();
    // createStreamWriter() prepared the stream
    try {
      streamWriter.writeString(REMOTE_SERVICE_INTERFACE_NAME);
      streamWriter.writeString("getTasks");
      streamWriter.writeInt(0);
      String payload = streamWriter.toString();
      toss = statsContext.isStatsAvailable() && statsContext.stats(statsContext.timeStat("TasksService_Proxy.getTasks",  "requestSerialized"));
      doInvoke(ResponseReader.OBJECT, "TasksService_Proxy.getTasks", statsContext, payload, callback);
    } catch (SerializationException ex) {
      callback.onFailure(ex);
    }
  }
  
  public void isSolved(java.lang.String taskId, com.google.gwt.user.client.rpc.AsyncCallback callback) {
    RpcStatsContext statsContext = new RpcStatsContext();
    boolean toss = statsContext.isStatsAvailable() && statsContext.stats(statsContext.timeStat("TasksService_Proxy.isSolved", "begin"));
    SerializationStreamWriter streamWriter = createStreamWriter();
    // createStreamWriter() prepared the stream
    try {
      streamWriter.writeString(REMOTE_SERVICE_INTERFACE_NAME);
      streamWriter.writeString("isSolved");
      streamWriter.writeInt(1);
      streamWriter.writeString("java.lang.String/2004016611");
      streamWriter.writeString(taskId);
      String payload = streamWriter.toString();
      toss = statsContext.isStatsAvailable() && statsContext.stats(statsContext.timeStat("TasksService_Proxy.isSolved",  "requestSerialized"));
      doInvoke(ResponseReader.BOOLEAN, "TasksService_Proxy.isSolved", statsContext, payload, callback);
    } catch (SerializationException ex) {
      callback.onFailure(ex);
    }
  }
}
