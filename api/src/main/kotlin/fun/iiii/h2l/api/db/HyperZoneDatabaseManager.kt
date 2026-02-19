package `fun`.iiii.h2l.api.db

interface HyperZoneDatabaseManager {
    val tablePrefix: String

    fun <T> executeTransaction(statement: () -> T): T
}