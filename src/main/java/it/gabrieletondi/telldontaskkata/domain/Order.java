package it.gabrieletondi.telldontaskkata.domain;

import it.gabrieletondi.telldontaskkata.useCase.OrderApprovalRequest;
import it.gabrieletondi.telldontaskkata.useCase.SellItemRequest;
import it.gabrieletondi.telldontaskkata.useCase.UnknownProductException;

import java.math.BigDecimal;
import java.util.List;

import static it.gabrieletondi.telldontaskkata.domain.OrderStatus.SHIPPED;

public class Order {
    private BigDecimal total;
    private String currency;
    private List<OrderItem> items;
    private BigDecimal tax;
    private OrderStatus status;
    private int id;

    public Order() {
    }

    public Order(OrderStatus status, int id) {
        this.status = status;
        this.id = id;
    }

    public Order(BigDecimal total, String currency, List<OrderItem> items, BigDecimal tax, OrderStatus status, int id) {
        this.total = total;
        this.currency = currency;
        this.items = items;
        this.tax = tax;
        this.status = status;
        this.id = id;
    }

    public void addOrderItem(SellItemRequest itemRequest, Product product) {
        if (product == null) {
            throw new UnknownProductException();
        } else {
            final OrderItem orderItem = new OrderItem(itemRequest, product);
            this.items.add(orderItem);
            addTaxesFromOrderItem(orderItem);
        }
    }

    private void addTaxesFromOrderItem(OrderItem orderItem) {
        this.total = this.total.add(orderItem.getTaxedAmount());
        this.tax = this.tax.add(orderItem.getTax());
    }

    void approve(OrderApprovalRequest request) {
        this.status = request.isApproved() ? OrderStatus.APPROVED : OrderStatus.REJECTED;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public String getCurrency() {
        return currency;
    }

    public List<OrderItem> getItems() {
        return items;
    }

    public BigDecimal getTax() {
        return tax;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public int getId() {
        return id;
    }

    void markAsShipped() {
        this.status = SHIPPED;
    }
}
