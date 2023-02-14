val files: ConfigurableFileCollection = objects.fileCollection()
val setOne = objects.domainObjectSet(String::class.java)
val setTwo = objects.domainObjectSet(String::class.java)

files.from(projectDir.resolve("gradlew"))

if (System.getProperty("addAllLaterFirst") == "true") {
    addAllLater()
    addAll()
}
else {
    addAll()
    addAllLater()
}

fun addAllLater() {
    setOne.addAllLater(
        files.elements.map { elements ->
            elements.map { element ->
                logger.lifecycle("In addAllLater provider")
                element.asFile.name + "_transformedOnce"
            }
        }
    )
}

fun addAll() {
    setOne.all { setTwo.add( this@all + "_transformedTwice") }
}

project.logger.lifecycle("Done domainObjectSet calls")

files.from(projectDir.resolve("build.gradle.kts"))

tasks.register("someTask") {
    doLast {
        logger.lifecycle("DOING THE TASK")
        logger.lifecycle("WE GOT THESE FILES")
        files.files.forEach { file -> println(file.name) }
        logger.lifecycle("SET ONE CONTAINED")
        setOne.forEach { println(it) }
        logger.lifecycle("SET TWO CONTAINED")
        setTwo.forEach { println(it) }
    }
}