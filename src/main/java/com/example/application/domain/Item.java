package com.example.application.domain;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

public class Item {

    @NotEmpty
    private  String name = "";
    private  BigDecimal price;
    @NotNull
    private  Integer count;

    public String getName() {
        return name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public Integer getCount() {
        return count;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public Item() {
    }

    @Override
    public String toString() {
        return "Item{" +
                "name='" + name + '\'' +
                ", price=" + price +
                ", count=" + count +
                '}';
    }

    public Item(ItemBuilder builder) {
        this.name = builder.name;
        this.price = builder.price;
        this.count = builder.count;
    }

    public static class ItemBuilder {
        private String name;
        private BigDecimal price;
        private Integer count;


        public ItemBuilder withName(String name) {
            this.name = name;
            return this;
        }

        public ItemBuilder withPrice(BigDecimal price) {
            this.price = price;
            return this;
        }

        public ItemBuilder withCount(Integer count) {
            this.count = count;
            return this;
        }

        public Item build() {
            return new Item(this);
        }
    }
}
