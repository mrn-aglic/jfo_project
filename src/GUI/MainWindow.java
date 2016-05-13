package GUI;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.ArrayList;

/**
 * Created by Marin on 10/05/16.
 */
public class MainWindow extends JFrame {

    private final ArrayList<Node> nodes;

    private GraphPane panel;

    public MainWindow(){

        nodes = new ArrayList<Node>();

        initMainWindow();

        add(initWest(), BorderLayout.WEST);
        add(initTop(), BorderLayout.NORTH);
        add(initBottom(), BorderLayout.SOUTH);

        panel = (GraphPane) initCenter();

        add(panel, BorderLayout.CENTER);
    }

    private GraphPane initCenter(){

        GraphPane panel = new GraphPane();

        pack();

        return panel;
    }

    private JPanel initWest(){

        JPanel west = new JPanel();

        BoxLayout boxLayout = new BoxLayout(west, BoxLayout.Y_AXIS);

        west.setLayout(boxLayout);

        JButton drawNodes = new JButton("Draw nodes");

        JPanel buttonsPanel = new JPanel();

        drawNodes.addActionListener(e -> drawNodes());

        BoxLayout buttonsLayout = new BoxLayout(buttonsPanel, BoxLayout.X_AXIS);
        buttonsPanel.setLayout(buttonsLayout);

        buttonsPanel.add(drawNodes);

        west.add(buttonsPanel);

        return west;
    }

    private void drawNodes() {

        int nodeSize = 30;

        Node n1 = new Node(nodeSize, 5);
        Node n2 = new Node(nodeSize, 6);
        Node n3 = new Node(nodeSize, 2);
        Node n4 = new Node(nodeSize, n1, 3);
        Node n5 = new Node(nodeSize, n2, 4);

        ArrayList<Node> temp = new ArrayList<>();

        temp.add(n3);
        temp.add(n4);
        temp.add(n5);

        Node n6 = new Node(nodeSize, temp, 1);

        panel.drawTree(n6);
    }

    private JPanel initTop(){

        JPanel top = new JPanel();

        JLabel label = new JLabel("Hello GUI");

        label.setFont(new Font(label.getFont().getFontName(), label.getFont().getStyle(), 20));
        label.setBorder(new EmptyBorder(5, 5, 5, 5));

        top.add(label);

        return top;
    }

    private JPanel initBottom() {

        FlowLayout flowLayout = new FlowLayout();
        flowLayout.setAlignment(FlowLayout.RIGHT);
        flowLayout.setHgap(5);
        flowLayout.setVgap(5);

        JPanel bottom = new JPanel(flowLayout);

        bottom.setBackground(Color.gray);

        bottom.add(initQuitButton());

        return bottom;
    }

    private JComponent initQuitButton(){

        JButton quitButton = new JButton("Quit");

        quitButton.setBackground(Color.white);
        quitButton.setPreferredSize(new Dimension(100, 25));

        quitButton.addActionListener(e -> System.exit(0));

        return quitButton;
    }

    private void initMainWindow(){

        setTitle("My window");
        //setPreferredSize(new Dimension(1200, 1000));
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

    public static void main(String[] args){

        EventQueue.invokeLater(() -> {
            MainWindow w = new MainWindow();
            w.pack();
            w.setVisible(true);
        });
    }
}
