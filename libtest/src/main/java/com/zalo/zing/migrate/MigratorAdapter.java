package com.zalo.zing.migrate;

import com.zing.zalo.zarcel.ZarcelMigrator;

public class MigratorAdapter implements ZarcelMigrator<MigratorClass> {
    @Override
    public void migrate(MigratorClass object, int fromVersion, int toVersion) {
        object.color = Integer.MAX_VALUE;
    }
}
