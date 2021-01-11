/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gasclub.classes;

import java.sql.Date;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

/**
 *
 * @author Saint
 */
public class UserClass {

    public UserClass() {

    }

    public static java.sql.Date CurrentDate() throws ParseException {
        Calendar currentdate = Calendar.getInstance();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MMM-dd");
        String Placeholder = formatter.format(currentdate.getTime());
        java.util.Date datenow = formatter.parse(Placeholder);
        java.sql.Date CurrentDate = new Date(datenow.getTime());
        return CurrentDate;
    }

    public static String CurrentTime() throws ParseException {
        Calendar currentdate = Calendar.getInstance();
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");
        String CurrentTime = formatter.format(currentdate.getTime());
        return CurrentTime;
    }

    public static boolean checkEmailAddressOrPhoneNumberExist(String EmailAddress) throws ClassNotFoundException, SQLException {
        boolean result = false;
        int usid = DBManager.GetInt(Tables.UserTable.ID, Tables.UserTable.Table, "where " + Tables.UserTable.Email + " = '" + EmailAddress + "' or " + Tables.UserTable.Phone + " = '" + EmailAddress + "'");
        if (usid != 0) {
            result = true;
        }
        return result;
    }

    public static int checkPasswordEmailMatch(String Password, String Email_PhoneNum) throws ClassNotFoundException, SQLException {
        int result = 0;
        String memPassword = "";
        String email = Email_PhoneNum;
        memPassword = DBManager.GetString(Tables.UserTable.Password, Tables.UserTable.Table, "where " + Tables.UserTable.Email + " = '" + Email_PhoneNum + "'");
        if (memPassword.equals("none")) {
            memPassword = DBManager.GetString(Tables.UserTable.Password, Tables.UserTable.Table, "where " + Tables.UserTable.Phone + " = '" + Email_PhoneNum + "'");
            email = DBManager.GetString(Tables.UserTable.Email, Tables.UserTable.Table, "where " + Tables.UserTable.Phone + " = '" + Email_PhoneNum + "'");
        }
        if (memPassword.equals(Password)) {
            result = DBManager.GetInt(Tables.UserTable.ID, Tables.UserTable.Table, "where " + Tables.UserTable.Email + " = '" + email + "'");
        }
        return result;
    }

    public static int checkEmailMatch(String EmailAddress) throws ClassNotFoundException, SQLException {
        int result = 0;
        result = DBManager.GetInt(Tables.UserTable.ID, Tables.UserTable.Table, "where " + Tables.UserTable.Email + " = '" + EmailAddress + "'");
        return result;
    }

    public static String getUserType(int UserID) throws ClassNotFoundException, SQLException {
        String result = DBManager.GetString(Tables.UserTable.UserType, Tables.UserTable.Table, "where " + Tables.UserTable.ID + " = " + UserID);
        return result;
    }

    public static HashMap<String, String> GetCustomerDetails(int UserID) throws ClassNotFoundException, SQLException {
        HashMap<String, String> UserDetails = new HashMap<>();
        HashMap<String, String> userDetails = DBManager.GetTableData(Tables.UserTable.Table, "where " + Tables.UserTable.ID + " = " + UserID);
        int locationid = Integer.parseInt(userDetails.get(Tables.UserTable.LocationID));
        String location = DBManager.GetString(Tables.LocationTable.Name, Tables.LocationTable.Table, "where " + Tables.LocationTable.ID + " = " + locationid);
        userDetails.put("location", location);
        HashMap<String, String> TableDetails = DBManager.GetTableData(Tables.CustomerTable.Table, "where " + Tables.CustomerTable.UserID + " = " + UserID);
        UserDetails.putAll(userDetails);
        UserDetails.putAll(TableDetails);
        return UserDetails;
    }

    public static HashMap<String, String> GetAdminDetails(int UserID) throws ClassNotFoundException, SQLException {
        HashMap<String, String> UserDetails = new HashMap<>();
        HashMap<String, String> userDetails = DBManager.GetTableData(Tables.UserTable.Table, "where " + Tables.UserTable.ID + " = " + UserID);
        int locationid = Integer.parseInt(userDetails.get(Tables.UserTable.LocationID));
        String location = DBManager.GetString(Tables.LocationTable.Name, Tables.LocationTable.Table, "where " + Tables.LocationTable.ID + " = " + locationid);
        userDetails.put("location", location);
        HashMap<String, String> TableDetails = DBManager.GetTableData(Tables.AdminTable.Table, "where " + Tables.AdminTable.UserID + " = " + UserID);
        UserDetails.putAll(userDetails);
        UserDetails.putAll(TableDetails);
        return UserDetails;
    }

