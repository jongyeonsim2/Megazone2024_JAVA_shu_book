package com.iteratrlearning.shu_book.chapter_06.web_adapter;

/*
 * Copyright (c) 2010-2020 Nathan Rajlich
 *
 *  Permission is hereby granted, free of charge, to any person
 *  obtaining a copy of this software and associated documentation
 *  files (the "Software"), to deal in the Software without
 *  restriction, including without limitation the rights to use,
 *  copy, modify, merge, publish, distribute, sublicense, and/or sell
 *  copies of the Software, and to permit persons to whom the
 *  Software is furnished to do so, subject to the following
 *  conditions:
 *
 *  The above copyright notice and this permission notice shall be
 *  included in all copies or substantial portions of the Software.
 *
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 *  EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES
 *  OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 *  NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
 *  HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
 *  WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
 *  FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR
 *  OTHER DEALINGS IN THE SOFTWARE.
 */

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.util.Collections;
import org.java_websocket.WebSocket;
import org.java_websocket.drafts.Draft;
import org.java_websocket.drafts.Draft_6455;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;

/**
 * A simple WebSocketServer implementation. Keeps track of a "chatroom".
 */
public class ChatServer extends WebSocketServer {

  public ChatServer(int port) throws UnknownHostException {
    super(new InetSocketAddress(port));
  }

  public ChatServer(InetSocketAddress address) {
    super(address);
  }

  public ChatServer(int port, Draft_6455 draft) {
    super(new InetSocketAddress(port), Collections.<Draft>singletonList(draft));
  }

  @Override
  public void onOpen(WebSocket conn, ClientHandshake handshake) {
    conn.send("Welcome to the server!"); //This method sends a message to the new client
    broadcast("new connection: " + handshake
        .getResourceDescriptor()); //This method sends a message to all clients connected
    System.out.println(
        conn.getRemoteSocketAddress().getAddress().getHostAddress() + " entered the room!");
  }

  @Override
  public void onClose(WebSocket conn, int code, String reason, boolean remote) {
    broadcast(conn + " has left the room!");
    System.out.println(conn + " has left the room!");
  }

  @Override
  public void onMessage(WebSocket conn, String message) {
    broadcast(message);
    System.out.println(conn + ": " + message);
  }

  @Override
  public void onMessage(WebSocket conn, ByteBuffer message) {
    broadcast(message.array());
    System.out.println(conn + ": " + message);
  }


  public static void main(String[] args) throws InterruptedException, IOException {
    //int port = 8887; // 843 flash policy port
	int port = 9001;
    try {
      port = Integer.parseInt(args[0]);
    } catch (Exception ex) {
    }
    
    // WebSocketServer 를 상속받은 클래스로 객체 생성.
    ChatServer s = new ChatServer(port);
    
    // 다수의 클라이언트의 접속을 허용하기 위해서 서버를 Thread 로 시작함.
    // 대부분 추상화가 되어있어, 사용하기 편리함.
    s.start();
    System.out.println("ChatServer started on port: " + s.getPort());

    // 네트워크 스트림 : 클라이언트가 송신한 데이터를 읽는 처리
    BufferedReader sysin = new BufferedReader(new InputStreamReader(System.in));
    
    // 무한 반복
    // 서버 또는 클라이언트가 종료되는 시점을 알 수 없기 때문.
    while (true) {
    	
      // 스트림에서 클라이언트의 데이터을 읽어옴.
      String in = sysin.readLine();
      
      // resquest 가 없어도 response 가 되도록 함.
      // webSocket 의 특징. => 트위터와 비슷함.
      // WebSocketServer 가 client socket을 관리하고 있기 때문.
      s.broadcast(in);
      if (in.equals("exit")) {
        s.stop(1000);
        break;
      }
    }
  }

  @Override
  public void onError(WebSocket conn, Exception ex) {
    ex.printStackTrace();
    if (conn != null) {
      // some errors like port binding failed may not be assignable to a specific websocket
    }
  }

  @Override
  public void onStart() {
    System.out.println("Server started!");
    setConnectionLostTimeout(0);
    setConnectionLostTimeout(100);
  }

}