package cn.easier.testswitch;

import com.google.gson.annotations.SerializedName;

/**
 * Created by wgy on 2017/4/25.
 */

class TradeWebPayResponse {


    @SerializedName("code")
    private String code;

    @SerializedName("message")
    private String msg;

    @SerializedName("data")
    private DataResponse data;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public DataResponse getData() {
        return data;
    }

    public void setData(DataResponse data) {
        this.data = data;
    }
}
