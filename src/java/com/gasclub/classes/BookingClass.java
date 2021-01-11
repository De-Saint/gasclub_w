/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gasclub.classes;

import java.sql.Date;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

/**
 *
 * @author Saint
 */
public class BookingClass {

    public BookingClass() {

    }

    public static String AddtoCart(int UserID, String productdetails) throws ClassNotFoundException, SQLException {
        HashMap<String, Object> tableData = new HashMap<>();
        tableData.put(Tables.CartTable.UserID, UserID);
        tableData.put(Tables.CartTable.ProductDetails, productdetails);
        tableData.put(Tables.CartTable.Status, "Pending");
        tableData.put(Tables.CartTable.ProductCount, 1);
        String result = DBManager.insertTableData(Tables.CartTable.Table, tableData, "");
        return result;
    }

    public static int getUserCartID(int UserID) throws ClassNotFoundException, SQLException {
        int result = 0;
        result = DBManager.GetInt(Tables.CartTable.ID, Tables.CartTable.Table, "where " + Tables.CartTable.Status + " = 'Pending' AND " + Tables.CartTable.UserID + " = " + UserID);
        return result;
    }

    public static int getUserCartProductCount(int UserID) throws ClassNotFoundException, SQLException {
        int result = 0;
        result = DBManager.GetInt(Tables.CartTable.ProductCount, Tables.CartTable.Table, "where " + Tables.CartTable.Status + " = 'Pending' AND " + Tables.CartTable.UserID + " = " + UserID);
        return result;
    }

    public static String getUserDisCountPointCount(int UserID) throws ClassNotFoundException, SQLException {
        String result = "";
        result = DBManager.GetString(Tables.UserTable.DiscountPoint, Tables.UserTable.Table, "where " + Tables.UserTable.ID + " = " + UserID);
        return result;
    }

    public static String getCartProductDetails(int cartid) throws ClassNotFoundException, SQLException {
        String result = "";
        result = DBManager.GetString(Tables.CartTable.ProductDetails, Tables.CartTable.Table, "where " + Tables.CartTable.ID + " = " + cartid);
        return result;
    }

    public static String UpdateCart(int cartid, String productdetails, int userid) throws ClassNotFoundException, SQLException {
        int count = getUserCartProductCount(userid);
        int NewCountValue = count + 1;
        String ProductDetails = getCartProductDetails(cartid);
        String NewProductDetails = ProductDetails + productdetails;
        String result = DBManager.UpdateStringData(Tables.CartTable.Table, Tables.CartTable.ProductDetails, NewProductDetails, "where " + Tables.CartTable.ID + " = " + cartid);
        DBManager.UpdateIntData(Tables.CartTable.ProductCount, NewCountValue, Tables.CartTable.Table, "where " + Tables.CartTable.ID + " = " + cartid);
        return result;
    }

    public static HashMap<String, String> GetCartDetails(int CartID) throws ClassNotFoundException, SQLException {
        HashMap<String, String> Details = DBManager.GetTableData(Tables.CartTable.Table, "where " + Tables.CartTable.ID + " = " + CartID);
        return Details;
    }

    public static String RemoveFromCart(int cartid, String productdetails) throws ClassNotFoundException, SQLException {
        int count = DBManager.GetInt(Tables.CartTable.ProductCount, Tables.CartTable.Table, "where " + Tables.CartTable.ID + " = " + cartid);
        int NewCountValue = count - 1;
        String result = DBManager.UpdateStringData(Tables.CartTable.Table, Tables.CartTable.ProductDetails, productdetails, "where " + Tables.CartTable.ID + " = " + cartid);
        DBManager.UpdateIntData(Tables.CartTable.ProductCount, NewCountValue, Tables.CartTable.Table, "where " + Tables.CartTable.ID + " = " + cartid);
        return result;
    }

    public static String EmptyCart(int cartid) throws ClassNotFoundException, SQLException {
        String result = DBManager.DeleteObject(Tables.CartTable.Table, "where " + Tables.CartTable.ID + " = " + cartid);
        return result;
    }

    public static int PlaceOrder(int UserID, String OrderDetails, String TotalAmount, String deliveryaddress, String deliveryfee) throws ClassNotFoundException, SQLException, ParseException {
        String OrderNumber = CreateOrderCode(8);
        Date CurrentDate = UserClass.CurrentDate();
        String CurrentTime = UserClass.CurrentTime();
        HashMap<String, Object> tableData = new HashMap<>();
        tableData.put(Tables.OrderTable.UserID, UserID);
        tableData.put(Tables.OrderTable.Amount, TotalAmount);
        tableData.put(Tables.OrderTable.OrderNumber, OrderNumber);
        tableData.put(Tables.OrderTable.ProductDetails, OrderDetails);
        tableData.put(Tables.OrderTable.Status, "Pending");
        tableData.put(Tables.OrderTable.SubStatus, "Pending Confirmation");
        tableData.put(Tables.OrderTable.BookedDate, CurrentDate);
        tableData.put(Tables.OrderTable.BookedTime, CurrentTime);
        tableData.put(Tables.OrderTable.DeliveryAddress, deliveryaddress);
        tableData.put(Tables.OrderTable.DeliveryFees, deliveryfee);
        int result = DBManager.insertTableDataReturnID(Tables.OrderTable.Table, tableData, "");
        return result;
    }

