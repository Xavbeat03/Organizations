package io.github.Xavbeat03.Organizations.db.flyway.migration;

import io.github.Xavbeat03.Organizations.utility.DB;
import io.github.Xavbeat03.Organizations.utility.Logger;
import org.flywaydb.core.api.migration.BaseJavaMigration;
import org.flywaydb.core.api.migration.Context;
import org.jooq.DSLContext;

import static io.github.ExampleUser.ExamplePlugin.db.schema.Tables.TEST;
import static org.jooq.impl.DSL.field;
import static org.jooq.impl.DSL.name;
import static org.jooq.impl.SQLDataType.VARCHAR;

/**
 * Example Java Flyway migration using jOOQ.
 */
public class V3__Example extends BaseJavaMigration {
    @Override
    public void migrate(Context flywayContext) throws Exception {
        try {
            final DSLContext context = DB.getContext(flywayContext.getConnection());

            context.alterTable(TEST).addColumn(field(name("meme"), VARCHAR)).execute();

            context
                .alterTableIfExists(TEST)
                .addColumnIfNotExists(field(name("column_name"), VARCHAR(32).notNull()/*.defaultValue("default")*/))
                .execute();
        } catch (Exception e) {
            Logger.get().error(e.getMessage());
        }
    }
}
