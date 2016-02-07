package pingenerator.tvtelecom.com;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
        String pinDigit = request.getParameter("pinDigit");
        String pinAmount = request.getParameter("pinAmount");
LOG.log(Level.INFO,"test1:{0}",new Object[]{Utils.dbpath() + " " + pinDigit + " " + pinAmount});

		response.setContentType("application/json");
		response.setCharacterEncoding(Utils.CharacterEncoding);
		PrintWriter out = response.getWriter();
		out.print("{\"jobId\": 445}");
		out.flush();
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
