import java.util.ArrayList;

public class Transacao {
    public int id;
    public Tipo tipo;
    public Categoria categoria;
    public Pagador pagador;
    public Data data;

    public double valor;
    public Integer estornoDeId;
    public String participante;
    public ArrayList<String> anexos = new ArrayList<>();
}
