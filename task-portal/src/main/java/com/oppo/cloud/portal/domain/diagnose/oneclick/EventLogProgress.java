/*
 * Copyright 2023 OPPO.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.oppo.cloud.portal.domain.diagnose.oneclick;

import lombok.Data;

@Data
public class EventLogProgress {

    private Long logSize;
    /**
     * status 0：processing 1：succeed 2：failed
     * (why integer value but string type?)
     */
    private String state;
    private String logPath;
    private String error;
    private Long time;
    private String appId;
    private String logType;

}
