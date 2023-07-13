package com.order.system.services.impl;

import com.order.system.enums.OrderStatus;
import com.order.system.exceptions.DriverPatchOrderStatusException;
import com.order.system.repositories.OrderRepository;
import com.order.system.repositories.OrderStatusOperationLockRepository;
import com.order.system.repositories.entities.OrderEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.params.provider.Arguments.arguments;

@ExtendWith(MockitoExtension.class)
public class DriverServiceImplTest {
    @Mock
    private OrderRepository orderRepository;

    @Mock
    private OrderStatusOperationLockRepository orderStatusOperationLockRepository;

    @InjectMocks
    DriverServiceImpl driverService;

    @Test
    public void driverPatchOrderStatusTest_SuccessReturn() {

        String orderUuid = "123";
        OrderStatus orderStatus = OrderStatus.TAKEN;

        Mockito.when(orderStatusOperationLockRepository.insertOrderUuid(orderUuid)).thenReturn(null);
        Mockito.doNothing().when(orderStatusOperationLockRepository).deleteById(orderUuid);

        Mockito.when(orderRepository.findById(orderUuid)).thenReturn(Optional.of(OrderEntity.builder().orderStatus("UNASSIGNED").build()));
        Mockito.lenient().when(orderRepository.updateOrderStatusById(orderUuid, orderStatus.name())).thenReturn(1);

        assertDoesNotThrow(() -> driverService.driverPatchOrderStatus(orderUuid, orderStatus));

    }
    private static Stream<Arguments> driverPatchOrderStatusTestProvider() {
        return Stream.of(
            arguments(Optional.empty(), 1, new DriverPatchOrderStatusException("Request Order does not exist.")),
            arguments(Optional.of(OrderEntity.builder().orderStatus("TAKEN").build()), 1, new DriverPatchOrderStatusException("Request Order is already taken.")),
            arguments(Optional.of(OrderEntity.builder().orderStatus("UNASSIGNED").build()), 0, new DriverPatchOrderStatusException("Fail to update request order. Please try again."))
        );
    }


    @ParameterizedTest
    @MethodSource("driverPatchOrderStatusTestProvider")
    public void driverPatchOrderStatusTest_throwDriverPatchOrderStatusException(Optional<OrderEntity> orderRecord, Integer successUpdateOrder, DriverPatchOrderStatusException expectException) {

        String orderUuid = "123";
        OrderStatus orderStatus = OrderStatus.TAKEN;

        Mockito.when(orderStatusOperationLockRepository.insertOrderUuid(orderUuid)).thenReturn(null);
        Mockito.doNothing().when(orderStatusOperationLockRepository).deleteById(orderUuid);

        Mockito.when(orderRepository.findById(orderUuid)).thenReturn(orderRecord);
        Mockito.lenient().when(orderRepository.updateOrderStatusById(orderUuid, orderStatus.name())).thenReturn(successUpdateOrder);

        Exception exception = assertThrows(DriverPatchOrderStatusException.class, () -> {
            driverService.driverPatchOrderStatus(orderUuid, orderStatus);
        });

        assertEquals(expectException.getMessage(), exception.getMessage());

    }

}
