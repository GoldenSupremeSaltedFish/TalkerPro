package org.example.payoff.Mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.example.payoff.entiy.AccountBalance;
import org.example.payoff.entiy.AccountPayment;
import org.example.payoff.entiy.DeductionRecord;

import java.sql.Timestamp;


@Mapper
public interface Account_balance {
    @Select("SELECT * FROM account_balance WHERE account_id = #{account_id}")
    public AccountBalance GetAllById(int account_id);

    @Update("UPDATE account_balance " +
            "SET remaining_points = #{remainingPoints}," +
            " last_transaction_id = #{lastTransactionId}, " +
            "updated_at = CURRENT_TIMESTAMP " +
            "WHERE account_id = #{accountId}")
    public void UpdateUserCount(AccountBalance accountBalance);

    @Select("select updated_at from account_balance where account_id = #{userid}")
    public Timestamp getLastTimeByUser(int userid);

    @Select("select last_transaction_id from account_balance where account_id = #{userid}")
    public Long getLastCountIdByUser(int userid);

    @Select("SELECT * FROM account_payment "
            +
            "WHERE bill_id =(SELECT last_payid "
            +
            "FROM deduction_record " +
            "WHERE account_id = #{userid})")
    public DeductionRecord getLastAcountPaymentByUser(int userid);

    @Insert("INSERT INTO account_balance (account_id=#{userid}, remaining_points=0, last_transaction_id=0, updated_at)")
    public void InsertNewAccount(int userid);


}
