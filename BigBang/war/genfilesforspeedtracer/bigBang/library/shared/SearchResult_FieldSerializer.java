package bigBang.library.shared;

import com.google.gwt.user.client.rpc.SerializationException;
import com.google.gwt.user.client.rpc.SerializationStreamReader;
import com.google.gwt.user.client.rpc.SerializationStreamWriter;

@SuppressWarnings("deprecation")
public class SearchResult_FieldSerializer {
  public static class Handler implements com.google.gwt.user.client.rpc.impl.TypeHandler {
    public void deserialize(SerializationStreamReader reader, Object object) throws SerializationException {
      bigBang.library.shared.SearchResult_FieldSerializer.deserialize(reader, (bigBang.library.shared.SearchResult)object);
    }
    public Object instantiate(SerializationStreamReader reader) throws SerializationException {
      return bigBang.library.shared.SearchResult_FieldSerializer.instantiate(reader);
    }
    public void serialize(SerializationStreamWriter writer, Object object) throws SerializationException {
      bigBang.library.shared.SearchResult_FieldSerializer.serialize(writer, (bigBang.library.shared.SearchResult)object);
    }
  }
  public static Class<?> concreteType() {
    return bigBang.library.shared.SearchResult.class;
  }
  
  public static void deserialize(SerializationStreamReader streamReader, bigBang.library.shared.SearchResult instance) throws SerializationException {
    instance.resultFields = (java.util.HashMap) streamReader.readObject();
    
  }
  
  public static bigBang.library.shared.SearchResult instantiate(SerializationStreamReader streamReader) throws SerializationException {
    return new bigBang.library.shared.SearchResult();
  }
  
  public static void serialize(SerializationStreamWriter streamWriter, bigBang.library.shared.SearchResult instance) throws SerializationException {
    streamWriter.writeObject(instance.resultFields);
    
  }
  
}
