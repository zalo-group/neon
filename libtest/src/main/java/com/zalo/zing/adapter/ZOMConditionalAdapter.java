package com.zalo.zing.adapter;

import com.zalo.zing.properties.ZOMConditionParam;
import com.zalo.zing.properties.ZOMConditionVisible;
import com.zalo.zing.properties.ZOMConditional;
import com.zing.zalo.adapter.ZarcelAdapter;
import com.zing.zalo.annotations.NonNull;
import com.zing.zalo.data.serialization.SerializedInput;
import com.zing.zalo.data.serialization.SerializedOutput;

public class ZOMConditionalAdapter implements ZarcelAdapter<ZOMConditional[]> {

    @Override
    public void serialize(@NonNull ZOMConditional[] object, SerializedOutput writer) {
        writer.writeInt32(object.length);
        for (ZOMConditional conditional : object) {
            if (conditional instanceof ZOMConditionVisible) {
                writer.writeInt32(conditional.mType);
                ((ZOMConditionVisible) conditional).serialize(writer);
            } else if (conditional instanceof ZOMConditionParam) {
                writer.writeInt32(conditional.mType);
                ((ZOMConditionParam) conditional).serialize(writer);
            }
        }
    }

    @Override
    public ZOMConditional[] createFromSerialized(SerializedInput reader) {
        int size = reader.readInt32();
        ZOMConditional[] mCondition = new ZOMConditional[size];
        for (int i = 0; i < size; i++) {
            int type = reader.readInt32();
            if (type == 22) {
                mCondition[i] = ZOMConditionParam.CREATOR.createFromSerialized(reader);
            } else if (type == 44) {
                mCondition[i] = ZOMConditionVisible.CREATOR.createFromSerialized(reader);
            }
        }
        return mCondition;
    }
}
