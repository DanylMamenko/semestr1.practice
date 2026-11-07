package practice09;

class Point2D{
    private double x;
    private double y;

    public Point2D(double x, double y){
        this.x=x;
        this.y=y;
    }

    public Point2D(Point2D other){
        this(other.x, other.y);
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public void translate(double dx, double dy){
        x += dx;
        y += dy;
    }

    public double distanceTo(Point2D other){
        return Math.hypot(x - other.x, y - other.y);
    }

    @Override
    public String toString(){
        return String.format("(%.2f; %.2f)", x, y);
    }
}
abstract class AbstractShape2D{
    private final String name;
    private final Point2D anchorPoint;

    public AbstractShape2D(String name, Point2D anchorPoint){
        this.name=name;
        this.anchorPoint = (anchorPoint != null) ? new Point2D(anchorPoint) : new Point2D(0, 0);
    }

    public String getName() {
        return name;
    }

    public Point2D getAnchorPoint(){
        return new Point2D(anchorPoint);
    }

    public void moveBy(double dx, double dy){
        anchorPoint.translate(dx, dy);
    }

    public abstract double getArea();
    public abstract double getPerimetr();
    protected abstract String getDimensionInfo();

    @Override public String toString(){
        return String.format("%s[name='%s', center=%s, %s | S=%.2f, P=%.2f]",
                getClass().getSimpleName(), name, anchorPoint, getDimensionInfo()
                , getArea(), getPerimetr());
    }

}

class Circle2D extends AbstractShape2D{
    private double radius;

    public Circle2D(String name, Point2D center, double radius){
        super(name, center);
        if (radius <= 0 ){
            System.out.println("Радіус має бути більшим за 0");
            this.radius = 1;
        } else {
            this.radius = radius;
        }
    }

    @Override public double getArea(){
        return Math.PI * radius * radius;
    }
    @Override public double getPerimetr(){
        return 2 * Math.PI * radius;
    }
    @Override protected String getDimensionInfo() {
        return String.format("r= %.2f ", this.radius);
    }
}

class Rectangle2D extends AbstractShape2D{
    private double width;
    private double height;

    public Rectangle2D(String name, Point2D center, double width, double height) {
        super(name, center);
        if (height <= 0 || width <= 0) {
            System.out.println("Ширина і висота мають бути більшими за 0");
            this.height = 1;
            this.width = 1;
        } else {
            this.height = height;
            this.width = width;
        }
    }

        @Override public double getArea(){
            return width * height;
        }

        @Override public double getPerimetr(){
        return 2 * (width+height);
        }

        @Override protected String getDimensionInfo(){
        return String.format("%.2f x %.2f", this.width, this.height);
        }
}

class RightTriangle2D extends AbstractShape2D{
    private double legA;
    private double legB;

    public RightTriangle2D(String name, Point2D center, double legA, double legB) {
        super(name, center);
        if (legA <= 0 || legB <= 0) {
            System.out.println("Катети мають бути більшими за 0");
            this.legA = 1;
            this.legB = 1;
        } else {
            this.legA = legA;
            this.legB = legB;
        }
    }

    private double getHypotenuse() {
        return Math.sqrt(legA * legA + legB * legB);
    }


    @Override public double getArea(){
        return 0.5 * legA * legB;
    }

    @Override public double getPerimetr(){
        return legA + legB + getHypotenuse();
    }

    @Override protected String getDimensionInfo(){
        return String.format("a=%.2f b=%.2f c=%.2f", this.legA, this.legB, getHypotenuse());
    }
}

class GeometryCanvas{
    private final String title;
    private final AbstractShape2D[] shapes;
    private int count;

    public GeometryCanvas(String title, int capacity) {
        this.title = title;
        this.shapes = new AbstractShape2D[capacity];
        this.count = 0;
    }

    public boolean addShape(AbstractShape2D shape){
        if (shape == null || count >= shapes.length) {
            return false;
        }
        shapes[count++] = shape;
        return true;
    }

    public double getTotalArea(){
        double sum = 0;
        for (int i = 0; i < count; i++) {
            sum += shapes[i].getArea();
        }
        return sum;
    }

    public double getTotalPerimeter() {
        double sum = 0;
        for (int i = 0; i < count; i++) {
            sum += shapes[i].getPerimetr();
        }
        return sum;
    }

    public AbstractShape2D findLargestShape() {
        if (count == 0) {
            return null;
        }
        AbstractShape2D largest = shapes[0];
        for (int i = 1; i < count; i++) {
            if (shapes[i].getArea() > largest.getArea()) {
                largest = shapes[i];
            }
        }
        return largest;
    }

    public void moveAllShapes(double dx, double dy) {
        for (int i = 0; i < count; i++) {
            shapes[i].moveBy(dx, dy);
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("Полотно: '%s' (Фігур: %d/%d)", title, count, shapes.length));
        for (int i = 0; i < count; i++) {
            sb.append(String.format("%n  %d. %s", i + 1, shapes[i]));
        }
        return sb.toString();
    }
}

public class Task1CompositeShapesCanvas {
    static void main(String[] args) {
        GeometryCanvas canvas = new GeometryCanvas("Інженерне креслення №1", 5);

        Point2D circleCenter = new Point2D(0, 0);
        Circle2D circle = new Circle2D("Коло-А", circleCenter, 5.0);
        circleCenter.translate(100, 100);          // на фігуру не впливає
        circle.getAnchorPoint().translate(50, 50); // це лише копія, фігура не змінюється

        canvas.addShape(circle);
        canvas.addShape(new Rectangle2D("Плита-Б", new Point2D(10, 5), 4.0, 6.0));
        canvas.addShape(new RightTriangle2D("Клин-В", new Point2D(-3, 2), 3.0, 4.0));

        String line = "=".repeat(77);

        System.out.println("================ ГЕОМЕТРИЧНЕ ПОЛОТНО (КОМПОЗИЦІЯ + ABSTRACT) ================");
        System.out.println(canvas);
        System.out.println("-".repeat(77));
        System.out.printf("%-28s%.2f кв.од.%n", "Сумарна площа креслення:", canvas.getTotalArea());
        System.out.printf("%-28s%.2f од.%n", "Сумарний периметр:", canvas.getTotalPerimeter());

        AbstractShape2D largest = canvas.findLargestShape();
        System.out.printf("%-28s%s (S = %.2f)%n", "Найбільша фігура:", largest.getName(), largest.getArea());

        double dx = 5.0;
        double dy = -2.0;
        System.out.printf("-> Переміщуємо всі фігури полотна на вектор (%+.1f; %+.1f)...%n", dx, dy);
        canvas.moveAllShapes(dx, dy);
        System.out.println("Новий центр '" + circle.getName() + "': " + circle.getAnchorPoint());
        System.out.println(line);
    }

}

