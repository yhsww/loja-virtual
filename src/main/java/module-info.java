module kyj.poo {
    requires com.google.gson;
    requires javafx.controls;

    exports kyj.poo;

    opens kyj.poo.regras to com.google.gson;
}
