package com.slozic.inventoryservice.event;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderPlacedEvent implements Serializable {

    private static final long serialVersionUID = -1138446817700416884L;

    @JsonProperty
    private String orderNumber;

    @Override
    public String toString() {
        return "OrderPlacedEvent{" +
                "orderNumber='" + orderNumber + '\'' +
                '}';
    }
}
