# Change Log

## v1.0.13
1. Added support for node js in `fs` #80.
2. Fix docs link #81.

## v1.0.12
1. Migrated `utilities`, `fs`, `concurrency`, `rx`, `rx-reaktive-runtime`, `graphl` and `graphql-plugin` to Kotlin 1.4.10
2. Some major Changes in `graphql` due to kotlinx.serialization update. 
3. Unable to migrate `cache` to `1.4.10` at this time due to a bug in resolving dependencies.

## v1.0.11
1. Support auth headers in graphql plugin
2. Skipped for all other libs

## v1.0.10
1. Update Kotlin to 1.3.70 


## v1.0.9
1. `[fs]` Replaced `NSURL.URLWithString` with `NSURL.fileURLWithPath` 

## v1.0.8
1. Support for byte read in fs.
1. First release of graphql and graphql-plugin
1. rx, rx-runtime packages introduced
1. 1.3.61 update
1. New docs site

## v1.0.7
1. Support for JVM in fs module.
2. Migrate GraphQL plugin from Node to Kotlin.

## v1.0.6
1. Renamed `threading` module to `concurrency`.
2. Introduced unified `Worker` api for all platforms.
3. Jobs which can resume on a different thread.
4. Cleaner `JobDispatcher` APIs.

## v1.0.4

1. Removed `core` from each package name.
2. Updated the bintray repository.
3. Brand new build system with `Suparnatural` gradle build plugin.
4. Fix: Code completion not working in ios source set.
