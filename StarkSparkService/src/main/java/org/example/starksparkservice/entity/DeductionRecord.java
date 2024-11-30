package org.example.starksparkservice.entity;

import lombok.Getter;
import lombok.Setter;

/**
 * @author HumphreyLi
 * @version 1.0
 * @brief
 * @details
 * @date 2024/11/30
 * @time 下午5:09
 */
@Getter
@Setter
public class DeductionRecord {
    private String Userid;

    private int deductionpoints;
    private String deductionmethod;
    public DeductionRecord(String Userid, int deductionpoints, String deductionmethod)
    {
        this.Userid = Userid;
        this.deductionpoints = deductionpoints;
        this.deductionmethod = deductionmethod;
    }

}
