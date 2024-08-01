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

import java.awt.Container;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.net.URI;
import java.net.URISyntaxException;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft;
import org.java_websocket.drafts.Draft_6455;
import org.java_websocket.handshake.ServerHandshake;

public class ChatClient extends JFrame implements ActionListener {

  private static final long serialVersionUID = -6056260699202978657L;

  // 화면 구성요소, 공부할 필요 없음.
  private final JTextField uriField;
  private final JButton connect;
  private final JButton close;
  private final JTextArea ta;
  private final JTextField chatField;
  private final JComboBox draft;
  
  // 서버와 통신을 위한 클라이언트 소켓
  // 클라이언트는 서버와 달리 WebSocketServer 를 상속받아서 구현할 필요가 없음.
  private WebSocketClient cc;

  // 생성자
  public ChatClient(String defaultlocation) {
    super("WebSocket Chat Client");
    
    // 화면 요소
    Container c = getContentPane();
    GridLayout layout = new GridLayout();
    layout.setColumns(1);
    layout.setRows(6);
    c.setLayout(layout);

    Draft[] drafts = {new Draft_6455()};
    draft = new JComboBox(drafts);
    c.add(draft);

    uriField = new JTextField();
    uriField.setText(defaultlocation);
    c.add(uriField);

    connect = new JButton("Connect");
    connect.addActionListener(this);
    c.add(connect);

    close = new JButton("Close");
    close.addActionListener(this);
    close.setEnabled(false);
    c.add(close);

    JScrollPane scroll = new JScrollPane();
    ta = new JTextArea();
    scroll.setViewportView(ta);
    c.add(scroll);

    chatField = new JTextField();
    chatField.setText("");
    chatField.addActionListener(this);
    c.add(chatField);

    java.awt.Dimension d = new java.awt.Dimension(300, 400);
    setPreferredSize(d);
    setSize(d);

    addWindowListener(new java.awt.event.WindowAdapter() {
      @Override
      public void windowClosing(WindowEvent e) {
        if (cc != null) {
          // 화면이 닫힐 때 서버와의 통신을 종료.
          cc.close();
        }
        dispose();
      }
    });

    setLocationRelativeTo(null);
    setVisible(true);
  }
  // end of 생성자

  // 화면의 구성 요소에 대한 event handling
  public void actionPerformed(ActionEvent e) {

	// 서버버에 전송할 내용 입력
    if (e.getSource() == chatField) {
      if (cc != null) {
    	// 서버로 입력한 내용을 전송
        cc.send(chatField.getText());
        chatField.setText("");
        chatField.requestFocus();
      }

    // 서버와의 접속( connect 버튼 )
    } else if (e.getSource() == connect) {
      try {
        // cc = new ChatClient(new URI(uriField.getText()), area, ( Draft ) draft.getSelectedItem() );
          
    	  // 서버와의 접속을 위한 클라이언트 웹 소켓을 생성.
    	  // 접속할 서버의 정보 : ws://localhost:9001
    	  cc = new WebSocketClient(new URI(uriField.getText()), (Draft) draft.getSelectedItem()) {

    	  // 서버가 braodcast 한 데이터를 받아서, 화면에 출력.
          @Override
          public void onMessage(String message) {
            ta.append("got: " + message + "\n");
            ta.setCaretPosition(ta.getDocument().getLength());
          }

          @Override
          public void onOpen(ServerHandshake handshake) {
            ta.append("You are connected to ChatServer: " + getURI() + "\n");
            ta.setCaretPosition(ta.getDocument().getLength());
          }

          @Override
          public void onClose(int code, String reason, boolean remote) {
            ta.append(
                "You have been disconnected from: " + getURI() + "; Code: " + code + " " + reason
                    + "\n");
            ta.setCaretPosition(ta.getDocument().getLength());
            connect.setEnabled(true);
            uriField.setEditable(true);
            draft.setEditable(true);
            close.setEnabled(false);
          }

          @Override
          public void onError(Exception ex) {
            ta.append("Exception occurred ...\n" + ex + "\n");
            ta.setCaretPosition(ta.getDocument().getLength());
            ex.printStackTrace();
            connect.setEnabled(true);
            uriField.setEditable(true);
            draft.setEditable(true);
            close.setEnabled(false);
          }
        };

        close.setEnabled(true);
        connect.setEnabled(false);
        uriField.setEditable(false);
        draft.setEditable(false);
        cc.connect();
      } catch (URISyntaxException ex) {
        ta.append(uriField.getText() + " is not a valid WebSocket URI\n");
      }
    } else if (e.getSource() == close) {
      cc.close();
    }
  }

  public static void main(String[] args) {
    String location;
    if (args.length != 0) {
      location = args[0];
      System.out.println("Default server url specified: \'" + location + "\'");
    } else {
      //location = "ws://localhost:8887";
      location = "ws://localhost:9001";
      System.out.println("Default server url not specified: defaulting to \'" + location + "\'");
    }
    
    // 생성자 호출
    // 1. 화면을 구성 후 display 함.
    // 2. 사용자가 화면을 제어 => connect button을 클릭 => 웹 소켓 클라이언트 생성.
    // 3. 화면에서 서버로 전송할 데이터를 작성 후 전송.
    // 4. request 를 하지 않아도, 서버가 송신한 response 를 수신 후 화면에 출력.
    //    onMessage() 가 처리.
    // 5. 서버와의 통신을 종료( close button, window close )
    new ChatClient(location);
  }

}