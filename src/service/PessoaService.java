package service;

import java.util.ArrayList;
import java.util.List;

import exception.BibliotecaException;
import exception.PessoaNaoEncontradaException;
import exception.RegistroDuplicadoException;
import model.Pessoa;

/** Guarda as pessoas cadastradas. Recusa documento repetido. */
public class PessoaService {

    /** Quando tiver banco, e aqui que entra o INSERT e o SELECT das pessoas. */
    private final List<Pessoa> pessoas = new ArrayList<>();

    /** No banco esse id seria gerado sozinho, aqui eu simulo com um contador. */
    private long proximoId = 1L;

    public void cadastrar(Pessoa pessoa) throws BibliotecaException {
        if (buscarPorDocumentoSilencioso(pessoa.getDocumento()) != null) {
            throw new RegistroDuplicadoException(
                    "Ja existe uma pessoa cadastrada com o documento "
                            + pessoa.getDocumento() + ".");
        }
        pessoa.setId(proximoId++);
        pessoas.add(pessoa);
    }

    public List<Pessoa> listarTodos() {
        return pessoas;
    }

    public Pessoa buscarPorDocumento(String documento) throws PessoaNaoEncontradaException {
        Pessoa pessoa = buscarPorDocumentoSilencioso(documento);
        if (pessoa == null) {
            throw new PessoaNaoEncontradaException(documento);
        }
        return pessoa;
    }

    private Pessoa buscarPorDocumentoSilencioso(String documento) {
        for (Pessoa p : pessoas) {
            if (p.getDocumento().equalsIgnoreCase(documento.trim())) {
                return p;
            }
        }
        return null;
    }
}
