/*
 * Copyright 2010 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.gradle.api.tasks.testing.testng

import groovy.xml.MarkupBuilder
import org.gradle.api.JavaVersion
import org.gradle.api.tasks.testing.TestFrameworkOptions

class TestNGOptions extends TestFrameworkOptions {

    static final String JDK_ANNOTATIONS = 'JDK'
    static final String JAVADOC_ANNOTATIONS = 'Javadoc'

    /**
     * The location to write TestNG's output.
     * <p>
     * Defaults to the owning test task's location for writing the HTML report, at the time the {@code useTestNG()} method is called.
     *
     * @since 1.11
     */
    File outputDirectory

    /**
     * When true, Javadoc annotations are used for these tests. When false, JDK annotations are used. If you use
     * Javadoc annotations, you will also need to specify "sourcedir".
     *
     * Defaults to JDK annotations if you're using the JDK 5 jar and to Javadoc annotations if you're using the JDK 1.4
     * jar.
     */
    boolean javadocAnnotations

    String getAnnotations() {
        javadocAnnotations ? JAVADOC_ANNOTATIONS : JDK_ANNOTATIONS
    }

    /**
     * List of all directories containing Test sources. Should be set if annotations is 'Javadoc'.
     */
    List testResources

    /**
     * The set of groups to run.
     */
    Set<String> includeGroups = new HashSet<String>()

    /**
     * The set of groups to exclude.
     */
    Set<String> excludeGroups = new HashSet<String>()

    /**
     * Fully qualified classes that are TestNG listeners (instances of org.testng.ITestListener or
     * org.testng.IReporter). By default, the listeners set is empty.
     *
     * Configuring extra listener:
     * <pre autoTested=''>
     * apply plugin: 'java'
     *
     * test {
     *   useTestNG() {
     *     //creates emailable HTML file
     *     //this reporter typically ships with TestNG library
     *     listeners << 'org.testng.reporters.EmailableReporter'
     *   }
     * }
     * </pre>
     */
    Set<String> listeners = new LinkedHashSet<String>()

    /**
     * The parallel mode to use for running the tests - either methods or tests.
     *
     * Not required.
     *
     * If not present, parallel mode will not be selected 
     */
    String parallel = null

    /**
     * The number of threads to use for this run. Ignored unless the parallel mode is also specified
     */
    int threadCount = 1

    /**
     * Whether the default listeners and reporters should be used.
     * Since Gradle 1.4 it defaults to 'false' so that Gradle can own the reports generation and provide various improvements.
     * This option might be useful for advanced TestNG users who prefer the reports generated by the TestNG library.
     * If you cannot live without some specific TestNG reporter please use {@link #listeners} property.
     * If you really want to use all default TestNG reporters (e.g. generate the old reports):
     *
     * <pre autoTested=''>
     * apply plugin: 'java'
     *
     * test {
     *   useTestNG() {
     *     //report generation delegated to TestNG library:
     *     useDefaultListeners = true
     *   }
     *
     *   //turn off Gradle's HTML report to avoid replacing the
     *   //reports generated by TestNG library:
     *   reports.html.enabled = false
     * }
     * </pre>
     *
     * Please refer to the documentation of your version of TestNG what are the default listeners.
     * At the moment of writing this documentation, the default listeners are a set of reporters that generate:
     * TestNG variant of HTML results, TestNG variant of XML results in JUnit format, emailable HTML test report,
     * XML results in TestNG format.
     *
     */
    boolean useDefaultListeners = false

    /**
     * Sets the default name of the test suite, if one is not specified in a suite XML file or in the source code.
     */
    String suiteName = 'Gradle suite'

    /**
     * Sets the default name of the test, if one is not specified in a suite XML file or in the source code.
     */
    String testName = 'Gradle test'

    /**
     * The suiteXmlFiles to use for running TestNG.
     *
     * Note: The suiteXmlFiles can be used in conjunction with the suiteXmlBuilder.
     */
    List<File> suiteXmlFiles = []

    transient StringWriter suiteXmlWriter = null
    transient MarkupBuilder suiteXmlBuilder = null
    private final File projectDir

    TestNGOptions(File projectDir) {
        this.projectDir = projectDir
    }

    void setAnnotationsOnSourceCompatibility(JavaVersion sourceCompatibilityProp) {
        if (sourceCompatibilityProp >= JavaVersion.VERSION_1_5) {
            jdkAnnotations()
        } else {
            javadocAnnotations()
        }
    }

    MarkupBuilder suiteXmlBuilder() {
        suiteXmlWriter = new StringWriter()
        suiteXmlBuilder = new MarkupBuilder(suiteXmlWriter)
        suiteXmlBuilder
    }

    /**
     * Add suite files by Strings. Each suiteFile String should be a path relative to the project root.
     */
    void suites(String... suiteFiles) {
        suiteFiles.each {
            suiteXmlFiles.add(new File(projectDir, it))
        }
    }

    /**
     * Add suite files by File objects.
     */
    void suites(File... suiteFiles) {
        suiteXmlFiles.addAll(Arrays.asList(suiteFiles))
    }

    List<File> getSuites(File testSuitesDir) {
        List<File> suites = []

        suites.addAll(suiteXmlFiles)

        if (suiteXmlBuilder != null) {
            File buildSuiteXml = new File(testSuitesDir.absolutePath, "build-suite.xml")

            if (buildSuiteXml.exists()) {
                if (!buildSuiteXml.delete()) {
                    throw new RuntimeException("failed to remove already existing build-suite.xml file")
                }
            }

            buildSuiteXml.append('<!DOCTYPE suite SYSTEM "http://testng.org/testng-1.0.dtd">')
            buildSuiteXml.append(suiteXmlWriter.toString())

            suites.add(buildSuiteXml);
        }

        return suites
    }

    TestNGOptions jdkAnnotations() {
        javadocAnnotations = false
        this
    }

    TestNGOptions javadocAnnotations() {
        javadocAnnotations = true
        this
    }

    TestNGOptions includeGroups(String... includeGroups) {
        this.includeGroups.addAll(Arrays.asList(includeGroups))
        this
    }

    TestNGOptions excludeGroups(String... excludeGroups) {
        this.excludeGroups.addAll(Arrays.asList(excludeGroups))
        this
    }

    TestNGOptions useDefaultListeners() {
        useDefaultListeners = true
        this
    }

    TestNGOptions useDefaultListeners(boolean useDefaultListeners) {
        this.useDefaultListeners = useDefaultListeners
        this
    }

    Object propertyMissing(String name) {
        if (suiteXmlBuilder != null) {
            return suiteXmlBuilder.getMetaClass()."${name}"
        }
        throw new MissingPropertyException(name, getClass());
    }

    Object methodMissing(String name, Object args) {
        if (suiteXmlBuilder != null) {
            return suiteXmlBuilder.getMetaClass().invokeMethod(suiteXmlBuilder, name, args)
        }
        throw new MissingMethodException(name, getClass(), args)
    }
}
