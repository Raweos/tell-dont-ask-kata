package it.gabrieletondi.telldontaskkata.useCase;

import it.gabrieletondi.telldontaskkata.domain.Order;
import it.gabrieletondi.telldontaskkata.domain.OrderApprover;
import it.gabrieletondi.telldontaskkata.repository.OrderRepository;

class OrderApprovalUseCase {
    private final OrderRepository orderRepository;

    OrderApprovalUseCase(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    void run(OrderApprovalRequest request) {
        final Order order = orderRepository.getById(request.getOrderId());
        final OrderApprover orderApprover = new OrderApprover(request, order);
        orderApprover.requestForApprove();
        orderRepository.save(order);
    }
}
