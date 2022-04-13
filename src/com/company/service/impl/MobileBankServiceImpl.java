package com.company.service.impl;

import com.company.dto.SimCardReChargeDto;
import com.company.enumeration.Operator;
import com.company.model.Transaction;
import com.company.service.MobileBankService;
import com.company.service.TransactionService;
import java.sql.SQLException;


public class MobileBankServiceImpl implements MobileBankService {

    TransactionService transactionService = new TransactionServiceImpl();
    AccountServiceImpl accountService = new AccountServiceImpl();
    CardService cardService = new CardService();
    @Override
    public void simCardCharge(SimCardReChargeDto simCardReChargeDto) throws SQLException {

        String accountNumber =findAccountNumberWithCard(simCardReChargeDto.getCardNumber());
        if (accountNumber !=null) {

            Transaction transaction = new Transaction();

            transaction.setAmount(simCardReChargeDto.getAmount());
            transaction.setDebitAccountNumber(accountNumber);
            transaction.setDescription("خرید شارژ موبایل");
            if (simCardReChargeDto.getOperator() == Operator.MCI) {
                transaction.setCreditAccountNumber("000000812218758817");
            }
            if (simCardReChargeDto.getOperator() == Operator.IRANCELL) {
                transaction.setCreditAccountNumber("000000892958732245");
            }
            if (simCardReChargeDto.getOperator() == Operator.RIGHTEL) {
                transaction.setCreditAccountNumber("000000812218758817");
            }

            transactionService.doTransaction(transaction);
        }
        else {
            System.out.println("شماره کارتی با این مشخصات در دیتابیس ذخیره نشده است .");
        }
        }

    public String findAccountNumberWithCard(String cardNumber) throws SQLException {
        return  cardService.findAccountNumberWithCard(cardNumber);
    }

    @Override
    public void moneyTransferWithCard(String debitCardNumber, String creditCardNumber, long amount) throws Exception {
        Transaction transaction = new Transaction();
        try {
            cardService.findCard(debitCardNumber);
        }catch (Exception e){
            System.out.println("شماره کارت مبدا اشتباه میباشد");
        }
        try {
            cardService.findCard(creditCardNumber);
        }catch (Exception e){
            System.out.println("شماره کارت مقصد اشتباه میباشد .");
        }
        String debitAccountNumber = findAccountNumberWithCard(debitCardNumber);
        String creditAccountNumber = findAccountNumberWithCard(creditCardNumber);

      transaction.setAmount(amount);
      transaction.setDebitAccountNumber(debitAccountNumber);
      transaction.setCreditAccountNumber(creditAccountNumber);
      transaction.setDescription("انتقال کارت به کارت");
      transactionService.doTransaction(transaction);


    }
}
