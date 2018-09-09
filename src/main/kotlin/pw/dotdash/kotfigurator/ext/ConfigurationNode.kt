/*
 * MIT License
 *
 * Copyright (c) 2018 Christian Hughes
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

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