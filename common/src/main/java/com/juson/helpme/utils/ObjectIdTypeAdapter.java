package com.juson.helpme.utils;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import org.bson.types.ObjectId;

import java.io.IOException;

/**
 * Created by qianliang on 25/3/2016.
 */
public class ObjectIdTypeAdapter extends TypeAdapter<ObjectId> {
    @Override
    public void write(final JsonWriter out, final ObjectId value) throws IOException {
        if (value == null) {
            out.nullValue();
        } else {
            out.value(value.toHexString());
        }
    }

    @Override
    public ObjectId read(final JsonReader in) throws IOException {
        if (in.peek() == JsonToken.NULL) {
            in.nextNull();
            return null;
        } else {
            String objectId = in.nextString();
            return new ObjectId(objectId);
        }
    }
}
