CREATE TABLE account_payment (
                                 id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                 bill_id BIGINT NOT NULL,
                                 account_id BIGINT NOT NULL,
                                 payment_method VARCHAR(50) NOT NULL,
                                 amount DECIMAL(10, 2) NOT NULL,
                                 payment_status VARCHAR(20) DEFAULT 'pending',
                                 created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                 updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                                 INDEX idx_bill_id (bill_id),         -- 为 bill_id 添加索引
                                 INDEX idx_account_id (account_id),   -- 为 account_id 添加索引
                                 INDEX idx_payment_method (payment_method)  -- 为 payment_method 添加索引
);



CREATE TABLE account_balance (
                                 account_id BIGINT PRIMARY KEY,
                                 remaining_points INT NOT NULL,
                                 last_transaction_id BIGINT,
                                 updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                                 INDEX idx_account_id (account_id)  -- 为 account_id 添加索引
);
-- auto-generated definition
create table deduction_record
(
    id               bigint auto_increment
        primary key,
    account_id       char                                not null,
    deduction_points int                                 not null,
    deduction_method varchar(50)                         not null,
    created_at       timestamp default CURRENT_TIMESTAMP null
);

create index idx_account_id
    on deduction_record (account_id);

create index idx_deduction_method
    on deduction_record (deduction_method);
