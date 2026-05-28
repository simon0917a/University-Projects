import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class ShapeFXApp extends Application {

    @Override
    public void start(Stage stage) {

        Label lblShape = new Label("Choose shape: 's','r','c'");
        TextField txtShape = new TextField();

        Label lblRadius = new Label("Input radius of circle");
        TextField txtRadius = new TextField();

        Label lblLength = new Label("Input length of square or rectangle");
        TextField txtLength = new TextField();

        Label lblWidth = new Label("Input width of rectangle");
        TextField txtWidth = new TextField();

        Label lblArea = new Label("Area:");
        TextField txtArea = new TextField();
        txtArea.setEditable(false);

        Label lblPeri = new Label("Perimeter:");
        TextField txtPeri = new TextField();
        txtPeri.setEditable(false);

        Button btn = new Button("Get Result");

        GridPane gp = new GridPane();
        gp.setPadding(new Insets(15));
        gp.setVgap(8);
        gp.setHgap(10);

        gp.add(lblShape, 0, 0); gp.add(txtShape, 1, 0);
        gp.add(lblRadius, 0, 1); gp.add(txtRadius, 1, 1);
        gp.add(lblLength, 0, 2); gp.add(txtLength, 1, 2);
        gp.add(lblWidth, 0, 3); gp.add(txtWidth, 1, 3);
        gp.add(lblArea, 0, 4); gp.add(txtArea, 1, 4);
        gp.add(lblPeri, 0, 5); gp.add(txtPeri, 1, 5);
        gp.add(btn, 1, 6);

        btn.setOnAction(e -> {
            String type = txtShape.getText().trim().toLowerCase();

            txtArea.setText("Invalid input");
            txtPeri.setText("Invalid input");

            try {
                Shape shape = null;

                switch (type) {
                    case "c":
                        if (txtRadius.getText().isBlank()) return;
                        float r = Float.parseFloat(txtRadius.getText());
                        if (r <= 0) return;
                        shape = new Circle(r);
                        break;

                    case "s":
                        if (txtLength.getText().isBlank()) return;
                        float s = Float.parseFloat(txtLength.getText());
                        if (s <= 0) return;
                        shape = new Square(s);
                        break;

                    case "r":
                        if (txtLength.getText().isBlank() ||
                            txtWidth.getText().isBlank()) return;
                        float l = Float.parseFloat(txtLength.getText());
                        float w = Float.parseFloat(txtWidth.getText());
                        if (l <= 0 || w <= 0) return;
                        shape = new Rectangle(l, w);
                        break;

                    default:
                        return;
                }

                shape.computeArea();
                shape.computePerimeter();

                txtArea.setText(Float.toString(shape.area));
                txtPeri.setText(Float.toString(shape.perimeter));

            } catch (Exception ex) {

            }
        });

        stage.setScene(new Scene(gp, 520, 300));
        stage.setTitle("Shape Calculator (JavaFX)");
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}