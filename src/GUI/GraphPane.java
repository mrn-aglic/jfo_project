package GUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;


/**
 * Created by Marin on 11/05/16.
 */
public class GraphPane extends JPanel {

    private Point2D offset;
    private Node selectedNode;
    private Node root;
    private int fullSize;
    private int maxDepth;
    private HashMap<Integer, ArrayList<Node>> nodesAtDepth;

    public GraphPane() {

        maxDepth = 0;

        nodesAtDepth = new HashMap<>();

        setBackground(Color.WHITE);

        initEvents();

        fullSize = 1000;

        setPreferredSize(new Dimension(fullSize, fullSize));
    }

    public void erase(){

        removeAll();

        this.nodesAtDepth = new HashMap<>();
        this.root = null;
        this.selectedNode = null;
        this.maxDepth = 0;

        revalidate();
    }

    public void drawTree(Node root) {

        this.root = root;

        this.nodesAtDepth = fillNodesAtDepth(this.root, 0, new HashMap<>());

        this.maxDepth = nodesAtDepth.keySet().stream().max(Comparator.comparing(x -> x)).get();

        setTreeBounds();

        repaint();
    }

    private void initEvents(){

        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {

                if(!(root == null || nodesAtDepth.values().isEmpty())){

                    ArrayList<Node> filtered =
                            nodesAtDepth.values().stream().reduce(new ArrayList<Node>(), (x, y) -> {

                                x.addAll(y);
                                return x;
                            }).stream().filter(
                                    x -> x.getBounds().contains(e.getPoint())
                            ).collect(Collectors.toCollection(ArrayList<Node>::new));

                    if (!filtered.isEmpty()) {

                        selectedNode = filtered.get(0);
                        offset = new Point2D.Double(e.getX() - selectedNode.getBounds().getX(), e.getY() - selectedNode.getBounds().getY());
                    }
                }

            }

            @Override
            public void mouseReleased(MouseEvent e) {

                selectedNode = null;
            }
        });

        this.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {

                if(selectedNode != null){

                    Point2D p = new Point2D.Double(e.getX() - offset.getX(), e.getY() - offset.getY());

                    selectedNode.moveTo(p);

                    Node overlap = findFirst(root, n -> selectedNode != n && selectedNode.getBounds()
                            .intersects(n.getBounds().getCenterX(), n.getBounds().getCenterY(), n.getBounds().getWidth(), n.getBounds().getHeight()));

                    if(overlap != null ){//&& (selectedNode.getChildren().isEmpty())){

                        swithcNode2(overlap);
                        //switchNode(root, overlap);

                        nodesAtDepth = fillNodesAtDepth(root, 0, new HashMap<>());
                        maxDepth = nodesAtDepth.keySet().stream().max(Comparator.comparing(x -> x)).get();
                    }
                }

                repaint();
            }
        });
    }

    private Node findFirst(Node root, Function<Node, Boolean> f){

        if(root == null || (!f.apply(root) && root.getChildren().isEmpty())) return null;
        else if(f.apply(root)) return root;
        else {

            for(Node n: root.getChildren()){

                Node p = findFirst(n, f);

                if(p != null) {

                    return p;
                }
            }

            return null;
        }
    }

    @Override
    public void paintComponent(Graphics g){

        super.paintComponent(g);

        nAryTreeTraversal(this.root, g);
    }

    private void nAryTreeTraversal(Node root, Graphics g){

        if(root == null)return;

        Graphics2D graphics2D = (Graphics2D)g.create();

        root.paint(graphics2D);

        graphics2D.dispose();

        root.getChildren().stream().forEach(n -> nAryTreeTraversal(n, g));
    }

    private int getMaxDepth(Node root){

        if(root.getChildren().isEmpty()) return 0;
        else {

            int max = 0;

            for (Node n: root.getChildren()){

                max = Math.max(getMaxDepth(n), max);
            }

            return max + 1;
        }
    }

    private HashMap<Integer, ArrayList<Node>> fillNodesAtDepth(Node root, int depth, HashMap<Integer, ArrayList<Node>> depthNum){

        if(depthNum.containsKey(depth)){

            depthNum.get(depth).add(root);
        }else {

            ArrayList<Node> nodes = new ArrayList<Node>();

            nodes.add(root);

            depthNum.put(depth, nodes);
        }

        for(Node n: root.getChildren()){

            depthNum = fillNodesAtDepth(n, depth + 1, depthNum);
        }

        return depthNum;
    }

    private void swithcNode2(Node overlap){

        if(overlap.isDescendant(selectedNode)) {

            Node selectedParent = selectedNode.getParent();
            Node overlapParent = overlap.getParent();

            selectedParent.getChildren().remove(selectedNode);

            selectedParent.getChildren().add(overlap);

            overlap.setParent(selectedParent);
            overlapParent.getChildren().remove(overlap);

            selectedNode.setParent(overlap);
        }
        else {

            Node selectedParent = selectedNode.getParent();

            selectedParent.getChildren().remove(selectedNode);

            overlap.getChildren().add(selectedNode);
            selectedNode.setParent(overlap);
        }
    }

    private void switchNode(Node root, Node newParent){

        if(root == null) return;

        if(newParent.isDescendant(selectedNode)){

            newParent.getParent().getChildren().remove(newParent);
            newParent.setParent(selectedNode.getParent());
            newParent.getChildren().add(selectedNode);
            selectedNode.setParent(newParent);
        }

        if(root.getChildren().contains(selectedNode)){

            root.getChildren().remove(selectedNode);
            selectedNode.setParent(null);
        }

        if(root == newParent) {

            newParent.getChildren().add(selectedNode);
            selectedNode.setParent(root);
        }

        for(Node n: root.getChildren()){

            switchNode(n, newParent);
        }
    }

    private void setTreeBounds(){

        int yPart = fullSize / ( maxDepth + 1);

        for(Map.Entry<Integer, ArrayList<Node>> e: nodesAtDepth.entrySet()){

            int depth = e.getKey();
            ArrayList<Node> nodes = e.getValue();

            int xOffset = 0;

            for(Node n: nodes){

                int x = fullSize / (nodes.size() + 1) + xOffset;

                xOffset = x;

                int y = yPart * depth;

                n.setBounds(x, y);
            }
        }
    }
}

