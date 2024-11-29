package org.example.payoff.entiy;

import jdk.jfr.Timestamp;
import lombok.Getter;
import lombok.Setter;

/**
 * @author HumphreyLi
 * @version 1.0
 * @brief 扣费记录实体类
 * @details 这个类用于表示扣费记录信息，包括扣费记录ID、账户ID、扣费点数、扣费方式、创建时间。
 * @date 2024/11/21
 * @time 下午10:32
 */

@Getter
@Setter
public class DeductionRecord {
    private Long id;
    private Long accountId;
    private int deductionPoints;
    private String deductionMethod;
    private Timestamp createdAt;
}
