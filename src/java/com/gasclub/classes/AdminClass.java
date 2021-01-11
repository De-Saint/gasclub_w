/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gasclub.classes;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author Saint
 */
public class AdminClass {

    public AdminClass() {

    }

    public static int AddProduct(int categoryid, String name, String price, String properties) throws ClassNotFoundException, SQLException {
        HashMap<String, Object> tableData = new HashMap<>();
        tableData.put(Tables.ProductTable.ProductCategoryID, categoryid);
        tableData.put(Tables.ProductTable.Name, name);
        tableData.put(Tables.ProductTable.Price, price);
        tableData.put(Tables.ProductTable.Properties, properties);
        int result = DBManager.insertTableDataReturnID(Tables.ProductTable.Table, tableData, "");
        return result;
    }

    public static String AddLocation(String name, String fees) throws ClassNotFoundException, SQLException {
        HashMap<String, Object> tableData = new HashMap<>();
        tableData.put(Tables.LocationTable.Name, name);
        tableData.put(Tables.LocationTable.Fees, fees);
        String result = DBManager.insertTableData(Tables.LocationTable.Table, tableData, "");
        return result;
    }

    public static String UpdateLocation(int locationid, String name, String fees) throws ClassNotFoundException, SQLException {
        String result = DBManager.UpdateStringData(Tables.LocationTable.Table, Tables.LocationTable.Name, name, "where " + Tables.LocationTable.ID + " = " + locationid);
        DBManager.UpdateStringData(Tables.LocationTable.Table, Tables.LocationTable.Fees, fees, "where " + Tables.LocationTable.ID + " = " + locationid);
        return result;
    }

    public static String DeleteLocation(int LocationID) throws ClassNotFoundException, SQLException {
        String result = DBManager.DeleteObject(Tables.LocationTable.Table, "where " + Tables.LocationTable.ID + " = " + LocationID);
        return result;
    }

    public static ArrayList<Integer> getUserIds(int start, int limit) throws ClassNotFoundException, SQLException {
        ArrayList<Integer> IDS = DBManager.GetIntArrayListDescending(Tables.UserTable.ID, Tables.UserTable.Table, "Where " + Tables.UserTable.UserType + " = 'User' ORDER by lastname ASC LIMIT " + start + ", " + limit);
        return IDS;
    }
    public static ArrayList<Integer> GetUserIds() throws ClassNotFoundException, SQLException {
        ArrayList<Integer> IDS = DBManager.GetIntArrayListDescending(Tables.UserTable.ID, Tables.UserTable.Table, "Where " + Tables.UserTable.UserType + " = 'User'");
        return IDS;
    }

    public static String DeleteCustomerAccount(int UserID) throws ClassNotFoundException, SQLException {
        String result = "";
        result = DBManager.DeleteObject(Tables.UserTable.Table, "where " + Tables.UserTable.ID + " = " + UserID);
        DBManager.DeleteObject(Tables.CustomerTable.Table, "where " + Tables.CustomerTable.UserID + " = " + UserID);
        ArrayList<Integer> UserCartIds = DBManager.GetIntArrayListDescending(Tables.CartTable.ID, Tables.CartTable.Table, "where " + Tables.CartTable.UserID + " = " + UserID);
        if (!UserCartIds.isEmpty()) {
            for (int cartid : UserCartIds) {
                DBManager.DeleteObject(Tables.CartTable.Table, "where " + Tables.CartTable.ID + " = " + cartid);
            }
        }
        ArrayList<Integer> UserOrderIds = DBManager.GetIntArrayListDescending(Tables.OrderTable.ID, Tables.OrderTable.Table, "where " + Tables.OrderTable.UserID + " = " + UserID);
        if (!UserOrderIds.isEmpty()) {
            for (int orderid : UserOrderIds) {
                DBManager.DeleteObject(Tables.OrderTable.Table, "where " + Tables.OrderTable.ID + " = " + orderid);
            }
        }
        ArrayList<Integer> UserMessageIds = DBManager.GetIntArrayListDescending(Tables.MessageTable.ID, Tables.MessageTable.Table, "where " + Tables.MessageTable.FromMemberID + " = " + UserID);
        if (!UserMessageIds.isEmpty()) {
            for (int msgid : UserMessageIds) {
                DBManager.DeleteObject(Tables.MessageTable.Table, "where " + Tables.MessageTable.ID + " = " + msgid);
            }
        }

        DBManager.DeleteObject(Tables.RecoveryTable.Table, "where " + Tables.RecoveryTable.UserID + " = " + UserID);
        return result;
    }

