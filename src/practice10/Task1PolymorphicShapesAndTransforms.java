package practice10;

interface Scalable2D{

    double MIN_SCALE_FACTOR = 0.1;

    void scale(double factor);
    };

interface Rotatable2D{
    void rotate(double deltaDegrees);
    double getRotationAngle();
}

abstract class AbstractShape2D {
    private final String name;
    private double centerX;
    private double centerY;

    public AbstractShape2D(String name, double centerX, double centerY) {
        this.name = name;
        this.centerX = centerX;
        this.centerY = centerY;
    }

    public String getName() {
        return name;
    }

    public double getCenterX() {
        return centerX;
    }

    public double getCenterY() {
        return centerY;
    }

    public abstract double getArea();

    public abstract double getPerimeter();

    protected String getAdditionalInfo() {
        return getAdditionalInfo();
    }

    ;

    public final double getIsoperimetricQuotient() {
        return 4 * Math.PI * getArea() / Math.pow(getPerimeter(), 2);
    }

    @Override
    public String toString() {
        return String.format("%s['%s', center=(%.1f; %.1f), %s | S=%.2f, P=%.2f, Q=%.4f]",
                getClass().getSimpleName(), name, centerX, centerY,
                getAdditionalInfo(), getArea(), getPerimeter(), getIsoperimetricQuotient());
    }
}
class CircleShape extends AbstractShape2D implements Scalable2D{
    private double radius;

    public CircleShape(String name,double centerX, double centerY, double radius){
        super(name, centerX, centerY);
        if (radius < 0) {
            System.out.println("Радіус має бути більшим за 0");
            radius = 1;
        } else {
            this.radius = radius;
        }
    }

    @Override
    public double getArea() {
        return Math.PI * radius * radius;
    }

    @Override
    public double getPerimeter() {
        return 2 * Math.PI * radius;
    }

    @Override
    public void scale(double factor){
        double F = Math.max(factor,MIN_SCALE_FACTOR);
        radius *= F;
        System.out.printf("   [Scalable2D] '%s' масштабовано в %.1f разів (новий r=%.2f)%n",
                getName(), F, radius);
    }

    @Override
    protected String getAdditionalInfo(){
        return String.format("r=%.2f", radius);
    }
}

class RectangleShape extends AbstractShape2D implements Scalable2D, Rotatable2D{
    private double width;
    private double height;
    private double rotationAngle;
    public RectangleShape(String name, double centerX, double centerY, double width, double height, double rotationAngle) {
        super(name, centerX, centerY);
        if (width < 0 || height < 0) {
            System.out.println("Ширина і висота мають бути більшими за 0");
            this.width = 1;
            this.height = 1;
            this.rotationAngle = 0.0;
        } else {
            this.width = width;
            this.height = height;
            this.rotationAngle = 0.0;
        }
    }
        @Override
        public double getArea(){
            return width*height;
        }
        @Override
        public double getPerimeter(){
            return 2 * (width + height);
        }
        @Override
        public void scale(double factor){
            double F = Math.max(factor,MIN_SCALE_FACTOR);
            width *= F;
            height *= F;
            System.out.printf("   [Scalable2D] '%s' масштабовано в %.1f разів(%.2f x %.2f)%n",getName(),F,width,height);
        }
        @Override
        public void rotate(double deltaDegrees){
            rotationAngle = ((rotationAngle+deltaDegrees)%360+360)%360;
            System.out.printf("   [Rotatable2D] '%s' повернуто на %+.1f° (поточний кут: %.1f°)%n",
                    getName(), deltaDegrees, rotationAngle);
        }

        @Override
        public double getRotationAngle(){
            return getRotationAngle();
        }

        @Override
        protected String getAdditionalInfo(){
            return String.format("%.2fx%.2f, angle=%.1f°", width, height, rotationAngle);
        }
}

final class ImmutableReferenceSquare extends AbstractShape2D{
    private final double side;

    public ImmutableReferenceSquare(String name, double centerX, double centerY, double side){
        super(name,centerX,centerY);
        if (side <= 0){
            System.out.println("Сторона має бути більшою за 0. Встановюю 1 за замовчуванням");
            this.side=1;
        } else {
            this.side = side;
        }
    }

    @Override
    public double getArea(){
        return side*side;
    }
    @Override
    public double getPerimeter(){
        return 4 * side;
    }

    @Override
    protected String getAdditionalInfo() {
        return String.format("side=%.2f", side) ;
    }
}

public class Task1PolymorphicShapesAndTransforms {
    public static double calculateTotalArea(AbstractShape2D[] shapes) {
        double sum = 0;
        for (AbstractShape2D shape : shapes) {
            sum += shape.getArea();
        }
        return sum;
    }

    public static void transformScene(AbstractShape2D[] shapes, double scaleFactor, double rotateDeg) {
        for (AbstractShape2D shape : shapes) {
            if (shape instanceof Scalable2D s) {
                s.scale(scaleFactor);
            }
            if (shape instanceof Rotatable2D r) {
                r.rotate(rotateDeg);
            }
            if (!(shape instanceof Scalable2D) && !(shape instanceof Rotatable2D)) {
                System.out.printf("   [Info] '%s' (final class) пропущено — не підтримує трансформації.%n",
                        shape.getName());
            }
        }
    }

    public static void main(String[] args) {
        AbstractShape2D[] shapes = {
                new CircleShape("Коло-Лінза", 0.0, 0.0, 5.0),
                new RectangleShape("Рамка-А", 2.0, 3.0, 4.0, 6.0, 0),
                new ImmutableReferenceSquare("Еталон-Квадрат", 10.0, 10.0, 5.0)
        };
        System.out.println("================ ПОЛІМОРФНА ГЕОМЕТРІЯ ТА ІНТЕРФЕЙСИ ТРАНСФОРМАЦІЙ ================");
        System.out.println("[СТАН СЦЕНИ ДО ТРАНСФОРМАЦІЇ]:");
        for (int i = 0; i < shapes.length; i++) {
            System.out.printf("  %d. %s%n", i + 1, shapes[i]);
        }
        System.out.printf("Сумарна площа сцени: %.2f кв.од.%n", Task1PolymorphicShapesAndTransforms.calculateTotalArea(shapes));
        System.out.println();
        System.out.println("-> Виконуємо пакетну трансформацію (scale x2.0, rotate +45.0°) через instanceof...");
        Task1PolymorphicShapesAndTransforms.transformScene(shapes, 2.0, 45.0);
        System.out.println("");
        System.out.println("[СТАН СЦЕНИ ПІСЛЯ ТРАНСФОРМАЦІЇ]:");
        for (int i = 0; i < shapes.length; i++) {
            System.out.printf("  %d. %s%n", i + 1, shapes[i]);
        }
        System.out.printf("Нова сумарна площа сцени: %.2f кв.од.", calculateTotalArea(shapes));
        System.out.println("==================================================================================");
    }
}


