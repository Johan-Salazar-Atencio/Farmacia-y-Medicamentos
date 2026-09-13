package com.farmacia.service.impl;

import com.farmacia.dto.MedicamentoForm;
import com.farmacia.entity.CategoriaMedicamento;
import com.farmacia.entity.Estado;
import com.farmacia.entity.Medicamento;
import com.farmacia.exception.ResourceNotFoundException;
import com.farmacia.repository.CategoriaMedicamentoRepository;
import com.farmacia.repository.MedicamentoRepository;
import com.farmacia.service.MedicamentoService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class MedicamentoServiceImpl implements MedicamentoService {

    private static final String PREFIJO_CODIGO = "MED-";
    private static final int LONGITUD_NUMERO = 5;

    private final MedicamentoRepository medicamentoRepository;
    private final CategoriaMedicamentoRepository categoriaRepository;

    public MedicamentoServiceImpl(MedicamentoRepository medicamentoRepository,
                                   CategoriaMedicamentoRepository categoriaRepository) {
        this.medicamentoRepository = medicamentoRepository;
        this.categoriaRepository = categoriaRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Medicamento> buscar(Estado estado, Long categoriaId) {
        return medicamentoRepository.buscar(estado, categoriaId);
    }

    @Override
    @Transactional(readOnly = true)
    public Medicamento obtenerPorId(Long id) {
        return medicamentoRepository.findByIdConCategoria(id)
                .orElseThrow(() -> new ResourceNotFoundException("No se encontró el medicamento con id " + id));
    }

    @Override
    public Medicamento crear(MedicamentoForm form) {
        CategoriaMedicamento categoria = obtenerCategoria(form.getCategoriaId());

        Medicamento medicamento = new Medicamento();
        medicamento.setCodigo(generarSiguienteCodigo());
        medicamento.setStock(0);
        aplicarDatos(medicamento, form, categoria);
        return medicamentoRepository.save(medicamento);
    }

    @Override
    public Medicamento actualizar(Long id, MedicamentoForm form) {
        Medicamento medicamento = obtenerPorId(id);
        CategoriaMedicamento categoria = obtenerCategoria(form.getCategoriaId());
        // El código (RF-FAR-06) y el stock no se tocan en una edición: el código es
        // inmutable una vez generado y el stock solo lo actualizan los lotes.
        aplicarDatos(medicamento, form, categoria);
        return medicamentoRepository.save(medicamento);
    }

    @Override
    public void cambiarEstado(Long id) {
        Medicamento medicamento = obtenerPorId(id);
        medicamento.setEstado(medicamento.getEstado() == Estado.ACTIVO ? Estado.INACTIVO : Estado.ACTIVO);
        medicamentoRepository.save(medicamento);
    }

    /**
     * RF-FAR-06: genera códigos secuenciales con formato MED-00001. El "synchronized"
     * evita que dos solicitudes concurrentes en la misma instancia generen el mismo
     * número; en un despliegue con varias instancias se recomendaría reemplazarlo por
     * una secuencia de base de datos.
     */
    private synchronized String generarSiguienteCodigo() {
        Optional<Medicamento> ultimo = medicamentoRepository.findTopByOrderByIdDesc();
        int siguienteNumero = 1;
        if (ultimo.isPresent()) {
            String codigoAnterior = ultimo.get().getCodigo();
            String parteNumerica = codigoAnterior.replace(PREFIJO_CODIGO, "");
            try {
                siguienteNumero = Integer.parseInt(parteNumerica) + 1;
            } catch (NumberFormatException ex) {
                siguienteNumero = (int) medicamentoRepository.count() + 1;
            }
        }
        String candidato = formatearCodigo(siguienteNumero);
        while (medicamentoRepository.existsByCodigo(candidato)) {
            siguienteNumero++;
            candidato = formatearCodigo(siguienteNumero);
        }
        return candidato;
    }

    private String formatearCodigo(int numero) {
        return PREFIJO_CODIGO + String.format("%0" + LONGITUD_NUMERO + "d", numero);
    }

    private CategoriaMedicamento obtenerCategoria(Long categoriaId) {
        return categoriaRepository.findById(categoriaId)
                .orElseThrow(() -> new ResourceNotFoundException("No se encontró la categoría con id " + categoriaId));
    }

    private void aplicarDatos(Medicamento medicamento, MedicamentoForm form, CategoriaMedicamento categoria) {
        medicamento.setNombre(form.getNombre());
        medicamento.setDescripcion(form.getDescripcion());
        medicamento.setPresentacion(form.getPresentacion());
        medicamento.setConcentracion(form.getConcentracion());
        medicamento.setPrecioVenta(form.getPrecioVenta());
        medicamento.setStockMinimo(form.getStockMinimo());
        medicamento.setCategoria(categoria);
    }
}
