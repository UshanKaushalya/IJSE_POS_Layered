package lk.ijse.pos.servlet;

import lk.ijse.pos.bo.BoFactory;
import lk.ijse.pos.bo.custom.ItemBo;
import lk.ijse.pos.model.ItemDTO;

import javax.json.*;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;

@WebServlet(urlPatterns = {"/item"})
public class ItemServlet extends HttpServlet{

    ItemBo itemBo = (ItemBo) BoFactory.getBoFactory().getBO(BoFactory.BOTypes.ITEM);
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        try(PrintWriter writer = resp.getWriter()){

            resp.addHeader("Access-Control-Allow-Origin","*");
            resp.addHeader("Content-Type","application/json");

            Class.forName("com.mysql.jdbc.Driver");
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/javaeePosApp", "root", "ushan1234");

            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM Item");
            ResultSet resultSet = preparedStatement.executeQuery();

            JsonArrayBuilder allItems = Json.createArrayBuilder();

            while(resultSet.next()){

                String code = resultSet.getString("itemCode");
                String name = resultSet.getString("itemName");
                String qty = resultSet.getString("itemQty");
                String price = resultSet.getString("itemPrice");

                JsonObjectBuilder item = Json.createObjectBuilder();

                item.add("code", code);
                item.add("name", name);
                item.add("qty", qty);
                item.add("price", price);

                allItems.add(item.build());

            }

            writer.print(allItems.build());

        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }


    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        resp.addHeader("Access-Control-Allow-Origin","*");

        String itemCode = req.getParameter("code");
        String itemName = req.getParameter("description");
        String itemQty = req.getParameter("itemQty");
        String itemPrice = req.getParameter("unitPrice");

        ItemDTO itemDTO = new ItemDTO(itemCode, itemName, Integer.parseInt(itemQty), Integer.parseInt(itemPrice));

        try {

            itemBo.addCItem(itemDTO);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

    }


    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        resp.addHeader("Content-Type","application/json");
        resp.addHeader("Access-Control-Allow-Origin","*");

        JsonReader reader = Json.createReader(req.getReader());
        JsonObject jsonObject = reader.readObject();

        String code = jsonObject.getString("code");
        String name = jsonObject.getString("name");
        String qty = jsonObject.getString("qty");
        String price = jsonObject.getString("price");

        ItemDTO itemDTO = new ItemDTO(code, name, Integer.parseInt(qty), Integer.parseInt(price));

        try {

            itemBo.updateItem(itemDTO);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

    }


    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.addHeader("Access-Control-Allow-Origin","*");

        String code = req.getParameter("code");

        try {

            itemBo.deleteItem(code);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }


    }


    @Override
    protected void doOptions(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.addHeader("Access-Control-Allow-Origin","*");
        resp.addHeader("Access-Control-Allow-Methods","PUT");
        resp.addHeader("Access-Control-Allow-Methods","DELETE");
        resp.addHeader("Access-Control-Allow-Headers","content-type");
    }

}