package org.example.payoff.Mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.example.payoff.entiy.AccountBalance;
import org.example.payoff.entiy.DeductionRecord;

import java.sql.Timestamp;

@Mapper
public interface AccountBalanceMapper {
    @Select("SELECT * FROM account_balance WHERE account_id = #{accountId}")
    public AccountBalance getAllByAccountId(int accountId);

    @Update("UPDATE account_balance " +
            "SET remaining_points = #{remainingPoints}," +
            " last_transaction_id = #{lastTransactionId}, " +
            "updated_at = CURRENT_TIMESTAMP " +
            "WHERE account_id = #{accountId}")
    public void updateUserCount(AccountBalance accountBalance);

    @Select("select updated_at from account_balance where account_id = #{userId}")
    public Timestamp getLastTimeByUser(int userId);

    @Select("select last_transaction_id from account_balance where account_id = #{userId}")
    public Long getLastTransactionIdByUser(int userId);

    @Select("SELECT * FROM account_payment " +
            "WHERE bill_id =(SELECT last_payid " +
            "FROM deduction_record " +
            "WHERE account_id = #{userId})")
    public DeductionRecord getLastAccountPaymentByUser(int userId);

    @Insert("INSERT INTO account_balance (account_id, remaining_points, last_transaction_id, updated_at) " +
            "VALUES (#{accountId}, 0, 0, CURRENT_TIMESTAMP)")
    public void insertNewAccount(int accountId);
}