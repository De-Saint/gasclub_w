/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gasclub.servlets;

import com.gasclub.classes.AdminClass;
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
public class AdminServlet extends HttpServlet {

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
            ArrayList<Integer> IDS = new ArrayList<>();
            String caseType = (String) jsonParameter.get("type");
            String json = "";
            String json1 = "";
            String json2 = "";
            String result = "";
            switch (caseType) {
                case "AddProduct": {
                    String catid = (String) jsonParameter.get("catid");
                    int categoryid = Integer.parseInt(catid);
                    String name = (String) jsonParameter.get("name");
                    String price = (String) jsonParameter.get("price");
                    String properties = (String) jsonParameter.get("properties");
                    int productid = AdminClass.AddProduct(categoryid, name, price, properties);
                    json = new Gson().toJson(productid);
                    break;
                }
                case "AddLocation": {
                    String name = (String) jsonParameter.get("name");
                    String fees = (String) jsonParameter.get("fees");
                    result = AdminClass.AddLocation(name, fees);
                    json = new Gson().toJson(result);
                    break;
                }
                case "EmptyCart": {
                    String cartid = (String) jsonParameter.get("cartid");
                    int CartID = Integer.parseInt(cartid);
                    result = BookingClass.EmptyCart(CartID);
                    json = new Gson().toJson(result);
                    break;
                }
                case "DeleteProduct": {
                    String productid = (String) jsonParameter.get("productid");
                    int ProductID = Integer.parseInt(productid);
                    result = AdminClass.DeleteProduct(ProductID);
                    json = new Gson().toJson(result);
                    break;
                }
                case "UpdateLocation": {
                    String id = (String) jsonParameter.get("id");
                    int locationid = Integer.parseInt(id);
                    String name = (String) jsonParameter.get("name");
                    String fees = (String) jsonParameter.get("fees");
                    result = AdminClass.UpdateLocation(locationid, name, fees);
                    json = new Gson().toJson(result);
                    break;
                }
                case "DeleteLocation": {
                    String locationid = (String) jsonParameter.get("locationid");
                    int LocationID = Integer.parseInt(locationid);
                    result = AdminClass.DeleteLocation(LocationID);
                    json = new Gson().toJson(result);
                    break;
                }
                case "getAllUsers": {
                    String countstr = (String) jsonParameter.get("count").toString();
                    int count = 0;
                    if (countstr != null) {
                        count = Integer.parseInt(countstr);
                    }
                    int end = 15;
                    IDS = AdminClass.getUserIds(count, end);

                    ArrayList<HashMap<String, String>> UserList = new ArrayList<>();
                    if (!IDS.isEmpty()) {
                        for (int id : IDS) {
                            HashMap<String, String> Userdetails = new HashMap<>();
                            Userdetails = UserClass.GetCustomerDetails(id);
                            if (!Userdetails.isEmpty()) {
                                Userdetails.put("count", "" + IDS.size());
                                UserList.add(Userdetails);
                            }
                        }
                        String code = "200";
                        json1 = new Gson().toJson(code);
                        json2 = new Gson().toJson(UserList);
                        json = "[" + json1 + "," + json2 + "]";
                    } else {
                        HashMap<String, String> Userdetails = new HashMap<>();
                        String code = "400";
                        String message = "Sorry, You do not have any user(s)";
                        Userdetails.put("empty", message);
                        UserList.add(Userdetails);
                        json1 = new Gson().toJson(code);
                        json2 = new Gson().toJson(UserList);
                        json = "[" + json1 + "," + json2 + "]";
                    }
                    break;
                }
                case "getLocation": {
                    ArrayList<Integer> ids = UserClass.getLocationIds();
                    HashMap<String, String> locations = new HashMap<>();
                    ArrayList<HashMap<String, String>> locationlist = new ArrayList<>();
                    if (!ids.isEmpty()) {
                        for (int i : ids) {
                            locations = UserClass.GetLocationName(i);
                            locationlist.add(locations);
                        }
                        json1 = new Gson().toJson("200");
                        json2 = new Gson().toJson(locationlist);
                        json = "[" + json1 + "," + json2 + "]";
                    } else {
                        ArrayList<HashMap<String, String>> locationList = new ArrayList<>();
                        json1 = new Gson().toJson("400");
                        json2 = new Gson().toJson(locationList);
                        json = "[" + json1 + "," + json2 + "]";
                    }
                    break;
                }
                case "GetProducts": {
                    ArrayList<Integer> productids = UserClass.getProductIds();
                    ArrayList<HashMap<String, String>> productlist = new ArrayList<>();
                    if (!productids.isEmpty()) {
                        for (int i : productids) {
                            HashMap<String, String> products = new HashMap<>();
                            products = UserClass.GetProduct(i);
                            productlist.add(products);
                        }
                        String code = "200";
                        json1 = new Gson().toJson(code);
                        json2 = new Gson().toJson(productlist);
                        json = "[" + json1 + "," + json2 + "]";
                    } else {
                        HashMap<String, String> productcats = new HashMap<>();
                        String code = "400";
                        String message = "Sorry, You do not have any Product(s)";
                        productcats.put("empty", message);
                        productlist.add(productcats);
                        json1 = new Gson().toJson(code);
                        json2 = new Gson().toJson(productlist);
                        json = "[" + json1 + "," + json2 + "]";
                    }
                    break;
                }
                case "GetRefillProducts": {
                    ArrayList<Integer> productids = UserClass.GetRefillProducts();
                    ArrayList<HashMap<String, String>> productlist = new ArrayList<>();
                    if (!productids.isEmpty()) {
                        for (int i : productids) {
                            HashMap<String, String> products = new HashMap<>();
                            products = UserClass.GetProduct(i);
                            productlist.add(products);
                        }
                        String code = "200";
                        json1 = new Gson().toJson(code);
                        json2 = new Gson().toJson(productlist);
                        json = "[" + json1 + "," + json2 + "]";
                    } else {
                        HashMap<String, String> productcats = new HashMap<>();
                        String code = "400";
                        String message = "Sorry, You do not have any Product(s)";
                        productcats.put("empty", message);
                        productlist.add(productcats);
                        json1 = new Gson().toJson(code);
                        json2 = new Gson().toJson(productlist);
                        json = "[" + json1 + "," + json2 + "]";
                    }
                    break;
                }
                case "GetAccessoriesProducts": {
                    ArrayList<Integer> productids = UserClass.GetAccessoriesProducts();
                    ArrayList<HashMap<String, String>> productlist = new ArrayList<>();
                    if (!productids.isEmpty()) {
                        for (int i : productids) {
                            HashMap<String, String> products = new HashMap<>();
                            products = UserClass.GetProduct(i);
                            productlist.add(products);
                        }
                        String code = "200";
                        json1 = new Gson().toJson(code);
                        json2 = new Gson().toJson(productlist);
                        json = "[" + json1 + "," + json2 + "]";
                    } else {
                        HashMap<String, String> productcats = new HashMap<>();
                        String code = "400";
                        String message = "Sorry, You do not have any Product(s)";
                        productcats.put("empty", message);
                        productlist.add(productcats);
                        json1 = new Gson().toJson(code);
                        json2 = new Gson().toJson(productlist);
                        json = "[" + json1 + "," + json2 + "]";
                    }
                    break;
                }
                case "getProductCategories": {
                    ArrayList<Integer> ids = AdminClass.getProductCategoryIds();
                    ArrayList<HashMap<String, String>> productcatlist = new ArrayList<>();
                    if (!ids.isEmpty()) {
                        for (int i : ids) {
                            HashMap<String, String> productcats = new HashMap<>();
                            productcats = AdminClass.GetProductCategory(i);
                            productcatlist.add(productcats);
                        }
                        String code = "200";
                        json1 = new Gson().toJson(code);
                        json2 = new Gson().toJson(productcatlist);
                        json = "[" + json1 + "," + json2 + "]";
                    } else {
                        String code = "400";
                        String message = "No Product";
                        json1 = new Gson().toJson(code);
                        json2 = new Gson().toJson(message);
                        json = "[" + json1 + "," + json2 + "]";
                    }
                    break;
                }
                case "getCustomerDetails": {
                    String userid = (String) jsonParameter.get("userid");
                    int UserID = Integer.parseInt(userid);
                    HashMap<String, String> UserDet = UserClass.GetCustomerDetails(UserID);
                    json = new Gson().toJson(UserDet);
                    break;
                }
                case "DeleteCustomer": {
                    String userid = (String) jsonParameter.get("userid");
                    int UserID = Integer.parseInt(userid);
                    result = AdminClass.DeleteCustomerAccount(UserID);
                    json = new Gson().toJson(result);
                    break;
                }
                case "Message": {
                    String memberid = (String) jsonParameter.get("userid");
                    int userid = Integer.parseInt(memberid);
                    ArrayList<Integer> list = new ArrayList<>();
                    String option = (String) jsonParameter.get("option");
                    HashMap<String, String> msgdetails = new HashMap<>();
                    if (option.equals("inbox")) {
                        list = AdminClass.getInboxMessageIDs(userid);
                    } else if (option.equals("sent")) {
                        list = AdminClass.getSentMessageIDs(userid);
                    }
                    ArrayList<HashMap<String, String>> msglist = new ArrayList<>();
                    if (!list.isEmpty()) {
                        for (int id : list) {
                            msgdetails = AdminClass.getMessageDetails(id);//
                            int senderid = Integer.parseInt(msgdetails.get(Tables.MessageTable.FromMemberID));
                            String sendername = AdminClass.getUserName(senderid);
                            msgdetails.put("sendername", sendername);
                            int recieverid = Integer.parseInt(msgdetails.get(Tables.MessageTable.ToMemberID));
                            String recievername = AdminClass.getUserName(recieverid);
                            msgdetails.put("recievername", recievername);
                            msglist.add(msgdetails);
                        }
                        String code = "200";
                        json1 = new Gson().toJson(code);
                        json2 = new Gson().toJson(msglist);
                        json = "[" + json1 + "," + json2 + "]";
                    } else {
                        String code = "400";
                        json1 = new Gson().toJson(code);
                        String message = "Sorry no message(s)";
                        json2 = new Gson().toJson(message);
                        json = "[" + json1 + "," + json2 + "]";
                    }
                    break;
                }
                case "MarkAsRead": {
                    String messageid = (String) jsonParameter.get("messageid");
                    int msgid = Integer.parseInt(messageid);
                    String res = AdminClass.MarkMessageAsRead(msgid);
                    if (res.equals("failed")) {
                        String message = "Something went wrong! try again Later";
                        json = new Gson().toJson(message);
                    } else {
                        String message = "Message Read";
                        json = new Gson().toJson(message);

                    }
                    break;
                }
                case "DeleteMessage": {
                    String messageid = (String) jsonParameter.get("messageid");
                    int msgid = Integer.parseInt(messageid);
                    String res = AdminClass.DeletMessage(msgid);
                    if (res.equals("failed")) {
                        String message = "Something went wrong! try again Later";
                        json = new Gson().toJson(message);
                    } else {
                        String message = "Message Deleted";
                        json = new Gson().toJson(message);

                    }
                    break;
                }

                case "MessageDetails": {
                    String messageid = (String) jsonParameter.get("messageid");
                    int MessageID = Integer.parseInt(messageid);
                    HashMap<String, String> det = new HashMap<>();
                    det = AdminClass.getMessageDetails(MessageID);
                    if (!det.isEmpty()) {
                        json = new Gson().toJson(det);
                    } else {
                        String message = "Something went wrong! try again Later";
                        json = new Gson().toJson(message);
                    }
                    break;
                }
                case "SendMessage": {
                    String fromuserid = (String) jsonParameter.get("fromuserid");
                    int FromUserID = Integer.parseInt(fromuserid);
                    String touserid = (String) jsonParameter.get("touserid");
                    int ToUserID = Integer.parseInt(touserid);
                    String title = (String) jsonParameter.get("msgTitle");
                    String bdy = (String) jsonParameter.get("msgBody");
                    if (bdy != null && bdy != "") {
                        UserClass.sendMemberMessage(ToUserID, bdy, title, FromUserID);
                        String message = "Message Sent";
                        json = new Gson().toJson(message);
                    } else {
                        String message = "Populate Empty Fields";
                        json = new Gson().toJson(message);
                    }
                    break;
                }
                case "getProductDetails": {
                    ArrayList<HashMap<String, String>> proplist = new ArrayList<>();
                    String productid = (String) jsonParameter.get("productid");
                    int ProductID = Integer.parseInt(productid);
                    HashMap<String, String> UserDet = UserClass.GetProduct(ProductID);

                    String Properties = UserDet.get(Tables.ProductTable.Properties);
                    String props[] = Properties.split(",");
                    for (String record : props) {
                        HashMap<String, String> PropDet = new HashMap<>();
                        String propName = record.split(":")[0];
                        String propValue = record.split(":")[1];
                        PropDet.put("PropName", propName);
                        PropDet.put("PropValue", propValue);
                        proplist.add(PropDet);
                    }
                    json1 = new Gson().toJson(UserDet);
                    json2 = new Gson().toJson(proplist);
                    json = "[" + json1 + "," + json2 + "]";
                    break;
                }
                case "UpdateProduct": {
                    String prodid = (String) jsonParameter.get("productid");
                    int ProductID = Integer.parseInt(prodid);
                    String name = (String) jsonParameter.get("name");
                    String price = (String) jsonParameter.get("price");
                    String properties = (String) jsonParameter.get("properties");
                    String productid = AdminClass.UpdateProduct(ProductID, name, price, properties);
                    json = new Gson().toJson(productid);
                    break;
                }
                case "GetAvailableBalance": {
                    ArrayList<Integer> DeliveredOrdersIDs = BookingClass.getDeliveredOrderIDS();
                    int totalamount = 0;
                    if (!DeliveredOrdersIDs.isEmpty()) {
                        for (int i : DeliveredOrdersIDs) {
                            String amount = BookingClass.getOrderAmount(i);
                            int Amount = Integer.parseInt(amount);
                            totalamount = totalamount + Amount;
                        }
                        json = new Gson().toJson(totalamount);
                    } else {
                        json = new Gson().toJson(totalamount);
                    }
                    break;
                }
                case "GetUsers": {
                    ArrayList<Integer> IDs = AdminClass.GetUserIds();
                    json = new Gson().toJson(IDs.size());
                    break;
                }
                case "GetInboxMessages": {
                    String memberid = (String) jsonParameter.get("userid");
                    int userid = Integer.parseInt(memberid);
                    ArrayList<Integer> IDs = AdminClass.getInboxMessageIDs(userid);
                    json = new Gson().toJson(IDs.size());
                    break;
                }
                case "GetSentMessages": {
                    String memberid = (String) jsonParameter.get("userid");
                    int userid = Integer.parseInt(memberid);
                    ArrayList<Integer> IDs = AdminClass.getSentMessageIDs(userid);
                    json = new Gson().toJson(IDs.size());
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
