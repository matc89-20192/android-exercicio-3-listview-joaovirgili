package matc89.exercicio3;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;

public class MainActivity extends AppCompatActivity {


    EditText editDescricao;
    EditText editPrioridade;

    Button buttonAdicionar;
    Button buttonRemover;

    ListView listView;

    ArrayList<Tarefa> tarefas;
    TarefaAdapter tarefaAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.inicializaPropriedades();
        this.iniciaAdapter();
    }

    public void removerPrimeiro(View view) {
        if (this.tarefaAdapter.isEmpty()) {
            return;
        }
        this.tarefaAdapter.remove(this.tarefaAdapter.getItem(0));
        if (this.tarefas.isEmpty()) {
            this.buttonRemover.setEnabled(false);
        }
    }

    public void clickAdicionar(View view) {

        Tarefa tarefa = carregaTarefa();

        if (!this.isTarefaValida(tarefa)) {
            return;
        }

        this.adicionaTarefaNaLista(tarefa);
        this.buttonRemover.setEnabled(true);
    }

    private void adicionaTarefaNaLista(Tarefa novaTarefa) {

        this.tarefaAdapter.add(novaTarefa);
        this.tarefaAdapter.sort(new MeuSort());
    }

    private void exibeToast(String mensagem) {
        Toast.makeText(getApplicationContext(), mensagem, Toast.LENGTH_SHORT).show();
    }

    private Tarefa carregaTarefa() {
        String descricao = this.editDescricao.getText().toString();
        int prioridade = Integer.parseInt(this.editPrioridade.getText().toString());
        return new Tarefa(descricao, prioridade);
    }

    private boolean isTarefaValida(Tarefa tarefa) {
        if (descricaoJaContida(tarefa.getDescricao())) {
            exibeToast("Tarefa já cadastrada.");
            return false;
        }

        if (prioridadeValida(tarefa.getPrioridade())) {
            exibeToast("A prioridade deve estar entre 1 e 10.");
            return false;
        }

        return true;
    }

    private boolean prioridadeValida(int prioridade) {
        return prioridade < 1 || prioridade > 10;
    }

    private boolean descricaoJaContida(String descricao) {
        if (this.tarefas.isEmpty()) {
            return false;
        }

        for (Tarefa tarefa : this.tarefas) {
            if (tarefa.getDescricao().equals(descricao)) {
                return true;
            }
        }

        return false;
    }

    private void inicializaPropriedades() {
        this.editDescricao = (EditText)findViewById(R.id.editDescricao);
        this.editDescricao.setText("");

        this.editPrioridade = (EditText) findViewById(R.id.editPrioridade);
        this.editPrioridade.setText("");

        this.buttonAdicionar = (Button) findViewById(R.id.buttonAdicionar);
        this.buttonRemover = (Button) findViewById(R.id.buttonRemover);
        this.buttonRemover.setEnabled(false);

        this.listView = (ListView) findViewById(R.id.listView);
        this.listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> a,
                                    View v, int position, long id) {
                tarefas.remove(position);
                tarefaAdapter.notifyDataSetChanged();
            }
        });

        this.tarefas = new ArrayList<>();
    }

    private void iniciaAdapter() {
        this.tarefaAdapter = new TarefaAdapter(this, this.tarefas);
        listView.setAdapter(tarefaAdapter);
    }


}

class MeuSort implements Comparator<Tarefa>
{
    public int compare(Tarefa a, Tarefa b)
    {
        return a.getPrioridade() - b.getPrioridade();
    }
}
