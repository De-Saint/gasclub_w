/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gasclub.servlets;

import com.gasclub.classes.BookingClass;
import com.gasclub.classes.Tables;
import com.gasclub.classes.UserClass;
import com.google.gson.Gson;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
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
public class BookingServlet extends HttpServlet {

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
                case "PlaceOrder": {
                    String userid = (String) jsonParameter.get("userid");
                    int UserID = Integer.parseInt(userid);
                    String cartid = (String) jsonParameter.get("cartid");
                    int CartID = Integer.parseInt(cartid);
                    String totalamount = (String) jsonParameter.get("totalamount");
                    String orderdetails = (String) jsonParameter.get("orderdetails");
                    String deliveryfee = (String) jsonParameter.get("deliveryfee");
                    String deliveryaddress = (String) jsonParameter.get("deliveryaddress");
                    int orderid = BookingClass.PlaceOrder(UserID, orderdetails, totalamount.trim(), deliveryaddress, deliveryfee);
                    UserClass.sendMemberMessage(UserID, "You placed an order", "Placed Order", 1);
                    UserClass.sendMemberMessage(1, "An order has been placed", "Placed Order", 1);
                    BookingClass.EmptyCart(CartID);
                    json = new Gson().toJson(orderid);
                    break;
                }
                case "AddToCart": {
                    String userid = (String) jsonParameter.get("userid");
                    int UserID = Integer.parseInt(userid);
                    String productid = (String) jsonParameter.get("productid");
                    String productdetails = (String) jsonParameter.get("productdetails");
                    int cartid = BookingClass.getUserCartID(UserID);
                    if (cartid == 0) {
                        result = BookingClass.AddtoCart(UserID, productdetails);
                    } else {
                        HashMap<String, String> CartDetails = BookingClass.GetCartDetails(cartid);
                        String OldProductDetails = CartDetails.get(Tables.CartTable.ProductDetails);
                        String details[] = OldProductDetails.split(";");
                        for (String record : details) {
                            if (record.split(",")[0].equals(productid)) {
                                result = "exist";
                                break;
                            } else {
                                result = BookingClass.UpdateCart(cartid, productdetails, UserID);
                                break;
                            }
                        }
                    }
                    json = new Gson().toJson(result);
                    break;
                }
                case "DeleteProductFromCart": {
                    String cartid = (String) jsonParameter.get("cartid");
                    int CartID = Integer.parseInt(cartid);
                    String ProductID = (String) jsonParameter.get("productid");
                    HashMap<String, String> CartDetails = BookingClass.GetCartDetails(CartID);
                    String productdetails = CartDetails.get(Tables.CartTable.ProductDetails);
                    String details[] = productdetails.split(";");
                    String newrecord = "";
                    for (String record : details) {
                        if (!record.split(",")[0].equals(ProductID)) {
                            newrecord = newrecord.concat(record) + ";";
                        }
                    }
                    String NewProductDetails = newrecord;
                    result = BookingClass.RemoveFromCart(CartID, NewProductDetails);
                    json = new Gson().toJson(result);
                    break;
                }
                case "UpdateCart": {
                    String cartid = (String) jsonParameter.get("cartid");
                    int CartID = Integer.parseInt(cartid);
                    String cartproductdetails = (String) jsonParameter.get("cartproductdetails");
                    result = BookingClass.UpdateCartProductDetails(CartID, cartproductdetails);
                    json = new Gson().toJson(result);
                    break;
                }
                case "GetUserCartCount": {
                    String userid = (String) jsonParameter.get("userid");
                    int UserID = Integer.parseInt(userid);
                    int count = BookingClass.getUserCartProductCount(UserID);
                    int cartid = BookingClass.getUserCartID(UserID);
                    json1 = new Gson().toJson(count);
                    json2 = new Gson().toJson(cartid);
                    json = "[" + json1 + "," + json2 + "]";
                    break;
                }
                case "GetUserCartID": {
                    String userid = (String) jsonParameter.get("userid");
                    int UserID = Integer.parseInt(userid);
                    int cartid = BookingClass.getUserCartID(UserID);
                    json = new Gson().toJson(cartid);
                    break;
                }
                case "GetlocationDetails": {
                    String locationid = (String) jsonParameter.get("locationid");
                    int LocationID = Integer.parseInt(locationid);
                    HashMap<String, String> UserDet = UserClass.GetLocationName(LocationID);
                    json = new Gson().toJson(UserDet);
                    break;
                }
                case "GetCartDetails": {
                    String cartid = (String) jsonParameter.get("cartid");
                    int CartID = Integer.parseInt(cartid);
                    ArrayList<HashMap<String, String>> productlist = new ArrayList<>();
                    HashMap<String, String> CartDetails = BookingClass.GetCartDetails(CartID);
                    String productdetails = CartDetails.get(Tables.CartTable.ProductDetails);
                    if (!productdetails.equals("")) {
                        String details[] = productdetails.split(";");
                        int TotalAmount = 0;
                        for (String record : details) {
                            String productid = record.split(",")[0];
                            int ProductID = Integer.parseInt(productid);
                            HashMap<String, String> ProductDet = UserClass.GetProduct(ProductID);
                            String Quantity = record.split(",")[1];
                            ProductDet.put("Quantity", Quantity);
                            String Amount = record.split(",")[2];
                            ProductDet.put("Amount", Amount);
                            int amt = Integer.parseInt(Amount);
                            TotalAmount = TotalAmount + amt;
                            productlist.add(ProductDet);
                        }
                        CartDetails.put("TotalAmount", "" + TotalAmount);
                        json1 = new Gson().toJson(CartDetails);
                        json2 = new Gson().toJson(productlist);
                        json = "[" + json1 + "," + json2 + "]";
                    } else {
                        BookingClass.EmptyCart(CartID);
                        json1 = new Gson().toJson("empty");
                        json = "[" + json1 + "]";
                    }

                    break;
                }
                case "getPlacedOrders": {
                    String usertype = (String) jsonParameter.get("usertype");
                    String userid = (String) jsonParameter.get("userid");
                    String ordertype = (String) jsonParameter.get("ordertype");
                    int UserID = Integer.parseInt(userid);
                    ArrayList<Integer> IDS = new ArrayList<>();

                    if (usertype.equals("Admin")) {
                        IDS = BookingClass.getOrderIDS();
                    } else if (usertype.equals("User")) {
                        IDS = BookingClass.GetOrderIDS(UserID);
                    }
                    ArrayList<HashMap<String, String>> OrderList = new ArrayList<>();
                    if (ordertype.equals("Pending")) {
                        if (!IDS.isEmpty()) {
                            for (int Oid : IDS) {
                                HashMap<String, String> OrderDetails = new HashMap<>();
                                OrderDetails = BookingClass.GetPendingOrderDetails(Oid);
                                if (!OrderDetails.isEmpty()) {
                                    OrderDetails.put("count", "" + IDS.size());
                                    OrderList.add(OrderDetails);
                                }
                            }
                            String code = "200";
                            json1 = new Gson().toJson(code);
                            json2 = new Gson().toJson(OrderList);
                            json = "[" + json1 + "," + json2 + "]";
                        } else {
                            HashMap<String, String> OrderDetails = new HashMap<>();
                            String code = "400";
                            String message = "Sorry, no order(s)";
                            OrderDetails.put("empty", message);
                            OrderList.add(OrderDetails);
                            json1 = new Gson().toJson(code);
                            json2 = new Gson().toJson(OrderList);
                            json = "[" + json1 + "," + json2 + "]";
                        }
                    } else if (ordertype.equals("Delivered")) {
                        if (!IDS.isEmpty()) {
                            for (int Oid : IDS) {
                                HashMap<String, String> OrderDetails = new HashMap<>();
                                OrderDetails = BookingClass.GetDeliveredOrderDetails(Oid);
                                if (!OrderDetails.isEmpty()) {
                                    OrderDetails.put("count", "" + IDS.size());
                                    OrderList.add(OrderDetails);
                                }
                            }
                            String code = "200";
                            json1 = new Gson().toJson(code);
                            json2 = new Gson().toJson(OrderList);
                            json = "[" + json1 + "," + json2 + "]";
                        } else {
                            HashMap<String, String> OrderDetails = new HashMap<>();
                            String code = "400";
                            String message = "Sorry, no order(s)";
                            OrderDetails.put("empty", message);
                            OrderList.add(OrderDetails);
                            json1 = new Gson().toJson(code);
                            json2 = new Gson().toJson(OrderList);
                            json = "[" + json1 + "," + json2 + "]";
                        }
                    } else if (ordertype.equals("Cancelled")) {
                        if (!IDS.isEmpty()) {
                            for (int Oid : IDS) {
                                HashMap<String, String> OrderDetails = new HashMap<>();
                                OrderDetails = BookingClass.GetCancelledOrderDetails(Oid);
                                if (!OrderDetails.isEmpty()) {
                                   OrderList.add(OrderDetails);
                                }
                            }
                            String code = "200";
                            json1 = new Gson().toJson(code);
                            json2 = new Gson().toJson(OrderList);
                            json = "[" + json1 + "," + json2 + "]";
                        } else {
                            HashMap<String, String> OrderDetails = new HashMap<>();
                            String code = "400";
                            String message = "Sorry, no order(s)";
                            OrderDetails.put("empty", message);
                            OrderList.add(OrderDetails);
                            json1 = new Gson().toJson(code);
                            json2 = new Gson().toJson(OrderList);
                            json = "[" + json1 + "," + json2 + "]";
                        }
                    }
                    break;
                }
              
