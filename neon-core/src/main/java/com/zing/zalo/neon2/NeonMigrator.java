package com.zing.zalo.neon2;

public interface NeonMigrator<T> {
    void migrate(T object, int fromVersion, int toVersion);
}
