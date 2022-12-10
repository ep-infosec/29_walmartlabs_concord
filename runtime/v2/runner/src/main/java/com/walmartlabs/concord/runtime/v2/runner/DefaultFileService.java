package com.walmartlabs.concord.runtime.v2.runner;

/*-
 * *****
 * Concord
 * -----
 * Copyright (C) 2017 - 2020 Walmart Inc.
 * -----
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
 * =====
 */

import com.walmartlabs.concord.runtime.v2.sdk.FileService;
import com.walmartlabs.concord.runtime.v2.sdk.WorkingDirectory;
import com.walmartlabs.concord.sdk.Constants;

import javax.inject.Inject;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class DefaultFileService implements FileService {

    private final WorkingDirectory workingDirectory;

    @Inject
    public DefaultFileService(WorkingDirectory workingDirectory) {
        this.workingDirectory = workingDirectory;
    }

    @Override
    public Path createTempFile(String prefix, String suffix) throws IOException {
        return Files.createTempFile(baseTmpDir(), prefix, suffix);
    }

    @Override
    public Path createTempDirectory(String prefix) throws IOException {
        return Files.createTempDirectory(baseTmpDir(), prefix);
    }

    private Path baseTmpDir() throws IOException {
        Path p = workingDirectory.getValue().resolve(Constants.Files.CONCORD_TMP_DIR_NAME);
        Files.createDirectories(p);
        return p;
    }
}
