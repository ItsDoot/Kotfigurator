package test.pw.dotdash.kotfigurator

import ninja.leaping.configurate.SimpleConfigurationNode
import ninja.leaping.configurate.commented.SimpleCommentedConfigurationNode
import pw.dotdash.kotfigurator.ConfigAdapter
import pw.dotdash.kotfigurator.invoke
import pw.dotdash.kotfigurator.list
import pw.dotdash.kotfigurator.map
import kotlin.test.Test
import kotlin.test.assertEquals

class ConfigAdapterTest {

    private class TestAdapter(adapter: ConfigAdapter) {

        val myString: String by adapter.string(key = "string-key", comment = "Shirt strings") { "my default value" }
        val myBoolean: Boolean by adapter.boolean(key = "bool-key", comment = "Truths") { true }
        val myInt: Int by adapter.int(key = "int-key", comment = "0..infinity") { 52 }
        val myLong: Long by adapter.long(key = "long-key", comment = "0..2362341") { 716 }
        val myFloat: Float by adapter.float(key = "float-key", comment = "0.5") { 5.1f }
        val myDouble: Double by adapter.double(key = "double-key", comment = "636.14") { 1526.14 }

        val testSection: TestSection by adapter.section(key = "section-key", comment = "My Nested Section") { TestSection(it) }

        var stringVar: String by adapter.string(key = "var-string-key", comment = "My edited value") { "my default variable value" }

        var stringList: List<String> by adapter.list(key = "string-list-key", comment = "Shirt strings list")
        var stringMap: Map<String, String> by adapter.map(key = "string-map-key", comment = "Shirt strings map")

        class TestSection(adapter: ConfigAdapter) {

            val myNestedString: String by adapter.string("nested-key", comment = "bird's nest") { "my nested value" }
        }
    }

    @Test
    fun `test default value & custom comments`() {
        val source = SimpleCommentedConfigurationNode.root()
        val testAdapter = (::TestAdapter)(source)

        assertEquals("my default value", testAdapter.myString)
        assertEquals(true, testAdapter.myBoolean)
        assertEquals(52, testAdapter.myInt)
        assertEquals(716, testAdapter.myLong)
        assertEquals(5.1f, testAdapter.myFloat)
        assertEquals(1526.14, testAdapter.myDouble)
        assertEquals("my nested value", testAdapter.testSection.myNestedString)

        assertEquals("Shirt strings", source.getNode("string-key").comment.get())
        assertEquals("Truths", source.getNode("bool-key").comment.get())
        assertEquals("0..infinity", source.getNode("int-key").comment.get())
        assertEquals("0..2362341", source.getNode("long-key").comment.get())
        assertEquals("0.5", source.getNode("float-key").comment.get())
        assertEquals("636.14", source.getNode("double-key").comment.get())
        assertEquals("My Nested Section", source.getNode("section-key").comment.get())
        assertEquals("bird's nest", source.getNode("section-key").getNode("nested-key").comment.get())
    }

    @Test
    fun `test custom key & new value set`() {
        val source = SimpleConfigurationNode.root()
        source.getNode("string-key").value = "my new value"
        source.getNode("bool-key").value = false
        source.getNode("int-key").value = 73
        source.getNode("long-key").value = 861
        source.getNode("float-key").value = 671265.31f
        source.getNode("double-key").value = 2762.1315

        val testAdapter = (::TestAdapter)(source)

        assertEquals("my new value", testAdapter.myString)
        assertEquals(false, testAdapter.myBoolean)
        assertEquals(73, testAdapter.myInt)
        assertEquals(861, testAdapter.myLong)
        assertEquals(671265.31f, testAdapter.myFloat)
        assertEquals(2762.1315, testAdapter.myDouble)
    }

    @Test
    fun `test value setting`() {
        val source = SimpleConfigurationNode.root()
        source.getNode("var-string-key").value = "My previously set value"

        val testAdapter = (::TestAdapter)(source)

        assertEquals("My previously set value", testAdapter.stringVar)

        testAdapter.stringVar = "My newly set value"
        assertEquals("My newly set value", testAdapter.stringVar)
        assertEquals("My newly set value", source.getNode("var-string-key").string)
    }

    @Test
    fun `test list and map`() {
        val source1 = SimpleConfigurationNode.root()
        val testAdapter1 = (::TestAdapter)(source1)

        assertEquals(listOf(), testAdapter1.stringList)
        assertEquals(mapOf(), testAdapter1.stringMap)

        val source2 = SimpleConfigurationNode.root()
        source2.getNode("string-list-key").value = listOf("My", "previously", "set", "value")
        source2.getNode("string-map-key", "key-1").value = "My"
        source2.getNode("string-map-key", "key-2").value = "previously"
        source2.getNode("string-map-key", "key-3").value = "set"
        source2.getNode("string-map-key", "key-4").value = "value"

        val testAdapter2 = (::TestAdapter)(source2)

        assertEquals(listOf("My", "previously", "set", "value"), testAdapter2.stringList)
        assertEquals(mapOf("key-1" to "My", "key-2" to "previously", "key-3" to "set", "key-4" to "value"), testAdapter2.stringMap)
    }
}