    public static ArrayList<Integer> getLocationIds() throws ClassNotFoundException, SQLException {
        ArrayList<Integer> stateIDs = new ArrayList<>();
        stateIDs = DBManager.GetIntArrayList(Tables.LocationTable.ID, Tables.LocationTable.Table, "ORDER by name DESC");
        return stateIDs;
    }

    public static ArrayList<Integer> GetRefillProducts() throws ClassNotFoundException, SQLException {
        ArrayList<Integer> IDs = new ArrayList<>();
        IDs = DBManager.GetIntArrayListDescending(Tables.ProductTable.ID, Tables.ProductTable.Table, "where " + Tables.ProductTable.ProductCategoryID + " = " + 2);
        return IDs;
    }
    public static ArrayList<Integer> GetAccessoriesProducts() throws ClassNotFoundException, SQLException {
        ArrayList<Integer> IDs = new ArrayList<>();
        IDs = DBManager.GetIntArrayListDescending(Tables.ProductTable.ID, Tables.ProductTable.Table, "where " + Tables.ProductTable.ProductCategoryID + " != " + 2);
        return IDs;
    }
    public static ArrayList<Integer> getProductIds() throws ClassNotFoundException, SQLException {
        ArrayList<Integer> IDs = new ArrayList<>();
        IDs = DBManager.GetIntArrayListDescending(Tables.ProductTable.ID, Tables.ProductTable.Table, "");
        return IDs;
    }

    public static HashMap<String, String> GetLocationName(int ID) throws ClassNotFoundException, SQLException {
        HashMap<String, String> Details = DBManager.GetTableData(Tables.LocationTable.Table, "where " + Tables.LocationTable.ID + " = " + ID);
        return Details;
    }

    public static HashMap<String, String> GetProduct(int ID) throws ClassNotFoundException, SQLException {
        HashMap<String, String> Details = DBManager.GetTableData(Tables.ProductTable.Table, "where " + Tables.ProductTable.ID + " = " + ID);
        return Details;
    }
    public static String GetProductProperties(int ProductID) throws ClassNotFoundException, SQLException {
        String Details = DBManager.GetString(Tables.ProductTable.Properties, Tables.ProductTable.Table, "where " + Tables.ProductTable.ProductCategoryID + " = " + 2 + " AND " + Tables.ProductTable.ID + " = " + ProductID);
        return Details;
    }

    public static HashMap<String, String> GetUserRecoveryDetails(int ID) throws ClassNotFoundException, SQLException {
        HashMap<String, String> Details = DBManager.GetTableData(Tables.RecoveryTable.Table, "where " + Tables.RecoveryTable.UserID + " = " + ID);
        return Details;
    }

    public static int RegisterUser(String Firstname, String Lastname, String EmailAddress, String PhoneNumber, String password, String Type, Date DateCreated, int locationid) throws ClassNotFoundException, SQLException {
        int userId = 0;
        HashMap<String, Object> tableData = new HashMap<>();
        tableData.put(Tables.UserTable.Email, EmailAddress);
        tableData.put(Tables.UserTable.Phone, PhoneNumber);
        tableData.put(Tables.UserTable.Password, password);
        tableData.put(Tables.UserTable.FirstName, Firstname);
        tableData.put(Tables.UserTable.LastName, Lastname);
        tableData.put(Tables.UserTable.UserType, Type);
        tableData.put(Tables.UserTable.DateCreated, DateCreated);
        tableData.put(Tables.UserTable.LocationID, locationid);
        tableData.put(Tables.UserTable.DiscountPoint, 0);
        userId = DBManager.insertTableDataReturnID(Tables.UserTable.Table, tableData, "");
        return userId;
    }

