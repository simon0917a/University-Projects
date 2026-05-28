package com.example.api;

import com.google.gson.Gson;
import jakarta.inject.Singleton;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.ArrayList;
import java.util.Objects;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


@Singleton
@Path("/bank")
/*
 * Endpoint: http://localhost:8080/bank
 * bank: path to this class
 */
public class SimpleBank {
    private ArrayList<User> database;
    public SimpleBank() {
        database = new ArrayList<>();
    }

    private User findUser(String userId) {
        for (User user : database) {
            if (user.getUserId().equals(userId)) {
                return user;
            }
        }
        return null;
    }

    @POST
    @Path("/create/{userId}/{password}/{fullname}/{balance}")
    public Response addUser(@PathParam("userId") String userId,
                            @PathParam("password") String password,
                            @PathParam("fullname") String fullname,
                            @PathParam("balance") double balance) {
        if (balance < 0) {
            return Response.status(400)
                    .entity("Status code 400: Deposit amount must be positive")
                    .build();
        }

        if (findUser(userId) != null) {
            return Response.status(400)
                    .entity("Status code 400: User ID already exists")
                    .build();
        }

        User newUser = new User(userId, password, fullname, balance);
        database.add(newUser);

        return Response.status(201)
                .entity("Status code 201: " + userId + " is added. There is a total of " + database.size() + " user(s) now.") // [cite: 86]
                .build();
    }


    /**
     * Retrieves account details for a specific user.
     * Endpoint: GET /users/{userId}
     */
    @GET
    @Path("/users/{userId}")
    public Response getUserInfo(
            @PathParam("userId") String userId,
            @QueryParam("x-pass") String password) {
        User user = findUser(userId);
        if (user == null) {
            return Response.status(404)
                    .entity("Status code 404: User not found")
                    .build();
        }

        if (password == null || !user.getPassword().equals(password)) {
            return Response.status(401)
                    .entity("Status code 401: Incorrect password")
                    .build();
        }

        return Response.status(200)
                .entity("Status code 200: " + user.getFullname() + "'s account has a balance of $" + user.getBalance())
                .build();
    }

    private String getTimestamp() {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }

    /**
   * Deposits funds into a user's account.
   * Endpoint: POST /accounts/{userId}/deposit
   */
  @POST
  @Path("/accounts/{userId}/deposit")
  public Response deposit(
          @PathParam("userId") String userId,
          @QueryParam("x-pass") String password,
          @QueryParam("amount") double amount) {

      if (amount < 0) {
          return Response.status(400)
                  .entity("Status code 400: Deposit amount must be positive")
                  .build();
      }

      User user = findUser(userId);
      if (user == null) {
          return Response.status(404)
                  .entity("Status code 404: User not found")
                  .build();
      }

      if (password == null || !user.getPassword().equals(password)) {
          return Response.status(401)
                  .entity("Status code 401: Incorrect password")
                  .build();
      }

      user.setBalance(user.getBalance() + amount);
      user.addTransaction("[" + getTimestamp() + "] Deposited: $" + amount + ". New balance: $" + user.getBalance());

      return Response.status(200)
              .entity("Status code 200: Successfully deposited $" + amount + " to " + user.getFullname() + "'s account. The new balance is $" + user.getBalance())
              .build();
  }

  /**
   * Withdraws funds from a user's account.
   * Endpoint: POST /accounts/{userId}/withdraw
   */
  @POST
  @Path("/accounts/{userId}/withdraw")
    public Response withdraw(
          @PathParam("userId") String userId,
          @QueryParam("x-pass") String password,
          @QueryParam("amount") double amount) {

      if (amount < 0) {
          return Response.status(400)
                  .entity("Status code 400: Withdrawn amount must be positive")
                  .build();
      }

      User user = findUser(userId);
      if (user == null) {
          return Response.status(404)
                  .entity("Status code 404: User not found")
                  .build();
      }

      if (password == null || !user.getPassword().equals(password)) {
          return Response.status(401)
                  .entity("Status code 401: Incorrect password")
                  .build();
      }

      if (user.getBalance() < amount) {
          return Response.status(400)
                  .entity("Status code 400: Insufficient funds")
                  .build();
      }

      user.setBalance(user.getBalance() - amount);
      user.addTransaction("[" + getTimestamp() + "] Withdrew: $" + amount + ". New balance: $" + user.getBalance());

      return Response.status(200)
              .entity("Status code 200: Successfully withdrawn $" + amount + " from " + user.getFullname() + "'s account. The new balance is $" + user.getBalance())
              .build();
    }

  /**
   * Transfers funds between two user accounts.
   * Endpoint: POST /accounts/transfer
   */
  @POST
  @Path("/accounts/transfer")
  public Response transfer(
          @QueryParam("from") String fromId,
          @QueryParam("to") String toId,
          @QueryParam("x-pass") String password,
          @QueryParam("amount") double amount)  {

      if (amount < 0) {
          return Response.status(400).entity("Status code 400: Transfer amount must be positive").build();
      }

      User sender = findUser(fromId);
      User recipient = findUser(toId);
      if (sender == null || recipient == null) {
          return Response.status(404).entity("Status code 404: One or both users not found").build();
      }

      if (password == null || !sender.getPassword().equals(password)) {
          return Response.status(401).entity("Status code 401: Incorrect password").build();
      }

      if (sender.getBalance() < amount) {
          return Response.status(400).entity("Status code 400: Insufficient funds").build();
      }

      sender.setBalance(sender.getBalance() - amount);
      recipient.setBalance(recipient.getBalance() + amount);

      sender.addTransaction("[" + getTimestamp() + "] Transferred $" + amount + " to " + toId);
      recipient.addTransaction("[" + getTimestamp() + "] Received $" + amount + " from " + fromId);

      return Response.status(200)
              .entity("Status code 200: Successfully transferred $" + amount + " from " + fromId + " to " + toId)
              .build();
  }

    @GET
    @Path("/users/{userId}/history")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getHistory(
            @PathParam("userId") String userId,
            @QueryParam("x-pass") String password) {

        User user = findUser(userId);
        if (user == null) return Response.status(404).entity("User not found").build();
        if (password == null || !user.getPassword().equals(password)) {
            return Response.status(401).entity("Incorrect password").build();
        }

        java.util.Map<String, Object> responseData = new java.util.HashMap<>();
        responseData.put("balance", user.getBalance());
        responseData.put("history", user.getTransactionHistory());

        return Response.ok(new Gson().toJson(responseData)).build();
    }

    @POST
    @Path("/users/secure-query")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response getSecureUserInfo(String jsonPayload) {
        Gson gson = new Gson();

        User credentials = gson.fromJson(jsonPayload, User.class);

        User user = findUser(credentials.getUserId());
        if (user == null) return Response.status(404).entity("User not found").build();

        if (!user.getPassword().equals(credentials.getPassword())) {
            return Response.status(401).entity("Incorrect password").build();
        }

        return Response.ok("Secure Access: Balance is $" + user.getBalance()).build();
    }
}



