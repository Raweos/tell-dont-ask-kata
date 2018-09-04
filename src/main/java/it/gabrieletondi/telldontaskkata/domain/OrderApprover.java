package it.gabrieletondi.telldontaskkata.domain;

import it.gabrieletondi.telldontaskkata.useCase.ApprovedOrderCannotBeRejectedException;
import it.gabrieletondi.telldontaskkata.useCase.OrderApprovalRequest;
import it.gabrieletondi.telldontaskkata.useCase.RejectedOrderCannotBeApprovedException;
import it.gabrieletondi.telldontaskkata.useCase.ShippedOrdersCannotBeChangedException;

public class OrderApprover {
    private OrderApprovalRequest request;
    private Order order;

    public OrderApprover(OrderApprovalRequest request, Order order) {
        this.request = request;
        this.order = order;
    }

    public void requestForApprove() {
        validateRequestForApprove();
        this.order.approve(this.request);
    }

    private void validateRequestForApprove() {
        if (this.order.getStatus().equals(OrderStatus.SHIPPED)) {
            throw new ShippedOrdersCannotBeChangedException();
        }
        if (this.request.isApproved() && this.order.getStatus().equals(OrderStatus.REJECTED)) {
            throw new RejectedOrderCannotBeApprovedException();
        }
        if (!this.request.isApproved() && this.order.getStatus().equals(OrderStatus.APPROVED)) {
            throw new ApprovedOrderCannotBeRejectedException();
        }
    }
}
