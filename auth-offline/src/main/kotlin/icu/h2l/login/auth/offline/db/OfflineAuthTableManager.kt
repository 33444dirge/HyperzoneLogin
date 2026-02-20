package icu.h2l.login.auth.offline.db

import com.velocitypowered.api.event.Subscribe
import icu.h2l.api.db.HyperZoneDatabaseManager
import icu.h2l.api.db.table.ProfileTable
import icu.h2l.api.event.db.TableSchemaAction
import icu.h2l.api.event.db.TableSchemaEvent
import org.jetbrains.exposed.sql.SchemaUtils
import java.util.logging.Logger

class OfflineAuthTableManager(
    private val logger: Logger,
    private val databaseManager: HyperZoneDatabaseManager,
    tablePrefix: String,
    profileTable: ProfileTable
) {
    val offlineAuthTable = OfflineAuthTable(tablePrefix, profileTable)

    fun createTable() {
        databaseManager.executeTransaction {
            SchemaUtils.create(offlineAuthTable)
            logger.info("已创建表: ${offlineAuthTable.tableName}")
        }
    }

    fun dropTable() {
        databaseManager.executeTransaction {
            SchemaUtils.drop(offlineAuthTable)
            logger.warning("已删除表: ${offlineAuthTable.tableName}")
        }
    }

    @Subscribe
    fun onSchemaEvent(event: TableSchemaEvent) {
        when (event.action) {
            TableSchemaAction.CREATE_ALL -> createTable()
            TableSchemaAction.DROP_ALL -> dropTable()
        }
    }
}