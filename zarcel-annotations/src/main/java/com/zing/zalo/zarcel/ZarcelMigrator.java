package com.zing.zalo.zarcel;

import com.zing.zalo.zarcel.helper.ZarcelSerializable;

public interface ZarcelMigrator<T extends ZarcelSerializable> {
    void migrate(T object, int fromVersion, int toVersion);
}
