package org.example.payoff.Mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.example.payoff.entiy.DeductionRecord;

@Mapper
public interface Deduction_record {
    @Select("SELECT * FROM deduction_record WHERE userid = #{userid}")
    public String getDeductionRecordByUserId(String userid);

    @Insert("INSERT into deduction_record ( userid, deduction_points, deduction_method, created_at) VALUES (#{userid}, #{deduction_points}, #{deduction_method}, #{created_at})")
    public void InsertPaypoint(DeductionRecord deduction_record);
}

