/*
 * Copyright 2017 the original author or authors.
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

package org.gradle.caching.internal.controller;

public class NoOpBuildCacheController implements BuildCacheController {

    public static final BuildCacheController INSTANCE = new NoOpBuildCacheController();

    private NoOpBuildCacheController() {
    }

    @Override
    public boolean isEnabled() {
        return false;
    }

    @Override
    public boolean isEmitDebugLogging() {
        return false;
    }

    @Override
    public <T> T load(BuildCacheLoadCommand<T> command) {
        return null;
    }

    @Override
    public void store(BuildCacheStoreCommand command) {

    }

    @Override
    public void close() {

    }

}
