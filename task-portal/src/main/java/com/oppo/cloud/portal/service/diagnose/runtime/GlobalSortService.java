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

package com.oppo.cloud.portal.service.diagnose.runtime;

import com.alibaba.fastjson2.JSONObject;
import com.oppo.cloud.common.constant.AppCategoryEnum;
import com.oppo.cloud.common.domain.eventlog.DataSkewAbnormal;
import com.oppo.cloud.common.domain.eventlog.DetectionStorage;
import com.oppo.cloud.common.domain.eventlog.DetectorResult;
import com.oppo.cloud.common.domain.eventlog.GlobalSortAbnormal;
import com.oppo.cloud.common.domain.eventlog.config.DetectorConfig;
import com.oppo.cloud.common.domain.eventlog.config.GlobalSortConfig;
import com.oppo.cloud.common.util.DateUtil;
import com.oppo.cloud.portal.domain.diagnose.Table;
import com.oppo.cloud.portal.domain.diagnose.runtime.GlobalSort;
import com.oppo.cloud.portal.util.MessageSourceUtil;
import com.oppo.cloud.portal.util.UnitUtil;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class GlobalSortService extends RunTimeBaseService<GlobalSort> {

    @Override
    public String getCategory() {
        return AppCategoryEnum.GLOBAL_SORT.getCategory();
    }

    @Override
    public GlobalSort generateData(DetectorResult detectorResult, DetectorConfig config) throws Exception {
        GlobalSort globalSort = new GlobalSort();
        List<GlobalSortAbnormal> globalSortAbnormalList = new ArrayList<>();
        for (JSONObject data : (List<JSONObject>) detectorResult.getData()) {
            globalSortAbnormalList.add(data.toJavaObject(GlobalSortAbnormal.class));
        }
        Table<GlobalSort.GlobalSortTable> globalSortTableTable = globalSort.getTable();
        List<GlobalSort.GlobalSortTable> globalSortTableList = globalSortTableTable.getData();
        globalSort.setAbnormal(detectorResult.getAbnormal() != null && detectorResult.getAbnormal());
        GlobalSortConfig conf = new GlobalSortConfig();
        List<String> infos = new ArrayList<>();
        for (GlobalSortAbnormal globalSortAbnormal : globalSortAbnormalList) {
            GlobalSort.GlobalSortTable globalSortTable = new GlobalSort.GlobalSortTable();
            globalSortTable.setJobId(String.valueOf(globalSortAbnormal.getJobId()));
            globalSortTable.setStageId(String.valueOf(globalSortAbnormal.getStageId()));
            globalSortTable.setTaskNum("1");
            globalSortTable.setDataOfColumns(UnitUtil.transferRows(globalSortAbnormal.getRecords()));
            globalSortTable.setDuration(DateUtil.timeSimplify(globalSortAbnormal.getDuration() / 1000.0));
            globalSortTableList.add(globalSortTable);
            conf = config.getGlobalSortConfig();
            infos.add(String.format(MessageSourceUtil.get("GLOBAL_SORT_CONCLUSION_INFO"),
                    globalSortAbnormal.getJobId(),
                    globalSortAbnormal.getStageId(), globalSortAbnormal.getTaskId(), globalSortTable.getDataOfColumns(),
                    globalSortTable.getDuration()));
        }
        globalSort.getVars().put("globalSortInfo", String.join(";", infos));
        globalSort.getVars().put("taskCount", String.valueOf(conf.getTaskCount() == null ? 1 : conf.getTaskCount()));
        globalSort.getVars().put("records",
                UnitUtil.transferRows(conf.getRecords() == null ? 50000000 : conf.getRecords()));
        globalSort.getVars().put("filterMin", String.valueOf(conf.getDuration() == null ? 30 : conf.getDuration() / (1000 * 60)));
        return globalSort;
    }

    @Override
    public String generateConclusionDesc(Map<String, String> thresholdMap) {
        return String.format(MessageSourceUtil.get("GLOBAL_SORT_CONCLUSION_DESC"), thresholdMap.get("taskCount"),
                thresholdMap.get("records"), thresholdMap.get("filterMin"));
    }

    @Override
    public String generateItemDesc() {
        return MessageSourceUtil.get("GLOBAL_SORT_ANALYSIS");
    }

    @Override
    public String getType() {
        return "table";
    }
}
