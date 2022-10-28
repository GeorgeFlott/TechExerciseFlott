import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/GuessFlott")
public class GuessFlott extends HttpServlet {
   private static final long serialVersionUID = 1L;

   public GuessFlott() {
      super();
   }

   protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
      String keyword = request.getParameter("keyword");
      guess(keyword, response);
   }

   void guess(String keyword, HttpServletResponse response) throws IOException {
      response.setContentType("text/html");
      PrintWriter out = response.getWriter();
      String title = "Results";
      String docType = "<!doctype html public \"-//w3c//dtd html 4.0 " + //
            "transitional//en\">\n"; //
      out.println(docType + //
            "<html>\n" + //
            "<head><title>" + title + "</title></head>\n" + //
            "<body bgcolor=\"#f0f0f0\">\n" + //
            "<h1 align=\"center\">" + title + "</h1>\n");

      Connection connection = null;
      PreparedStatement preparedStatement = null;
      try {
         DBConnectionFlott.getDBConnection();
         connection = DBConnectionFlott.connection;

         /*if (keyword.isEmpty()) {
            String selectSQL = "SELECT * FROM MyTableFlott0910";
            preparedStatement = connection.prepareStatement(selectSQL);
         } else {
            String selectSQL = "SELECT * FROM MyTableFlott0910 WHERE EMAIL LIKE ?";*/
         String selectSQL = "SELECT * FROM MyTableTechExercise";
         preparedStatement = connection.prepareStatement(selectSQL);
         //preparedStatement.setString(1, keyword + "%");
         ResultSet rs = preparedStatement.executeQuery();

         while (rs.next()) {
            //int id = rs.getInt("id");
        	 System.out.println(rs.getString("numclicks"));
            int numClicks = Integer.parseInt(rs.getString("numclicks").trim());
            //String email = rs.getString("email").trim();
            //String phone = rs.getString("phone").trim();
            //String address = rs.getString("address").trim();

            /*if (keyword.isEmpty() || email.contains(keyword)) {
               out.println("ID: " + id + ", ");
               out.println("User: " + userName + ", ");
               out.println("Email: " + email + ", ");
               out.println("Phone: " + phone + ", ");
               out.println("Address: " + address + "<br>");
            }*/
            if (keyword.isEmpty()) {
            	out.println("Non-integer value entered.");
            }
            else
            	try {
            	if (Integer.parseInt(keyword) == numClicks)
            		out.println("Correct! The number of clicks is " + numClicks);	
            	else
            		out.println("Incorrect!");
            	}
            	catch (NumberFormatException e) {
            		out.println("Non-integer value entered.");
            	}
         }
         out.println("<a href=/webproject/guess_flott.html>Guess again?</a> <br>");
         out.println("</body></html>");
         rs.close();
         preparedStatement.close();
         
         String updateSQL = "update MyTableTechExercise set numclicks = numclicks + 1";
         PreparedStatement preparedStatement2 = connection.prepareStatement(updateSQL);
         preparedStatement2.execute();
         
         preparedStatement2.close();
         
         connection.close();
      } catch (SQLException se) {
         se.printStackTrace();
      } catch (Exception e) {
         e.printStackTrace();
      } finally {
         try {
            if (preparedStatement != null)
               preparedStatement.close();
         } catch (SQLException se2) {
         }
         try {
            if (connection != null)
               connection.close();
         } catch (SQLException se) {
            se.printStackTrace();
         }
      }
   }

   protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
      doGet(request, response);
   }

}
