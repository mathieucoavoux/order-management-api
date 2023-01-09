package fr.comprehensiveit.samples.om.response;

import fr.comprehensiveit.samples.om.model.Order;

import java.util.List;

public class OrdersResponse extends Response{

    public List<String> getOrders() {
        return orders;
    }

    public void setOrders(List<String> orders) {
        this.orders = orders;
    }

    public List<String> orders;
}
