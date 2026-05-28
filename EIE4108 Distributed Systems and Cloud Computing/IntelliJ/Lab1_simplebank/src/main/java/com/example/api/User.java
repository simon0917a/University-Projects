package com.example.api;

import java.util.ArrayList;
import java.util.List;

public class User {
  private String fullname;
  private String userId;
  private String password;
  private double balance;
  private List<String> transactionHistory;
  
  public User(String userId, String password, String fullname, double balance) {
    this.userId = userId;
    this.password = password;
    this.fullname = fullname;
    this.balance = balance;
    this.transactionHistory = new ArrayList<>();
  }
  
  // Getters and Setters
  public String getFullname() { return this.fullname; }
  public String getUserId() { return this.userId; }
  public String getPassword() { return this.password; }
  public double getBalance() { return this.balance; }

  public List<String> getTransactionHistory() {
    return this.transactionHistory;
  }

  public void setBalance(double balance) {
    this.balance = balance;
  }

  public void addTransaction(String message) {
    this.transactionHistory.add(message);
  }
}
