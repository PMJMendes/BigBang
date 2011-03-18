package bigBang.library.shared;

import com.google.gwt.user.client.rpc.SerializationException;
import com.google.gwt.user.client.rpc.SerializationStreamReader;
import com.google.gwt.user.client.rpc.SerializationStreamWriter;

@SuppressWarnings("deprecation")
public class SearchResult_Array_Rank_1_FieldSerializer {
  public static class Handler implements com.google.gwt.user.client.rpc.impl.TypeHandler {
    public void deserialize(SerializationStreamReader reader, Object object) throws SerializationException {
      bigBang.library.shared.SearchResult_Array_Rank_1_FieldSerializer.deserialize(reader, (bigBang.library.shared.SearchResult[])object);
    }
    public Object instantiate(SerializationStreamReader reader) throws SerializationException {
      return bigBang.library.shared.SearchResult_Array_Rank_1_FieldSerializer.instantiate(reader);
    }
    public void serialize(SerializationStreamWriter writer, Object object) throws SerializationException {
      bigBang.library.shared.SearchResult_Array_Rank_1_FieldSerializer.serialize(writer, (bigBang.library.shared.SearchResult[])object);
    }
  }
  public static Class<?> concreteType() {
    return bigBang.library.shared.SearchResult[].class;
  }
  
  public static void deserialize(SerializationStreamReader streamReader, bigBang.library.shared.SearchResult[] instance) throws SerializationException {
    com.google.gwt.user.client.rpc.core.java.lang.Object_Array_CustomFieldSerializer.deserialize(streamReader, instance);
  }
  
  public static bigBang.library.shared.SearchResult[] instantiate(SerializationStreamReader streamReader) throws SerializationException {
    int size = streamReader.readInt();
    return new bigBang.library.shared.SearchResult[size];
  }
  
  public static void serialize(SerializationStreamWriter streamWriter, bigBang.library.shared.SearchResult[] instance) throws SerializationException {
    com.google.gwt.user.client.rpc.core.java.lang.Object_Array_CustomFieldSerializer.serialize(streamWriter, instance);
  }
  
}
