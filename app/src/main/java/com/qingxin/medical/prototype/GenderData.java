package com.qingxin.medical.prototype;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 性别数据
 *
 * @author zhikuo
 */
public class GenderData {

    private final static Map<Gender, GenderData> INNER_GENDER_MAP = new LinkedHashMap<>();
    private final static Map<Integer, GenderData> INNER_KEY_ID_GENDER_MAP = new LinkedHashMap<>();
    /**
     * 性别, key为枚举
     */
    public final static Map<Gender, GenderData> GENDER_MAP = Collections.unmodifiableMap(INNER_GENDER_MAP);
    /**
     * 性别, key为id
     */
    public final static Map<Integer, GenderData> KEY_ID_GENDER_MAP = Collections.unmodifiableMap(INNER_KEY_ID_GENDER_MAP);

    static {
        // 初始化数据
        initGenderData();
    }

    private int mId;
    private Gender mEnum;
    private String mName;

    private static void initGenderData() {
        INNER_GENDER_MAP.put(Gender.male, new GenderData(Gender.male, "男"));
        INNER_GENDER_MAP.put(Gender.female, new GenderData(Gender.female, "女"));
    }

    private GenderData(Gender mEnum, String mName) {
        this.mEnum = mEnum;
        this.mName = mName;
        this.mId = mEnum.ordinal();

        INNER_KEY_ID_GENDER_MAP.put(this.mId, this);
    }

    public int getId() {
        return mId;
    }

    public Gender getEnum() {
        return mEnum;
    }

    public String getName() {
        return mName;
    }

}
