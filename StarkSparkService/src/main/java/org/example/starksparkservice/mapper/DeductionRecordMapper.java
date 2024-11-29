package org.example.starksparkservice.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

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
        UPDATE users
        SET points = points - #{tokenUsed}
        WHERE account_id = #{accountId}
          AND points >= #{tokenUsed}
    """)
    int deductPoints(@Param("accountId") String accountId, @Param("tokenUsed") int tokenUsed);
}