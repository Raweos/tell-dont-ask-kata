package it.gabrieletondi.telldontaskkata.domain;

import it.gabrieletondi.telldontaskkata.service.ShipmentService;
import it.gabrieletondi.telldontaskkata.useCase.*;

import java.math.BigDecimal;
import java.util.List;

import static it.gabrieletondi.telldontaskkata.domain.OrderStatus.*;

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

    public void requestForApprove(OrderApprovalRequest request) {
        validateRequestForApprove(request);
        this.status = (request.isApproved() ? OrderStatus.APPROVED : OrderStatus.REJECTED);
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

    public void orderShipment(ShipmentService shipmentService) {
        checkIfOrderValidToShip();
        shipmentService.ship(this);
        this.status = OrderStatus.SHIPPED;

    }

    private void validateRequestForApprove(OrderApprovalRequest request) {
        if (this.status.equals(OrderStatus.SHIPPED)) {
            throw new ShippedOrdersCannotBeChangedException();
        }
        if (request.isApproved() && this.status.equals(OrderStatus.REJECTED)) {
            throw new RejectedOrderCannotBeApprovedException();
        }
        if (!request.isApproved() && this.status.equals(OrderStatus.APPROVED)) {
            throw new ApprovedOrderCannotBeRejectedException();
        }
    }

    private void checkIfOrderValidToShip() {
        if (this.status.equals(CREATED) || this.status.equals(REJECTED)) {
            throw new OrderCannotBeShippedException();
        }

        if (this.status.equals(SHIPPED)) {
            throw new OrderCannotBeShippedTwiceException();
        }
    }

    private void addTaxesFromOrderItem(OrderItem orderItem) {
        this.total = this.total.add(orderItem.getTaxedAmount());
        this.tax = this.tax.add(orderItem.getTax());
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
}
