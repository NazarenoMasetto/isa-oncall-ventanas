package ar.edu.um.isa.oncall.web.rest;

import ar.edu.um.isa.oncall.repository.VentanaDeMantenimientoRepository;
import ar.edu.um.isa.oncall.service.VentanaDeMantenimientoQueryService;
import ar.edu.um.isa.oncall.service.VentanaDeMantenimientoService;
import ar.edu.um.isa.oncall.service.criteria.VentanaDeMantenimientoCriteria;
import ar.edu.um.isa.oncall.service.dto.VentanaDeMantenimientoDTO;
import ar.edu.um.isa.oncall.web.rest.errors.BadRequestAlertException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link ar.edu.um.isa.oncall.domain.VentanaDeMantenimiento}.
 */
@RestController
@RequestMapping("/api/ventana-de-mantenimientos")
public class VentanaDeMantenimientoResource {

    private static final Logger LOG = LoggerFactory.getLogger(VentanaDeMantenimientoResource.class);

    private static final String ENTITY_NAME = "ventanaDeMantenimiento";

    @Value("${jhipster.clientApp.name:oncall}")
    private String applicationName;

    private final VentanaDeMantenimientoService ventanaDeMantenimientoService;

    private final VentanaDeMantenimientoRepository ventanaDeMantenimientoRepository;

    private final VentanaDeMantenimientoQueryService ventanaDeMantenimientoQueryService;

    public VentanaDeMantenimientoResource(
        VentanaDeMantenimientoService ventanaDeMantenimientoService,
        VentanaDeMantenimientoRepository ventanaDeMantenimientoRepository,
        VentanaDeMantenimientoQueryService ventanaDeMantenimientoQueryService
    ) {
        this.ventanaDeMantenimientoService = ventanaDeMantenimientoService;
        this.ventanaDeMantenimientoRepository = ventanaDeMantenimientoRepository;
        this.ventanaDeMantenimientoQueryService = ventanaDeMantenimientoQueryService;
    }

    /**
     * {@code POST  /ventana-de-mantenimientos} : Create a new ventanaDeMantenimiento.
     *
     * @param ventanaDeMantenimientoDTO the ventanaDeMantenimientoDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new ventanaDeMantenimientoDTO, or with status {@code 400 (Bad Request)} if the ventanaDeMantenimiento has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<VentanaDeMantenimientoDTO> createVentanaDeMantenimiento(
        @Valid @RequestBody VentanaDeMantenimientoDTO ventanaDeMantenimientoDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to save VentanaDeMantenimiento : {}", ventanaDeMantenimientoDTO);
        if (ventanaDeMantenimientoDTO.getId() != null) {
            throw new BadRequestAlertException("A new ventanaDeMantenimiento cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ventanaDeMantenimientoDTO = ventanaDeMantenimientoService.save(ventanaDeMantenimientoDTO);
        return ResponseEntity.created(new URI("/api/ventana-de-mantenimientos/" + ventanaDeMantenimientoDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, ventanaDeMantenimientoDTO.getId().toString()))
            .body(ventanaDeMantenimientoDTO);
    }

    /**
     * {@code PUT  /ventana-de-mantenimientos/:id} : Updates an existing ventanaDeMantenimiento.
     *
     * @param id the id of the ventanaDeMantenimientoDTO to save.
     * @param ventanaDeMantenimientoDTO the ventanaDeMantenimientoDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated ventanaDeMantenimientoDTO,
     * or with status {@code 400 (Bad Request)} if the ventanaDeMantenimientoDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the ventanaDeMantenimientoDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<VentanaDeMantenimientoDTO> updateVentanaDeMantenimiento(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody VentanaDeMantenimientoDTO ventanaDeMantenimientoDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update VentanaDeMantenimiento : {}, {}", id, ventanaDeMantenimientoDTO);
        if (ventanaDeMantenimientoDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, ventanaDeMantenimientoDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!ventanaDeMantenimientoRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        ventanaDeMantenimientoDTO = ventanaDeMantenimientoService.update(ventanaDeMantenimientoDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, ventanaDeMantenimientoDTO.getId().toString()))
            .body(ventanaDeMantenimientoDTO);
    }

    /**
     * {@code PATCH  /ventana-de-mantenimientos/:id} : Partial updates given fields of an existing ventanaDeMantenimiento, field will ignore if it is null
     *
     * @param id the id of the ventanaDeMantenimientoDTO to save.
     * @param ventanaDeMantenimientoDTO the ventanaDeMantenimientoDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated ventanaDeMantenimientoDTO,
     * or with status {@code 400 (Bad Request)} if the ventanaDeMantenimientoDTO is not valid,
     * or with status {@code 404 (Not Found)} if the ventanaDeMantenimientoDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the ventanaDeMantenimientoDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<VentanaDeMantenimientoDTO> partialUpdateVentanaDeMantenimiento(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody VentanaDeMantenimientoDTO ventanaDeMantenimientoDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update VentanaDeMantenimiento partially : {}, {}", id, ventanaDeMantenimientoDTO);
        if (ventanaDeMantenimientoDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, ventanaDeMantenimientoDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!ventanaDeMantenimientoRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<VentanaDeMantenimientoDTO> result = ventanaDeMantenimientoService.partialUpdate(ventanaDeMantenimientoDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, ventanaDeMantenimientoDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /ventana-de-mantenimientos} : get all the Ventana De Mantenimientos.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of Ventana De Mantenimientos in body.
     */
    @GetMapping("")
    public ResponseEntity<List<VentanaDeMantenimientoDTO>> getAllVentanaDeMantenimientos(
        VentanaDeMantenimientoCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get VentanaDeMantenimientos by criteria: {}", criteria);

        Page<VentanaDeMantenimientoDTO> page = ventanaDeMantenimientoQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /ventana-de-mantenimientos/count} : count all the ventanaDeMantenimientos.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countVentanaDeMantenimientos(VentanaDeMantenimientoCriteria criteria) {
        LOG.debug("REST request to count VentanaDeMantenimientos by criteria: {}", criteria);
        return ResponseEntity.ok().body(ventanaDeMantenimientoQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /ventana-de-mantenimientos/:id} : get the "id" ventanaDeMantenimiento.
     *
     * @param id the id of the ventanaDeMantenimientoDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the ventanaDeMantenimientoDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<VentanaDeMantenimientoDTO> getVentanaDeMantenimiento(@PathVariable("id") Long id) {
        LOG.debug("REST request to get VentanaDeMantenimiento : {}", id);
        Optional<VentanaDeMantenimientoDTO> ventanaDeMantenimientoDTO = ventanaDeMantenimientoService.findOne(id);
        return ResponseUtil.wrapOrNotFound(ventanaDeMantenimientoDTO);
    }

    /**
     * {@code DELETE  /ventana-de-mantenimientos/:id} : delete the "id" ventanaDeMantenimiento.
     *
     * @param id the id of the ventanaDeMantenimientoDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteVentanaDeMantenimiento(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete VentanaDeMantenimiento : {}", id);
        ventanaDeMantenimientoService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
