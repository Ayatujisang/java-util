package com.kk.model;

import org.apache.commons.lang.builder.ToStringBuilder;

import java.util.HashMap;
import java.util.Map;

public class ResultSupport implements IResult {

    private boolean isSuccess = false;

    private Map<String, Object> resultMap = new HashMap<String, Object>();

    public void setSuccess(boolean isSuccess) {
        this.isSuccess = isSuccess;
    }

    public boolean isSuccess() {
        return isSuccess;
    }

    public int getCode() {
        return getInt("code");
    }

    public String getMsg() {
        return getString("msg");
    }

    public void setResult(String paramKey, Object paramValue) {
        if (resultMap == null) {
            resultMap = new HashMap<String, Object>();
        }
        if (paramKey == null || paramKey.equals("")) {
            paramKey = DEFAULT_RESULT_KEY;
        }
        resultMap.put(paramKey, paramValue);
    }

    public void setDefaultResult(Object paramValue) {
        resultMap.put(DEFAULT_RESULT_KEY, paramValue);
    }

    public Object getResult(String paramKey) {
        if (resultMap == null) {
            return null;
        }
        return resultMap.get(paramKey);
    }

    public Map<String, Object> getResults() {
        return resultMap;
    }

    public Object getDefaultResult() {
        return resultMap.get(DEFAULT_RESULT_KEY);
    }

    public boolean containsResult(Object paramKey) {
        if (resultMap == null) {
            return false;
        }
        return resultMap.containsKey(paramKey);

    }

    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public String getString(String paramKey) {
        Object ret = getResult(paramKey);
        if (ret == null) {
            return null;
        }
        return ret.toString();
    }

    @Override
    public int getInt(String paramKey) {
        Object ret = getResult(paramKey);
        return Integer.valueOf(ret.toString());
    }

    public static IResult getResult(int code, String msg) {
        IResult check = new ResultSupport();
        if (code != 0) {
            check.setSuccess(false);
        } else {
            check.setSuccess(true);
        }
        check.setResult("code", code);
        check.setResult("msg", msg);
        return check;
    }

    @Override
    public double getDouble(String paramKey) {
        Object ret = getResult(paramKey);
        return Double.valueOf(ret.toString());
    }

}