    public static ArrayList<Integer> getSentMessageIDs(int UserID) throws ClassNotFoundException, SQLException {
        ArrayList<Integer> ids = new ArrayList<>();
        ids = DBManager.GetIntArrayList(Tables.MessageTable.ID, Tables.MessageTable.Table, "where " + Tables.MessageTable.FromMemberID + " = " + UserID);//"where from_member_id = " + meid);
        return ids;
    }

    public static ArrayList<Integer> getInboxMessageIDs(int UserID) throws ClassNotFoundException, SQLException {
        ArrayList<Integer> ids = new ArrayList<>();
        ids = DBManager.GetIntArrayList(Tables.MessageTable.ID, Tables.MessageTable.Table, "where " + Tables.MessageTable.ToMemberID + " = " + UserID);
        return ids;
    }

    public static ArrayList<Integer> getUserCartIds(int UserID) throws ClassNotFoundException, SQLException {
        ArrayList<Integer> ids = new ArrayList<>();
        ids = DBManager.GetIntArrayListDescending(Tables.CartTable.ID, Tables.CartTable.Table, "where " + Tables.CartTable.Status + "= 'Pending' AND " + Tables.CartTable.UserID + " = " + UserID);
        return ids;
    }

    public static HashMap<String, String> getMessageDetails(int msgID) throws ClassNotFoundException, SQLException {
        HashMap<String, String> Details = DBManager.GetTableData(Tables.MessageTable.Table, "where " + Tables.MessageTable.ID + " = " + msgID);
        int senderid = Integer.parseInt(Details.get(Tables.MessageTable.FromMemberID));
        String sendername = AdminClass.getUserName(senderid);
        Details.put("sendername", sendername);
        int recieverid = Integer.parseInt(Details.get(Tables.MessageTable.ToMemberID));
        String recievername = AdminClass.getUserName(recieverid);
        Details.put("recievername", recievername);
        return Details;
    }

    public static String getUserName(int UserID) throws ClassNotFoundException, SQLException {
        String result = DBManager.GetString(Tables.UserTable.LastName, Tables.UserTable.Table, "where " + Tables.UserTable.ID + " = " + UserID);
        return result;
    }

    public static String MarkMessageAsRead(int MessageID) throws ClassNotFoundException, SQLException {
        String result = DBManager.UpdateIntData(Tables.MessageTable.IsRead, 1, Tables.MessageTable.Table, "where " + Tables.MessageTable.ID + " = " + MessageID);
        return result;
    }

    public static String DeletMessage(int MessageID) throws ClassNotFoundException, SQLException {
        String result = DBManager.DeleteObject(Tables.MessageTable.Table, "where " + Tables.MessageTable.ID + " = " + MessageID);
        return result;
    }
 
     public static String DeleteProduct(int ProductID) throws ClassNotFoundException, SQLException {
        String result = DBManager.DeleteObject(Tables.ProductTable.Table, "where " + Tables.ProductTable.ID + " = " + ProductID);
        return result;
    }
    public static ArrayList<Integer> getProductCategoryIds() throws ClassNotFoundException, SQLException {
        ArrayList<Integer> stateIDs = new ArrayList<>();
        stateIDs = DBManager.GetIntArrayListDescending(Tables.ProductCategoriesTable.ID, Tables.ProductCategoriesTable.Table, "");
        return stateIDs;
    }

    public static HashMap<String, String> GetProductCategory(int ID) throws ClassNotFoundException, SQLException {
        HashMap<String, String> Details = DBManager.GetTableData(Tables.ProductCategoriesTable.Table, "where " + Tables.ProductCategoriesTable.ID + " = " + ID);
        return Details;
    }

    public static String UpdateProduct(int productid, String name, String price, String properties) throws ClassNotFoundException, SQLException {
        String result = "";
        result = DBManager.UpdateStringData(Tables.ProductTable.Table, Tables.ProductTable.Name, name, "where " + Tables.ProductTable.ID + " = " + productid);
        DBManager.UpdateStringData(Tables.ProductTable.Table, Tables.ProductTable.Price, price, "where " + Tables.ProductTable.ID + " = " + productid);
        DBManager.UpdateStringData(Tables.ProductTable.Table, Tables.ProductTable.Properties, properties, "where " + Tables.ProductTable.ID + " = " + productid);
        return result;
    }

}
