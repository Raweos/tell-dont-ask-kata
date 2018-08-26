package it.gabrieletondi.telldontaskkata.domain;

import it.gabrieletondi.telldontaskkata.useCase.SellItemRequest;

import java.math.BigDecimal;

import static java.math.BigDecimal.valueOf;
import static java.math.RoundingMode.HALF_UP;

public class OrderItem {
    private Product product;
    private int quantity;
    private BigDecimal taxedAmount;
    private BigDecimal tax;

    public void createItem(SellItemRequest itemRequest, Product product){
        final BigDecimal unitaryTax = product.getPrice().divide(valueOf(100)).multiply(product.getCategory().getTaxPercentage()).setScale(2, HALF_UP);
        final BigDecimal unitaryTaxedAmount = product.getPrice().add(unitaryTax).setScale(2, HALF_UP);
        final BigDecimal taxedAmount = unitaryTaxedAmount.multiply(BigDecimal.valueOf(itemRequest.getQuantity())).setScale(2, HALF_UP);
        final BigDecimal taxAmount = unitaryTax.multiply(BigDecimal.valueOf(itemRequest.getQuantity()));

        this.product = product;
        this.quantity = itemRequest.getQuantity();
        this.tax = taxAmount;
        this.taxedAmount = taxedAmount;
    }

    public Product getProduct() {
        return product;
    }

    public int getQuantity() {
        return quantity;
    }

    public BigDecimal getTaxedAmount() {
        return taxedAmount;
    }

    public BigDecimal getTax() {
        return tax;
    }
}
