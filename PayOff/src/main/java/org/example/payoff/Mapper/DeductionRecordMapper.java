package org.example.payoff.Mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.example.payoff.entiy.DeductionRecord;

@Mapper
public interface DeductionRecordMapper {
    @Select("SELECT * FROM deduction_record WHERE user_id = #{userId}")
    public DeductionRecord getDeductionRecordByUserId(String userId);

    @Insert("INSERT INTO deduction_record (user_id, deduction_points, deduction_method, created_at) " +
            "VALUES (#{userId}, #{deductionPoints}, #{deductionMethod}, #{createdAt})")
    public void insertPaypoint(DeductionRecord deductionRecord);
}