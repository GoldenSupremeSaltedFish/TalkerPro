package org.example.payoff.entiy;

/**
 * @author HumphreyLi
 * @version 1.0
 * @brief 账户支付实体类
 * @file null.java
 * @details
 * 这个类用于表示账户支付信息，包括支付ID、账单ID、账户ID、支付方式、支付金额、支付状态、创建时间和更新时间。
 * @date 2024/11/21
 * @time 下午10:23
 */


import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.sql.Timestamp;



@Getter
@Setter
public class AccountPayment {
    private Long id;
    private Long billId;
    private Long accountId;
    private String paymentMethod;
    private BigDecimal amount;
    private String paymentStatus;
    private Timestamp createdAt;
    private Timestamp updatedAt;
}


