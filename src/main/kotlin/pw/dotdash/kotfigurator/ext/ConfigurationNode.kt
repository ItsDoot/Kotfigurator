package pw.dotdash.kotfigurator.ext

import com.google.common.reflect.TypeToken
import ninja.leaping.configurate.ConfigurationNode
import pw.dotdash.kotfigurator.mapTypeToken
import pw.dotdash.kotfigurator.mapTypeTokenOf
import pw.dotdash.kotfigurator.typeToken

inline fun <reified E> ConfigurationNode.getList(): List<E> =
        this.getList(typeToken<E>())

inline fun <reified E> ConfigurationNode.getList(def: List<E>?): List<E> =
        this.getList(typeToken<E>(), def)

inline fun <reified E> ConfigurationNode.getList(noinline defSupplier: () -> List<E>): List<E> =
        this.getList(typeToken<E>(), defSupplier)

fun <K, V> ConfigurationNode.getMap(keyType: TypeToken<K>, valueType: TypeToken<V>): Map<K, V> =
        this.getValue(mapTypeTokenOf(keyType, valueType)) ?: emptyMap()

fun <K, V> ConfigurationNode.getMap(keyType: TypeToken<K>, valueType: TypeToken<V>, def: Map<K, V>?): Map<K, V> =
        this.getValue(mapTypeTokenOf(keyType, valueType), def) ?: emptyMap()

fun <K, V> ConfigurationNode.getMap(keyType: TypeToken<K>, valueType: TypeToken<V>, defSupplier: () -> Map<K, V>): Map<K, V> =
        this.getValue(mapTypeTokenOf(keyType, valueType), defSupplier) ?: emptyMap()

inline fun <reified K, reified V> ConfigurationNode.getMap(): Map<K, V> =
        this.getValue(mapTypeToken()) ?: emptyMap()

inline fun <reified K, reified V> ConfigurationNode.getMap(def: Map<K, V>?): Map<K, V> =
        this.getValue(mapTypeToken(), def) ?: emptyMap()

inline fun <reified K, reified V> ConfigurationNode.getMap(noinline defSupplier: () -> Map<K, V>): Map<K, V> =
        this.getValue(mapTypeToken(), defSupplier) ?: emptyMap()