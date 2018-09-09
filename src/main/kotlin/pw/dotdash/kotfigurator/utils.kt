package pw.dotdash.kotfigurator

import com.google.common.reflect.TypeParameter
import com.google.common.reflect.TypeToken

inline fun <reified T> typeToken(): TypeToken<T> = object : TypeToken<T>() {}

inline fun <reified K, reified V> mapTypeToken(): TypeToken<Map<K, V>> =
        (object : TypeToken<Map<K, V>>() {})
                .where(object : TypeParameter<K>() {}, typeToken<K>())
                .where(object : TypeParameter<V>() {}, typeToken<V>())

fun <K, V> mapTypeTokenOf(keyType: TypeToken<K>, valueType: TypeToken<V>): TypeToken<Map<K, V>> =
        (object : TypeToken<Map<K, V>>() {})
                .where(object : TypeParameter<K>() {}, keyType)
                .where(object : TypeParameter<V>() {}, valueType)