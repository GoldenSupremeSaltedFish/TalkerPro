package org.example.starksparkservice.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;
import org.example.starksparkservice.entity.DeductionRecord;

/**
 * @author HumphreyLi
 * @version 1.0
 * @brief
 * @details
 * @date 2024/11/29
 * @time 下午3:35
 */
@Mapper
public interface DeductionRecordMapper {

    /**
     * 根据 account_id 扣减用户点数
     *
     * @param accountId 用户账户 ID
     * @param tokenUsed 扣减的点数
     * @return 更新的记录数
     */
    @Update("""
        UPDATE account_balance
        SET remaining_points = account_balance.remaining_points - #{tokenUsed}
        
        WHERE account_id = #{accountId}
          AND account_balance.remaining_points >= #{tokenUsed}
    """)
    void uodatepoint(@Param("accountId") String accountId, @Param("tokenUsed") int tokenUsed);

    /**
     * 插入一条新的扣费记录
     *
     *  accountId 用户账户 ID
     *  tokenUsed 扣减的点数
     *  deductionMethod 扣费方式
     *  插入的记录数
     */
    @Insert("INSERT INTO deduction_record (account_id, deduction_points, deduction_method) " +
            "VALUES (#{accountId}, #{deductionPoints}, #{deductionMethod})")
    void insertDeductionRecord(DeductionRecord record);
}