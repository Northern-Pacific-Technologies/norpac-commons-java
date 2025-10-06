# NorPac Commons Java

Common Java utilities and libraries for Northern Pacific Technologies, LLC.

## Installation

### Using as a Maven Dependency

To use this library in your Maven project, add the following to your `pom.xml`:

```xml
<repositories>
  <repository>
    <id>github</id>
    <url>https://maven.pkg.github.com/norpactech/norpac-commons</url>
  </repository>
</repositories>

<dependencies>
  <dependency>
    <groupId>com.norpactech.commons</groupId>
    <artifactId>norpac-commons-java</artifactId>
    <version>1.0.0</version>
  </dependency>
</dependencies>
```

### Authentication for GitHub Packages

To download packages from GitHub Packages, you need to authenticate. Add this to your Maven `settings.xml` file (usually located at `~/.m2/settings.xml`):

```xml
<settings>
  <servers>
    <server>
      <id>github</id>
      <username>YOUR_GITHUB_USERNAME</username>
      <password>YOUR_GITHUB_TOKEN</password>
    </server>
  </servers>
</settings>
```

Replace `YOUR_GITHUB_USERNAME` with your GitHub username and `YOUR_GITHUB_TOKEN` with a GitHub Personal Access Token that has `read:packages` permission.

## Features

This library includes the following utilities:

### DateUtils
- `formatDayMinutesSeconds(ZonedDateTime)` - Format dates with pattern "MM-dd-yyyy HH:mm:ss Z"
- `toInstant(java.sql.Date)` - Convert java.sql.Date to Instant

### FileUtils
- `zipToFiles(String, byte[])` - Extract zip files to filesystem
- `zipToString(byte[])` - Extract zip contents as string
- `zipToFile(String, byte[])` - Write zip data to file
- `filesToZip(List<DownloadFileVO>)` - Create zip from file objects

### Value Objects
- `DownloadFileVO` - Represents downloadable files with path and content

## Building

To build the project locally:

```bash
mvn clean compile
```

To run tests:

```bash
mvn test
```

## Publishing

This project is automatically published to GitHub Packages when:
- A new release is created on GitHub
- Code is pushed to the main/master branch

## License

© 2025 Northern Pacific Technologies, LLC. All Rights Reserved.