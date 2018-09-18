# iri-mdx-doclet
Java Doclet for generating MDX files from Javadoc

### Run
Clone project, then run with maven:
```maven clean compile package install```

Add plugin to the project (For now this looks for the IRI services/API class since thats the functions we publish)
```

By default, this outputs into ```target/site/```

<plugin>
    <groupId>org.apache.maven.plugins</groupId>
    <artifactId>maven-javadoc-plugin</artifactId>
    <version>3.0.0</version>
    <configuration>
        <doclet>com.iota.mdxdoclet.MDXDoclet</doclet>
        <sourcepath>src/main/java</sourcepath>
        <useStandardDocletOptions>false</useStandardDocletOptions>
        <destDir>docs</destDir>
        <additionalOptions>
            <additionalOption>-version "${project.version}"</additionalOption>
        </additionalOptions>
        <quiet>true</quiet>
        <docletArtifact>
            <groupId>com.iota</groupId>
            <artifactId>mdxdoclet</artifactId>
            <version>0.1</version>
        </docletArtifact>
    </configuration>
</plugin>
```
