package com.iteratrlearning.shu_book.chapter_06.web_adapter;

import com.iteratrlearning.shu_book.chapter_06.database.DatabaseTwootRepository;
import com.iteratrlearning.shu_book.chapter_06.database.DatabaseUserRepository;
import com.iteratrlearning.shu_book.chapter_06.TwootRepository;
import com.iteratrlearning.shu_book.chapter_06.Twootr;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.DefaultServlet;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;

// {"cmd": "logon", "userName": "Joe", "password": "ahc5ez2aiV" }
// {"cmd": "logon", "userName": "John", "password": "ahc5ez2aiV" }
// {"cmd": "follow", "userName": "John" }
// {"cmd": "sendTwoot", "content": "Hello World" }

public class TwootrServer extends WebSocketServer {
    public static final int STATIC_PORT = 8000;
    public static final int WEBSOCKET_PORT = 9000;

    private static final String USER_NAME = "Joe";
    private static final String PASSWORD = "ahc5ez2aiV";
    private static final String OTHER_USER_NAME = "John";

    public static void main(String[] args) throws Exception {

    	// TCP/IP 통신 => socket 통신
    	/*
    	 * 트우터 서버를 네트워크 상에서 기동
    	 * - 주소할당이 필요. ip 주소, port 로 설정.
    	 * - localhost : 127.0.0.1
    	 */
        var websocketAddress = new InetSocketAddress("localhost", WEBSOCKET_PORT);
        var twootrServer = new TwootrServer(websocketAddress);
        twootrServer.start();

        
        
        
        /*
         * 테스트용 
         * 
         * 서버
         *     - http(sevlet) server
         *       트위터 서비스를 사용할 수 있는 웹페이지(index.html)
         *       => 트위트 서비스를 사용하기 위해서 트위터 server 로 접속
         *       
         *     - 트위터 server
         *     
         *     - 서버의 구분
         *       트위트 서버와 웹서버는 ip는 동일하지만, port 가 다름.(프로세스가 다름)
         *       
        System.setProperty("org.eclipse.jetty.LEVEL", "INFO");

        var context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.setResourceBase(System.getProperty("user.dir") + "/src/main/webapp");
        context.setContextPath("/");

        ServletHolder staticContentServlet = new ServletHolder(
            "staticContentServlet", DefaultServlet.class);
        staticContentServlet.setInitParameter("dirAllowed", "true");
        context.addServlet(staticContentServlet, "/");

        var jettyServer = new Server(STATIC_PORT);
        jettyServer.setHandler(context);
        jettyServer.start();
        jettyServer.dumpStdErr();
        jettyServer.join();
        */
    }

    private final TwootRepository twootRepository = new DatabaseTwootRepository();
    // 트위터 기능을 구현한 핵심 클래스.
    // Twootr 객체를 생성시, 데이터베이스 저장소 정보를 전달.
    private final Twootr twootr = new Twootr(new DatabaseUserRepository(), twootRepository);
    // 서버에 접속한 클라이언트를 관리
    private final Map<WebSocket, WebSocketEndPoint> socketToEndPoint = new HashMap<>();

    // main 함수에서 생성자를 호출해서 트우터 서버를 기동
    public TwootrServer(final InetSocketAddress address) {
        super(address, 1);

        // 트우터 서비스를 위한 기본 데이터 저장
        // 트위터 서비스를 위해서 사용자 2명을 등록.
        twootr.onRegisterUser(USER_NAME, PASSWORD);
        twootr.onRegisterUser(OTHER_USER_NAME, PASSWORD);
    }

    @Override
    public void onOpen(final WebSocket webSocket, final ClientHandshake clientHandshake) {
        // 소켓 클라이언트 관리를 Map 등록. 
    	socketToEndPoint.put(webSocket, new WebSocketEndPoint(twootr, webSocket));
    }

    @Override
    public void onClose(final WebSocket webSocket, final int i, final String s, final boolean b) {
        // 통신 종료시 클라이언트를 삭제
    	socketToEndPoint.remove(webSocket);
    }

    @Override
    public void onMessage(final WebSocket webSocket, final String message) {
        var endPoint = socketToEndPoint.get(webSocket);
        try {
            endPoint.onMessage(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onError(final WebSocket webSocket, final Exception e) {
        e.printStackTrace();
    }

	@Override
	public void onStart() {
		// TODO Auto-generated method stub
		// 기동과 관련된 추가 처리
	}
}
