module kyj.poo {
    requires com.google.gson;
    exports kyj.poo;

    opens kyj.poo.regras.modelo to com.google.gson;
}
