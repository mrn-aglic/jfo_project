package GUI;

import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;

/**
 * Created by Marin on 10/05/16.
 */
public class Node {

    private Node parent;
    private ArrayList<Node> children;
    private int size;
    private Ellipse2D bounds;

    private int data;

    public Node(int size, int data){

        children = new ArrayList<Node>();
        this.data = data;
        this.parent = null;

        this.bounds = new Ellipse2D.Double(0, 0, size, size);
        this.size = size;
    }

    public Node(int size, Node node, int data){

        this(size, data);

        setParent(node);

        node.getChildren().add(this);
    }

    public Node(int size, Node node, ArrayList<Node> children, int data) {

        this(size, node, data);

        setChildren(children);
    }

    public void paint(Graphics2D g){

        Font font = new Font("Tahoma", Font.ITALIC, 12);

        Graphics2D graphics2D = (Graphics2D)g.create();

        graphics2D.setFont(font);

        Point2D point1 = new Point.Double(bounds.getCenterX(), bounds.getCenterY());

        for(Node n: getChildren()){

            Point2D point2 = new Point.Double(n.getBounds().getCenterX(), n.getBounds().getCenterY());

            graphics2D.draw(new Line2D.Double(point1, point2));
        }

        graphics2D.drawString(data + "", (int)bounds.getCenterX(), (int)bounds.getCenterY() + size);

        graphics2D.translate(bounds.getX(), bounds.getY());
        graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        graphics2D.drawOval(0, 0, this.size, this.size);
    }

    public void setBounds(int x, int y){

        this.bounds = new Ellipse2D.Double(x, y, this.size, this.size);
    }

    public void setBounds(Ellipse2D bounds){

        this.bounds = bounds;
    }

    public void setParent(Node n){

        parent = n;
    }

    public Node getParent(){

        return parent;
    }

    public void setChildren(ArrayList<Node> children){

        this.children = children;
    }

    public ArrayList<Node> getChildren(){

        return children;
    }

    public Ellipse2D getBounds(){

        return this.bounds;
    }

    public boolean contains(Point2D point){

        return bounds.contains(point);
    }

    public void moveTo(Point2D point){

        setBounds(new Ellipse2D.Double((int)point.getX(), (int)point.getY(), size, size));
    }

    public boolean isDescendant(Node n){

        if(n == null) return false;
        else if(n.getChildren().contains(this)) return true;
        else {

            for(Node node: n.getChildren()){

                if(isDescendant(node)) return true;
            }

            return false;
        }
    }

    @Override
    public String toString(){

        return data + "";
    }
}
