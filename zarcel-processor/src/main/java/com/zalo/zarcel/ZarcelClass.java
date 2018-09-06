package com.zalo.zarcel;

import com.google.auto.value.AutoValue;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.processing.Filer;
import java.io.IOException;
import java.util.Map;
import java.util.Set;

@AutoValue
public abstract class ZarcelClass {

    @Nonnull
    public abstract String name();

    @Nonnull
    public abstract String thisPackage();

    public abstract boolean inheritanceSupported();

    @Nullable
    public abstract Map.Entry<String, String> parentClass();

    @Nonnull
    public abstract ZarcelArrayList<ZarcelProperty> properties();

    public abstract ZarcelArrayList<Map.Entry<String, String>> serializeException();

    public abstract ZarcelArrayList<Map.Entry<String, String>> deserializeException();

    public abstract int version();


    public abstract int compatibleSince();

    public static Builder builder() {
        return new com.zalo.zarcel.AutoValue_ZarcelClass.Builder()
                .setVersion(0)
                .setCompatibleSince(0)
                .setInheritanceSupported(false);
    }

    @AutoValue.Builder
    public abstract static class Builder {
        public abstract Builder setName(String name);

        public abstract Builder setThisPackage(String thisPackage);

        public abstract Builder setVersion(int version);

        public abstract Builder setCompatibleSince(int compatibleSince);

        public abstract Builder setInheritanceSupported(boolean serializeParent);

        public abstract Builder setParentClass(Map.Entry<String, String> parentClass);

        abstract ZarcelArrayList.Builder<ZarcelProperty> propertiesBuilder();

        public Builder addProperty(ZarcelProperty property) {
            propertiesBuilder().add(property);
            return this;
        }

        abstract ZarcelArrayList.Builder<Map.Entry<String, String>> serializeExceptionBuilder();

        public Builder addSerializeException(Map.Entry<String, String> exceptionClass) {
            serializeExceptionBuilder().add(exceptionClass);
            return this;
        }

        abstract ZarcelArrayList.Builder<Map.Entry<String, String>> deserializeExceptionBuilder();

        public Builder addDeserializeException(Map.Entry<String, String> exceptionClass) {
            deserializeExceptionBuilder().add(exceptionClass);
            return this;
        }

        public abstract ZarcelClass build();
    }

    public void generateFile(Filer filer) throws IOException {
        ZarcelGenerator generator = new ZarcelGenerator();
        generator.generateFile(this, filer);
    }
}
