package pw.dotdash.kotfigurator

import com.google.common.reflect.TypeToken
import ninja.leaping.configurate.ConfigurationNode
import ninja.leaping.configurate.commented.CommentedConfigurationNode
import kotlin.reflect.KProperty

class NodeConfigAdapter(private val node: ConfigurationNode) : ConfigAdapter {

    override fun <T> value(key: String?, comment: String?, type: TypeToken<T>, default: () -> T): ConfigAdapter.Value<T> =
            ValueDelegate(node, key, comment, { this.getValue(type, default) }, { this.setValue(type, it) })

    override fun string(key: String?, comment: String?, default: () -> String): ConfigAdapter.Value<String> =
            ValueDelegate(node, key, comment, { this.getString(default()) }, { this.value = it })

    override fun boolean(key: String?, comment: String?, default: () -> Boolean): ConfigAdapter.Value<Boolean> =
            ValueDelegate(node, key, comment, { this.getBoolean(default()) }, { this.value = it })

    override fun int(key: String?, comment: String?, default: () -> Int): ConfigAdapter.Value<Int> =
            ValueDelegate(node, key, comment, { this.getInt(default()) }, { this.value = it })

    override fun long(key: String?, comment: String?, default: () -> Long): ConfigAdapter.Value<Long> =
            ValueDelegate(node, key, comment, { this.getLong(default()) }, { this.value = it })

    override fun float(key: String?, comment: String?, default: () -> Float): ConfigAdapter.Value<Float> =
            ValueDelegate(node, key, comment, { this.getFloat(default()) }, { this.value = it })

    override fun double(key: String?, comment: String?, default: () -> Double): ConfigAdapter.Value<Double> =
            ValueDelegate(node, key, comment, { this.getDouble(default()) }, { this.value = it })

    override fun <T> list(key: String?, comment: String?, elementType: TypeToken<T>, default: () -> List<T>): ConfigAdapter.Value<List<T>> =
            ValueDelegate(node, key, comment, { this.getList(elementType, default) }, { this.value = it })

    override fun <K, V> map(key: String?, comment: String?, keyType: TypeToken<K>, valueType: TypeToken<V>, default: () -> Map<K, V>): ConfigAdapter.Value<Map<K, V>> =
            ValueDelegate(node, key, comment, { this.getValue(mapTypeTokenOf(keyType, valueType), default) }, { this.value = it })

    override fun <T> section(key: String?, comment: String?, init: (ConfigAdapter) -> T): ConfigAdapter.Section<T> =
            SectionDelegate(node, key, comment, init)

    class ValueDelegate<T>(private val node: ConfigurationNode,
                           override val key: String?,
                           override val comment: String?,
                           private val getter: ConfigurationNode.() -> T,
                           private val setter: ConfigurationNode.(T) -> Unit) : ConfigAdapter.Value<T> {

        private var valueNode: ConfigurationNode? = null
            set(value) {
                (value as? CommentedConfigurationNode)?.setComment(comment)
                field = value
            }

        override fun getValue(self: Any?, property: KProperty<*>): T {
            if (valueNode == null) {
                valueNode = node.getNode(key ?: property.name)
            }
            return (valueNode ?: throw IllegalStateException("Tried to access an uninitialized config node")).getter()
        }

        override fun setValue(self: Any?, property: KProperty<*>, value: T) {
            if (valueNode == null) {
                valueNode = node.getNode(key ?: property.name)
            }
            return (valueNode
                    ?: throw IllegalStateException("Tried to access an uninitialized config node")).setter(value)
        }
    }

    class SectionDelegate<T>(private val node: ConfigurationNode,
                             override val key: String?,
                             override val comment: String?,
                             private val init: (ConfigAdapter) -> T) : ConfigAdapter.Section<T> {

        override fun getValue(self: Any?, property: KProperty<*>): T = lazy {
            val sectionNode = node.getNode(key ?: property.name)
            (sectionNode as? CommentedConfigurationNode)?.setComment(comment)
            return@lazy init(NodeConfigAdapter(sectionNode))
        }.value
    }
}

operator fun <T> ((ConfigAdapter) -> T).invoke(node: ConfigurationNode): T = this(NodeConfigAdapter(node))