                case "GetOrderDetails": {
                    String orderid = (String) jsonParameter.get("orderid");
                    int OrderID = Integer.parseInt(orderid);
                    ArrayList<HashMap<String, String>> Productlist = new ArrayList<>();
                    HashMap<String, String> OrderDetails = BookingClass.GetOrderDetails(OrderID);
                    int UserID = Integer.parseInt(OrderDetails.get(Tables.OrderTable.UserID));
                    String UserName = UserClass.getUserName(UserID);
                    OrderDetails.put("CustomerName", UserName);
                    String UserPhone = UserClass.getUserPhone(UserID);
                    OrderDetails.put("CustomerPhone", UserPhone);
                    OrderDetails.put("CustomerID", "" + UserID);

                    String orderdetails = OrderDetails.get(Tables.OrderTable.ProductDetails);
                    if (!orderdetails.equals("")) {
                        String details[] = orderdetails.split(";");
                        for (String record : details) {
                            String productid = record.split(",")[0];
                            int ProductID = Integer.parseInt(productid);
                            HashMap<String, String> ProductDet = UserClass.GetProduct(ProductID);
                            String Quantity = record.split(",")[1];
                            ProductDet.put("Quantity", Quantity);
                            String Amount = record.split(",")[2];
                            ProductDet.put("Amount", Amount);
                            Productlist.add(ProductDet);
                        }
                        json1 = new Gson().toJson(OrderDetails);
                        json2 = new Gson().toJson(Productlist);
                        json = "[" + json1 + "," + json2 + "]";
                    } else {
                        break;
                    }
                    break;
                }
                case "ConfirmOrder": {
                    String OID = (String) jsonParameter.get("orderid");
                    int OrderID = Integer.parseInt(OID);
                    String userid = (String) jsonParameter.get("userid");
                    int UserID = Integer.parseInt(userid);
                    String usertype = (String) jsonParameter.get("usertype");
                    String Status = BookingClass.getOrderStatus(OrderID);
                    if (Status.equals("Pending")) {
                        if (usertype.equals("Admin")) {
                            String OrderStatus = "Delivered";
                            result = BookingClass.UpdateOrderSubStatus(OrderID, "Confirmed by GasClub");
                            BookingClass.UpdateOrderStatus(OrderID, OrderStatus);
                            if (result.equals("success")) {
                                result = BookingClass.CalculateAndUpdateUserDiscountPoint(OrderID, UserID);
                                BookingClass.UpdateDeliveryDateAndTime(OrderID);
                                String OrderNumber = BookingClass.getOrderNumber(OrderID);
                                String Subject = "Order Confirmed";
                                String Content = "Order " + OrderNumber + " has been delivered and payment confirmed by GasClub";
                                UserClass.sendMemberMessage(UserID, Content, Subject, 1);
                                String code = "200";
                                json1 = new Gson().toJson(code);
                                json2 = new Gson().toJson(result);
                                json = "[" + json1 + "," + json2 + "]";
                            } else {
                                String code = "400";
                                String message = "Sorry, no history";
                                json1 = new Gson().toJson(code);
                                json2 = new Gson().toJson(message);
                                json = "[" + json1 + "," + json2 + "]";
                            }
                        } else if (usertype.equals("User")) {
                            String OrderStatus = "Delivered";
                            result = BookingClass.UpdateOrderStatus(OrderID, OrderStatus);
                            String username = UserClass.getUserName(UserID);

                            BookingClass.UpdateOrderSubStatus(OrderID, "Confirmed by " + username);
                            if (result.equals("success")) {
                                result = BookingClass.CalculateAndUpdateUserDiscountPoint(OrderID, UserID);
                                BookingClass.UpdateDeliveryDateAndTime(OrderID);
                                String OrderNumber = BookingClass.getOrderNumber(OrderID);
                                String Subject = "Order Delivered";
                                String Content = "Order " + OrderNumber + " has been delivered to " + username;
                                UserClass.sendMemberMessage(1, Content, Subject, UserID);
                                String code = "200";
                                json1 = new Gson().toJson(code);
                                json2 = new Gson().toJson(result);
                                json = "[" + json1 + "," + json2 + "]";
                            } else {
                                String code = "400";
                                String message = "Sorry, no history";
                                json1 = new Gson().toJson(code);
                                json2 = new Gson().toJson(message);
                                json = "[" + json1 + "," + json2 + "]";
                            }
                        }
                    } else {
                        String code = "400";
                        String message = "Sorry, Order has already been confirmed";
                        json1 = new Gson().toJson(code);
                        json2 = new Gson().toJson(message);
                        json = "[" + json1 + "," + json2 + "]";
                    }
                    break;
                }
                case "AcceptOrder": {
                    String OID = (String) jsonParameter.get("orderid");
                    int OrderID = Integer.parseInt(OID);
                    String OrdersubStatus = "Pending Delivery";
                    String customeruserid = (String) jsonParameter.get("customeruserid");
                    int CustomerUserID = Integer.parseInt(customeruserid);
                    String Status = BookingClass.getOrderStatus(OrderID);
                    if (!Status.equals("Delivered")) {
                        result = BookingClass.UpdateOrderSubStatus(OrderID, OrdersubStatus);
                        if (result.equals("success")) {
                            String OrderNumber = BookingClass.getOrderNumber(OrderID);
                            String Subject = "Order Accepted";
                            String Content = "Order " + OrderNumber + " has been accepted and its pending delivery";
                            UserClass.sendMemberMessage(CustomerUserID, Content, Subject, 1);
                            String code = "200";
                            json1 = new Gson().toJson(code);
                            json2 = new Gson().toJson(CustomerUserID);
                            json = "[" + json1 + "," + json2 + "]";
                        } else {
                            String code = "400";
                            String message = "Sorry, no history";
                            json1 = new Gson().toJson(code);
                            json2 = new Gson().toJson(message);
                            json = "[" + json1 + "," + json2 + "]";
                        }
                    } else {

                    }

                    break;
                }
                case "CancelOrder": {
                    String oid = (String) jsonParameter.get("orderid");
                    String usertype = (String) jsonParameter.get("usertype");
                    int OrderID = Integer.parseInt(oid);
                    String userid = (String) jsonParameter.get("userid");
                    int UserID = Integer.parseInt(userid);
                    String Status = BookingClass.getOrderStatus(OrderID);
                    if (!Status.equals("Cancelled")) {
                        if (usertype.equals("User")) {
                            String Username = UserClass.getUserName(UserID);
                            result = BookingClass.CancelOrderRequest(OrderID, Username);
                            String Subject = "Order Cancelled";
                            String OrderNumber = BookingClass.getOrderNumber(OrderID);
                            String Content = "Order Number " + OrderNumber + " has been cancelled by " + Username;
                            UserClass.sendMemberMessage(1, Content, Subject, UserID);
                            String Content2 = "You cancelled Order Number " + OrderNumber;
                            UserClass.sendMemberMessage(UserID, Content2, Subject, 1);
                        } else {
                            result = BookingClass.CancelOrderRequest(OrderID, "GasClub");
                            String Subject = "Order Cancelled";
                            String OrderNumber = BookingClass.getOrderNumber(OrderID);
                            String Content = "Order Number " + OrderNumber + " has been cancelled by GasClub";
                            UserClass.sendMemberMessage(UserID, Content, Subject, 1);
                        }
                        String code = "200";
                        json1 = new Gson().toJson(code);
                        json2 = new Gson().toJson(result);
                        json = "[" + json1 + "," + json2 + "]";
                    } else {
                        String code = "400";
                        json1 = new Gson().toJson(code);
                        json2 = new Gson().toJson("Order has already been cancelled");
                        json = "[" + json1 + "," + json2 + "]";
                    }
                    break;
                }
                case "DeleteOrder": {
                    String oid = (String) jsonParameter.get("orderid");
                    String usertype = (String) jsonParameter.get("usertype");
                    int OrderID = Integer.parseInt(oid);
                    String Status = BookingClass.getOrderStatus(OrderID);
                    if (Status.equals("Cancelled") || Status.equals("Delivered")) {
                        if (usertype.equals("User")) {
                            result = BookingClass.DeleteOrder(OrderID);
                        } else {
                            result = BookingClass.DeleteOrder(OrderID);
                        }
                        String code = "200";
                        json1 = new Gson().toJson(code);
                        json2 = new Gson().toJson(result);
                        json = "[" + json1 + "," + json2 + "]";
                    } else {
                        String code = "400";
                        json1 = new Gson().toJson(code);
                        json2 = new Gson().toJson("Order has already been cancelled");
                        json = "[" + json1 + "," + json2 + "]";
                    }
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
