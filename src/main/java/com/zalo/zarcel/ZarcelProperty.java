package com.zalo.zarcel;

import com.google.auto.value.AutoValue;

import java.util.Map;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

@AutoValue
public abstract class ZarcelProperty {

    @Nonnull
    public abstract Type type();

    @Nonnull
    public abstract String propertyName();

    /**
     * Nếu một object phụ thuộc vào type, thì object sẽ là abstract class.
     * conditionalProperties bao gồm các Object phụ thuộc.
     * Map.Entry<Map.Entry<String,String>,String> chứa tên Object, và type của Object đó.
     * Trong đó, tên Object bao gồm key: package của object, value: tên của Object
     */
    public abstract ZarcelArrayList<Map.Entry<Map.Entry<String, String>, Integer>> conditionalProperties();


    /**
     *
     * @return DataType chứa key là package, value là tên Class.
     */
    @Nonnull
    public abstract Map.Entry<String, String> dataType();

    /**
     * Khai báo kích thước mảng. Nếu kích thước mảng không biết,
     * Serialize có dạng "true","size", "element_1", ... , "element_n"
     */
    @Nullable
    abstract Integer arraySize();

    public abstract int version();

    public abstract boolean objectNullable();

    public static ZarcelProperty.Builder builder() {
        return new AutoValue_ZarcelProperty.Builder()
                .setVersion(0)
                .setObjectNullable(true);
    }

    @AutoValue.Builder
    public abstract static class Builder {
        public abstract Builder setType(Type type);

        public abstract Builder setDataType(Map.Entry<String, String> dataType);

        public abstract Builder setPropertyName(String propertyName);

        public abstract Builder setArraySize(Integer arraySize);

        public abstract ZarcelArrayList.Builder<Map.Entry<Map.Entry<String, String>, Integer>> conditionalPropertiesBuilder();

        public Builder addConditionalProperty(Map.Entry<Map.Entry<String, String>, Integer> property) {
            conditionalPropertiesBuilder().add(property);
            return this;
        }

        public abstract Builder setVersion(int version);

        public abstract Builder setObjectNullable(boolean objectNullable);

        public abstract ZarcelProperty build();
    }

    public enum Type {
        OBJECT, PRIMITIVE, CONDITIONAL_OBJECT, OBJECT_ARRAY, PRIMITIVE_ARRAY, CONDITIONAL_OBJECT_ARRAY
    }
}
