package com.zing.zalo.neon2;

import com.zing.zalo.neon.helper.NeonSerializable;

public interface NeonMigrator<T extends NeonSerializable> {
    void migrate(T object, int fromVersion, int toVersion);
}
