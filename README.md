# BlackBoard Circuit Designer

![Logo](web/images/splash_1.1_final.png)

Blackboard is intended for the hobby enthusiast and should help building prototypes on so called breadboards easily.

Currently BlackBoard provides the following features:

* Create so called breadboards (also stripe or perf boards) easily
* Create simple and easy to read schematics
* NGSpice integration, thus the ability to simulate the current schematic with NGSpice
* A big library of electronic parts with more than 400 pieces and library with 500 symbols 
* integrated part and symbol editor
* layer based editing

## Screenshots

![Perfboard Editing](web/images/bb_boards.jpg)

![Schematics Editing](web/images/bb_schematics.jpg)

![NGSpice simulation](web/images/bb_sim.jpg)

![Scale Editing](web/images/bb_scale.jpg)

![Symbol Editing](web/images/bb_symbols.jpg)

![Part Library](web/images/bb_parts.jpg)

## Prerequisites

A current Java runtime is required (the start scripts use `--enable-native-access=ALL-UNNAMED`). To build from source you also need [Apache Ant](https://ant.apache.org/).

User data (parts, symbols, models, preferences) lives in `~/.BlackBoard` (`%USERPROFILE%\.BlackBoard` on Windows).

## Installation

**Windows:** For Windows download the provided installer under releases (`BlackBoardSetup_…exe`). For a portable installation use the release zip file (`blackboard-bin-…zip`).

**Linux:** Download a release zip or build one with ant. And start with `./Linux_Start.sh`.

On the first start from the JAR, BlackBoard copies missing data folders (`parts`, `symbols`, `models`, `datasheets`, `simulators`) from the install directory into `~/.BlackBoard`. You do not copy those folders by hand like in earlier versions.

You can also run:

```shell
java --enable-native-access=ALL-UNNAMED -jar BlackBoard.jar
```

**hint for Linux Users**: The first JAR start writes a GNOME/KDE menu entry to `~/.local/share/applications/org.pmedv.blackboard.desktop` and registers the app-icon. `.bb` files are registered as `application/x-blackboard`.


## Installation from source (development)

```shell
git clone git@github.com:PBahner/blackboard.git
cd blackboard
ant
```

Running from the IDE or classes on disk is **not** treated as an install. BlackBoard does **not** copy bundled data and does **not** install a desktop menu or file association. For a first development setup, copy the libraries into the user data directory yourself:

```shell
mkdir -p ~/.BlackBoard
cp -R models symbols parts datasheets simulators ~/.BlackBoard
```

Start the main class `org.pmedv.blackboard.app.BlackBoard` from the IDE (with `--enable-native-access=ALL-UNNAMED` if the JVM requires it), or use the JAR from `dist/lib` as in the normal install.


## Important Notes

In order to run the simulation, you must run blackboard as Administrator.

## Documentation 

Find the german manual here:
https://www.pueski.de/bb/doc/