    public static String RegisterCustomer(int UserID, String Address) throws ClassNotFoundException, SQLException {
        HashMap<String, Object> tableData = new HashMap<>();
        tableData.put(Tables.CustomerTable.UserID, UserID);
        tableData.put(Tables.CustomerTable.Address, Address);
        String result = DBManager.insertTableData(Tables.CustomerTable.Table, tableData, "");
        return result;
    }

    public static void sendMemberMessage(int ToMemberId, String bdy, String sub, int FromMemberId) throws ClassNotFoundException, SQLException, ParseException {
        String Time = CurrentTime();
        Date date = CurrentDate();
        HashMap<String, Object> data = new HashMap<>();
        data.put(Tables.MessageTable.Date, date);
        data.put(Tables.MessageTable.Time, Time);
        data.put(Tables.MessageTable.Subject, sub);
        data.put(Tables.MessageTable.IsRead, 0);
        data.put(Tables.MessageTable.FromMemberID, FromMemberId);
        data.put(Tables.MessageTable.ToMemberID, ToMemberId);
        data.put(Tables.MessageTable.Body, bdy);
        DBManager.insertTableData(Tables.MessageTable.Table, data, "");

    }

    public static String CreateRecovery(int userID, String question, String answer) throws ClassNotFoundException, SQLException {
        HashMap<String, Object> data = new HashMap<>();
        data.put(Tables.RecoveryTable.UserID, userID);
        data.put(Tables.RecoveryTable.Question, question);
        data.put(Tables.RecoveryTable.Answer, answer);
        String result = DBManager.insertTableData(Tables.RecoveryTable.Table, data, "");
        return result;
    }

    public static String UpdateUserPassword(int UserID, String Password) throws ClassNotFoundException, SQLException {
        String result = DBManager.UpdateStringData(Tables.UserTable.Table, Tables.UserTable.Password, Password, "where " + Tables.UserTable.ID + " = " + UserID);
        return result;
    }

    public static String UpdateFirstName(int UserID, String FirstName) throws ClassNotFoundException, SQLException {
        String result = DBManager.UpdateStringData(Tables.UserTable.Table, Tables.UserTable.FirstName, FirstName, "where " + Tables.UserTable.ID + " = " + UserID);
        return result;
    }

    public static String UpdateLastName(int UserID, String LastName) throws ClassNotFoundException, SQLException {
        String result = DBManager.UpdateStringData(Tables.UserTable.Table, Tables.UserTable.LastName, LastName, "where " + Tables.UserTable.ID + " = " + UserID);
        return result;
    }

    public static String UpdateEmail(int UserID, String Email) throws ClassNotFoundException, SQLException {
        String result = DBManager.UpdateStringData(Tables.UserTable.Table, Tables.UserTable.Email, Email, "where " + Tables.UserTable.ID + " = " + UserID);
        return result;
    }

    public static String UpdatePhone(int UserID, String Phone) throws ClassNotFoundException, SQLException {
        String result = DBManager.UpdateStringData(Tables.UserTable.Table, Tables.UserTable.Phone, Phone, "where " + Tables.UserTable.ID + " = " + UserID);
        return result;
    }
    public static int getUserLocationByID(int UserID) throws ClassNotFoundException, SQLException {
        int result = DBManager.GetInt(Tables.UserTable.LocationID, Tables.UserTable.Table, "where " + Tables.UserTable.ID + " = " + UserID);
        return result;
    }
    public static String getUserAddress(int UserID) throws ClassNotFoundException, SQLException {
        String  result = DBManager.GetString(Tables.CustomerTable.Address, Tables.CustomerTable.Table, "where " + Tables.CustomerTable.UserID + " = " + UserID);
        return result;
    }
    public static String getUserName(int UserID) throws ClassNotFoundException, SQLException {
        String result = "";
        String  lname = DBManager.GetString(Tables.UserTable.LastName, Tables.UserTable.Table, "where " + Tables.UserTable.ID + " = " + UserID);
        String  fname = DBManager.GetString(Tables.UserTable.FirstName, Tables.UserTable.Table, "where " + Tables.UserTable.ID + " = " + UserID);
       result = lname + " " + fname;
        return result;
    }
    public static String getUserPhone(int UserID) throws ClassNotFoundException, SQLException {
        String  result = DBManager.GetString(Tables.UserTable.Phone, Tables.UserTable.Table, "where " + Tables.UserTable.ID + " = " + UserID);
        return result;
    }

}
