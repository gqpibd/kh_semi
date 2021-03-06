package server.dao;

import java.net.Socket;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import dto.BestSaleMenuDto;
import dto.OrderedMenuDto;
import server.communicator.SocketWriter;
import server.dao.interfaces.SelectionsDaoImpl;
import server.db.DBClose;
import server.db.DBConnection;

public class SelectionsDao implements SelectionsDaoImpl { // 특수한 Select를 모아놓았다.
	public static final int DATE = 0;
	public static final int SALES = 1;
	public static final int SCORE = 2;
	public static final int CUSTOMER = 3;
	public static final int ADDRESS = 4;

	public void execute(int number, Object dto, Socket sock) {

		switch (number) {
		case DATE: // selectByDate (날짜순)
			selectByDate(sock);
			System.out.println("날짜순 으로 판매 데이터를 불러왔습니다");
			break;
		case SALES: // selectBySalse (매출순)
			selectBySalse(sock);
			System.out.println("매출순으로 판매 데이터를 불러왔습니다");
			break;
		case SCORE: // selectByScore (별점순)
			selectByScore(sock);
			System.out.println("별점순으로 판매 데이터를 불러왔습니다");
			break;
		case CUSTOMER: // selectCustomerOrder (회원관리)
			selectCustomerOrder(sock);
			System.out.println("회원정보를 불러왔습니다");
			break;
		case ADDRESS: // 주소 검색
			selectAddress(dto.toString(), sock);
			System.out.println("주소 정보를 불러왔습니다");
		}
	}

