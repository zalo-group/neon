package com.zalo.zarcel;

import com.google.auto.value.AutoValue;

import javax.annotation.Nonnull;
import java.util.Map;

@AutoValue
public abstract class ZarcelProperty {

    @Nonnull
    abstract Type type();

    @Nonnull
    abstract String propertyName();

    /**
     * Nếu một object phụ thuộc vào type, thì object sẽ là abstract class.
     * conditionalProperties bao gồm các Object phụ thuộc.
     *
     * @return Map.Entry&lt;Map.Entry&lt;String,String&gt;,String&gt; chứa tên Object, và type của Object đó.
     * Trong đó, tên Object bao gồm key: package của object, value: tên của Object
     */
    public abstract ZarcelArrayList<Map.Entry<Map.Entry<String, String>, Integer>> conditionalProperties();


    /**
     * @return DataType chứa key là package, value là tên Class.
     */
    @Nonnull
    abstract Map.Entry<String, String> dataType();

    abstract int version();

    abstract boolean objectNullable();

    static ZarcelProperty.Builder builder() {
        return new com.zalo.zarcel.AutoValue_ZarcelProperty.Builder()
                .setVersion(0)
                .setObjectNullable(true);
    }

    @AutoValue.Builder
    public abstract static class Builder {
        abstract Builder setType(Type type);

        abstract Builder setDataType(Map.Entry<String, String> dataType);

        abstract Builder setPropertyName(String propertyName);

        abstract ZarcelArrayList.Builder<Map.Entry<Map.Entry<String, String>, Integer>> conditionalPropertiesBuilder();

        public Builder addConditionalProperty(Map.Entry<Map.Entry<String, String>, Integer> property) {
            conditionalPropertiesBuilder().add(property);
            return this;
        }

        abstract Builder setVersion(int version);

        abstract Builder setObjectNullable(boolean objectNullable);

        abstract ZarcelProperty build();

    }

    public enum Type {
        OBJECT, PRIMITIVE, OBJECT_ARRAY, PRIMITIVE_ARRAY, CUSTOM_ADAPTER
    }
}
