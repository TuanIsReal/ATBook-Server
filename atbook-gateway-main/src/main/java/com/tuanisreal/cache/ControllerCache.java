package com.tuanisreal.cache;

import lombok.extern.slf4j.Slf4j;
import com.tuanisreal.constant.Constant;
import com.tuanisreal.controller.BaseAPI;
import com.tuanisreal.utils.StringUtil;

import java.util.Collection;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
public class ControllerCache {
    private static final Map<String, APIData> CONTROLLER_MAP = new ConcurrentHashMap<>();

    private static long lastTimePrintSize = System.currentTimeMillis();

    public static void add(String reqId, BaseAPI baseController) {
        APIData apiData = new APIData(reqId, baseController, System.currentTimeMillis());
        CONTROLLER_MAP.put(reqId, apiData);
    }

    public static void remove(String reqId) {
        CONTROLLER_MAP.remove(reqId);
        if (System.currentTimeMillis() - lastTimePrintSize >= Constant.A_MINUTE) {
            lastTimePrintSize = System.currentTimeMillis();
        }
    }

    public static BaseAPI get(String reqId) {
        if(!StringUtil.validateString(reqId)) {
            return null;
        }
        APIData apiData = CONTROLLER_MAP.get(reqId);
        if (Objects.nonNull(apiData)) {
            return apiData.getApi();
        }
        return null;
    }

    public static Collection<APIData> getAll() {
        return CONTROLLER_MAP.values();
    }
}
