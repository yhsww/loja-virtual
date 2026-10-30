module kyj.poo {
    requires java.desktop;
    requires com.google.gson;


    opens kyj.poo.regras to com.google.gson;
    opens kyj.poo.regras.carrinho to com.google.gson;
    opens kyj.poo.regras.compra to com.google.gson;
    opens kyj.poo.persistencia to com.google.gson;
}