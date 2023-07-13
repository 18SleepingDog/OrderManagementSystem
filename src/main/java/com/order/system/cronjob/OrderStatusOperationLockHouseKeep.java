package com.order.system.cronjob;

import com.order.system.repositories.OrderStatusOperationLockRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class OrderStatusOperationLockHouseKeep {

    @Autowired
    OrderStatusOperationLockRepository orderStatusOperationLockRepository;

    /**
     * Performs a housekeeping task to release orphan locks that have been held for more than 5 minutes
     * in the `deliver_order_status_operation_lock` table. This method is scheduled to run every 5 minutes
     * using a cron expression specified in the `@Scheduled` annotation.
     */
    @Transactional
    @Scheduled(cron = "0 */5 * * * *")
    public void orderStatusOperationLockHouseKeep(){
        log.info("[orderStatusOperationLockHouseKeep] - Start release orphan lock which more than 5 minutes in deliver_order_status_operation_lock table");
        orderStatusOperationLockRepository.deleteOrphanedKeyLocks();
    }
}
