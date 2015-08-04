import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;
public class TCPDesktopServer implements Runnable {

	public static final String SERVERIP = "127.0.0.1";

	public static final int SERVERPORT = 1818;

	@SuppressWarnings("deprecation")
	public void run() {

		try {

			// System.out.println("S: Connecting...");

			System.out.println("服务器:连接中...");
			ServerSocket serverSocket = new ServerSocket(SERVERPORT);

			while (true) {

				Socket client = serverSocket.accept();

				System.out.println("服务器: 正在接收来自客户端的数据...");

				try {

					BufferedReader in = new BufferedReader(
							new InputStreamReader(client.getInputStream()));

					String strall = in.readLine();
					SimpleDateFormat formatter = new SimpleDateFormat(
							"yyyyMMddhhmmssSSS");
					String sss[] = new String[3];
					sss = strall.split(",");
					String str = sss[0];
					String dateString = sss[1];
					Date d1 = formatter.parse(dateString);
					System.out.println("服务器: 收到手机设备码: '" + str + "'");
					
					//返回签到消息到客户端
					OutputStream os = client.getOutputStream();
					OutputStreamWriter osw = new OutputStreamWriter(os);
					PrintWriter pw = new PrintWriter(osw,true);
					
					pw.println("签到成功，欢迎上课！");
					
					try {

						String dbURL = "jdbc:mysql://localhost:3306/student";
						String dbUser = "root";
						String dbPassword = "root";
						Connection cn = null;
						cn = DriverManager.getConnection(dbURL, dbUser,
								dbPassword);
						Statement stmt = cn.createStatement();
						String sql = "select * from student where simei='"
								+ str + "'"; 										// 查询语句
						ResultSet rs = stmt.executeQuery(sql);
						String sname = null;
						String sclass = null;
						String sgrade = null;
						int firstSign;
						int secondSign;
						int thirdSign;
						int fourthSign;
						int fifthSign;					
						if (rs.next()) {

							sname = rs.getString(3) ;
							sclass = rs.getString(4);
							sgrade = rs.getString(5);
							firstSign = rs.getInt(8);
							secondSign	=	rs.getInt(11);
							thirdSign = rs.getInt(14);
							fourthSign = rs.getInt(17);
							fifthSign = rs.getInt(20);
							java.util.Locale locale = java.util.Locale.CHINA;
							SimpleDateFormat formatter2 = new SimpleDateFormat(
									"yyyyMMddhhmmssSSS", locale);
							Date currentTime2 = new Date();
							String dateString2 = formatter.format(currentTime2);

							Date d2 = formatter2.parse(dateString2);
							long diff = d2.getTime() - d1.getTime();
							if((d1.getHours()>=8)&&(d1.getHours()<10)){
								String sql2 = "UPDATE student SET firstSign=1 WHERE (firstSign=0 AND simei='"+str +"')";
								stmt.executeUpdate(sql2);
								if(0==firstSign){
									System.out.println("服务器:年级           "+ sgrade +"     班级                "+ sclass + "学生                     " + sname + "签到成功.        " + "耗时:"
											+ diff + "毫秒.    " + "签到时间:     " + currentTime2);
								}else{
									System.out.println("你已经签到，请不要重复签到！");
								}
							}else if((d1.getHours()>=10)&&(d1.getHours()<11)){
								String sql2 = "UPDATE student SET secondSign=1 WHERE (secondSign=0 AND simei='"+str +"')";
								stmt.executeUpdate(sql2);
								if(0==secondSign){
									System.out.println("服务器:年级           "+ sgrade +"     班级                "+ sclass + "学生                     " + sname + "签到成功.        " + "耗时:"
											+ diff + "毫秒.    " + "签到时间:     " + currentTime2);
								}else{
									System.out.println("你已经签到，请不要重复签到！");
								}
							}else if((d1.getHours()>=1)&&(d1.getHours()<3)){
								String sql2 = "UPDATE student SET thirdSign=1 WHERE (thirdSign=0 AND simei='"+str +"')";
								stmt.executeUpdate(sql2);
								if(0==thirdSign){
									System.out.println("服务器:年级           "+ sgrade +"     班级                "+ sclass + "学生                     " + sname + "签到成功.        " + "耗时:"
											+ diff + "毫秒.    " + "签到时间:     " + currentTime2);
								}else{
									System.out.println("你已经签到，请不要重复签到！");
								System.out.println("服务器:年级           "+ sgrade +"     班级                "+ sclass + "学生                     " + sname + "签到成功.        " + "耗时:"
										+ diff + "毫秒.    " + "签到时间:     " + currentTime2);
								}
							}else if((d1.getHours()>=3)&&(d1.getHours()<6)){
								String sql2 = "UPDATE student SET fourthSign=1 WHERE (fourthSign=0 AND simei='"+str +"')";
								stmt.executeUpdate(sql2);
								if(0==fourthSign){
									System.out.println("服务器:年级           "+ sgrade +"     班级                "+ sclass + "学生                     " + sname + "签到成功.        " + "耗时:"
											+ diff + "毫秒.    " + "签到时间:     " + currentTime2);
								}else{
									System.out.println("你已经签到，请不要重复签到！");
								}								
							}else if((d1.getHours()>=6)&&(d1.getHours()<8)){
								String sql2 = "UPDATE student SET fifthSign=1 WHERE (fifthSign=0 AND simei='"+str +"')";
								stmt.executeUpdate(sql2);
								if(0==fifthSign){
									System.out.println("服务器:年级           "+ sgrade +"     班级                "+ sclass + "学生                     " + sname + "签到成功.        " + "耗时:"
											+ diff + "毫秒.    " + "签到时间:     " + currentTime2);
								}else{
									System.out.println("你已经签到，请不要重复签到！");
								}
							}else{
								System.out.println("不在上课时间段");
							}

						}
						rs.close();
						stmt.close();
						cn.close();

					} catch (SQLException e) {

						e.printStackTrace();
					}

				} catch (Exception e) {

					System.out.println("S: Error");

					e.printStackTrace();

				} finally {

					client.close();

					// System.out.println("S: Done.");

				}

			}

		} catch (Exception e) {

			System.out.println("S: Error");

			e.printStackTrace();
		}
	}

	public static void main(String a[]) {
		try {
			Class.forName("com.mysql.jdbc.Driver");

		} catch (Exception e) {
			// TODO Auto-generated catch block"
			e.printStackTrace();
		}
		Thread desktopServerThread = new Thread(new TCPDesktopServer());

		desktopServerThread.start();
	}
}
