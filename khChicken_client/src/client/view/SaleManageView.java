package client.view;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import client.singleton.Singleton;
import dto.OrderedMenuDto;

public class SaleManageView extends JFrame {
	
	private JTable jTable;
	private JScrollPane jScrPane;
	
	String columNames[] = {
			"번호", "메뉴타입",  "주문일자", "주문메뉴", "수량", "음료쿠폰", "총액"
	};
	
	
	public Object obj;
	Object rowData[][];
	
	DefaultTableModel model;
	
	ArrayList<Object> list = new ArrayList<Object>();
	
	public SaleManageView() {
		super("판매 내역");
		setLayout(null);
		
		Singleton s = Singleton.getInstance();
		
		
		// controller로 영수증 목록 취득
		list = s.getOrderCtrl().select();
		
		// 확인용
		for (int i = 0; i < list.size(); i++) {
			System.out.println(list.get(i));
		}
		
		JLabel label = new JLabel("판매 내역");
		label.setBounds(10, 10, 120, 15);
		add(label);
		
		
		
		int bbsNum = 1;
		
		rowData = new Object[list.size()][8];	// 테이블의 2차원배열
		
		for (int i = 0; i < list.size(); i++) {
			OrderedMenuDto dto = (OrderedMenuDto) list.get(i);
			int bev_price = 0;
			
			if(dto.getMenu_type().equals("drink") && dto.getCoupon() != 0) {
				bev_price = dto.getPrice() * dto.getCoupon();
			};
			//Date order_date, String id, String menu_type, String menu_name, int count, int coupon, int price
			rowData[i][0] = bbsNum;				// 글번호
			
			rowData[i][1] = dto.getOrder_date();// 주문일자
			
			rowData[i][2] = dto.getId();		// 주문자 아이디
			
			rowData[i][3] = dto.getMenu_type();	// 메뉴타입
			
			rowData[i][4] = dto.getMenu_name();	// 주문메뉴
			
			rowData[i][5] = dto.getCount();		// 수량
			
			rowData[i][6] = dto.getCoupon();	// 음료쿠폰
			
			rowData[i][7] = (dto.getPrice() * dto.getCount()) - bev_price;		// 총액
			bbsNum++;
		}
		
		// 테이블의 폭을 설정하기 위한 Model
		model = new DefaultTableModel(columNames, 0);
		model.setDataVector(rowData, columNames);
		
		// 테이블 생성
		jTable = new JTable(model);
		//jTable.addMouseListener(this);
		
		// 컬럼의 넓이를 설정
		jTable.getColumnModel().getColumn(0).setMaxWidth(50);	// 글번호 폭
		jTable.getColumnModel().getColumn(1).setMaxWidth(500);	// 주문일자 폭
		jTable.getColumnModel().getColumn(2).setMaxWidth(200);	// 주문메뉴 폭
		jTable.getColumnModel().getColumn(3).setMaxWidth(200);	// 수량 폭
		jTable.getColumnModel().getColumn(4).setMaxWidth(200);	// 음료쿠폰 폭
		jTable.getColumnModel().getColumn(5).setMaxWidth(200);	// 총액 폭
		
		// 테이블 안의 컬럼의 쓰기 설정(왼쪽, 오른쪽, 중간)
		DefaultTableCellRenderer celAlignCenter = new DefaultTableCellRenderer();
		celAlignCenter.setHorizontalAlignment(JLabel.CENTER);
		//"번호", "주문일자", "주문메뉴", "수량", "음료쿠폰", "총액"
		jTable.getColumn("번호").setCellRenderer(celAlignCenter);
		jTable.getColumn("주문일자").setCellRenderer(celAlignCenter);
		jTable.getColumn("주문메뉴").setCellRenderer(celAlignCenter);
		jTable.getColumn("수량").setCellRenderer(celAlignCenter);
		jTable.getColumn("음료쿠폰").setCellRenderer(celAlignCenter);
		jTable.getColumn("총액").setCellRenderer(celAlignCenter);
		
		jScrPane = new JScrollPane(jTable);
		jScrPane.setBounds(10, 50, 600, 300);
		add(jScrPane);
		
		
		
		
		
		
		
		
		
		
		
		
		setBounds(100, 100, 640, 480);
		getContentPane().setBackground(Color.lightGray);
		setVisible(true);
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);			
	}

}