package com.example.dubboprovider.switchCase;

/**
 * switch 重构方案 1
 */
public  abstract class OrderStatusBase {
    OrderStatusBase() {}
    /**
     * 1 -->审核
     * 2 --> 打印
     * @return
     */
   public  abstract String getStatus();


    static OrderStatusBase create(int status) {
        OrderStatusBase base = null;
        switch (status) {
            case 1:
                base = new CheckStatus();
                break;
            case 2:
                base = new PrintStatus();
                default:
        }
        return base;
    }


}
