use orderSystem;

CREATE TABLE `deliver_order` (
    order_uuid VARCHAR(255) NOT NULL,
    order_status VARCHAR(255),
    origin_latitude VARCHAR(255) NOT NULL,
    origin_longitude VARCHAR(255) NOT NULL,
    destination_latitude VARCHAR(255) NOT NULL,
    destination_longitude VARCHAR(255) NOT NULL,
    total_distance INTEGER NOT NULL,
    create_date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    last_modified_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (order_uuid),
    KEY idx_order_status (order_status),
    KEY idx_create_date (create_date)
) ENGINE=InnoDB;

CREATE TABLE `deliver_order_status_operation_lock` (
    order_uuid VARCHAR(255) NOT NULL,
    create_date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (order_uuid),
    KEY idx_create_date (create_date)
) ENGINE=InnoDB;