package ar.edu.um.isa.oncall.web.rest;

import static ar.edu.um.isa.oncall.domain.ServicioEnVentanaAsserts.*;
import static ar.edu.um.isa.oncall.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import ar.edu.um.isa.oncall.IntegrationTest;
import ar.edu.um.isa.oncall.domain.Servicio;
import ar.edu.um.isa.oncall.domain.ServicioEnVentana;
import ar.edu.um.isa.oncall.domain.VentanaDeMantenimiento;
import ar.edu.um.isa.oncall.repository.ServicioEnVentanaRepository;
import ar.edu.um.isa.oncall.service.ServicioEnVentanaService;
import ar.edu.um.isa.oncall.service.dto.ServicioEnVentanaDTO;
import ar.edu.um.isa.oncall.service.mapper.ServicioEnVentanaMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link ServicioEnVentanaResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class ServicioEnVentanaResourceIT {

    private static final Boolean DEFAULT_SUPRIMIR_ALERTAS = false;
    private static final Boolean UPDATED_SUPRIMIR_ALERTAS = true;

    private static final Boolean DEFAULT_SUPRIMIR_ESCALAMIENTO = false;
    private static final Boolean UPDATED_SUPRIMIR_ESCALAMIENTO = true;

    private static final String DEFAULT_NOTAS = "AAAAAAAAAA";
    private static final String UPDATED_NOTAS = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/servicio-en-ventanas";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ServicioEnVentanaRepository servicioEnVentanaRepository;

    @Mock
    private ServicioEnVentanaRepository servicioEnVentanaRepositoryMock;

    @Autowired
    private ServicioEnVentanaMapper servicioEnVentanaMapper;

    @Mock
    private ServicioEnVentanaService servicioEnVentanaServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restServicioEnVentanaMockMvc;

    private ServicioEnVentana servicioEnVentana;

    private ServicioEnVentana insertedServicioEnVentana;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ServicioEnVentana createEntity(EntityManager em) {
        ServicioEnVentana servicioEnVentana = new ServicioEnVentana()
            .suprimirAlertas(DEFAULT_SUPRIMIR_ALERTAS)
            .suprimirEscalamiento(DEFAULT_SUPRIMIR_ESCALAMIENTO)
            .notas(DEFAULT_NOTAS);
        // Add required entity
        VentanaDeMantenimiento ventanaDeMantenimiento;
        if (TestUtil.findAll(em, VentanaDeMantenimiento.class).isEmpty()) {
            ventanaDeMantenimiento = VentanaDeMantenimientoResourceIT.createEntity(em);
            em.persist(ventanaDeMantenimiento);
            em.flush();
        } else {
            ventanaDeMantenimiento = TestUtil.findAll(em, VentanaDeMantenimiento.class).get(0);
        }
        servicioEnVentana.setVentana(ventanaDeMantenimiento);
        // Add required entity
        Servicio servicio;
        if (TestUtil.findAll(em, Servicio.class).isEmpty()) {
            servicio = ServicioResourceIT.createEntity(em);
            em.persist(servicio);
            em.flush();
        } else {
            servicio = TestUtil.findAll(em, Servicio.class).get(0);
        }
        servicioEnVentana.setServicio(servicio);
        return servicioEnVentana;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ServicioEnVentana createUpdatedEntity(EntityManager em) {
        ServicioEnVentana updatedServicioEnVentana = new ServicioEnVentana()
            .suprimirAlertas(UPDATED_SUPRIMIR_ALERTAS)
            .suprimirEscalamiento(UPDATED_SUPRIMIR_ESCALAMIENTO)
            .notas(UPDATED_NOTAS);
        // Add required entity
        VentanaDeMantenimiento ventanaDeMantenimiento;
        if (TestUtil.findAll(em, VentanaDeMantenimiento.class).isEmpty()) {
            ventanaDeMantenimiento = VentanaDeMantenimientoResourceIT.createUpdatedEntity(em);
            em.persist(ventanaDeMantenimiento);
            em.flush();
        } else {
            ventanaDeMantenimiento = TestUtil.findAll(em, VentanaDeMantenimiento.class).get(0);
        }
        updatedServicioEnVentana.setVentana(ventanaDeMantenimiento);
        // Add required entity
        Servicio servicio;
        if (TestUtil.findAll(em, Servicio.class).isEmpty()) {
            servicio = ServicioResourceIT.createUpdatedEntity(em);
            em.persist(servicio);
            em.flush();
        } else {
            servicio = TestUtil.findAll(em, Servicio.class).get(0);
        }
        updatedServicioEnVentana.setServicio(servicio);
        return updatedServicioEnVentana;
    }

    @BeforeEach
    void initTest() {
        servicioEnVentana = createEntity(em);
    }

    @AfterEach
    void cleanup() {
        if (insertedServicioEnVentana != null) {
            servicioEnVentanaRepository.delete(insertedServicioEnVentana);
            insertedServicioEnVentana = null;
        }
    }

    @Test
    @Transactional
    void createServicioEnVentana() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the ServicioEnVentana
        ServicioEnVentanaDTO servicioEnVentanaDTO = servicioEnVentanaMapper.toDto(servicioEnVentana);
        var returnedServicioEnVentanaDTO = om.readValue(
            restServicioEnVentanaMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(servicioEnVentanaDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            ServicioEnVentanaDTO.class
        );

        // Validate the ServicioEnVentana in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedServicioEnVentana = servicioEnVentanaMapper.toEntity(returnedServicioEnVentanaDTO);
        assertServicioEnVentanaUpdatableFieldsEquals(returnedServicioEnVentana, getPersistedServicioEnVentana(returnedServicioEnVentana));

        insertedServicioEnVentana = returnedServicioEnVentana;
    }

    @Test
    @Transactional
    void createServicioEnVentanaWithExistingId() throws Exception {
        // Create the ServicioEnVentana with an existing ID
        servicioEnVentana.setId(1L);
        ServicioEnVentanaDTO servicioEnVentanaDTO = servicioEnVentanaMapper.toDto(servicioEnVentana);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restServicioEnVentanaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(servicioEnVentanaDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ServicioEnVentana in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkSuprimirAlertasIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        servicioEnVentana.setSuprimirAlertas(null);

        // Create the ServicioEnVentana, which fails.
        ServicioEnVentanaDTO servicioEnVentanaDTO = servicioEnVentanaMapper.toDto(servicioEnVentana);

        restServicioEnVentanaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(servicioEnVentanaDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkSuprimirEscalamientoIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        servicioEnVentana.setSuprimirEscalamiento(null);

        // Create the ServicioEnVentana, which fails.
        ServicioEnVentanaDTO servicioEnVentanaDTO = servicioEnVentanaMapper.toDto(servicioEnVentana);

        restServicioEnVentanaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(servicioEnVentanaDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllServicioEnVentanas() throws Exception {
        // Initialize the database
        insertedServicioEnVentana = servicioEnVentanaRepository.saveAndFlush(servicioEnVentana);

        // Get all the servicioEnVentanaList
        restServicioEnVentanaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(servicioEnVentana.getId().intValue())))
            .andExpect(jsonPath("$.[*].suprimirAlertas").value(hasItem(DEFAULT_SUPRIMIR_ALERTAS)))
            .andExpect(jsonPath("$.[*].suprimirEscalamiento").value(hasItem(DEFAULT_SUPRIMIR_ESCALAMIENTO)))
            .andExpect(jsonPath("$.[*].notas").value(hasItem(DEFAULT_NOTAS)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllServicioEnVentanasWithEagerRelationshipsIsEnabled() throws Exception {
        when(servicioEnVentanaServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restServicioEnVentanaMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(servicioEnVentanaServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllServicioEnVentanasWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(servicioEnVentanaServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restServicioEnVentanaMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(servicioEnVentanaRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getServicioEnVentana() throws Exception {
        // Initialize the database
        insertedServicioEnVentana = servicioEnVentanaRepository.saveAndFlush(servicioEnVentana);

        // Get the servicioEnVentana
        restServicioEnVentanaMockMvc
            .perform(get(ENTITY_API_URL_ID, servicioEnVentana.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(servicioEnVentana.getId().intValue()))
            .andExpect(jsonPath("$.suprimirAlertas").value(DEFAULT_SUPRIMIR_ALERTAS))
            .andExpect(jsonPath("$.suprimirEscalamiento").value(DEFAULT_SUPRIMIR_ESCALAMIENTO))
            .andExpect(jsonPath("$.notas").value(DEFAULT_NOTAS));
    }

    @Test
    @Transactional
    void getServicioEnVentanasByIdFiltering() throws Exception {
        // Initialize the database
        insertedServicioEnVentana = servicioEnVentanaRepository.saveAndFlush(servicioEnVentana);

        Long id = servicioEnVentana.getId();

        defaultServicioEnVentanaFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultServicioEnVentanaFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultServicioEnVentanaFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllServicioEnVentanasBySuprimirAlertasIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedServicioEnVentana = servicioEnVentanaRepository.saveAndFlush(servicioEnVentana);

        // Get all the servicioEnVentanaList where suprimirAlertas equals to
        defaultServicioEnVentanaFiltering(
            "suprimirAlertas.equals=" + DEFAULT_SUPRIMIR_ALERTAS,
            "suprimirAlertas.equals=" + UPDATED_SUPRIMIR_ALERTAS
        );
    }

    @Test
    @Transactional
    void getAllServicioEnVentanasBySuprimirAlertasIsInShouldWork() throws Exception {
        // Initialize the database
        insertedServicioEnVentana = servicioEnVentanaRepository.saveAndFlush(servicioEnVentana);

        // Get all the servicioEnVentanaList where suprimirAlertas in
        defaultServicioEnVentanaFiltering(
            "suprimirAlertas.in=" + DEFAULT_SUPRIMIR_ALERTAS + "," + UPDATED_SUPRIMIR_ALERTAS,
            "suprimirAlertas.in=" + UPDATED_SUPRIMIR_ALERTAS
        );
    }

    @Test
    @Transactional
    void getAllServicioEnVentanasBySuprimirAlertasIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedServicioEnVentana = servicioEnVentanaRepository.saveAndFlush(servicioEnVentana);

        // Get all the servicioEnVentanaList where suprimirAlertas is not null
        defaultServicioEnVentanaFiltering("suprimirAlertas.specified=true", "suprimirAlertas.specified=false");
    }

    @Test
    @Transactional
    void getAllServicioEnVentanasBySuprimirEscalamientoIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedServicioEnVentana = servicioEnVentanaRepository.saveAndFlush(servicioEnVentana);

        // Get all the servicioEnVentanaList where suprimirEscalamiento equals to
        defaultServicioEnVentanaFiltering(
            "suprimirEscalamiento.equals=" + DEFAULT_SUPRIMIR_ESCALAMIENTO,
            "suprimirEscalamiento.equals=" + UPDATED_SUPRIMIR_ESCALAMIENTO
        );
    }

    @Test
    @Transactional
    void getAllServicioEnVentanasBySuprimirEscalamientoIsInShouldWork() throws Exception {
        // Initialize the database
        insertedServicioEnVentana = servicioEnVentanaRepository.saveAndFlush(servicioEnVentana);

        // Get all the servicioEnVentanaList where suprimirEscalamiento in
        defaultServicioEnVentanaFiltering(
            "suprimirEscalamiento.in=" + DEFAULT_SUPRIMIR_ESCALAMIENTO + "," + UPDATED_SUPRIMIR_ESCALAMIENTO,
            "suprimirEscalamiento.in=" + UPDATED_SUPRIMIR_ESCALAMIENTO
        );
    }

    @Test
    @Transactional
    void getAllServicioEnVentanasBySuprimirEscalamientoIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedServicioEnVentana = servicioEnVentanaRepository.saveAndFlush(servicioEnVentana);

        // Get all the servicioEnVentanaList where suprimirEscalamiento is not null
        defaultServicioEnVentanaFiltering("suprimirEscalamiento.specified=true", "suprimirEscalamiento.specified=false");
    }

    @Test
    @Transactional
    void getAllServicioEnVentanasByNotasIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedServicioEnVentana = servicioEnVentanaRepository.saveAndFlush(servicioEnVentana);

        // Get all the servicioEnVentanaList where notas equals to
        defaultServicioEnVentanaFiltering("notas.equals=" + DEFAULT_NOTAS, "notas.equals=" + UPDATED_NOTAS);
    }

    @Test
    @Transactional
    void getAllServicioEnVentanasByNotasIsInShouldWork() throws Exception {
        // Initialize the database
        insertedServicioEnVentana = servicioEnVentanaRepository.saveAndFlush(servicioEnVentana);

        // Get all the servicioEnVentanaList where notas in
        defaultServicioEnVentanaFiltering("notas.in=" + DEFAULT_NOTAS + "," + UPDATED_NOTAS, "notas.in=" + UPDATED_NOTAS);
    }

    @Test
    @Transactional
    void getAllServicioEnVentanasByNotasIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedServicioEnVentana = servicioEnVentanaRepository.saveAndFlush(servicioEnVentana);

        // Get all the servicioEnVentanaList where notas is not null
        defaultServicioEnVentanaFiltering("notas.specified=true", "notas.specified=false");
    }

    @Test
    @Transactional
    void getAllServicioEnVentanasByNotasContainsSomething() throws Exception {
        // Initialize the database
        insertedServicioEnVentana = servicioEnVentanaRepository.saveAndFlush(servicioEnVentana);

        // Get all the servicioEnVentanaList where notas contains
        defaultServicioEnVentanaFiltering("notas.contains=" + DEFAULT_NOTAS, "notas.contains=" + UPDATED_NOTAS);
    }

    @Test
    @Transactional
    void getAllServicioEnVentanasByNotasNotContainsSomething() throws Exception {
        // Initialize the database
        insertedServicioEnVentana = servicioEnVentanaRepository.saveAndFlush(servicioEnVentana);

        // Get all the servicioEnVentanaList where notas does not contain
        defaultServicioEnVentanaFiltering("notas.doesNotContain=" + UPDATED_NOTAS, "notas.doesNotContain=" + DEFAULT_NOTAS);
    }

    @Test
    @Transactional
    void getAllServicioEnVentanasByVentanaIsEqualToSomething() throws Exception {
        VentanaDeMantenimiento ventana;
        if (TestUtil.findAll(em, VentanaDeMantenimiento.class).isEmpty()) {
            servicioEnVentanaRepository.saveAndFlush(servicioEnVentana);
            ventana = VentanaDeMantenimientoResourceIT.createEntity(em);
        } else {
            ventana = TestUtil.findAll(em, VentanaDeMantenimiento.class).get(0);
        }
        em.persist(ventana);
        em.flush();
        servicioEnVentana.setVentana(ventana);
        servicioEnVentanaRepository.saveAndFlush(servicioEnVentana);
        Long ventanaId = ventana.getId();
        // Get all the servicioEnVentanaList where ventana equals to ventanaId
        defaultServicioEnVentanaShouldBeFound("ventanaId.equals=" + ventanaId);

        // Get all the servicioEnVentanaList where ventana equals to (ventanaId + 1)
        defaultServicioEnVentanaShouldNotBeFound("ventanaId.equals=" + (ventanaId + 1));
    }

    @Test
    @Transactional
    void getAllServicioEnVentanasByServicioIsEqualToSomething() throws Exception {
        Servicio servicio;
        if (TestUtil.findAll(em, Servicio.class).isEmpty()) {
            servicioEnVentanaRepository.saveAndFlush(servicioEnVentana);
            servicio = ServicioResourceIT.createEntity(em);
        } else {
            servicio = TestUtil.findAll(em, Servicio.class).get(0);
        }
        em.persist(servicio);
        em.flush();
        servicioEnVentana.setServicio(servicio);
        servicioEnVentanaRepository.saveAndFlush(servicioEnVentana);
        Long servicioId = servicio.getId();
        // Get all the servicioEnVentanaList where servicio equals to servicioId
        defaultServicioEnVentanaShouldBeFound("servicioId.equals=" + servicioId);

        // Get all the servicioEnVentanaList where servicio equals to (servicioId + 1)
        defaultServicioEnVentanaShouldNotBeFound("servicioId.equals=" + (servicioId + 1));
    }

    private void defaultServicioEnVentanaFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultServicioEnVentanaShouldBeFound(shouldBeFound);
        defaultServicioEnVentanaShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultServicioEnVentanaShouldBeFound(String filter) throws Exception {
        restServicioEnVentanaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(servicioEnVentana.getId().intValue())))
            .andExpect(jsonPath("$.[*].suprimirAlertas").value(hasItem(DEFAULT_SUPRIMIR_ALERTAS)))
            .andExpect(jsonPath("$.[*].suprimirEscalamiento").value(hasItem(DEFAULT_SUPRIMIR_ESCALAMIENTO)))
            .andExpect(jsonPath("$.[*].notas").value(hasItem(DEFAULT_NOTAS)));

        // Check, that the count call also returns 1
        restServicioEnVentanaMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultServicioEnVentanaShouldNotBeFound(String filter) throws Exception {
        restServicioEnVentanaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restServicioEnVentanaMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingServicioEnVentana() throws Exception {
        // Get the servicioEnVentana
        restServicioEnVentanaMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingServicioEnVentana() throws Exception {
        // Initialize the database
        insertedServicioEnVentana = servicioEnVentanaRepository.saveAndFlush(servicioEnVentana);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the servicioEnVentana
        ServicioEnVentana updatedServicioEnVentana = servicioEnVentanaRepository.findById(servicioEnVentana.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedServicioEnVentana are not directly saved in db
        em.detach(updatedServicioEnVentana);
        updatedServicioEnVentana
            .suprimirAlertas(UPDATED_SUPRIMIR_ALERTAS)
            .suprimirEscalamiento(UPDATED_SUPRIMIR_ESCALAMIENTO)
            .notas(UPDATED_NOTAS);
        ServicioEnVentanaDTO servicioEnVentanaDTO = servicioEnVentanaMapper.toDto(updatedServicioEnVentana);

        restServicioEnVentanaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, servicioEnVentanaDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(servicioEnVentanaDTO))
            )
            .andExpect(status().isOk());

        // Validate the ServicioEnVentana in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedServicioEnVentanaToMatchAllProperties(updatedServicioEnVentana);
    }

    @Test
    @Transactional
    void putNonExistingServicioEnVentana() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        servicioEnVentana.setId(longCount.incrementAndGet());

        // Create the ServicioEnVentana
        ServicioEnVentanaDTO servicioEnVentanaDTO = servicioEnVentanaMapper.toDto(servicioEnVentana);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restServicioEnVentanaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, servicioEnVentanaDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(servicioEnVentanaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ServicioEnVentana in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchServicioEnVentana() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        servicioEnVentana.setId(longCount.incrementAndGet());

        // Create the ServicioEnVentana
        ServicioEnVentanaDTO servicioEnVentanaDTO = servicioEnVentanaMapper.toDto(servicioEnVentana);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restServicioEnVentanaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(servicioEnVentanaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ServicioEnVentana in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamServicioEnVentana() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        servicioEnVentana.setId(longCount.incrementAndGet());

        // Create the ServicioEnVentana
        ServicioEnVentanaDTO servicioEnVentanaDTO = servicioEnVentanaMapper.toDto(servicioEnVentana);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restServicioEnVentanaMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(servicioEnVentanaDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ServicioEnVentana in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateServicioEnVentanaWithPatch() throws Exception {
        // Initialize the database
        insertedServicioEnVentana = servicioEnVentanaRepository.saveAndFlush(servicioEnVentana);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the servicioEnVentana using partial update
        ServicioEnVentana partialUpdatedServicioEnVentana = new ServicioEnVentana();
        partialUpdatedServicioEnVentana.setId(servicioEnVentana.getId());

        partialUpdatedServicioEnVentana.suprimirAlertas(UPDATED_SUPRIMIR_ALERTAS).notas(UPDATED_NOTAS);

        restServicioEnVentanaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedServicioEnVentana.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedServicioEnVentana))
            )
            .andExpect(status().isOk());

        // Validate the ServicioEnVentana in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertServicioEnVentanaUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedServicioEnVentana, servicioEnVentana),
            getPersistedServicioEnVentana(servicioEnVentana)
        );
    }

    @Test
    @Transactional
    void fullUpdateServicioEnVentanaWithPatch() throws Exception {
        // Initialize the database
        insertedServicioEnVentana = servicioEnVentanaRepository.saveAndFlush(servicioEnVentana);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the servicioEnVentana using partial update
        ServicioEnVentana partialUpdatedServicioEnVentana = new ServicioEnVentana();
        partialUpdatedServicioEnVentana.setId(servicioEnVentana.getId());

        partialUpdatedServicioEnVentana
            .suprimirAlertas(UPDATED_SUPRIMIR_ALERTAS)
            .suprimirEscalamiento(UPDATED_SUPRIMIR_ESCALAMIENTO)
            .notas(UPDATED_NOTAS);

        restServicioEnVentanaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedServicioEnVentana.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedServicioEnVentana))
            )
            .andExpect(status().isOk());

        // Validate the ServicioEnVentana in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertServicioEnVentanaUpdatableFieldsEquals(
            partialUpdatedServicioEnVentana,
            getPersistedServicioEnVentana(partialUpdatedServicioEnVentana)
        );
    }

    @Test
    @Transactional
    void patchNonExistingServicioEnVentana() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        servicioEnVentana.setId(longCount.incrementAndGet());

        // Create the ServicioEnVentana
        ServicioEnVentanaDTO servicioEnVentanaDTO = servicioEnVentanaMapper.toDto(servicioEnVentana);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restServicioEnVentanaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, servicioEnVentanaDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(servicioEnVentanaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ServicioEnVentana in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchServicioEnVentana() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        servicioEnVentana.setId(longCount.incrementAndGet());

        // Create the ServicioEnVentana
        ServicioEnVentanaDTO servicioEnVentanaDTO = servicioEnVentanaMapper.toDto(servicioEnVentana);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restServicioEnVentanaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(servicioEnVentanaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ServicioEnVentana in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamServicioEnVentana() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        servicioEnVentana.setId(longCount.incrementAndGet());

        // Create the ServicioEnVentana
        ServicioEnVentanaDTO servicioEnVentanaDTO = servicioEnVentanaMapper.toDto(servicioEnVentana);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restServicioEnVentanaMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(servicioEnVentanaDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ServicioEnVentana in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteServicioEnVentana() throws Exception {
        // Initialize the database
        insertedServicioEnVentana = servicioEnVentanaRepository.saveAndFlush(servicioEnVentana);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the servicioEnVentana
        restServicioEnVentanaMockMvc
            .perform(delete(ENTITY_API_URL_ID, servicioEnVentana.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return servicioEnVentanaRepository.count();
    }

    protected void assertIncrementedRepositoryCount(long countBefore) {
        assertThat(countBefore + 1).isEqualTo(getRepositoryCount());
    }

    protected void assertDecrementedRepositoryCount(long countBefore) {
        assertThat(countBefore - 1).isEqualTo(getRepositoryCount());
    }

    protected void assertSameRepositoryCount(long countBefore) {
        assertThat(countBefore).isEqualTo(getRepositoryCount());
    }

    protected ServicioEnVentana getPersistedServicioEnVentana(ServicioEnVentana servicioEnVentana) {
        return servicioEnVentanaRepository.findById(servicioEnVentana.getId()).orElseThrow();
    }

    protected void assertPersistedServicioEnVentanaToMatchAllProperties(ServicioEnVentana expectedServicioEnVentana) {
        assertServicioEnVentanaAllPropertiesEquals(expectedServicioEnVentana, getPersistedServicioEnVentana(expectedServicioEnVentana));
    }

    protected void assertPersistedServicioEnVentanaToMatchUpdatableProperties(ServicioEnVentana expectedServicioEnVentana) {
        assertServicioEnVentanaAllUpdatablePropertiesEquals(
            expectedServicioEnVentana,
            getPersistedServicioEnVentana(expectedServicioEnVentana)
        );
    }
}
