package bigBang.module.tasksModule.shared;

import com.google.gwt.user.client.rpc.SerializationException;
import com.google.gwt.user.client.rpc.SerializationStreamReader;
import com.google.gwt.user.client.rpc.SerializationStreamWriter;

@SuppressWarnings("deprecation")
public class Task_FieldSerializer {
  public static class Handler implements com.google.gwt.user.client.rpc.impl.TypeHandler {
    public void deserialize(SerializationStreamReader reader, Object object) throws SerializationException {
      bigBang.module.tasksModule.shared.Task_FieldSerializer.deserialize(reader, (bigBang.module.tasksModule.shared.Task)object);
    }
    public Object instantiate(SerializationStreamReader reader) throws SerializationException {
      return bigBang.module.tasksModule.shared.Task_FieldSerializer.instantiate(reader);
    }
    public void serialize(SerializationStreamWriter writer, Object object) throws SerializationException {
      bigBang.module.tasksModule.shared.Task_FieldSerializer.serialize(writer, (bigBang.module.tasksModule.shared.Task)object);
    }
  }
  public static Class<?> concreteType() {
    return bigBang.module.tasksModule.shared.Task.class;
  }
  
  public static void deserialize(SerializationStreamReader streamReader, bigBang.module.tasksModule.shared.Task instance) throws SerializationException {
    instance.description = streamReader.readString();
    instance.entryDate = streamReader.readString();
    instance.id = streamReader.readString();
    instance.operationId = streamReader.readString();
    instance.operationInstanceId = streamReader.readString();
    instance.requesterName = streamReader.readString();
    instance.targetId = streamReader.readString();
    
  }
  
  public static bigBang.module.tasksModule.shared.Task instantiate(SerializationStreamReader streamReader) throws SerializationException {
    return new bigBang.module.tasksModule.shared.Task();
  }
  
  public static void serialize(SerializationStreamWriter streamWriter, bigBang.module.tasksModule.shared.Task instance) throws SerializationException {
    streamWriter.writeString(instance.description);
    streamWriter.writeString(instance.entryDate);
    streamWriter.writeString(instance.id);
    streamWriter.writeString(instance.operationId);
    streamWriter.writeString(instance.operationInstanceId);
    streamWriter.writeString(instance.requesterName);
    streamWriter.writeString(instance.targetId);
    
  }
  
}
