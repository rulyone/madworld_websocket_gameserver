// automatically generated by the FlatBuffers compiler, do not modify

package io.uninspired.madworld.core.network.protocol;

import java.nio.*;
import java.lang.*;
import java.util.*;
import com.google.flatbuffers.*;

@SuppressWarnings("unused")
public final class PGrid extends Table {
  public static void ValidateVersion() { Constants.FLATBUFFERS_2_0_0(); }
  public static PGrid getRootAsPGrid(ByteBuffer _bb) { return getRootAsPGrid(_bb, new PGrid()); }
  public static PGrid getRootAsPGrid(ByteBuffer _bb, PGrid obj) { _bb.order(ByteOrder.LITTLE_ENDIAN); return (obj.__assign(_bb.getInt(_bb.position()) + _bb.position(), _bb)); }
  public void __init(int _i, ByteBuffer _bb) { __reset(_i, _bb); }
  public PGrid __assign(int _i, ByteBuffer _bb) { __init(_i, _bb); return this; }

  public io.uninspired.madworld.core.network.protocol.PEntity entities(int j) { return entities(new io.uninspired.madworld.core.network.protocol.PEntity(), j); }
  public io.uninspired.madworld.core.network.protocol.PEntity entities(io.uninspired.madworld.core.network.protocol.PEntity obj, int j) { int o = __offset(4); return o != 0 ? obj.__assign(__indirect(__vector(o) + j * 4), bb) : null; }
  public int entitiesLength() { int o = __offset(4); return o != 0 ? __vector_len(o) : 0; }
  public io.uninspired.madworld.core.network.protocol.PEntity.Vector entitiesVector() { return entitiesVector(new io.uninspired.madworld.core.network.protocol.PEntity.Vector()); }
  public io.uninspired.madworld.core.network.protocol.PEntity.Vector entitiesVector(io.uninspired.madworld.core.network.protocol.PEntity.Vector obj) { int o = __offset(4); return o != 0 ? obj.__assign(__vector(o), 4, bb) : null; }

  public static int createPGrid(FlatBufferBuilder builder,
      int entitiesOffset) {
    builder.startTable(1);
    PGrid.addEntities(builder, entitiesOffset);
    return PGrid.endPGrid(builder);
  }

  public static void startPGrid(FlatBufferBuilder builder) { builder.startTable(1); }
  public static void addEntities(FlatBufferBuilder builder, int entitiesOffset) { builder.addOffset(0, entitiesOffset, 0); }
  public static int createEntitiesVector(FlatBufferBuilder builder, int[] data) { builder.startVector(4, data.length, 4); for (int i = data.length - 1; i >= 0; i--) builder.addOffset(data[i]); return builder.endVector(); }
  public static void startEntitiesVector(FlatBufferBuilder builder, int numElems) { builder.startVector(4, numElems, 4); }
  public static int endPGrid(FlatBufferBuilder builder) {
    int o = builder.endTable();
    return o;
  }

  public static final class Vector extends BaseVector {
    public Vector __assign(int _vector, int _element_size, ByteBuffer _bb) { __reset(_vector, _element_size, _bb); return this; }

    public PGrid get(int j) { return get(new PGrid(), j); }
    public PGrid get(PGrid obj, int j) {  return obj.__assign(__indirect(__element(j), bb), bb); }
  }
}

