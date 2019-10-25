package emp;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class UpdateEmpServ
 */
@WebServlet("/UpdateEmpServ")
public class UpdateEmpServ extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public UpdateEmpServ() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.getWriter().append("Served at: ").append(request.getContextPath());
		String empId = request.getParameter("empId");
		String salary = request.getParameter("salary");
		Employee emp = new Employee();
		emp.setEmployeeId(Integer.parseInt(empId));
		emp.setSalary(Integer.parseInt(salary));
		
		EmpDAO dao = new EmpDAO();
		dao.updateEmployee(emp);
	}
	
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
