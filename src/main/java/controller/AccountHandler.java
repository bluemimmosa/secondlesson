/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.sql.ResultSet;
import java.sql.SQLException;
import model.Account;
import java.util.ArrayList;

/**
 *
 * @author Primax
 */
public class AccountHandler {
    //private Account acc;
    private ArrayList<Account> acc;
    private DbConnection dbConn;
    

    public AccountHandler() {
        this.acc = new ArrayList<>();
        dbConn = new DbConnection();
    }
    public boolean createAccount(long accountNumber, String name, long mobileNo, double balance, char accountType){
        Account acco = new Account(accountNumber, name, mobileNo, balance, accountType);
        //this.acc.add(acco);
        String sql ="INSERT INTO `bankdb`.`account` (`accountNumber`, `name`, `mobileNumber`, `balance`, `accountType`) VALUES ('"+acco.getAccountNumber()+"', '"+acco.getName()+"', '"+acco.getMobileNo()+"', '"+acco.getAmount()+"', '"+acco.getAccountType()+"');";
      //  System.out.println(sql);
        return dbConn.iud(sql);
        //return true;
    }
    
    //deleteAccount business logic only.
    public boolean deleteAccount(Account ac){
        String sql = "DELETE FROM `account` WHERE (`accountNumber` = '"+ac.getAccountNumber()+"');";
        return dbConn.iud(sql);
    }
    
    public double deposit(Account ac, double balance){
        double tempAmount = ac.getAmount();
        tempAmount += balance;
        ac.setAmount(tempAmount);
        String sql = "UPDATE `account` SET `balance` = '"+tempAmount+"' WHERE (`accountNumber` = '"+ac.getAccountNumber()+"');";
        if(dbConn.iud(sql)){
            return ac.getAmount();
        }
        else{
            return -1;
        }     
    }
    
    /*
    public void deleteAccount(long accountNumber){
        Account ac = search(accountNumber);
        if(ac != null)
        {
            System.out.println("Account Details being Deleted: ");
            System.out.println(ac.toString());
            char choice;
            System.out.print("Are you sure you want to delete (Y/N)? ");
            Scanner sc = new Scanner(System.in);
            choice = sc.next().charAt(0);
            if(choice == 'Y' || choice == 'y'){
                for(int i=0; i<acc.size(); i++){
                    if(acc.get(i).getAccountNumber() == accountNumber){
                        acc.remove(i);
                        System.out.println("Account deleted.");
                        break;
                    }
                }
            }
            else{
                System.out.println("Account not deleted.");
            }
        }
        else{
            System.out.println("Account not found.");
        }
    }
    */
    /*
    public void deposit(long accountNumber, double balance){
        for(int i=0; i<acc.size(); i++){
            if(acc.get(i).getAccountNumber() == accountNumber){
                double tempAmount = acc.get(i).getAmount();
                tempAmount += balance;
                acc.get(i).setAmount(tempAmount);
                System.out.println("Now available balance is Rs. " + acc.get(i).getAmount());
                System.out.println("Money deposited successfully.");
                break;
            }        
        }
    }
    */
    public double withdraw(Account ac, double balance){
                double tempAmount = ac.getAmount();
                if(tempAmount < balance){
                    return -2;
                }
                tempAmount -= balance;
                ac.setAmount(tempAmount);
                String sql = "UPDATE `account` SET `balance` = '"+tempAmount+"' WHERE (`accountNumber` = '"+ac.getAccountNumber()+"');";
                if(dbConn.iud(sql)){
                    return ac.getAmount();
                }
                else{
                    return -1;
                }
    }
    
//    public void withdraw(long accountNumber, double balance){
//         for(int i=0; i<acc.size(); i++){
//            if(acc.get(i).getAccountNumber() == accountNumber){
//                double tempAmount = acc.get(i).getAmount();
//                if(tempAmount >= balance){
//                    tempAmount -= balance;
//                    acc.get(i).setAmount(tempAmount);
//                    System.out.println("Now available balance is Rs. " + acc.get(i).getAmount());
//                    System.out.println("Money withdrawn successfully.");
//                    break;
//                }
//                else{
//                    System.out.println("Account has no sufficient balance (Rs. "+acc.get(i).getAmount()+") to withdraw.");
//                }
//                
//            }        
//        }
//    }
//    
    public ArrayList<Account> searchName(String name){
//        ArrayList al = new ArrayList<Account>();
//        for(Account ac:acc){
//            if(ac.getName().equalsIgnoreCase(name)){
//                al.add(ac);
//            }
//        }
//        return al;
        String sql ="SELECT * FROM account WHERE name like \"%"+name+"%\";";
        ResultSet rs = dbConn.select(sql);
        ArrayList<Account> data = new ArrayList<>();
        try {
            while (rs.next()) {
                Account tmp = new Account(rs.getLong(1), rs.getString(2), rs.getLong(3), rs.getDouble(4), rs.getString(5).charAt(0));
                data.add(tmp);
            }
        } catch (SQLException sQLException) {
           return null;
        }
        return data;
    }
    
    public Account search(long accountNumber){
//        for(Account ac:acc){
//            if(ac.getAccountNumber() == accountNumber){
//                return ac;
//            }
//        }
        String sql ="SELECT * FROM account WHERE accountnumber ="+accountNumber+";";
        ResultSet rs = dbConn.select(sql);
        try{
            while(rs.next()){
                Account tmp = new Account(rs.getLong(1), rs.getString(2), rs.getLong(3), rs.getDouble(4), rs.getString(5).charAt(0));
                return tmp;
            }
        } catch (SQLException sQLException) {
           return null;
        }
        return null;
    }
    
    public boolean FundTransfer(Account srcAccount, Account trgtAccount, double amount){
        double fundA, fundB;
        fundA = srcAccount.getAmount();
        fundB = trgtAccount.getAmount();
        if(fundA < amount){
            return false;
        }
        fundB += amount;
        fundA -= amount;
        String sql1 = "UPDATE `account` SET `balance` = '"+fundA+"' WHERE (`accountNumber` = '"+srcAccount.getAccountNumber()+"');";
        String sql2 = "UPDATE `account` SET `balance` = '"+fundB+"' WHERE (`accountNumber` = '"+trgtAccount.getAccountNumber()+"');";
        return (dbConn.iud(sql1) && dbConn.iud(sql2));
        
        //return true;
    }
    public ArrayList<Account> listAll(){
        String sql ="SELECT * FROM account;";
        ResultSet rs = dbConn.select(sql);
        ArrayList<Account> data = new ArrayList<>();
        try {
            while (rs.next()) {
                Account tmp = new Account(rs.getLong(1), rs.getString(2), rs.getLong(3), rs.getDouble(4), rs.getString(5).charAt(0));
                data.add(tmp);
            }
        } catch (SQLException sQLException) {
           return null;
        }
        return data;
    }
}
