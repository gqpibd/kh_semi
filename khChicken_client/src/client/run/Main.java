package client.run;

import client.singleton.Singleton;
import client.view.Window_Account;
import client.view.Window_Review;

public class Main {

	public static void main(String[] args) {
		Singleton s = Singleton.getInstance(); 
<<<<<<< HEAD
		s.getComm().makeConnection(); // 소켓 연결을 먼저 한다.
		s.getMainView();
		//new Window_Account();
=======
		System.out.println("111");
		s.getComm().makeConnection(); // 소켓 연결을 먼저 한다.
		System.out.println("222");
		s.getMainView();
		//new Window_Account();
	
>>>>>>> branch 'yun_2' of https://github.com/gqpibd/kh_semi
	}

}
