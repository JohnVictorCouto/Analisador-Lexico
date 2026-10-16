package compiler;

import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.Reader;

public class Main {

    public static void main(String[] args) {
        Reader reader;
        if (args.length == 0) {
            reader = new InputStreamReader(System.in);
        } else {
            if (args.length != 1) {
                System.out.println("Uso: java -jar jacaparser.jar <nome do arquivo>");
                return;
            }
            try {
                reader = new InputStreamReader(new FileInputStream(args[0]));
            } catch (java.io.FileNotFoundException e) {
                System.out.println("Arquivo não encontrado: \"" + args[0] + "\"");
                return;
            }
        }
        try {
            Parser parser = new Parser(reader);
            parser.parse();
            System.out.println("Análise sintática terminada com sucesso!");
        } catch (Exception e) {
            System.out.println("Exceção: " + e.getMessage());
        }

    }
}
