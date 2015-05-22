package com.ylt.plugin.vo;

/**
 * Created by ylt on 15/3/26.
 */
public class XmlItem {

    private String methodName;

    public String getCbMethodName() {
        return cbMethodName;
    }

    public void setCbMethodName(String cbMethodName) {
        this.cbMethodName = cbMethodName;
    }

    private String cbMethodName;

    private int type=0;//默认为0，没有回调；1-有cb回调；2-on回调

    private String[] params;//参数


    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String[] getParams() {
        return params;
    }

    public void setParams(String[] params) {
        this.params = params;
    }
}
