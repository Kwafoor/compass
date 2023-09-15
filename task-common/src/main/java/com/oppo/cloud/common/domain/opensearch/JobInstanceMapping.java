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

package com.oppo.cloud.common.domain.opensearch;

import java.util.AbstractMap;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;


public class JobInstanceMapping extends Mapping {

    public static Map<String, Object> build(boolean sourceEnabled) {
        return Stream.of(
                new AbstractMap.SimpleEntry<>("properties", build()),
                new AbstractMap.SimpleEntry<>("_source", enabled(sourceEnabled)))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    public static Map<String, Object> build() {
        return Stream.of(
                /* 任务所属用户: [{userId: 23432, username: "someone"}] */
                new AbstractMap.SimpleEntry<>("users", users()),
                /* 项目名称 */
                new AbstractMap.SimpleEntry<>("projectName", text()),
                /* 项目ID */
                new AbstractMap.SimpleEntry<>("projectId", digit("integer")),
                /* 工作流名称 */
                new AbstractMap.SimpleEntry<>("flowName", text()),
                /* 工作流ID */
                new AbstractMap.SimpleEntry<>("flowId", digit("integer")),
                /* 任务名称 */
                new AbstractMap.SimpleEntry<>("taskName", text()),
                /* 任务ID */
                new AbstractMap.SimpleEntry<>("taskId", digit("integer")),
                /* 整个工作流开始执行时间 */
                new AbstractMap.SimpleEntry<>("executionDate", date()),
                /* 任务开始执行时间 */
                new AbstractMap.SimpleEntry<>("startTime", date()),
                /* 任务结束执行时间 */
                new AbstractMap.SimpleEntry<>("endTime", date()),
                /* 任务执行耗时 */
                new AbstractMap.SimpleEntry<>("duration", digit("double")),
                /* 任务执行状态: success、fail */
                new AbstractMap.SimpleEntry<>("taskState", text()),
                /* 运行所需要application memory·second */
                new AbstractMap.SimpleEntry<>("memorySeconds", digit("double")),
                /* 运行所需要application vcore·second */
                new AbstractMap.SimpleEntry<>("vcoreSeconds", digit("double")),
                /* 任务类型： SHELL, PYTHON, SPARK */
                new AbstractMap.SimpleEntry<>("taskType", text()),
                /* 失败重试次数 */
                new AbstractMap.SimpleEntry<>("retryTimes", digit("integer")),
                /* 创建时间 */
                new AbstractMap.SimpleEntry<>("createTime", date()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }
}
