package proyect;

import javax.swing.*;
import java.awt.event.*;
import java.io.*;
import java.net.Socket;
import java.util.Base64;

public class ScoreClient extends JFrame implements ActionListener {
    private static final int PORT = 1234;
    private static final String HOST = "localhost";

    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;

    //Variables GUI
    JButton jbGetScores;
    JButton jbSendScore;
    JTextArea jtaMesgs;
    JTextField jtfName;
    JTextField jtfScore;

    public ScoreClient() {
        super("ScoreClient");

        initializeGUI();
        makeContact();

        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                closeLink();
            }
        });
    }

    private void initializeGUI() {
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));

        // Crear componentes
        jbGetScores = new JButton("Obtener Scores");
        jbSendScore = new JButton("Enviar Score"); // Antes jtfScore, ahora es un botón correctamente nombrado
        jtaMesgs = new JTextArea(10, 30);
        jtaMesgs.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(jtaMesgs);
        jtfName = new JTextField(15);
        jtfScore = new JTextField(5);

        // Panel para entrada de nombre y score
        JPanel inputPanel = new JPanel();
        inputPanel.add(new JLabel("Nombre:"));
        inputPanel.add(jtfName);
        inputPanel.add(new JLabel("Score:"));
        inputPanel.add(jtfScore);
        inputPanel.add(jbSendScore);

        // Agregar componentes al frame
        add(inputPanel);
        add(jbGetScores);
        add(scrollPane);

        // Añadir action listeners
        jbGetScores.addActionListener(this);
        jbSendScore.addActionListener(this); // Ahora este es el botón correcto

        setVisible(true);
    }

    private void closeLink(){
        try{
            out.println ("bye");
            socket.close();
        } catch(Exception e){
            System.out.println(e);
        }
    }

    private void makeContact(){
        try{
            socket = new Socket(HOST,PORT);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(),true);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void sendGet(){
        try{
            out.println ("get");
            String line = in.readLine();
            System.out.println (line);
            if ((line.length()>=7) && (line.substring(0,6).equals("HIGH$$"))) {
                showHigh(line.substring(6).trim());
            } else{
                jtaMesgs.append(line+"\n");
            }
        } catch (IOException ex) {
            jtaMesgs.append("Problem obtaining high scores \n");
            System.out.println(ex.getMessage());
        }
    }

    private void sendScore(){
        try {
            ScoreInfo si = new ScoreInfo(jtfName.getText(), Integer.parseInt(jtfScore.getText()));

            // Serializar el objeto ScoreInfo
            ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteStream);
            objectOutputStream.writeObject(si);

            // Enviar el objeto serializado codificado en Base64
            String encodedData = Base64.getEncoder().encodeToString(byteStream.toByteArray());
            out.println("score " + encodedData);
            out.flush();
        } catch (Exception e){
            jtaMesgs.append("Error enviando high score\n");
            System.out.println("Error en sendHighScore: " + e.getMessage());
        }
    }

    private void showHigh(String line){
        try{
            String[] scores =line.split(";");
            StringBuilder stringBuilder = new StringBuilder("Score:\n");

            for (String s : scores) {
                stringBuilder.append(s).append("\n");
            }

            jtaMesgs.append(stringBuilder.toString());
        }catch (Exception e) {
            jtaMesgs.append("Error mostrando high scores\n");
            System.out.println("Error en showHigh: " + e.getMessage());
        }
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == jbGetScores) {
            sendGet();
        }
        else if (e.getSource() == jbSendScore) {
            sendScore();
        }
    }
}
