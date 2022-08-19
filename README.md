# PluginEvents Project

An event handler for subscribing to custom events for plugins that do not have dependencies on Bukkit or BungeeCord. Specifically, this library is used when a Bukkit and/or BungeeCord project has a core module that has no interactions with the Bukkit and/or BungeeCord API(s). Events can still be passed from project core to project core, all without relying on unnecessary APIs.

## Obtaining PluginEvents

You can obtain a copy of PluginEvents via the following methods:
- Download a pre-built copy from the [Releases page](https://github.com/bspfsystems/PluginEvents/releases/latest/). The latest version is 0.3.0-SNAPSHOT.
- Build from source (see below).
- Include it as a dependency in your project (see the Development API section).

### Build from Source

PluginEvents uses [Apache Maven](https://maven.apache.org/) to build and handle dependencies.

#### Requirements

- Java Development Kit (JDK) 8 or higher
- Git
- Apache Maven

#### Compile / Build

Run the following commands to build the library `.jar` file:
```
git clone https://github.com/bspfsystems/PluginEvents.git
cd PluginEvents/
mvn clean install
```

The `.jar` file will be located in the `target/` folder.

## Developer API

### Add PluginEvents as a Dependency

To add PluginEvents as a dependency to your project, use one of the following common methods (you may use others that exist, these are the common ones):

_**PLEASE NOTE:** PluginEvents is in the initial buildout stage. PluginEvent's API artifacts only exist in the `snapshots/` repositories at this time. When an initial release has been made, the repository will switch to `releases/`. Please be sure to update your code to reflect this status._

**Maven:**<br />
Include the following in your `pom.xml` file:<br />
```
<repositories>
    <repository>
        <id>sonatype-repo</id>
        <url>https://oss.sonatype.org/content/repositories/snapshots/</url>
    </repository>
</repositories>

<dependencies>
    <dependency>
        <groupId>org.bspfsystems</groupId>
        <artifactId>pluginevents</artifactId>
        <version>0.3.0-SNAPSHOT</version>
        <scope>compile</scope>
    </dependency>
</dependencies>
```

**Gradle:**<br />
Include the following in your `build.gradle` file:<br />
```
repositories {
    maven {
        url "https://oss.sonatype.org/content/repositories/snapshots/"
    }
}

dependencies {
    implementation "org.bspfsystems:pluginevents:0.3.0-SNAPSHOT"
}
```

### API Examples

These are some basic usages of PluginEvents; for a full scope of what the library offers, please see the Javadocs section below.
```
// Create a custom Event for a plugin
public class PluginStartEvent extends Event {
    
    private final String pluginName;
    
    public PluginStartEvent(String pluginName) {
        this.pluginName = pluginName;
    }
    
    public String getPluginName() {
        return this.pluginName;
    }
}

////////////////////////////////////////////////////////////////

// Create a custom EventHandler for the Event
public class PluginStartEventListener implements EventListener {
    
    @EventHandler(priority = EventPriority.NORMAL)
    public void onPluginStartEvent(PluginStartEvent event) {
        System.out.println("Plugin " + event.getPluginName() + " is starting.");
    }
}

////////////////////////////////////////////////////////////////

// Register the EventHandler with the EventBus
Logger listenerLogger = Logger.getLogger("PluginStartEventListener");
EventBus eventBus = EventBus.getInstance();

eventBus.registerListener(new PluginStartEventListener(), listenerLogger);

// Call the event
eventBus.callEvent(new PluginStartEvent("ExamplePlugin"));
```

### Javadocs

The API Javadocs can be found [here](https://bspfsystems.org/docs/pluginevents/), kindly hosted by [javadoc.io](https://javadoc.io).

## Contributing, Support, and Issues

Please check out [CONTRIBUTING.md](CONTRIBUTING.md) for more information.

## Licensing

PluginEvents uses the following licenses:
- [The Apache License, Version 2.0](https://apache.org/licenses/LICENSE-2.0.html)

### Contributions & Licensing

Contributions to the project will remain licensed under the respective license, as defined by the particular license. Copyright/ownership of the contributions shall be governed by the license. The use of an open source license in the hopes that contributions to the project will have better clarity on legal rights of those contributions.

_Please Note: This is not legal advice. If you are unsure on what your rights are, please consult a lawyer._
