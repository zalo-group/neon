package com.zing.zalo.neon;

import com.google.auto.value.AutoValue;
import com.squareup.javapoet.ClassName;
import java.io.IOException;
import java.util.Map;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.processing.Filer;
import javax.lang.model.element.Element;

@AutoValue
public abstract class NeonClass {

    @Nonnull
    public abstract String name();

    @Nonnull
    public abstract String thisPackage();

    public abstract boolean inheritanceSupported();

    @Nullable
    public abstract Map.Entry<String, String> parentClass();

    @Nonnull
    public abstract NeonArrayList<NeonProperty> properties();

    public abstract NeonArrayList<Map.Entry<String, String>> serializeException();

    public abstract NeonArrayList<Map.Entry<String, String>> deserializeException();

    public abstract int version();

    public abstract int compatibleSince();

    @Nullable
    public abstract ClassName migrateClass();

    public static Builder builder() {
        return new AutoValue_NeonClass.Builder()
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

        abstract NeonArrayList.Builder<NeonProperty> propertiesBuilder();

        public Builder addProperty(NeonProperty property) {
            propertiesBuilder().add(property);
            return this;
        }

        abstract NeonArrayList.Builder<Map.Entry<String, String>> serializeExceptionBuilder();

        public Builder addSerializeException(Map.Entry<String, String> exceptionClass) {
            serializeExceptionBuilder().add(exceptionClass);
            return this;
        }

        abstract NeonArrayList.Builder<Map.Entry<String, String>> deserializeExceptionBuilder();

        public Builder addDeserializeException(Map.Entry<String, String> exceptionClass) {
            deserializeExceptionBuilder().add(exceptionClass);
            return this;
        }

        public abstract Builder setMigrateClass(ClassName migrateClass);

        public abstract NeonClass build();
    }

    public void generateFile(Filer filer, Element originElement) throws IOException {
        NeonGenerator generator = new NeonGenerator();
        generator.generateFile(this, filer, originElement);
    }
}
