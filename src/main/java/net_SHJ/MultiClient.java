package net_SHJ;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Scanner;

class SendThread extends Thread {
	Socket socket = null;
	String name;
	Scanner scanner = new Scanner(System.in);
	public SendThread(Socket socket, String name) {
		this.socket = socket;
		this.name = name;
	}
	
	@Override
	public void run() {
		try {
			PrintStream out = new PrintStream(socket.getOutputStream());
			out.println(name);
			out.flush();
			
			while(true) {
				String outputMessage = scanner.nextLine();
				out.println(outputMessage);
				out.flush();
				if("quit".equals(outputMessage)) break;
			}
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
}

public class MultiClient {

	public static void main(String[] args) {
		MultiClient multiclient = new MultiClient();
		multiclient.start();
	}
	
	public void start() {
		Socket socket = null;
		BufferedReader in = null;
		try {
			socket = new Socket("127.0.0.1", 7777);
			System.out.println("[서버와 연결되었습니다]");
			
			String name = "SHJ" + (int)(Math.random()*10);
			Thread sendThread = new SendThread(socket, name);
			sendThread.start();
			in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			
			while(in != null) {
				String inputMessage = in.readLine();
				System.out.println("From:"+inputMessage);
				if(("["+name+"]님이 나가셨습니다.").equals(inputMessage)) break;
			}
			
		} catch(IOException e) {
			System.out.println("[서버 접속 끊김]");
		} finally {
			try {
				socket.close();
			} catch(IOException e) {
				e.printStackTrace();
			}
		}
		System.out.println("[서버 연결종료]");
	}
}