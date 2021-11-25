package socketprogramming;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

class ServerFrame extends JFrame {
    
    JTextField text;
    JButton button;
    JLabel serverLabel, clientLabel;
    MouseListener textListener, buttonListener;
    String outgoing, incoming = "deneme";
    final ServerSocket serverSocket;
    final Socket clientSocket;
    final BufferedReader in;
    final PrintWriter out;

    public ServerFrame() throws IOException {

        this.add(text = new JTextField());
        text.setSize(200, 50);
        text.setText("Mesaj Girin");
        text.setLocation(150, 0);
        text.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent me) {
                text.setText("");
            }

            @Override
            public void mousePressed(MouseEvent me) {
            }

            @Override
            public void mouseReleased(MouseEvent me) {
            }

            @Override
            public void mouseEntered(MouseEvent me) {
            }

            @Override
            public void mouseExited(MouseEvent me) {
            }
        });

        this.add(button = new JButton());
        button.setSize(100, 50);
        button.setLocation(200, 70);
        button.setText("Yolla");
        button.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent me) {
                System.out.println("Çalıştı");
                outgoing = text.getText();
                write(outgoing);
                send(outgoing);

            }

            @Override
            public void mousePressed(MouseEvent me) {
            }

            @Override
            public void mouseReleased(MouseEvent me) {
            }

            @Override
            public void mouseEntered(MouseEvent me) {
            }

            @Override
            public void mouseExited(MouseEvent me) {
            }
        });

        this.add(serverLabel = new JLabel("Server: "));
        serverLabel.setVisible(true);
        serverLabel.setSize(250, 80);
        serverLabel.setLocation(20, serverY);

        this.add(clientLabel = new JLabel("Client: "));
        clientLabel.setVisible(true);
        clientLabel.setSize(250, 80);
        clientLabel.setLocation(350 - incoming.length(), 120);

        int screenWidth = WIDTH;
        int screenHeight = HEIGHT;
        this.setTitle("Soket Programlama");
        this.setLocation((screenWidth - this.getWidth()) / 2, (screenHeight - this.getHeight()) / 2);
        this.setLayout(null);
        this.setVisible(true);
        this.setSize(500, 500);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        

        serverSocket = new ServerSocket(5000);
        clientSocket = serverSocket.accept();
        out = new PrintWriter(clientSocket.getOutputStream());
        in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        repaint();
    }

    public void send(String msg) {

        try {

            Thread sender = new Thread(new Runnable() {

                public void run() {

                    out.println(msg);
                    out.flush();

                }

            });
            sender.start();
            Thread receiver = new Thread(new Runnable() {
                String msg;

                public void run() {
                    try {
                        msg = in.readLine();
                        while (msg != null) {
                            System.out.println("Client: " + msg);
                            msg = in.readLine();
                        }

                        System.out.println("server out of service");
                        out.close();
                        clientSocket.close();
                        serverSocket.close();

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }

            });

            receiver.start();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    int serverY = 100;
    int clientY = 125;

    public void write(String message) {
        serverLabel.setText(serverLabel.getText()+message+" ");
        serverLabel.setLocation(20, serverY);
        serverY+=25;
        
    }

}

class serverSend extends JLabel{
    
}

class ChatServer {

    public static void main(String[] args) throws IOException {
        ServerFrame frame = new ServerFrame();
    }
}
