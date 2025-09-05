module light.gestion_ecole {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires javafx.base;

    requires com.dlsc.formsfx;
    requires org.kordamp.bootstrapfx.core;

    requires java.sql;
    requires java.desktop;

    opens light.gestion_ecole to javafx.fxml;
    opens light.gestion_ecole.Controller to javafx.fxml;
    opens light.gestion_ecole.Model to javafx.base;

    exports light.gestion_ecole;
    exports light.gestion_ecole.Controller;
    //opens light.gestion_ecole.Controller to javafx.fxml;

    //
    // opens light.gestion_ecole.Model to javafx.base;
    exports light.gestion_ecole.Model;
}