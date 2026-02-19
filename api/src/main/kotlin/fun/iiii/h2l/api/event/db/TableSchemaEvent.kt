package `fun`.iiii.h2l.api.event.db

import java.util.concurrent.CopyOnWriteArrayList

enum class TableSchemaAction {
    CREATE_ALL,
    DROP_ALL,
}

data class TableSchemaEvent(
    val action: TableSchemaAction,
)

object TableSchemaEventApi {
    private val listeners = CopyOnWriteArrayList<(TableSchemaEvent) -> Unit>()

    @JvmStatic
    fun registerListener(listener: (TableSchemaEvent) -> Unit) {
        listeners.add(listener)
    }

    @JvmStatic
    fun fire(event: TableSchemaEvent) {
        listeners.forEach { it(event) }
    }
}
