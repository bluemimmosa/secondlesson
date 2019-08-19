/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view;

import model.Account;
import controller.AccountHandler;
import controller.DbConnection;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

/**
 *
 * @author Primax
 */
public class Launchpad {
    
    private String name;
    private long mobileNumber;
    private double balance;
    private char accountType;
    private long accountNumber;
    static AccountHandler ach = new AccountHandler();
    static DbConnection db;
    public void listAll(){
        ArrayList<Account> arr = ach.listAll();
        for(Account a:arr){
            System.out.println(a.toString());
        }
    }
    
    public void Launchpad(){
        db = new DbConnection();
    }
    //Delete account view with inputs being taken from user.
    public void deleteAcc(long accNo){
        Account aSrc = ach.search(accNo);
        if(aSrc == null){
            System.out.println("Account does not exist in system.");
        }
        else{
            System.out.println("Account Found.");
            System.out.println(aSrc.toString());
            boolean status = ach.deleteAccount(aSrc);
            if(status){
                System.out.println("Account Deleted Successfully.");
            }
            else{
                System.out.println("Accounts could not be deleted.");
            }
        }
    }
    
    //Deposit account view with inputs being taken from user.
    public void deposit(long accNo, double bal){
        Account aCnt = ach.search(accNo);
        if(aCnt == null){
            System.out.println("Account does not exist in system.");
        }
        else{
            System.out.println("Account Found.");
            System.out.println(aCnt.toString());
            double status = ach.deposit(aCnt, bal);
            if(status == -1){
                System.out.println("Cannot deposit to the account.");
            }
            else{
                System.out.println("Deposit successs. Current balance is Rs. "+status);
            }
        }
    }
    
    public void withdraw(long accNo, double bal){
        Account aCnt = ach.search(accNo);
        if(aCnt == null){
            System.out.println("Account does not exist in system.");
        }
        else{
            System.out.println("Account Found.");
            System.out.println(aCnt.toString());
            double status = ach.withdraw(aCnt, bal);
            if(status == -2){
                System.out.println("Insufficient balance in account.");
            }
            else if(status == -1){
                System.out.println("Cannot withdraw from account. Database problem.");
            }
            else{
                System.out.println("Withdraw Successful. Now balance is Rs. "+status);
            }
        }
    }
    
    public void createAcc(){
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter name: ");
        this.name = sc.nextLine();
        System.out.print("Enter Mobile Number: ");
        this.mobileNumber = sc.nextLong();
        System.out.print("Enter openening balance: ");
        this.balance = sc.nextDouble();
        System.out.println("Enter account type (S for savings, F for fixed and C for current: ");
        this.accountType = sc.next().charAt(0);
        
        //Random number generation codde.
        long x = 1000000001L;
        long y = 9999999999L;
        Random r = new Random();
        this.accountNumber = x+((long)(r.nextDouble()*(y-x)));
        
        //this.accountNumber = new Random().nextLong();
        if(ach.createAccount(accountNumber, name, mobileNumber, balance, accountType)){
        System.out.println("Accounts created sucessfully.");
        }
        else{
            System.out.println("Accounts cannot be created.");
        }
    }
    
    public static void main(String[] args){
        int choice;
        long accno;
        double bal;
        long srcAcc, trgtAcc;
        String name;
        //DbConnection db;
        Launchpad launch=new Launchpad();
        //db = new DbConnection();
        while(true){
            System.out.println("Welocme to XYZ Bank");
            System.out.println();
            System.out.println("1. Create Account");
            System.out.println("2. Delete Account");
            System.out.println("3. Withdraw money");
            System.out.println("4. Deposit Money");
            System.out.println("5. List all accounts");
            System.out.println("6. Search Accounts by Name");
            System.out.println("7. Inter Account Fund transfer");
            System.out.println("0. exit");
            System.out.println();
            System.out.print("Enter your choice: ");
        
            Scanner sc = new Scanner(System.in);
            choice = sc.nextInt();
        
            switch(choice){
                case 1:
                    launch.createAcc();
                    break;
                case 2:
                    System.out.println("Enter acc number:");
                    accno = sc.nextLong();
                    launch.deleteAcc(accno);
                    break;
                case 3:
                    // implement your own withdraw method.
                    System.out.println("Enter Account Number: ");
                    accno = sc.nextLong();
                    System.out.println("Enter amount to withdraw: ");
                    bal = sc.nextDouble();
                    launch.withdraw(accno, bal);
                    //ach.withdraw(accno, bal);
                    break;
                case 4:
                    // implement your own deposit method.
                    System.out.println("Enter Account Number: ");
                    accno = sc.nextLong();
                    System.out.println("Enter amount to deposit: ");
                    bal = sc.nextDouble();
                    launch.deposit(accno, bal);
                    break;
                
                case 5:
                    launch.listAll();
                    break;
                   
                case 6:
                    // implement name search functionality.
                    System.out.println("Enter Account Name to search: ");
                    sc.useDelimiter("\\s*\n\\s*"); // thi is used because sc.nextLine() cannot be used here.
                    name = sc.next();
                    ArrayList<Account> al = ach.searchName(name);
                    if(al.size() == 0){
                        System.out.println("Cannot find the particular account with the given name.");
                        break;
                    }
                    else{
                        for(Account a:al){
                        System.out.println(a.toString());
                        }
                    }
                    
                    break;
                
                case 7:
                    System.out.println("Enter Source Account number:");
                    srcAcc = sc.nextLong();
                    Account aSrc = ach.search(srcAcc);
                    if(aSrc == null){
                        System.out.println("Source account does not exist in system.");
                        break;
                    }
                    System.out.println("Enter Target Account number:");
                    trgtAcc = sc.nextLong();
                    Account aTrgt = ach.search(trgtAcc);
                    if(aTrgt == null){
                        System.out.println("Target account does not exist in system.");
                        break;
                    }
                    System.out.println("Enter amount to transfer: ");
                    bal = sc.nextDouble();
                    boolean result = ach.FundTransfer(aSrc, aTrgt, bal);
                    if(!result){
                        System.out.println("Insufficient balance is source account. Cannot transfer.");
                        break;
                    }
                    System.out.println("Fund transfered successfully.");
                    break;
                    
                case 0:
                    System.exit(0);
                    break;
                default:
                    System.out.println("Invalid choice");
            }
        }
    }
}