    public static String CreateOrderCode(int LengthOfCode) {
        String OrderCode = "";
        char[] chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890".toCharArray();
        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < LengthOfCode; i++) {
            char c = chars[random.nextInt(chars.length)];
            sb.append(c);
        }
        OrderCode = sb.toString();
        return OrderCode;
    }

    public static ArrayList<Integer> GetOrderIDS(int UserID) throws ClassNotFoundException, SQLException {
        ArrayList<Integer> IDS = new ArrayList<>();
        IDS = DBManager.GetIntArrayList(Tables.OrderTable.ID, Tables.OrderTable.Table, "where " + Tables.OrderTable.UserID + " = " + UserID);
        return IDS;
    }

    public static ArrayList<Integer> getOrderIDS() throws ClassNotFoundException, SQLException {
        ArrayList<Integer> IDS = new ArrayList<>();
        IDS = DBManager.GetIntArrayList(Tables.OrderTable.ID, Tables.OrderTable.Table, "");
        return IDS;
    }

    public static HashMap<String, String> GetOrderDetails(int OrderID) throws ClassNotFoundException, SQLException {
        HashMap<String, String> Details = DBManager.GetTableData(Tables.OrderTable.Table, "where " + Tables.OrderTable.ID + " = " + OrderID);
        return Details;
    }

    public static String GetOrderProductDetails(int OrderID) throws ClassNotFoundException, SQLException {
        String result = DBManager.GetString(Tables.OrderTable.ProductDetails, Tables.OrderTable.Table, "where " + Tables.OrderTable.Status + " = 'Delivered' AND " + Tables.OrderTable.ID + " = " + OrderID);
        return result;
    }

    public static HashMap<String, String> GetPendingOrderDetails(int OrderID) throws ClassNotFoundException, SQLException {
        HashMap<String, String> Details = DBManager.GetTableData(Tables.OrderTable.Table, "where " + Tables.OrderTable.Status + " = 'Pending' AND " + Tables.OrderTable.ID + " = " + OrderID + " ORDER by id DESC");
        return Details;
    }

    public static HashMap<String, String> GetDeliveredOrderDetails(int OrderID) throws ClassNotFoundException, SQLException {
        HashMap<String, String> Details = DBManager.GetTableData(Tables.OrderTable.Table, "where " + Tables.OrderTable.Status + " = 'Delivered' AND " + Tables.OrderTable.ID + " = " + OrderID + " ORDER by id DESC");
        return Details;
    }

    public static HashMap<String, String> GetCancelledOrderDetails(int OrderID) throws ClassNotFoundException, SQLException {
        HashMap<String, String> Details = DBManager.GetTableData(Tables.OrderTable.Table, "where " + Tables.OrderTable.Status + " = 'Cancelled' AND " + Tables.OrderTable.ID + " = " + OrderID + " ORDER by id DESC");
        return Details;
    }

    public static ArrayList<HashMap<String, String>> GetOrderProductdetails(String productdetails) throws ClassNotFoundException, SQLException {
        ArrayList<HashMap<String, String>> ProductList = new ArrayList<>();
        String details[] = productdetails.split(";");
        for (String record : details) {
            String productid = record.split(",")[0];
            int ProductID = Integer.parseInt(productid);
            HashMap<String, String> ProductDet = UserClass.GetProduct(ProductID);
            ProductList.add(ProductDet);
        }
        return ProductList;
    }

    public static String getOrderStatus(int OrderID) throws ClassNotFoundException, SQLException {
        String result = "";
        result = DBManager.GetString(Tables.OrderTable.Status, Tables.OrderTable.Table, "where " + Tables.OrderTable.ID + " = " + OrderID);
        return result;
    }

    public static String getOrderNumber(int OrderID) throws ClassNotFoundException, SQLException {
        String result = "";
        result = DBManager.GetString(Tables.OrderTable.OrderNumber, Tables.OrderTable.Table, "where " + Tables.OrderTable.ID + " = " + OrderID);
        return result;
    }

    public static String UpdateOrderSubStatus(int OrderID, String SubStatus) throws ClassNotFoundException, SQLException {
        String result = "";
        result = DBManager.UpdateStringData(Tables.OrderTable.Table, Tables.OrderTable.SubStatus, SubStatus, "where " + Tables.OrderTable.ID + " = " + OrderID);
        return result;
    }

    public static String UpdateUserDiscountPoint(int UserID, String DiscountPoint) throws ClassNotFoundException, SQLException {
        String result = "";
        result = DBManager.UpdateStringData(Tables.UserTable.Table, Tables.UserTable.DiscountPoint, DiscountPoint, "where " + Tables.UserTable.ID + " = " + UserID);
        return result;
    }

    public static String UpdateOrderStatus(int OrderID, String Status) throws ClassNotFoundException, SQLException {
        String result = "";
        result = DBManager.UpdateStringData(Tables.OrderTable.Table, Tables.OrderTable.Status, Status, "where " + Tables.OrderTable.ID + " = " + OrderID);
        return result;
    }

    public static String DeleteOrder(int OrderID) throws ClassNotFoundException, SQLException {
        String result = "";
        result = DBManager.DeleteObject(Tables.OrderTable.Table, "where " + Tables.OrderTable.ID + " = " + OrderID);
        return result;
    }

    public static String UpdateDeliveryDateAndTime(int OrderID) throws ClassNotFoundException, SQLException {
        String result = "";
        DBManager.UpdateCurrentDate(Tables.OrderTable.Table, Tables.OrderTable.DeliveryDate, "where " + Tables.OrderTable.ID + " = " + OrderID);
        result = DBManager.UpdateCurrentTime(Tables.OrderTable.Table, Tables.OrderTable.DeliveryTime, "where " + Tables.OrderTable.ID + " = " + OrderID);
        return result;
    }

    public static String CancelOrderRequest(int OrderID, String userName) throws ClassNotFoundException, SQLException {
        String result = "";
        DBManager.UpdateStringData(Tables.OrderTable.Table, Tables.OrderTable.Status, "Cancelled", "where " + Tables.OrderTable.ID + " = " + OrderID);
        result = DBManager.UpdateStringData(Tables.OrderTable.Table, Tables.OrderTable.SubStatus, "Cancelled by " + userName, "where " + Tables.OrderTable.ID + " = " + OrderID);
        return result;
    }

    public static ArrayList<Integer> getDeliveredOrderIDS() throws ClassNotFoundException, SQLException {
        ArrayList<Integer> IDS = new ArrayList<>();
        IDS = DBManager.GetIntArrayListDescending(Tables.OrderTable.ID, Tables.OrderTable.Table, "where " + Tables.OrderTable.Status + " = 'Delivered'");
        return IDS;
    }

    public static String getOrderAmount(int OrderID) throws ClassNotFoundException, SQLException {
        String result = "";
        result = DBManager.GetString(Tables.OrderTable.Amount, Tables.OrderTable.Table, "where " + Tables.OrderTable.ID + " = " + OrderID);
        return result;
    }

    public static String UpdateCartProductDetails(int CartID, String CartProductDetails) throws ClassNotFoundException, SQLException {
        String result = "";
        result = DBManager.UpdateStringData(Tables.CartTable.Table, Tables.CartTable.ProductDetails, CartProductDetails, "where " + Tables.CartTable.ID + " = " + CartID);
        return result;
    }

    public static String CalculateAndUpdateUserDiscountPoint(int OrderID, int UserID) throws ClassNotFoundException, SQLException {
        String result = "";
        String productdetails = BookingClass.GetOrderProductDetails(OrderID);
        String details[] = productdetails.split(";");
        for (String record : details) {
            String productid = record.split(",")[0];
            String Quantity = record.split(",")[1];
            int ProductID = Integer.parseInt(productid);
            String productprops = UserClass.GetProductProperties(ProductID);
            if (!productprops.equals("none")) {
                String props[] = productprops.split(",");
                for (String prop : props) {
                    String name = prop.split(":")[0];
                    String value = prop.split(":")[1];
                    if (name.matches("Guage") || name.contains("guage") || name.contains("Guage")) {
                        String newvalue = value.substring(0, value.length() - 2);
                        if (newvalue.contains(".")) {
                            newvalue = "13";
                            int propvalue = Integer.parseInt(newvalue);
                            int pquantity = Integer.parseInt(Quantity);
                            int NewDiscountPoint = propvalue * pquantity;
                            String discountPoint = BookingClass.getUserDisCountPointCount(UserID);
                            int OldDiscountPoint = Integer.parseInt(discountPoint);
                            int DP = OldDiscountPoint + NewDiscountPoint;
                            String DiscountPoint = "" + DP;
                            result = BookingClass.UpdateUserDiscountPoint(UserID, DiscountPoint);
                        } else {
                            int propvalue = Integer.parseInt(newvalue);
                            int pquantity = Integer.parseInt(Quantity);
                            int NewDiscountPoint = propvalue * pquantity;
                            String discountPoint = BookingClass.getUserDisCountPointCount(UserID);
                            int OldDiscountPoint = Integer.parseInt(discountPoint);
                            int DP = OldDiscountPoint + NewDiscountPoint;
                            String DiscountPoint = "" + DP;
                            result = BookingClass.UpdateUserDiscountPoint(UserID, DiscountPoint);
                        }
                    }
                }
            }
        }
        return result;
    }
    
}
