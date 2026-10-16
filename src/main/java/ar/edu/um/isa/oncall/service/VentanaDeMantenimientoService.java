package ar.edu.um.isa.oncall.service;

import ar.edu.um.isa.oncall.domain.VentanaDeMantenimiento;
import ar.edu.um.isa.oncall.repository.VentanaDeMantenimientoRepository;
import ar.edu.um.isa.oncall.service.dto.VentanaDeMantenimientoDTO;
import ar.edu.um.isa.oncall.service.mapper.VentanaDeMantenimientoMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link ar.edu.um.isa.oncall.domain.VentanaDeMantenimiento}.
 */
@Service
@Transactional
public class VentanaDeMantenimientoService {

    private static final Logger LOG = LoggerFactory.getLogger(VentanaDeMantenimientoService.class);

    private final VentanaDeMantenimientoRepository ventanaDeMantenimientoRepository;

    private final VentanaDeMantenimientoMapper ventanaDeMantenimientoMapper;

    public VentanaDeMantenimientoService(
        VentanaDeMantenimientoRepository ventanaDeMantenimientoRepository,
        VentanaDeMantenimientoMapper ventanaDeMantenimientoMapper
    ) {
        this.ventanaDeMantenimientoRepository = ventanaDeMantenimientoRepository;
        this.ventanaDeMantenimientoMapper = ventanaDeMantenimientoMapper;
    }

    /**
     * Save a ventanaDeMantenimiento.
     *
     * @param ventanaDeMantenimientoDTO the entity to save.
     * @return the persisted entity.
     */
    public VentanaDeMantenimientoDTO save(VentanaDeMantenimientoDTO ventanaDeMantenimientoDTO) {
        LOG.debug("Request to save VentanaDeMantenimiento : {}", ventanaDeMantenimientoDTO);
        VentanaDeMantenimiento ventanaDeMantenimiento = ventanaDeMantenimientoMapper.toEntity(ventanaDeMantenimientoDTO);
        ventanaDeMantenimiento = ventanaDeMantenimientoRepository.save(ventanaDeMantenimiento);
        return ventanaDeMantenimientoMapper.toDto(ventanaDeMantenimiento);
    }

    /**
     * Update a ventanaDeMantenimiento.
     *
     * @param ventanaDeMantenimientoDTO the entity to save.
     * @return the persisted entity.
     */
    public VentanaDeMantenimientoDTO update(VentanaDeMantenimientoDTO ventanaDeMantenimientoDTO) {
        LOG.debug("Request to update VentanaDeMantenimiento : {}", ventanaDeMantenimientoDTO);
        VentanaDeMantenimiento ventanaDeMantenimiento = ventanaDeMantenimientoMapper.toEntity(ventanaDeMantenimientoDTO);
        ventanaDeMantenimiento = ventanaDeMantenimientoRepository.save(ventanaDeMantenimiento);
        return ventanaDeMantenimientoMapper.toDto(ventanaDeMantenimiento);
    }

    /**
     * Partially update a ventanaDeMantenimiento.
     *
     * @param ventanaDeMantenimientoDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<VentanaDeMantenimientoDTO> partialUpdate(VentanaDeMantenimientoDTO ventanaDeMantenimientoDTO) {
        LOG.debug("Request to partially update VentanaDeMantenimiento : {}", ventanaDeMantenimientoDTO);

        return ventanaDeMantenimientoRepository
            .findById(ventanaDeMantenimientoDTO.getId())
            .map(existingVentanaDeMantenimiento -> {
                ventanaDeMantenimientoMapper.partialUpdate(existingVentanaDeMantenimiento, ventanaDeMantenimientoDTO);

                return existingVentanaDeMantenimiento;
            })
            .map(ventanaDeMantenimientoRepository::save)
            .map(ventanaDeMantenimientoMapper::toDto);
    }

    /**
     * Get all the ventanaDeMantenimientos with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<VentanaDeMantenimientoDTO> findAllWithEagerRelationships(Pageable pageable) {
        return ventanaDeMantenimientoRepository.findAllWithEagerRelationships(pageable).map(ventanaDeMantenimientoMapper::toDto);
    }

    /**
     * Get one ventanaDeMantenimiento by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<VentanaDeMantenimientoDTO> findOne(Long id) {
        LOG.debug("Request to get VentanaDeMantenimiento : {}", id);
        return ventanaDeMantenimientoRepository.findOneWithEagerRelationships(id).map(ventanaDeMantenimientoMapper::toDto);
    }

    /**
     * Delete the ventanaDeMantenimiento by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete VentanaDeMantenimiento : {}", id);
        ventanaDeMantenimientoRepository.deleteById(id);
    }
}
