package org.example.payoff.Mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.example.payoff.entiy.AccountBalance;
import org.example.payoff.entiy.AccountPayment;

@Mapper
public interface Account_payment {
    @Select("SELECT *FROM  account_balance where account_id = #{account_id}")
    public AccountBalance getAccountPaymentByAccountId(String account_id);

    @Insert("INSERT into account_payment " +
            "(bill_id," +
            "account_id,payment_method,amount,payment_status,created_at,updated_at) VALUES (#{bill_id},#{account_id},#{payment_method},#{amount},#{payment_status},#{created_at},#{updated_at})")
    public void insertAccountPayment(AccountPayment account_payment);


}
