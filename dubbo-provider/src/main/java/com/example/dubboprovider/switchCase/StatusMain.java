package com.example.dubboprovider.switchCase;


public class StatusMain {

    public static void main(String[] args) throws ClassNotFoundException, IllegalAccessException, InstantiationException {
//        OrderStatusBase orderStatusBase = OrderStatusBase.create(2);
//        String status = orderStatusBase.getStatus();
//        System.err.println(status);


        Class<?> aClass = Class.forName("com.example.dubboprovider.switchCase.OrderStatus1");
        OrderStatus orderStatus = (OrderStatus)aClass.newInstance();

        System.out.println(orderStatus.getStatus());
        System.out.println(StatusMain.class.getPackage().getName());
    }


}
