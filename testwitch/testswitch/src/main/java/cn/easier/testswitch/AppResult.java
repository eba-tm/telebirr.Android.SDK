package cn.easier.testswitch;

/**
 * 功能描述
 *
 * @author liudo
 * @version [V8.5.70.1, 2021/06/22]
 * @since V8.5.70.1
 */
public class AppResult {
    public int getTradeStatus() {
        return tradeStatus;
    }

    public void setTradeStatus(int tradeStatus) {
        this.tradeStatus = tradeStatus;
    }

    public String getOutTradeNo() {
        return outTradeNo;
    }

    public void setOutTradeNo(String outTradeNo) {
        this.outTradeNo = outTradeNo;
    }

    public String getTradeNo() {
        return tradeNo;
    }

    public void setTradeNo(String tradeNo) {
        this.tradeNo = tradeNo;
    }

    private int tradeStatus;

    private String outTradeNo;

    private String tradeNo;

}
