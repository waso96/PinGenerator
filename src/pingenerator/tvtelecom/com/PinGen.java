package pingenerator.tvtelecom.com;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
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
import javax.sql.DataSource;

@WebServlet("/PinGen")
public class PinGen extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public PinGen() {
        super();
    }
    
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//response.getWriter().append("Served at: ").append(request.getContextPath());
        Logger LOG = Logger.getLogger(PinGen.class.getName());
        request.setCharacterEncoding(Utils.CharacterEncoding);
        String userId = request.getParameter("userId");        
        String pinDigit = request.getParameter("pinDigit");
        String pinAmount = request.getParameter("pinAmount");
LOG.log(Level.INFO,"queryString:{0}",new Object[]{userId + " " + pinDigit + " " + pinAmount});
		int jobId = 1;
		Connection con = null;
		
		Statement st1 = null;
		String sql1 ="select max(jobid) from job";
		ResultSet rs1 = null;
		
		String sql2 ="insert into job values (jobId," + pinDigit + "," + userId + ",CURRENT_TIMESTAMP)";
		
		PreparedStatement st3 = null;
		String sql3 = "insert into pin values (?,jobId)";
		//ResultSet rs3 = null;
		
		String result="failed";
		
		try {
			Context ctx = new InitialContext();
			DataSource ds = (DataSource)ctx.lookup("java:comp/env/jdbc/PinGen");
		
			con = ds.getConnection();
			st1 = con.createStatement();
			rs1 = st1.executeQuery(sql1);
			if (rs1.next()) {
				jobId = rs1.getInt("JOBID");
				jobId++;
			}
            if (rs1 != null) {rs1.close();}
            sql2.replace("jobId", Integer.toString(jobId));
LOG.log(Level.INFO,"sql2:{0}",new Object[]{sql2});
            st1.executeUpdate(sql2);
            if (st1 != null) {st1.close();}
            
            sql3.replace("jobId", Integer.toString(jobId));
            st3 = con.prepareStatement(sql3);
            long amount = Long.parseLong(pinAmount);
            for (int i = 1; i <= amount; i++) {
                do {
                    st3.setString(1, randomNumber(Integer.parseInt(pinDigit)));
                } while (st3.executeUpdate() != 1);
            }

            result = "succeed";
LOG.log(Level.INFO,"success - jobId:{0} Digit:{1} Amount:{2}",new Object[]{jobId,pinDigit,pinAmount});
		} catch(NamingException | SQLException ex) {
LOG.log(Level.SEVERE, ex.getMessage(), ex);
			result = "failed";
		} finally {
            try {
                if (rs1 != null) {rs1.close();}
                if (con != null) {con.close();}
            } catch (SQLException ex) {
LOG.log(Level.WARNING, ex.getMessage(), ex);
            }
		}

		response.setContentType("application/json");
		response.setCharacterEncoding(Utils.CharacterEncoding);
		PrintWriter out = response.getWriter();
		out.print("{\"jobId\":"+jobId+",\"result\":\""+result+"\"}");
		out.flush();
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}
	
	private String randomNumber(int digit) {
		long res;
		long upperbound = 0;
		long x = 9;
		for (int i = 1; i <= digit; i++) {
			upperbound = x + upperbound;
			x = 9 * 10^i;
		}
		long lowerbound = 0;
		res = (long)(Math.random() * ((upperbound - lowerbound) + 1) + lowerbound);
		return String.format("%0" + digit + "d", res);
	}

}
