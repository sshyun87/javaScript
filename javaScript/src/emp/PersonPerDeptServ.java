package emp;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@WebServlet("/PersonPerDeptServ")
public class PersonPerDeptServ extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public PersonPerDeptServ() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//		response.getWriter().append("Served at: ").append(request.getContextPath());
		JSONArray ary = new JSONArray();
		JSONObject obj = new JSONObject();
		EmpDAO dao = new EmpDAO();
		Map<String, Integer> list = dao.getPersonPerDept();
		Set<Map.Entry<String, Integer>> set = list.entrySet();
		for(Map.Entry<String, Integer> map : set) {
			System.out.println(map.getKey() + map.getValue());//부서명, 인원.
		
//			obj = new JSONObject();
			obj.put("name", map.getKey());
			obj.put("data", map.getValue());
			ary.add(obj);
			
		}
		PrintWriter out = response.getWriter();
		out.print(ary);
	} 

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
