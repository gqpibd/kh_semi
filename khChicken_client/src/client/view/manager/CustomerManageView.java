package client.view.manager;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import client.singleton.Singleton;
import dto.CustomerManageDto;
import dto.OrderedMenuDto;
import dto.ReviewDto;

public class CustomerManageView extends JFrame implements ActionListener, MouseListener{
	
	private JTable jTable;
	private JScrollPane jScrPane;
	private String columNames[] = { "번호", "아이디", "이름", "주소", "전화번호", "총 주문건수" };
	Object rowData[][];
	DefaultTableModel model;
	DefaultTableCellRenderer celAlignCenter; // 셀 가운데 정렬용
	JButton backBtn; // 돌아가기 버튼
	
	// 특정 회원의 주문이력을 받아오기 위한 변수
	private Object[] Obj_Rview = { "번호", "메뉴 이름", "주문한 날짜", "내가쓴 리뷰", "별점" };
	private Object rdats[][];
	
	
	public CustomerManageView() {
		super("고객 관리");
		setLayout(null);
		
		model = new DefaultTableModel() {
			@Override
			public boolean isCellEditable(int row, int column) {
				// TODO Auto-generated method stub
				return false;
			}
		};
		jTable = new JTable();
		celAlignCenter = new DefaultTableCellRenderer(); 
		celAlignCenter.setHorizontalAlignment(SwingConstants.CENTER);

		setTableByCustomerOrder();

		jScrPane = new JScrollPane(jTable);
		jScrPane.setBounds(10, 50, 600, 300);
		add(jScrPane);

		JLabel label = new JLabel("판매 내역");
		label.setBounds(10, 10, 120, 15);
		add(label);

		
		backBtn = new JButton("메인으로");
		backBtn.setBounds(500, 370, 90, 40);
		backBtn.addActionListener(this);
		add(backBtn);
		
		
		setBounds(100, 100, 640, 480);
		getContentPane().setBackground(Color.lightGray);
		setVisible(true);
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		
	}
	
	
	
	
	@Override
	public void actionPerformed(ActionEvent e) {
		String str = e.getActionCommand();
		Singleton s = Singleton.getInstance();
		
		if(str.equals("메인으로")) {
			s.getMemCtrl().manageView(this);
		} else if(str.equals("뒤로")) {
			setTableByCustomerOrder();
			backBtn.setText("메인으로");
		}
		
	}


	public void setTableByCustomerOrder() {
		Singleton s = Singleton.getInstance();

		// controller로 전체 주문자(건수별 정렬) 목록 취득
		ArrayList<CustomerManageDto> list = s.getCusCtrl().customerOrder();

		int bbsNum = 1;

		rowData = new Object[list.size()][6]; // 테이블의 2차원배열

		for (int i = 0; i < list.size(); i++) {
			CustomerManageDto dto1 = list.get(i);
			
			// A.ID, A.NAME, A.ADR, A.PHONE, B.주문건수
			rowData[i][0] = bbsNum; 				// 글번호
			rowData[i][1] = dto1.getId();			// 주문자 아이디
			rowData[i][2] = dto1.getName();			// 주문자 이름
			rowData[i][3] = dto1.getAdr();			// 주소
			rowData[i][4] = dto1.getPhone();		// 전화번호
			rowData[i][5] = dto1.getOrderCount();	// 주문건수
		
			bbsNum++;
		}

		// 테이블의 폭을 설정하기 위한 Model

		model.setDataVector(rowData, columNames);

		// 테이블 생성
		jTable.setModel(model);
		jTable.addMouseListener(this);

		// 컬럼의 넓이를 설정
		jTable.getColumnModel().getColumn(0).setMaxWidth(50); 	// 글번호 폭
		jTable.getColumnModel().getColumn(1).setMaxWidth(100);	// 아이디 폭
		jTable.getColumnModel().getColumn(2).setMaxWidth(100);	// 이름 폭
		jTable.getColumnModel().getColumn(3).setMaxWidth(500);	// 주소 폭
		jTable.getColumnModel().getColumn(4).setMaxWidth(300);	// 전화번호 폭
		jTable.getColumnModel().getColumn(5).setMaxWidth(100);	// 주문건수 폭

		for (int i = 0; i < model.getColumnCount(); i++) { // 칼럼 내용 가운데 정렬
			jTable.getColumnModel().getColumn(i).setCellRenderer(celAlignCenter);

		}
	}




	@Override
	public void mouseClicked(MouseEvent e) {
		
		int rowNum = jTable.getSelectedRow();
		// 마우스 클릭한 열의 id를 구함
		String selectedId = jTable.getValueAt(rowNum, 1) + "";
		
		// 선택된 고객의 과거 주문이력 전부 받아오기
		ReviewDto revDto = new ReviewDto();
		revDto.setUserId(selectedId);
		List<ReviewDto> R_List; // 자기가 시켜먹은것의 대한 정보를 뽑아올 리스트
		Singleton s = Singleton.getInstance();
		R_List= s.getRevCtrl().my_getList(revDto);
		System.out.println("받아온 R_List 길이 = " + R_List.size());
		
		
		// 테이블에 깔기
		rdats = new Object[R_List.size()][5]; // 테이블의 2차원배열
		int bbsNum = 1;
		//private Object[] Obj_Rview = { "아이디", "메뉴 이름", "주문한 날짜", "내가쓴 리뷰", "별점" };
		for (int i = 0; i < R_List.size(); i++) {
			ReviewDto rdto = R_List.get(i);
			
			rdats[i][0] = bbsNum;  //아이디
			rdats[i][1] = rdto.getMenuName(); //메뉴이름
			rdats[i][2] = rdto.getOrderDate(); //주문한 날짜
			rdats[i][3] = rdto.getReview(); // 내가쓴리뷰
			rdats[i][4] = rdto.getScore(); // 내가준별점
			
			bbsNum++;
		}
		
		// 테이블의 폭을 설정하기 위한 Model
		model.setDataVector(rdats, Obj_Rview);

		// 테이블 생성
		jTable.setModel(model);
		jTable.removeMouseListener(this);
		// 컬럼의 넓이를 설정
		jTable.getColumnModel().getColumn(0).setPreferredWidth(50);	// 아이디 폭
		jTable.getColumnModel().getColumn(1).setPreferredWidth(200);// 메뉴이름 폭
		jTable.getColumnModel().getColumn(2).setPreferredWidth(350);// 주문한 날짜 폭
		jTable.getColumnModel().getColumn(3).setPreferredWidth(800);// 내가쓴리뷰 폭
		jTable.getColumnModel().getColumn(4).setPreferredWidth(50);	// 내가준별점 폭

		for (int i = 0; i < model.getColumnCount(); i++) { // 칼럼 내용 가운데 정렬
			jTable.getColumnModel().getColumn(i).setCellRenderer(celAlignCenter);

		}
		
		 
		backBtn.setText("뒤로");
		
	}




	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}




	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}




	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}




	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	
	
	
	
	

}
