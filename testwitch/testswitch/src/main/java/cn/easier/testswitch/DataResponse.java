package cn.easier.testswitch;

import com.google.gson.annotations.SerializedName;

class DataResponse {

   @SerializedName("toPayUrl")
   private String toPayUrl;

   public String getToPayUrl() {
       return toPayUrl;
   }

   public void setToPayUrl(String toPayUrl) {
       this.toPayUrl = toPayUrl;
   }
}
