# Kotfigurator

A Kotlin library for easily mapping [configurate](https://github.com/SpongePowered/configurate) values to class fields.

**NOTE:** Comments only work when the configuration format itself supports comments.

## Usage

#### Gradle

```groovy
repositories {
    mavenCentral()
    maven {
        name = 'jitpack'
        url = 'https://jitpack.io'
    }
}

dependencies {
    compile 'com.github.TheFrontier:Kotfigurator:1.0.0'
}
```

#### Maven

```xml
<repositories>
    <repository>
        <id>jitpack</id>
        <url>https://jitpack.io</url>
    </repository>
</repositories>

<dependencies>
    <dependency>
        <groupId>com.github.TheFrontier</groupId>
        <artifactId>Kotfigurator</artifactId>
        <version>1.0.0</version>
    </dependency>
</dependencies>
```

## Example

#### Supported Data Types:
- `String`
- `Boolean`
- `Int`
- `Long`
- `Float`
- `Double`
- Any type with an associated TypeSerializer
- `List`s of the aforementioned types
- `Map`s of the aforementioned types

```kotlin
class MyPluginConfig(adapter: ConfigAdapter) {

    val joinMessage: String by adapter.string(comment = "The broadcasted message when a player joins.") { "&e%player% has joined the game." }

    val enabledModules: List<String> by adapter.list(key = "enabled-modules") { listOf("module-1", "module-2") }

    val blocks: BlocksSection by adapter.section(comment = "Blocks that require a permission to obtain") { BlocksSection(it) }

    class BlocksSection(adapter: ConfigAdapter) {

        val blockPermissions: Map<String, String> by adapter.map(key = "blocks-with-permissions") {
            mapOf("minecraft:grass" to "my.grass.permission",
                    "minecraft:bedrock" to "my.admin.permission")
        }
    }
}

fun initializeConfig() {
    val loader: HoconConfigurationLoader = ... // Works with any configuration format supported by configurate.
    val node = loader.load()

    val myPluginConfig = MyPluginConfig(NodeConfigAdapter(node))
    
    print(myPluginConfig.joinMessage)
    for (module in myPluginConfig.enabledModules) {
       // enable them
    }
}
```