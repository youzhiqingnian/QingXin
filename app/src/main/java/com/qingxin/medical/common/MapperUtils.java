package com.qingxin.medical.common;

import com.vlee78.android.vl.VLUtils;
import org.codehaus.jackson.map.ObjectMapper;

public class MapperUtils {

    private static final ObjectMapper mObjectMapper = new ObjectMapper();

    public static ObjectMapper getObjectMapper() {
        return mObjectMapper;
    }

    public static String toJson(Object object) {
        try {
            return mObjectMapper.writeValueAsString(object);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String stringMd5(Object object){
        return VLUtils.stringMd5(toJson(object));
    }

    public static boolean isChanged(Object object, Object object1) {
        return isChanged(VLUtils.stringMd5(toJson(object)), VLUtils.stringMd5(toJson(object1)));
    }

    public static boolean isChanged(String startMd5, String endMd5) {
        return !VLUtils.equals(startMd5, endMd5);
    }
}
