package emp;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import common.DAO;

public class EmpDAO {
	Connection conn = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	
	public Map<String, Integer> getPersonPerDept(){
		conn = DAO.getConnect();
		String sql = "select d.department_name, e.department_id, count(*) as cnt  " + 
				"from employees e, departments d " + 
				"where e.department_id = d.department_id " + 
				"group by d.department_name, e.department_id";
		Map<String, Integer> list = new HashMap<>();
		try {
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			while(rs.next()) {
				String dept = rs.getString("department_name");
				Integer cnt = rs.getInt("cnt");
				list.put(dept, cnt); 
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			try {
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return list;
	}
	
	public List<Employee>getJsonData(){
		conn = DAO.getConnect();
		String sql = "select first_name, last_name, salary, hire_date, email, job_id"
				+ " from emp_temp";
		List<Employee> list = new ArrayList<>();
		try {
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			while(rs.next()) {
				Employee emp = new Employee();
				emp.setFirstName(rs.getString("first_name"));
				emp.setLastName(rs.getString("last_name"));
				emp.setSalary(rs.getInt("salary"));
				emp.setHireDate(rs.getString("hire_date"));
				emp.setEmail(rs.getString("email"));
				emp.setJobId(rs.getString("job_id"));
				list.add(emp);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			try {
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return list;
	}

	public void updateEmployee(Employee emp) {
		conn = DAO.getConnect();
		String sql = "update emp_temp set salary = ? where employee_id = ?";
				try {
					pstmt = conn.prepareStatement(sql);
					pstmt.setInt(1, emp.getSalary());
					pstmt.setInt(2, emp.getEmployeeId());
					int r = pstmt.executeUpdate();
					System.out.println(r+"건이 변경 되었습니다.");
				} catch (SQLException e) {
					e.printStackTrace();
				} finally {
					try {
						conn.close();
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
		
	}
	
	public void deleteEmployee(int empId) {
		conn = DAO.getConnect();
		String sql = " delete from emp_temp where employee_id = ?";
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, empId);
			int r = pstmt.executeUpdate();
			System.out.println(r+"건이 삭제됨.");
			pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				conn.close();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}

		}

	}

	public Employee getEmployee(int empId) {
		conn = DAO.getConnect();
		String sql = "select * from emp_temp where employee_id = ?";
//		String sql1 = "{? = call get_dept_name(?)}";
		
		Employee emp = null;
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, empId);
			rs = pstmt.executeQuery();
//			CallableStatement cstmt = conn.prepareCall(sql1);
//			cstmt.registerOutParameter(1, java.sql.Types.VARCHAR);
//			cstmt.setInt(2,  empId);
//			cstmt.execute();
//			String deptName = cstmt.getString(1);
			if (rs.next()) {
				emp = new Employee();
				emp.setEmployeeId(rs.getInt("employee_id"));
				emp.setFirstName(rs.getString("first_name"));
				emp.setLastName(rs.getString("last_name"));
				emp.setEmail(rs.getString("email"));
				emp.setHireDate(rs.getString("hire_date"));
				emp.setJobId(rs.getString("job_id"));
				emp.setSalary(rs.getInt("salary"));
//				emp.setDeptName(deptName);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}

		}

		return emp;

	}

	public void insertEmpProc(Employee emp) {
		conn = DAO.getConnect();
		String sql = "{call add_new_member(?,?,?,?,?,?)}";
		try {
			CallableStatement cstmt = conn.prepareCall(sql);
			cstmt.setString(1, emp.getFirstName());
			cstmt.setString(2, emp.getLastName());
			cstmt.setInt(3, emp.getSalary());
			cstmt.setString(4, emp.getJobId());
			cstmt.setString(5, emp.getHireDate());
			cstmt.setString(6, emp.getEmail());
			cstmt.execute();

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

	}

	public void insertEmp(Employee emp) {
		conn = DAO.getConnect();
		String sql = "insert into emp_temp(employee_id," + " first_name, last_name, email, job_id,"
				+ " hire_date, salary)\r\n" + "values(employees_seq.nextval,?,?,?,?,?,?)";
		int rCnt = 0;
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(++rCnt, emp.getFirstName());
			pstmt.setString(++rCnt, emp.getLastName());
			pstmt.setString(++rCnt, emp.getEmail());
			pstmt.setString(++rCnt, emp.getJobId());
			pstmt.setString(++rCnt, emp.getHireDate());
			pstmt.setInt(++rCnt, emp.getSalary());
			int r = pstmt.executeUpdate();
			System.out.println(r + " 건이 입력되었습니다.");
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

	}

	public List<Employee> getEmpList() {
		List<Employee> list = new ArrayList<Employee>();
		conn = DAO.getConnect();
		String sql = "select * from emp_temp";
		Employee emp = null;
		try {
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				emp = new Employee();
				emp.setEmployeeId(rs.getInt("employee_id"));
				emp.setFirstName(rs.getString("first_name"));
				emp.setLastName(rs.getString("last_name"));
				emp.setHireDate(rs.getString("hire_date"));
				emp.setEmail(rs.getString("email"));
				emp.setSalary(rs.getInt("salary"));
				emp.setJobId(rs.getString("job_id"));
				list.add(emp);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}

		}
		return list;

	}

}
