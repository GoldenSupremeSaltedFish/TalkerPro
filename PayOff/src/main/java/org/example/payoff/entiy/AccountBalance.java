package org.example.payoff.entiy;


import lombok.Getter;
import lombok.Setter;
/**
 * @author HumphreyLi
 * @version 1.0
 * @brief 账户余额实体类
 * @details 这个类用于表示账户余额信息，包括账户ID、剩余点数、最后交易ID以及更新时间
 * @date 2024/11/21
 * @time 下午10:32
 */
@Getter
@Setter
public class AccountBalance {
    private Long accountId;
    private int remainingPoints;
    private Long lastpayid;
    private java.sql.Timestamp updatedAt;
}
