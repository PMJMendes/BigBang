package bigBang.module.tasksModule.shared;

import com.google.gwt.user.client.rpc.SerializationException;
import com.google.gwt.user.client.rpc.SerializationStreamReader;
import com.google.gwt.user.client.rpc.SerializationStreamWriter;

@SuppressWarnings("deprecation")
public class Task_Array_Rank_1_FieldSerializer {
  public static class Handler implements com.google.gwt.user.client.rpc.impl.TypeHandler {
    public void deserialize(SerializationStreamReader reader, Object object) throws SerializationException {
      bigBang.module.tasksModule.shared.Task_Array_Rank_1_FieldSerializer.deserialize(reader, (bigBang.module.tasksModule.shared.Task[])object);
    }
    public Object instantiate(SerializationStreamReader reader) throws SerializationException {
      return bigBang.module.tasksModule.shared.Task_Array_Rank_1_FieldSerializer.instantiate(reader);
    }
    public void serialize(SerializationStreamWriter writer, Object object) throws SerializationException {
      bigBang.module.tasksModule.shared.Task_Array_Rank_1_FieldSerializer.serialize(writer, (bigBang.module.tasksModule.shared.Task[])object);
    }
  }
  public static Class<?> concreteType() {
    return bigBang.module.tasksModule.shared.Task[].class;
  }
  
  public static void deserialize(SerializationStreamReader streamReader, bigBang.module.tasksModule.shared.Task[] instance) throws SerializationException {
    com.google.gwt.user.client.rpc.core.java.lang.Object_Array_CustomFieldSerializer.deserialize(streamReader, instance);
  }
  
  public static bigBang.module.tasksModule.shared.Task[] instantiate(SerializationStreamReader streamReader) throws SerializationException {
    int size = streamReader.readInt();
    return new bigBang.module.tasksModule.shared.Task[size];
  }
  
  public static void serialize(SerializationStreamWriter streamWriter, bigBang.module.tasksModule.shared.Task[] instance) throws SerializationException {
    com.google.gwt.user.client.rpc.core.java.lang.Object_Array_CustomFieldSerializer.serialize(streamWriter, instance);
  }
  
}
