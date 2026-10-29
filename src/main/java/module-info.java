module kyj.poo {
    requires com.google.gson;
    exports kyj.poo;

    opens kyj.poo.regras to com.google.gson;
}
