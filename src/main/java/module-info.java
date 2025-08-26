module light.gestion_ecole {
    requires javafx.controls;
    requires javafx.fxml;

    requires com.dlsc.formsfx;
    requires org.kordamp.bootstrapfx.core;
    requires java.sql;
    requires javafx.graphics;

    opens light.gestion_ecole to javafx.fxml;
    exports light.gestion_ecole;
    exports light.gestion_ecole.Controller;
    opens light.gestion_ecole.Controller to javafx.fxml;

    opens light.gestion_ecole.Model to javafx.base;
}