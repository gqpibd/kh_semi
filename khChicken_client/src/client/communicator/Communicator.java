package client.communicator;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;

import javax.imageio.ImageIO;
 
public class Communicator {
	public static int INSERT = 0;
	public static int DELETE = 1;
	public static int UPDATE = 2;
	public static int SELECT = 3;
	private Socket sock;

	public void makeConnection() {
		try {
			InetSocketAddress sockAddr = new InetSocketAddress("192.168.30.35", 6000); // 포트번호는 서버의 포트번호와 동일하게 해준다.
			sock = new Socket();
			sock.connect(sockAddr);
			System.out.println("연결성공");

		} catch (IOException e) {
			e.printStackTrace();
		}
	}	
	
	public void SendMessage(int number, Object o) {
		ObjectOutputStream oos = null;
		try {
			oos = new ObjectOutputStream(sock.getOutputStream());

			oos.writeInt(number);
			oos.writeObject(o);

			oos.flush();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void sendImage(String path) {
		ObjectOutputStream oos = null;
		try {
			oos = new ObjectOutputStream(sock.getOutputStream());

			BufferedImage im = ImageIO.read(new File(path));
			System.out.println(im.toString());
			ImageIO.write(im, "jpg", oos);
			oos.flush();
			oos.close();
			makeConnection();
			System.out.println("이미지 보냄");

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public Object receiveObject() {
		ObjectInputStream ois = null;
		Object obj = null;
		try {
			ois = new ObjectInputStream(sock.getInputStream());
			obj = ois.readObject();
		} catch (IOException e) {
			System.out.println("객체 입력에 오류가 발생했습니다.");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}

		return obj;
	}	

}
