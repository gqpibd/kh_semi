package server.communicator;

import java.io.EOFException;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;
<<<<<<< HEAD

=======
>>>>>>> refs/remotes/origin/daseul
import dto.MemberDto;
import dto.MenuShowDto;
import dto.OrderedMenuDto;
import dto.ReviewDto;
import server.singleton.Singleton;

public class ReadThread extends Thread {

	Socket sock;

	public ReadThread(Socket sock) {
		this.sock = sock;
	}

	@Override
	public void run() {
		super.run();
		Singleton s = Singleton.getInstance();
		ObjectOutputStream oos = null;
		ObjectInputStream ois = null;

		try {
<<<<<<< HEAD
			while (true) {
				ois = new ObjectInputStream(sock.getInputStream()); // dto받기
=======
			ois = new ObjectInputStream(sock.getInputStream()); // dto받기
			while (true) {
>>>>>>> refs/remotes/origin/daseul
				int number = ois.readInt();
				System.out.println(number);

				switch (number) {
				case 0: // insert
				case 1: // select
				case 2: // delete
				case 3: // update

					Object obj = ois.readObject();
					// 어떤 dto 인지 구분
					if (obj instanceof MemberDto) {

					} else if (obj instanceof MenuShowDto) {
						System.out.println("MenuShowDto received");
						s.getMenuCtrl().execute(number, (MenuShowDto) obj, sock);
<<<<<<< HEAD
=======
						
>>>>>>> refs/remotes/origin/daseul
					} else if (obj instanceof OrderedMenuDto) {

					} else if (obj instanceof ReviewDto) {

					}
					break;

				case 4: // menu 불러오기

				case 5: // review 불러오기

				case 6: // 전체매출 불러오기

				case 7: // 내 주문내역 불러오기

				}
				sleep(100);
			}
		} catch (EOFException e) {
			System.out.println("다 읽음");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (SocketException e){
<<<<<<< HEAD
		}catch (IOException e) {
			System.out.println("소켓이 닫혔습니다");
=======
			System.out.println("소켓이 닫힙니다");
		}catch (IOException e) {
			e.printStackTrace();
>>>>>>> refs/remotes/origin/daseul
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} 

	}

}
