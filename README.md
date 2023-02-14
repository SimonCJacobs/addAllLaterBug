# addAllLater() Gradle bug with FileCollection provider called eagerly

Normal behaviour, run:
```
./gradlew someTask
```
This gives the output:
```
> Configure project :
In addAllLater provider
Done domainObjectSet calls

> Task :someTask
DOING THE TASK
WE GOT THESE FILES
gradlew
build.gradle.kts
SET ONE CONTAINED
In addAllLater provider
In addAllLater provider
In addAllLater provider
In addAllLater provider
gradlew_transformedOnce
build.gradle.kts_transformedOnce
SET TWO CONTAINED
gradlew_transformedOnce_transformedTwice
build.gradle.kts_transformedOnce_transformedTwice
```
So each `DomainObjectSet` is up-to-date with all objects.

Incorrect behaviour, run:
```
./gradlew someTask -DaddAllLaterFirst=true
```
This gives the output:
```
> Configure project :
In addAllLater provider
In addAllLater provider
In addAllLater provider
Done domainObjectSet calls

> Task :someTask
DOING THE TASK
WE GOT THESE FILES
gradlew
build.gradle.kts
SET ONE CONTAINED
gradlew_transformedOnce
SET TWO CONTAINED
gradlew_transformedOnce_transformedTwice
```
The `FileCollection` provider passed to `addAllLater()` has been evaluated eagerly and thus
the transformations of the second file have been missed.