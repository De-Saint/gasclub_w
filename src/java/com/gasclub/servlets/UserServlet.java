/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gasclub.servlets;

import com.gasclub.classes.BookingClass;
import com.gasclub.classes.UserClass;
import com.google.gson.Gson;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

/**
 *
 * @author Saint
 */
public class UserServlet extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            StringBuilder sb = new StringBuilder();
            try {
                BufferedReader br = request.getReader();
                String str = null;
                while ((str = br.readLine()) != null) {
                    sb.append(str);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            JSONParser parser = new JSONParser();
            JSONObject jsonParameter = null;
            try {
                jsonParameter = (JSONObject) parser.parse(sb.toString());
            } catch (org.json.simple.parser.ParseException e) {
                e.printStackTrace();
            }
            String caseType = (String) jsonParameter.get("type");
            String json = "";
            String json1 = "";
            String json2 = "";
            String result = "";
            switch (caseType) {
                case "Login": {
                    String Email_PhoneNumber = (String) jsonParameter.get("emailphone");
                    String Password = (String) jsonParameter.get("password");
                    String UserType = "";
                    HashMap<String, String> UserDetails = new HashMap<>();
                    int UserID = 0;
                    if (UserClass.checkEmailAddressOrPhoneNumberExist(Email_PhoneNumber)) {
                        UserID = UserClass.checkPasswordEmailMatch(Password, Email_PhoneNumber);
                        if (UserID != 0) {
                            UserType = UserClass.getUserType(UserID);
                            if (UserType.equals("User")) {
                                UserDetails = UserClass.GetCustomerDetails(UserID);
                                String code = "200";
                                json1 = new Gson().toJson(code);
                                json2 = new Gson().toJson(UserDetails);
                                json = "[" + json1 + "," + json2 + "]";
                            } else if (UserType.equals("Admin")) {
                                UserDetails = UserClass.GetAdminDetails(UserID);
                                String code = "200";
                                json1 = new Gson().toJson(code);
                                json2 = new Gson().toJson(UserDetails);
                                json = "[" + json1 + "," + json2 + "]";
                            }
                        } else {
                            result = "Incorrect Login Parameters";
                            String code = "400";
                            json1 = new Gson().toJson(code);
                            json2 = new Gson().toJson(result);
                            json = "[" + json1 + "," + json2 + "]";
                        }
                    } else {
                        result = "Email or Phone Number Entered Doesn't Exist";
                        String code = "400";
                        json1 = new Gson().toJson(code);
                        json2 = new Gson().toJson(result);
                        json = "[" + json1 + "," + json2 + "]";
                    }
                    break;
                }

                case "UserRegistration": {
                    String firstname = (String) jsonParameter.get("firstname");
                    String lastname = (String) jsonParameter.get("lastname");
                    String location = (String) jsonParameter.get("location");
                    String email = (String) jsonParameter.get("email");
                    String phone = (String) jsonParameter.get("phone");
                    String password = (String) jsonParameter.get("password");
                    String address = (String) jsonParameter.get("address");
                    String type = "User";
                    String question = (String) jsonParameter.get("question");
                    String answer = (String) jsonParameter.get("answer");
                    int locationid = Integer.parseInt(location);
                    java.sql.Date DateCreated = UserClass.CurrentDate();
                    int UserID = 0;
                    if (!UserClass.checkEmailAddressOrPhoneNumberExist(email)) {
                        if (!UserClass.checkEmailAddressOrPhoneNumberExist(phone)) {
                            UserID = UserClass.RegisterUser(firstname, lastname, email, phone, password, type, DateCreated, locationid);
                            if (UserID != 0) {
                                result = UserClass.RegisterCustomer(UserID, address);
                                UserClass.CreateRecovery(UserID, question, answer);
                                String msgbdy = "Your have successfully registered as a User of GasClub. Thank you for using GasClub, the safest and most trusted mobile platform to order for Cooking Gas";
                                UserClass.sendMemberMessage(UserID, msgbdy, "Account Creation", 1);
                            } else {
                                result = "Something went wrong while creating Account";
                                String code = "400";
                                json1 = new Gson().toJson(code);
                                json2 = new Gson().toJson(result);
                                json = "[" + json1 + "," + json2 + "]";
                            }
                        } else {
                            result = "Account with Phone Number already Exists";
                            String code = "400";
                            json1 = new Gson().toJson(code);
                            json2 = new Gson().toJson(result);
                            json = "[" + json1 + "," + json2 + "]";
                        }
                    } else {
                        result = "Account with Email Address already Exists";
                        String code = "400";
                        json1 = new Gson().toJson(code);
                        json2 = new Gson().toJson(result);
                        json = "[" + json1 + "," + json2 + "]";
                    }
                    String code = "200";
                    json1 = new Gson().toJson(code);
                    json2 = new Gson().toJson(result);
                    json = "[" + json1 + "," + json2 + "]";
                    break;
                }
                case "checkEmail": {
                    String typedEmail = (String) jsonParameter.get("email");
                    if (typedEmail.length() > 2) {
                        if (UserClass.checkEmailAddressOrPhoneNumberExist(typedEmail)) {
                            result = "Exists";
                            json = new Gson().toJson(result);
                        } else {
                            result = "Available";
                            json = new Gson().toJson(result);
                        }
                    } else {
                        break;
                    }
                    break;
                }
                case "getRecoveryDetails": {
                    String UserInput = (String) jsonParameter.get("email");
                    int userid = UserClass.checkEmailMatch(UserInput);
                    if (userid == 0) {
                        String code = "400";
                        json1 = new Gson().toJson(code);
                        json2 = new Gson().toJson("Invalid Email");
                        json = "[" + json1 + "," + json2 + "]";
                    } else {
                        HashMap<String, String> data = UserClass.GetUserRecoveryDetails(userid);
                        json = new Gson().toJson(data);
                    }
                    break;
                }
                case "ResetPassword": {
                    String UserInput = (String) jsonParameter.get("userid");
                    int userid = Integer.parseInt(UserInput);
                    String newpassword = (String) jsonParameter.get("password");
                    result = UserClass.UpdateUserPassword(userid, newpassword);
                    String body = "Password has been reset to " + newpassword;
                    UserClass.sendMemberMessage(userid, body, "Password Reset", 1);
                    String code = "200";
                    json1 = new Gson().toJson(code);
                    json2 = new Gson().toJson(result);
                    json = "[" + json1 + "," + json2 + "]";
                    break;
                }
                case "GetUserDeliveryDetails": {
                    String UserInput = (String) jsonParameter.get("userid");
                    int userid = Integer.parseInt(UserInput);
                    int locationid = UserClass.getUserLocationByID(userid);
                    HashMap<String, String> UserDet = UserClass.GetLocationName(locationid);
                    String DeliveryAddress = UserClass.getUserAddress(userid);
                    String discountpoint = BookingClass.getUserDisCountPointCount(userid);
                    int DiscountPoint = Integer.parseInt(discountpoint);
                    UserDet.put("DeliveryAddress", DeliveryAddress);
                    UserDet.put("DiscountPoint", "" + DiscountPoint);
                    json = new Gson().toJson(UserDet);
                    break;
                }
                case "UpdateProfile": {
                    String Mid = (String) jsonParameter.get("id");
                    String FirstName = (String) jsonParameter.get("firstname");
                    String LastName = (String) jsonParameter.get("lastname");
                    String PhoneNumber = (String) jsonParameter.get("phone");
                    String Password = (String) jsonParameter.get("password");

                    int id = Integer.parseInt(Mid);
                    if (FirstName != null) {
                        result = UserClass.UpdateFirstName(id, FirstName);
                    }
                    if (LastName != null) {
                        result = UserClass.UpdateLastName(id, LastName);
                    }
                    if (Password != null) {
                        result = UserClass.UpdateUserPassword(id, Password);
                    }
                    if (PhoneNumber != null) {
                        result = UserClass.UpdatePhone(id, PhoneNumber);
                    }
                    if (result.equals("success")) {
                        String code = "200";
                        String message = "Details Updated";
                        json1 = new Gson().toJson(code);
                        json2 = new Gson().toJson(message);
                        json = "[" + json1 + "," + json2 + "]";
                    } else {
                        String code = "400";
                        String message = "Something went wrong. Try again later";
                        json1 = new Gson().toJson(code);
                        json2 = new Gson().toJson(message);
                        json = "[" + json1 + "," + json2 + "]";
                    }
                    break;
                }
                case "GetUserDiscountPointCount": {
                    String userid = (String) jsonParameter.get("userid");
                    int UserID = Integer.parseInt(userid);
                     String discountpoint = BookingClass.getUserDisCountPointCount(UserID);
                    int count = Integer.parseInt(discountpoint);
                    json = new Gson().toJson(count);
                    break;
                }
            }
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write(json);
        } catch (Exception ex) {
        }
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
