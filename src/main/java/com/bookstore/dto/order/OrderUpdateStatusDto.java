package com.bookstore.dto.order;

import com.bookstore.model.Status;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderUpdateStatusDto {
    @NotNull
    private Status status;
}