	// 날짜순
	public void selectByDate(Socket sock) {

		String sql = " SELECT DISTINCT A.ORDER_DATE, A.ID, B.MENU_TYPE,  A.MENU_NAME, A.COUNTS, A.BEV_COUPON, B.PRICE "
				+ " FROM ORDER_DETAIL A, MENU B " + " WHERE A.MENU_NAME = B.MENU_NAME "
				+ " ORDER BY A.ORDER_DATE DESC ";

		Connection conn = null;
		PreparedStatement psmt = null;
		ResultSet rs = null;

		ArrayList<OrderedMenuDto> list = new ArrayList<>();

		try {
			conn = DBConnection.getConnection();
			psmt = conn.prepareStatement(sql);
			rs = psmt.executeQuery();

			while (rs.next()) {
				OrderedMenuDto omd = new OrderedMenuDto(rs.getDate("ORDER_DATE"), rs.getString("ID"),
						rs.getString("MENU_TYPE"), rs.getNString("MENU_NAME"), rs.getInt("COUNTS"),
						rs.getInt("BEV_COUPON"), rs.getInt("PRICE"));
				list.add(omd);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			// 담은 리스트를 소켓에 실어 보내자!
			DBClose.close(psmt, conn, rs);
		}

		SocketWriter.Write(sock, list);
	}

	// 매출순
	public void selectBySalse(Socket sock) {

		String sql = " SELECT b.menu_type, A.menu_name, B.price, A.판매량, A.사용쿠폰, (B.PRICE*(A.판매량-A.사용쿠폰)) 총판매액, B.AVG_RATE "
				+ " FROM (SELECT 정렬.menu_name , 정렬.판매량 , 정렬.쿠폰 사용쿠폰 "
					  + " FROM(SELECT menu_name , SUM(counts) 판매량, SUM(BEV_COUPON) 쿠폰 " 
					  + " FROM ORDER_DETAIL "
					  + " GROUP BY menu_name) 정렬) A, MENU B " 
			    + " WHERE A.menu_name = B.MENU_NAME "
				+ " ORDER BY (B.PRICE*A.판매량) DESC ";

		Connection conn = null;
		PreparedStatement psmt = null;
		ResultSet rs = null;

		ArrayList<BestSaleMenuDto> list = new ArrayList<>();

		try {
			conn = DBConnection.getConnection();
			psmt = conn.prepareStatement(sql);
			rs = psmt.executeQuery();
			while (rs.next()) {
				// menu_type, menu_name, price, total_sale, total_coupon, total_price, score
//				BestSaleMenuDto omd = new BestSaleMenuDto(rs.getString("MENU_TYPE"), rs.getString("MENU_NAME"),
//						rs.getInt("PRICE"), rs.getInt("판매량"), rs.getInt("사용쿠폰"), rs.getInt("총판매액"), 0);
				BestSaleMenuDto omd = new BestSaleMenuDto(rs.getString(1), rs.getString(2),
						rs.getInt(3), rs.getInt(4), rs.getInt(5), rs.getInt(6), rs.getInt(7));
				list.add(omd);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBClose.close(psmt, conn, rs);
		}

		SocketWriter.Write(sock, list);
	}

	// 별점순
	public void selectByScore(Socket sock) {

		String sql = " SELECT b.menu_type, A.menu_name, B.price, A.판매량, A.사용쿠폰, (B.PRICE*A.판매량) 총판매액, A.별점 "
				+ " FROM (SELECT 정렬.menu_name , 정렬.판매량 , 정렬.쿠폰 사용쿠폰, 정렬.별점 "
				+ " FROM(SELECT menu_name , SUM(counts) 판매량, SUM(BEV_COUPON) 쿠폰, ROUND(AVG(SCORE), 2) 별점 "
				+ " FROM ORDER_DETAIL " + " GROUP BY menu_name " + " HAVING AVG(SCORE) IS NOT NULL "
				+ " ORDER BY AVG(SCORE) DESC) 정렬) A, MENU B " + " WHERE A.menu_name = B.MENU_NAME ";

		Connection conn = null;
		PreparedStatement psmt = null;
		ResultSet rs = null;

		ArrayList<BestSaleMenuDto> list = new ArrayList<>();

		try {
			conn = DBConnection.getConnection();
			psmt = conn.prepareStatement(sql);
			rs = psmt.executeQuery();
			while (rs.next()) {
				// b.menu_type, A.menu_name, B.price, A.판매량, A.사용쿠폰, (B.PRICE*A.판매량) 총판매액, A.별점
				BestSaleMenuDto omd = new BestSaleMenuDto(rs.getString("MENU_TYPE"), rs.getString("MENU_NAME"),
						rs.getInt("PRICE"), rs.getInt("판매량"), rs.getInt("사용쿠폰"), rs.getInt("총판매액"), rs.getDouble("별점"));
				list.add(omd);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBClose.close(psmt, conn, rs);
		}

		SocketWriter.Write(sock, list);
	}

	// 회원정보
	public void selectCustomerOrder(Socket sock) {

		String sql = " SELECT A.ID, A.NAME, A.ADR, A.PHONE, B.주문건수 " + " FROM MEMBER A, (SELECT A.ID, COUNT(*) 주문건수 "
				+ " FROM ORDER_DETAIL A, MENU B " + " WHERE A.MENU_NAME = B.MENU_NAME AND B.MENU_TYPE = '메인' "
				+ " GROUP BY ID " + " ORDER BY COUNT(*) DESC) B " + " WHERE A.ID = B.ID ";

		Connection conn = null;
		PreparedStatement psmt = null;
		ResultSet rs = null;

		ArrayList<String> list = new ArrayList<>();

		try {
			conn = DBConnection.getConnection();
			psmt = conn.prepareStatement(sql);
			rs = psmt.executeQuery();

			while (rs.next()) {
				// A.ID, A.NAME, A.ADR, A.PHONE, B.주문건수
				list.add(rs.getString("ID") + "__" + rs.getString("NAME") + "__" + rs.getString("ADR") + "__"
						+ rs.getString("PHONE") + "__" + rs.getInt("주문건수"));
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBClose.close(psmt, conn, rs);
		}

		SocketWriter.Write(sock, list);
	}

	// 주소 정보
	public void selectAddress(Object obj, Socket sock) {
		String sql = "SELECT DISTINCT SIDO, SIGUNGU, LOAD, EUBMEONDONG" + " FROM LOADNAME_ADD " + " WHERE LOAD LIKE '%"+obj.toString()+"%' " +
					" ORDER BY LOAD ";

		Connection conn = null;
		PreparedStatement psmt = null;
		ResultSet rs = null;
		ArrayList<String> list = new ArrayList<>();
		try {

			conn = DBConnection.getConnection();
			psmt = conn.prepareStatement(sql);
			//psmt.setString(1, obj.toString());
			rs = psmt.executeQuery();

			while (rs.next()) {
				if (rs.getString(4) == null) {
					//list.add(rs.getString(1) + " " + rs.getString(2) + " " + rs.getString(3));
				} else {
					list.add(rs.getString(1) + " " + rs.getString(2) + " " + rs.getString(3) + " (" + rs.getString(4)
							+ ")");
				}
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBClose.close(psmt, conn, rs);
		}

		SocketWriter.Write(sock, list);

	}

}
