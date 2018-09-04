package it.gabrieletondi.telldontaskkata.domain;

import it.gabrieletondi.telldontaskkata.service.ShipmentService;
import it.gabrieletondi.telldontaskkata.useCase.OrderCannotBeShippedException;
import it.gabrieletondi.telldontaskkata.useCase.OrderCannotBeShippedTwiceException;

import static it.gabrieletondi.telldontaskkata.domain.OrderStatus.*;

public class ShipmentManager {
    private final ShipmentService shipmentService;
    private Order order;

    public ShipmentManager(Order order, ShipmentService shipmentService) {
        this.order = order;
        this.shipmentService = shipmentService;
    }

    public void orderShipment() {
        checkIfOrderValidToShip();
        this.shipmentService.ship(order);
        order.markAsShipped();
    }

    private void checkIfOrderValidToShip() {
        if (order.getStatus().equals(CREATED) || this.order.getStatus().equals(REJECTED)) {
            throw new OrderCannotBeShippedException();
        }

        if (this.order.getStatus().equals(SHIPPED)) {
            throw new OrderCannotBeShippedTwiceException();
        }
    }

}
