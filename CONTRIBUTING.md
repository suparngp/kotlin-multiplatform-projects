# Contributing

## Guide

- Ensure that your PR is tied to an issue. You can use [hub](https://hub.github.com) to later convert the issue in a PR.
- The IntelliJ idea code style settings are added to the repo. Please use them wherever applicable.
- For every target following directory tree must exist even if the rest of the directory is empty.

```
├── [target]Main
│   ├── kotlin
│   │   └── .keep
│   └── resources
│       └── .keep
├── [target]Test
│   ├── kotlin
│   │   └── .keep
│   └── resources
│       └── .keep

// for example
├── androidMain
│   ├── kotlin
│   │   └── .keep
│   └── resources
│       └── .keep
├── androidTest
│   ├── kotlin
│   │   └── .keep
│   └── resources
│       └── .keep
```

- Try to match the file structure with the existing targets so that it is easy for others to compare.
- Check the notes of the Build Plugin to understand how to add a new target and dependencies.
- Only the latest stable `kotlin` release is supported at the moment.
- All PRs addressing bugs, crashes will be prioritized until the projects are mature enough before new features are added.
- PRs to support new platforms are welcome.

## Opening issues

Please use the appropriate template for opening issues.

### Users

- Any thing which you need help with including bugs, crashes, suggestions, enhancements or just general help.

### Contributors

Please open a new issue outside of your Pull Request if any of the following happens. Someone will work with you to resolve it. Please do not try to address such issues your PRs because these may require a broader discussion.

- the build plugin does not support a particular platform or is missing some configuration support.
- the current supported `kotlin` version is missing a new feature which you are using in your PR.
- any changes to `android` sdk versions.
- any changes to the currently supported `kotlin` version.
- any changes to supported `jvm` versions.
- any changes to the project structure in general.
