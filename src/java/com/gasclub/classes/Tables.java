/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gasclub.classes;

/**
 *
 * @author Saint
 */
public class Tables {
    public static class AdminTable {

        public static String Table = "admins";
        public static String ID = "id";
        public static String UserID = "userid";
        public static String Address = "address";
    }
    public static class CartTable {

        public static String Table = "carts";
        public static String ID = "id";
        public static String UserID = "userid";
        public static String ProductDetails = "product_details";
        public static String Status = "status";
        public static String ProductCount = "product_count";
    }
    public static class CustomerTable {

        public static String Table = "customers";
        public static String ID = "id";
        public static String UserID = "userid";
        public static String Address = "address";
    }
    public static class LocationTable {

        public static String Table = "locations";
        public static String ID = "id";
        public static String Name = "name";
        public static String Fees = "fees";
    }
    public static class MessageTable {

        public static String Table = "messages";
        public static String ID = "id";
        public static String Date = "date";
        public static String Time = "time";
        public static String Subject = "subject";
        public static String IsRead = "is_read";
        public static String FromMemberID = "from_member_id";
        public static String ToMemberID = "to_member_id";
        public static String Body = "body";
    }
    public static class OrderTable {

        public static String Table = "orders";
        public static String ID = "id";
        public static String UserID = "userid";
        public static String ProductDetails = "productdetails";
        public static String Status = "status";
        public static String BookedTime = "bookedtime";
        public static String BookedDate = "bookeddate";
        public static String DeliveryTime = "deliverytime";
        public static String DeliveryDate = "deliverydate";
        public static String OrderNumber = "ordernumber";
        public static String SubStatus = "substatus";
        public static String Amount = "amount";
        public static String DeliveryAddress = "deliveryaddress";
        public static String DeliveryFees = "deliveryfee";
    }
  
    public static class ProductTable {

        public static String Table = "products";
        public static String ID = "id";
        public static String Name = "name";
        public static String Price = "price";
        public static String Properties = "properties";
        public static String ProductCategoryID  = "product_categoryid";
    }
     public static class ProductCategoriesTable {

        public static String Table = "product_categories";
        public static String ID = "id";
        public static String Name = "name";
    }
    public static class RecoveryTable {

        public static String Table = "recovery";
        public static String ID = "id";
        public static String UserID = "userid";
        public static String Question = "question";
        public static String Answer = "answer";
    }
    public static class UserTable {

        public static String Table = "users";
        public static String ID = "id";
        public static String FirstName = "firstname";
        public static String LastName = "lastname";
        public static String Email = "email";
        public static String Phone = "phone";
        public static String Password = "password";
        public static String UserType = "type";
        public static String DateCreated = "date_created";
        public static String TokenID = "tokenid";
        public static String DeviceToken = "devicetoken";
        public static String LocationID = "locationid";
        public static String DiscountPoint = "discountpoint";
        
    }

}
