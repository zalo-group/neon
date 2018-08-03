package com.zalo.zarcel;

import com.google.auto.value.AutoValue;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.processing.Filer;

@AutoValue
public abstract class ZarcelClass {

    @Nonnull
    public abstract String name();

    @Nonnull
    public abstract String thisPackage();

    public abstract boolean serializeParent();

    @Nullable
    public abstract Map.Entry<String, String> parentClass();

    @Nonnull
    public abstract ZarcelArrayList<ZarcelProperty> properties();

    public abstract int version();

    public static Builder builder() {
        return new AutoValue_ZarcelClass.Builder()
                .setVersion(0)
                .setSerializeParent(false);
    }

    @AutoValue.Builder
    public abstract static class Builder {
        public abstract Builder setName(String name);

        public abstract Builder setThisPackage(String thisPackage);

        public abstract Builder setVersion(int version);

        public abstract Builder setSerializeParent(boolean serializeParent);

        public abstract Builder setParentClass(Map.Entry<String, String> parentClass);

        abstract ZarcelArrayList.Builder<ZarcelProperty> propertiesBuilder();

        public Builder addProperty(ZarcelProperty property) {
            propertiesBuilder().add(property);
            return this;
        }

        public abstract ZarcelClass build();
    }

    public void generateFile(Filer filer) throws IOException {
        ZarcelGenerator.generateFile(this, filer);
    }
}
