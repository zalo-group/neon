package com.zing.zalo.zarcel;

import com.zing.zalo.data.serialization.Serializable;

public interface ZarcelMigrator<T extends Serializable> {
    void migrate(T object, int fromVersion, int toVersion);
}
