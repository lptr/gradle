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
package org.gradle.plugins.ide.idea.model

import org.gradle.api.JavaVersion
import spock.lang.Specification

/**
 * @author: Szczepan Faber, created at: 7/14/11
 */
class IdeaLanguageLevelTest extends Specification {

    def "formats language level in IDEA fancy format"() {
        expect:
        new IdeaLanguageLevel(JavaVersion.VERSION_1_3).formatted == "JDK_1_3"
        new IdeaLanguageLevel(JavaVersion.VERSION_1_4).formatted == "JDK_1_4"
        new IdeaLanguageLevel(JavaVersion.VERSION_1_5).formatted == "JDK_1_5"
        new IdeaLanguageLevel(JavaVersion.VERSION_1_6).formatted == "JDK_1_6"
        new IdeaLanguageLevel(JavaVersion.VERSION_1_7).formatted == "JDK_1_7"
    }
}
