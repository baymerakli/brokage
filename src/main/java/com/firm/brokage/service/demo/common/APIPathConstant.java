package com.firm.brokage.service.demo.common;

public class APIPathConstant {

    // REF_IDs
    public static final String CUSTOMER_REF_ID = "customerId";
    public static final String ORDER_REF_ID = "orderId";

    // Assets
    public static final String ASSETS_LIST_URL = "/assets";

    // Customers
    public static final String CUSTOMERS_BASE_URL = "/customers";
    public static final String CUSTOMER_WITHDRAW_URL = CUSTOMERS_BASE_URL + "/{" + CUSTOMER_REF_ID + "}/withdraw";
    public static final String CUSTOMER_DEPOSIT_URL = CUSTOMERS_BASE_URL + "/{" + CUSTOMER_REF_ID + "}/deposit";

    // Orders
    public static final String ORDERS_BASE_URL = "/orders";
    public static final String GET_ORDER_URL = ORDERS_BASE_URL + "/{" + ORDER_REF_ID + "}";
    public static final String CREATE_ORDER_URL = ORDERS_BASE_URL;
    public static final String LIST_ORDERS_URL = ORDERS_BASE_URL;
    public static final String DELETE_ORDER_URL = ORDERS_BASE_URL + "/{" + ORDER_REF_ID + "}";
}
