package pingenerator.tvtelecom.com;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;

@WebServlet("/PinGenBatch")
public class PinGenBatch extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public PinGenBatch() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Logger LOG = Logger.getLogger(PinGenBatch.class.getName());
        request.setCharacterEncoding(Utils.CharacterEncoding);       
        String pinDigit = request.getParameter("pinDigit");
        String pinAmount = request.getParameter("pinAmount");


		HttpSession session = request.getSession(true);
		String userId = (String)session.getAttribute("userId");
		
LOG.log(Level.INFO,"queryString:{0}",new Object[]{userId + " " + pinDigit + " " + pinAmount});
		
		Connection con = null;
		Statement st = null;
		String sql ="insert into job (PINDIGIT,PINAMOUNT,STATUS,CREATOR,CREATEDDATE) values (" + pinDigit + "," + pinAmount + ",'I',"+ userId + ",CURRENT_TIMESTAMP)";
LOG.log(Level.INFO,"sql:{0}",new Object[]{sql});
		String result="failed";
		
		try {
			Context ctx = new InitialContext();
			DataSource ds = (DataSource)ctx.lookup("java:comp/env/jdbc/PinGen");

			con = ds.getConnection();
			st = con.createStatement();
			st.executeUpdate(sql);
			result = "succeed";
		} catch(NamingException | SQLException ex) {
LOG.log(Level.SEVERE, ex.getMessage(), ex);
			result = "failed";
		} finally {
		    try {
		        if (st != null) {st.close();}
		        if (con != null) {con.close();}
		    } catch (SQLException ex) {
LOG.log(Level.WARNING, ex.getMessage(), ex);
		    }
		}

		response.setContentType("application/json");
		response.setCharacterEncoding(Utils.CharacterEncoding);
		PrintWriter out = response.getWriter();
		out.print("{\"result\":\""+result+"\"}");
		out.flush();
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
