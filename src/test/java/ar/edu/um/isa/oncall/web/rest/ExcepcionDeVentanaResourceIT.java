package ar.edu.um.isa.oncall.web.rest;

import static ar.edu.um.isa.oncall.domain.ExcepcionDeVentanaAsserts.*;
import static ar.edu.um.isa.oncall.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import ar.edu.um.isa.oncall.IntegrationTest;
import ar.edu.um.isa.oncall.domain.ExcepcionDeVentana;
import ar.edu.um.isa.oncall.domain.User;
import ar.edu.um.isa.oncall.domain.VentanaDeMantenimiento;
import ar.edu.um.isa.oncall.domain.enumeration.TipoExcepcionVentana;
import ar.edu.um.isa.oncall.repository.ExcepcionDeVentanaRepository;
import ar.edu.um.isa.oncall.repository.UserRepository;
import ar.edu.um.isa.oncall.service.ExcepcionDeVentanaService;
import ar.edu.um.isa.oncall.service.dto.ExcepcionDeVentanaDTO;
import ar.edu.um.isa.oncall.service.mapper.ExcepcionDeVentanaMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import java.time.Instant;
import java.time.LocalDate;
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
 * Integration tests for the {@link ExcepcionDeVentanaResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class ExcepcionDeVentanaResourceIT {

    private static final LocalDate DEFAULT_FECHA_OCURRENCIA = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_FECHA_OCURRENCIA = LocalDate.parse("2023-12-04");
    private static final LocalDate SMALLER_FECHA_OCURRENCIA = LocalDate.ofEpochDay(-1L);

    private static final TipoExcepcionVentana DEFAULT_TIPO = TipoExcepcionVentana.CANCELACION;
    private static final TipoExcepcionVentana UPDATED_TIPO = TipoExcepcionVentana.REPROGRAMACION;

    private static final Instant DEFAULT_NUEVO_INICIO = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_NUEVO_INICIO = Instant.ofEpochMilli(1701729143509L);

    private static final Instant DEFAULT_NUEVO_FIN = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_NUEVO_FIN = Instant.ofEpochMilli(1701729143509L);

    private static final String DEFAULT_MOTIVO = "AAAAAAAAAA";
    private static final String UPDATED_MOTIVO = "BBBBBBBBBB";

    private static final Instant DEFAULT_CREADA_EN = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREADA_EN = Instant.ofEpochMilli(1701729143509L);

    private static final String ENTITY_API_URL = "/api/excepcion-de-ventanas";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ExcepcionDeVentanaRepository excepcionDeVentanaRepository;

    @Autowired
    private UserRepository userRepository;

    @Mock
    private ExcepcionDeVentanaRepository excepcionDeVentanaRepositoryMock;

    @Autowired
    private ExcepcionDeVentanaMapper excepcionDeVentanaMapper;

    @Mock
    private ExcepcionDeVentanaService excepcionDeVentanaServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restExcepcionDeVentanaMockMvc;

    private ExcepcionDeVentana excepcionDeVentana;

    private ExcepcionDeVentana insertedExcepcionDeVentana;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ExcepcionDeVentana createEntity(EntityManager em) {
        ExcepcionDeVentana excepcionDeVentana = new ExcepcionDeVentana()
            .fechaOcurrencia(DEFAULT_FECHA_OCURRENCIA)
            .tipo(DEFAULT_TIPO)
            .nuevoInicio(DEFAULT_NUEVO_INICIO)
            .nuevoFin(DEFAULT_NUEVO_FIN)
            .motivo(DEFAULT_MOTIVO)
            .creadaEn(DEFAULT_CREADA_EN);
        // Add required entity
        VentanaDeMantenimiento ventanaDeMantenimiento;
        if (TestUtil.findAll(em, VentanaDeMantenimiento.class).isEmpty()) {
            ventanaDeMantenimiento = VentanaDeMantenimientoResourceIT.createEntity(em);
            em.persist(ventanaDeMantenimiento);
            em.flush();
        } else {
            ventanaDeMantenimiento = TestUtil.findAll(em, VentanaDeMantenimiento.class).get(0);
        }
        excepcionDeVentana.setVentana(ventanaDeMantenimiento);
        return excepcionDeVentana;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ExcepcionDeVentana createUpdatedEntity(EntityManager em) {
        ExcepcionDeVentana updatedExcepcionDeVentana = new ExcepcionDeVentana()
            .fechaOcurrencia(UPDATED_FECHA_OCURRENCIA)
            .tipo(UPDATED_TIPO)
            .nuevoInicio(UPDATED_NUEVO_INICIO)
            .nuevoFin(UPDATED_NUEVO_FIN)
            .motivo(UPDATED_MOTIVO)
            .creadaEn(UPDATED_CREADA_EN);
        // Add required entity
        VentanaDeMantenimiento ventanaDeMantenimiento;
        if (TestUtil.findAll(em, VentanaDeMantenimiento.class).isEmpty()) {
            ventanaDeMantenimiento = VentanaDeMantenimientoResourceIT.createUpdatedEntity(em);
            em.persist(ventanaDeMantenimiento);
            em.flush();
        } else {
            ventanaDeMantenimiento = TestUtil.findAll(em, VentanaDeMantenimiento.class).get(0);
        }
        updatedExcepcionDeVentana.setVentana(ventanaDeMantenimiento);
        return updatedExcepcionDeVentana;
    }

    @BeforeEach
    void initTest() {
        excepcionDeVentana = createEntity(em);
    }

    @AfterEach
    void cleanup() {
        if (insertedExcepcionDeVentana != null) {
            excepcionDeVentanaRepository.delete(insertedExcepcionDeVentana);
            insertedExcepcionDeVentana = null;
        }
    }

    @Test
    @Transactional
    void createExcepcionDeVentana() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the ExcepcionDeVentana
        ExcepcionDeVentanaDTO excepcionDeVentanaDTO = excepcionDeVentanaMapper.toDto(excepcionDeVentana);
        var returnedExcepcionDeVentanaDTO = om.readValue(
            restExcepcionDeVentanaMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(excepcionDeVentanaDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            ExcepcionDeVentanaDTO.class
        );

        // Validate the ExcepcionDeVentana in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedExcepcionDeVentana = excepcionDeVentanaMapper.toEntity(returnedExcepcionDeVentanaDTO);
        assertExcepcionDeVentanaUpdatableFieldsEquals(
            returnedExcepcionDeVentana,
            getPersistedExcepcionDeVentana(returnedExcepcionDeVentana)
        );

        insertedExcepcionDeVentana = returnedExcepcionDeVentana;
    }

    @Test
    @Transactional
    void createExcepcionDeVentanaWithExistingId() throws Exception {
        // Create the ExcepcionDeVentana with an existing ID
        excepcionDeVentana.setId(1L);
        ExcepcionDeVentanaDTO excepcionDeVentanaDTO = excepcionDeVentanaMapper.toDto(excepcionDeVentana);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restExcepcionDeVentanaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(excepcionDeVentanaDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ExcepcionDeVentana in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkFechaOcurrenciaIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        excepcionDeVentana.setFechaOcurrencia(null);

        // Create the ExcepcionDeVentana, which fails.
        ExcepcionDeVentanaDTO excepcionDeVentanaDTO = excepcionDeVentanaMapper.toDto(excepcionDeVentana);

        restExcepcionDeVentanaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(excepcionDeVentanaDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTipoIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        excepcionDeVentana.setTipo(null);

        // Create the ExcepcionDeVentana, which fails.
        ExcepcionDeVentanaDTO excepcionDeVentanaDTO = excepcionDeVentanaMapper.toDto(excepcionDeVentana);

        restExcepcionDeVentanaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(excepcionDeVentanaDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkMotivoIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        excepcionDeVentana.setMotivo(null);

        // Create the ExcepcionDeVentana, which fails.
        ExcepcionDeVentanaDTO excepcionDeVentanaDTO = excepcionDeVentanaMapper.toDto(excepcionDeVentana);

        restExcepcionDeVentanaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(excepcionDeVentanaDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCreadaEnIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        excepcionDeVentana.setCreadaEn(null);

        // Create the ExcepcionDeVentana, which fails.
        ExcepcionDeVentanaDTO excepcionDeVentanaDTO = excepcionDeVentanaMapper.toDto(excepcionDeVentana);

        restExcepcionDeVentanaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(excepcionDeVentanaDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllExcepcionDeVentanas() throws Exception {
        // Initialize the database
        insertedExcepcionDeVentana = excepcionDeVentanaRepository.saveAndFlush(excepcionDeVentana);

        // Get all the excepcionDeVentanaList
        restExcepcionDeVentanaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(excepcionDeVentana.getId().intValue())))
            .andExpect(jsonPath("$.[*].fechaOcurrencia").value(hasItem(DEFAULT_FECHA_OCURRENCIA.toString())))
            .andExpect(jsonPath("$.[*].tipo").value(hasItem(DEFAULT_TIPO.toString())))
            .andExpect(jsonPath("$.[*].nuevoInicio").value(hasItem(DEFAULT_NUEVO_INICIO.toString())))
            .andExpect(jsonPath("$.[*].nuevoFin").value(hasItem(DEFAULT_NUEVO_FIN.toString())))
            .andExpect(jsonPath("$.[*].motivo").value(hasItem(DEFAULT_MOTIVO)))
            .andExpect(jsonPath("$.[*].creadaEn").value(hasItem(DEFAULT_CREADA_EN.toString())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllExcepcionDeVentanasWithEagerRelationshipsIsEnabled() throws Exception {
        when(excepcionDeVentanaServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restExcepcionDeVentanaMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(excepcionDeVentanaServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllExcepcionDeVentanasWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(excepcionDeVentanaServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restExcepcionDeVentanaMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(excepcionDeVentanaRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getExcepcionDeVentana() throws Exception {
        // Initialize the database
        insertedExcepcionDeVentana = excepcionDeVentanaRepository.saveAndFlush(excepcionDeVentana);

        // Get the excepcionDeVentana
        restExcepcionDeVentanaMockMvc
            .perform(get(ENTITY_API_URL_ID, excepcionDeVentana.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(excepcionDeVentana.getId().intValue()))
            .andExpect(jsonPath("$.fechaOcurrencia").value(DEFAULT_FECHA_OCURRENCIA.toString()))
            .andExpect(jsonPath("$.tipo").value(DEFAULT_TIPO.toString()))
            .andExpect(jsonPath("$.nuevoInicio").value(DEFAULT_NUEVO_INICIO.toString()))
            .andExpect(jsonPath("$.nuevoFin").value(DEFAULT_NUEVO_FIN.toString()))
            .andExpect(jsonPath("$.motivo").value(DEFAULT_MOTIVO))
            .andExpect(jsonPath("$.creadaEn").value(DEFAULT_CREADA_EN.toString()));
    }

    @Test
    @Transactional
    void getExcepcionDeVentanasByIdFiltering() throws Exception {
        // Initialize the database
        insertedExcepcionDeVentana = excepcionDeVentanaRepository.saveAndFlush(excepcionDeVentana);

        Long id = excepcionDeVentana.getId();

        defaultExcepcionDeVentanaFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultExcepcionDeVentanaFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultExcepcionDeVentanaFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllExcepcionDeVentanasByFechaOcurrenciaIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedExcepcionDeVentana = excepcionDeVentanaRepository.saveAndFlush(excepcionDeVentana);

        // Get all the excepcionDeVentanaList where fechaOcurrencia equals to
        defaultExcepcionDeVentanaFiltering(
            "fechaOcurrencia.equals=" + DEFAULT_FECHA_OCURRENCIA,
            "fechaOcurrencia.equals=" + UPDATED_FECHA_OCURRENCIA
        );
    }

    @Test
    @Transactional
    void getAllExcepcionDeVentanasByFechaOcurrenciaIsInShouldWork() throws Exception {
        // Initialize the database
        insertedExcepcionDeVentana = excepcionDeVentanaRepository.saveAndFlush(excepcionDeVentana);

        // Get all the excepcionDeVentanaList where fechaOcurrencia in
        defaultExcepcionDeVentanaFiltering(
            "fechaOcurrencia.in=" + DEFAULT_FECHA_OCURRENCIA + "," + UPDATED_FECHA_OCURRENCIA,
            "fechaOcurrencia.in=" + UPDATED_FECHA_OCURRENCIA
        );
    }

    @Test
    @Transactional
    void getAllExcepcionDeVentanasByFechaOcurrenciaIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedExcepcionDeVentana = excepcionDeVentanaRepository.saveAndFlush(excepcionDeVentana);

        // Get all the excepcionDeVentanaList where fechaOcurrencia is not null
        defaultExcepcionDeVentanaFiltering("fechaOcurrencia.specified=true", "fechaOcurrencia.specified=false");
    }

    @Test
    @Transactional
    void getAllExcepcionDeVentanasByFechaOcurrenciaIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedExcepcionDeVentana = excepcionDeVentanaRepository.saveAndFlush(excepcionDeVentana);

        // Get all the excepcionDeVentanaList where fechaOcurrencia is greater than or equal to
        defaultExcepcionDeVentanaFiltering(
            "fechaOcurrencia.greaterThanOrEqual=" + DEFAULT_FECHA_OCURRENCIA,
            "fechaOcurrencia.greaterThanOrEqual=" + UPDATED_FECHA_OCURRENCIA
        );
    }

    @Test
    @Transactional
    void getAllExcepcionDeVentanasByFechaOcurrenciaIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedExcepcionDeVentana = excepcionDeVentanaRepository.saveAndFlush(excepcionDeVentana);

        // Get all the excepcionDeVentanaList where fechaOcurrencia is less than or equal to
        defaultExcepcionDeVentanaFiltering(
            "fechaOcurrencia.lessThanOrEqual=" + DEFAULT_FECHA_OCURRENCIA,
            "fechaOcurrencia.lessThanOrEqual=" + SMALLER_FECHA_OCURRENCIA
        );
    }

    @Test
    @Transactional
    void getAllExcepcionDeVentanasByFechaOcurrenciaIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedExcepcionDeVentana = excepcionDeVentanaRepository.saveAndFlush(excepcionDeVentana);

        // Get all the excepcionDeVentanaList where fechaOcurrencia is less than
        defaultExcepcionDeVentanaFiltering(
            "fechaOcurrencia.lessThan=" + UPDATED_FECHA_OCURRENCIA,
            "fechaOcurrencia.lessThan=" + DEFAULT_FECHA_OCURRENCIA
        );
    }

    @Test
    @Transactional
    void getAllExcepcionDeVentanasByFechaOcurrenciaIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedExcepcionDeVentana = excepcionDeVentanaRepository.saveAndFlush(excepcionDeVentana);

        // Get all the excepcionDeVentanaList where fechaOcurrencia is greater than
        defaultExcepcionDeVentanaFiltering(
            "fechaOcurrencia.greaterThan=" + SMALLER_FECHA_OCURRENCIA,
            "fechaOcurrencia.greaterThan=" + DEFAULT_FECHA_OCURRENCIA
        );
    }

    @Test
    @Transactional
    void getAllExcepcionDeVentanasByTipoIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedExcepcionDeVentana = excepcionDeVentanaRepository.saveAndFlush(excepcionDeVentana);

        // Get all the excepcionDeVentanaList where tipo equals to
        defaultExcepcionDeVentanaFiltering("tipo.equals=" + DEFAULT_TIPO, "tipo.equals=" + UPDATED_TIPO);
    }

    @Test
    @Transactional
    void getAllExcepcionDeVentanasByTipoIsInShouldWork() throws Exception {
        // Initialize the database
        insertedExcepcionDeVentana = excepcionDeVentanaRepository.saveAndFlush(excepcionDeVentana);

        // Get all the excepcionDeVentanaList where tipo in
        defaultExcepcionDeVentanaFiltering("tipo.in=" + DEFAULT_TIPO + "," + UPDATED_TIPO, "tipo.in=" + UPDATED_TIPO);
    }

    @Test
    @Transactional
    void getAllExcepcionDeVentanasByTipoIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedExcepcionDeVentana = excepcionDeVentanaRepository.saveAndFlush(excepcionDeVentana);

        // Get all the excepcionDeVentanaList where tipo is not null
        defaultExcepcionDeVentanaFiltering("tipo.specified=true", "tipo.specified=false");
    }

    @Test
    @Transactional
    void getAllExcepcionDeVentanasByNuevoInicioIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedExcepcionDeVentana = excepcionDeVentanaRepository.saveAndFlush(excepcionDeVentana);

        // Get all the excepcionDeVentanaList where nuevoInicio equals to
        defaultExcepcionDeVentanaFiltering("nuevoInicio.equals=" + DEFAULT_NUEVO_INICIO, "nuevoInicio.equals=" + UPDATED_NUEVO_INICIO);
    }

    @Test
    @Transactional
    void getAllExcepcionDeVentanasByNuevoInicioIsInShouldWork() throws Exception {
        // Initialize the database
        insertedExcepcionDeVentana = excepcionDeVentanaRepository.saveAndFlush(excepcionDeVentana);

        // Get all the excepcionDeVentanaList where nuevoInicio in
        defaultExcepcionDeVentanaFiltering(
            "nuevoInicio.in=" + DEFAULT_NUEVO_INICIO + "," + UPDATED_NUEVO_INICIO,
            "nuevoInicio.in=" + UPDATED_NUEVO_INICIO
        );
    }

    @Test
    @Transactional
    void getAllExcepcionDeVentanasByNuevoInicioIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedExcepcionDeVentana = excepcionDeVentanaRepository.saveAndFlush(excepcionDeVentana);

        // Get all the excepcionDeVentanaList where nuevoInicio is not null
        defaultExcepcionDeVentanaFiltering("nuevoInicio.specified=true", "nuevoInicio.specified=false");
    }

    @Test
    @Transactional
    void getAllExcepcionDeVentanasByNuevoFinIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedExcepcionDeVentana = excepcionDeVentanaRepository.saveAndFlush(excepcionDeVentana);

        // Get all the excepcionDeVentanaList where nuevoFin equals to
        defaultExcepcionDeVentanaFiltering("nuevoFin.equals=" + DEFAULT_NUEVO_FIN, "nuevoFin.equals=" + UPDATED_NUEVO_FIN);
    }

    @Test
    @Transactional
    void getAllExcepcionDeVentanasByNuevoFinIsInShouldWork() throws Exception {
        // Initialize the database
        insertedExcepcionDeVentana = excepcionDeVentanaRepository.saveAndFlush(excepcionDeVentana);

        // Get all the excepcionDeVentanaList where nuevoFin in
        defaultExcepcionDeVentanaFiltering(
            "nuevoFin.in=" + DEFAULT_NUEVO_FIN + "," + UPDATED_NUEVO_FIN,
            "nuevoFin.in=" + UPDATED_NUEVO_FIN
        );
    }

    @Test
    @Transactional
    void getAllExcepcionDeVentanasByNuevoFinIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedExcepcionDeVentana = excepcionDeVentanaRepository.saveAndFlush(excepcionDeVentana);

        // Get all the excepcionDeVentanaList where nuevoFin is not null
        defaultExcepcionDeVentanaFiltering("nuevoFin.specified=true", "nuevoFin.specified=false");
    }

    @Test
    @Transactional
    void getAllExcepcionDeVentanasByMotivoIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedExcepcionDeVentana = excepcionDeVentanaRepository.saveAndFlush(excepcionDeVentana);

        // Get all the excepcionDeVentanaList where motivo equals to
        defaultExcepcionDeVentanaFiltering("motivo.equals=" + DEFAULT_MOTIVO, "motivo.equals=" + UPDATED_MOTIVO);
    }

    @Test
    @Transactional
    void getAllExcepcionDeVentanasByMotivoIsInShouldWork() throws Exception {
        // Initialize the database
        insertedExcepcionDeVentana = excepcionDeVentanaRepository.saveAndFlush(excepcionDeVentana);

        // Get all the excepcionDeVentanaList where motivo in
        defaultExcepcionDeVentanaFiltering("motivo.in=" + DEFAULT_MOTIVO + "," + UPDATED_MOTIVO, "motivo.in=" + UPDATED_MOTIVO);
    }

    @Test
    @Transactional
    void getAllExcepcionDeVentanasByMotivoIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedExcepcionDeVentana = excepcionDeVentanaRepository.saveAndFlush(excepcionDeVentana);

        // Get all the excepcionDeVentanaList where motivo is not null
        defaultExcepcionDeVentanaFiltering("motivo.specified=true", "motivo.specified=false");
    }

    @Test
    @Transactional
    void getAllExcepcionDeVentanasByMotivoContainsSomething() throws Exception {
        // Initialize the database
        insertedExcepcionDeVentana = excepcionDeVentanaRepository.saveAndFlush(excepcionDeVentana);

        // Get all the excepcionDeVentanaList where motivo contains
        defaultExcepcionDeVentanaFiltering("motivo.contains=" + DEFAULT_MOTIVO, "motivo.contains=" + UPDATED_MOTIVO);
    }

    @Test
    @Transactional
    void getAllExcepcionDeVentanasByMotivoNotContainsSomething() throws Exception {
        // Initialize the database
        insertedExcepcionDeVentana = excepcionDeVentanaRepository.saveAndFlush(excepcionDeVentana);

        // Get all the excepcionDeVentanaList where motivo does not contain
        defaultExcepcionDeVentanaFiltering("motivo.doesNotContain=" + UPDATED_MOTIVO, "motivo.doesNotContain=" + DEFAULT_MOTIVO);
    }

    @Test
    @Transactional
    void getAllExcepcionDeVentanasByCreadaEnIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedExcepcionDeVentana = excepcionDeVentanaRepository.saveAndFlush(excepcionDeVentana);

        // Get all the excepcionDeVentanaList where creadaEn equals to
        defaultExcepcionDeVentanaFiltering("creadaEn.equals=" + DEFAULT_CREADA_EN, "creadaEn.equals=" + UPDATED_CREADA_EN);
    }

    @Test
    @Transactional
    void getAllExcepcionDeVentanasByCreadaEnIsInShouldWork() throws Exception {
        // Initialize the database
        insertedExcepcionDeVentana = excepcionDeVentanaRepository.saveAndFlush(excepcionDeVentana);

        // Get all the excepcionDeVentanaList where creadaEn in
        defaultExcepcionDeVentanaFiltering(
            "creadaEn.in=" + DEFAULT_CREADA_EN + "," + UPDATED_CREADA_EN,
            "creadaEn.in=" + UPDATED_CREADA_EN
        );
    }

    @Test
    @Transactional
    void getAllExcepcionDeVentanasByCreadaEnIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedExcepcionDeVentana = excepcionDeVentanaRepository.saveAndFlush(excepcionDeVentana);

        // Get all the excepcionDeVentanaList where creadaEn is not null
        defaultExcepcionDeVentanaFiltering("creadaEn.specified=true", "creadaEn.specified=false");
    }

    @Test
    @Transactional
    void getAllExcepcionDeVentanasByVentanaIsEqualToSomething() throws Exception {
        VentanaDeMantenimiento ventana;
        if (TestUtil.findAll(em, VentanaDeMantenimiento.class).isEmpty()) {
            excepcionDeVentanaRepository.saveAndFlush(excepcionDeVentana);
            ventana = VentanaDeMantenimientoResourceIT.createEntity(em);
        } else {
            ventana = TestUtil.findAll(em, VentanaDeMantenimiento.class).get(0);
        }
        em.persist(ventana);
        em.flush();
        excepcionDeVentana.setVentana(ventana);
        excepcionDeVentanaRepository.saveAndFlush(excepcionDeVentana);
        Long ventanaId = ventana.getId();
        // Get all the excepcionDeVentanaList where ventana equals to ventanaId
        defaultExcepcionDeVentanaShouldBeFound("ventanaId.equals=" + ventanaId);

        // Get all the excepcionDeVentanaList where ventana equals to (ventanaId + 1)
        defaultExcepcionDeVentanaShouldNotBeFound("ventanaId.equals=" + (ventanaId + 1));
    }

    @Test
    @Transactional
    void getAllExcepcionDeVentanasByAutorizadaPorIsEqualToSomething() throws Exception {
        User autorizadaPor;
        if (TestUtil.findAll(em, User.class).isEmpty()) {
            excepcionDeVentanaRepository.saveAndFlush(excepcionDeVentana);
            autorizadaPor = UserResourceIT.createEntity();
        } else {
            autorizadaPor = TestUtil.findAll(em, User.class).get(0);
        }
        em.persist(autorizadaPor);
        em.flush();
        excepcionDeVentana.setAutorizadaPor(autorizadaPor);
        excepcionDeVentanaRepository.saveAndFlush(excepcionDeVentana);
        Long autorizadaPorId = autorizadaPor.getId();
        // Get all the excepcionDeVentanaList where autorizadaPor equals to autorizadaPorId
        defaultExcepcionDeVentanaShouldBeFound("autorizadaPorId.equals=" + autorizadaPorId);

        // Get all the excepcionDeVentanaList where autorizadaPor equals to (autorizadaPorId + 1)
        defaultExcepcionDeVentanaShouldNotBeFound("autorizadaPorId.equals=" + (autorizadaPorId + 1));
    }

    private void defaultExcepcionDeVentanaFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultExcepcionDeVentanaShouldBeFound(shouldBeFound);
        defaultExcepcionDeVentanaShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultExcepcionDeVentanaShouldBeFound(String filter) throws Exception {
        restExcepcionDeVentanaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(excepcionDeVentana.getId().intValue())))
            .andExpect(jsonPath("$.[*].fechaOcurrencia").value(hasItem(DEFAULT_FECHA_OCURRENCIA.toString())))
            .andExpect(jsonPath("$.[*].tipo").value(hasItem(DEFAULT_TIPO.toString())))
            .andExpect(jsonPath("$.[*].nuevoInicio").value(hasItem(DEFAULT_NUEVO_INICIO.toString())))
            .andExpect(jsonPath("$.[*].nuevoFin").value(hasItem(DEFAULT_NUEVO_FIN.toString())))
            .andExpect(jsonPath("$.[*].motivo").value(hasItem(DEFAULT_MOTIVO)))
            .andExpect(jsonPath("$.[*].creadaEn").value(hasItem(DEFAULT_CREADA_EN.toString())));

        // Check, that the count call also returns 1
        restExcepcionDeVentanaMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultExcepcionDeVentanaShouldNotBeFound(String filter) throws Exception {
        restExcepcionDeVentanaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restExcepcionDeVentanaMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingExcepcionDeVentana() throws Exception {
        // Get the excepcionDeVentana
        restExcepcionDeVentanaMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingExcepcionDeVentana() throws Exception {
        // Initialize the database
        insertedExcepcionDeVentana = excepcionDeVentanaRepository.saveAndFlush(excepcionDeVentana);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the excepcionDeVentana
        ExcepcionDeVentana updatedExcepcionDeVentana = excepcionDeVentanaRepository.findById(excepcionDeVentana.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedExcepcionDeVentana are not directly saved in db
        em.detach(updatedExcepcionDeVentana);
        updatedExcepcionDeVentana
            .fechaOcurrencia(UPDATED_FECHA_OCURRENCIA)
            .tipo(UPDATED_TIPO)
            .nuevoInicio(UPDATED_NUEVO_INICIO)
            .nuevoFin(UPDATED_NUEVO_FIN)
            .motivo(UPDATED_MOTIVO)
            .creadaEn(UPDATED_CREADA_EN);
        ExcepcionDeVentanaDTO excepcionDeVentanaDTO = excepcionDeVentanaMapper.toDto(updatedExcepcionDeVentana);

        restExcepcionDeVentanaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, excepcionDeVentanaDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(excepcionDeVentanaDTO))
            )
            .andExpect(status().isOk());

        // Validate the ExcepcionDeVentana in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedExcepcionDeVentanaToMatchAllProperties(updatedExcepcionDeVentana);
    }

    @Test
    @Transactional
    void putNonExistingExcepcionDeVentana() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        excepcionDeVentana.setId(longCount.incrementAndGet());

        // Create the ExcepcionDeVentana
        ExcepcionDeVentanaDTO excepcionDeVentanaDTO = excepcionDeVentanaMapper.toDto(excepcionDeVentana);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restExcepcionDeVentanaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, excepcionDeVentanaDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(excepcionDeVentanaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ExcepcionDeVentana in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchExcepcionDeVentana() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        excepcionDeVentana.setId(longCount.incrementAndGet());

        // Create the ExcepcionDeVentana
        ExcepcionDeVentanaDTO excepcionDeVentanaDTO = excepcionDeVentanaMapper.toDto(excepcionDeVentana);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restExcepcionDeVentanaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(excepcionDeVentanaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ExcepcionDeVentana in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamExcepcionDeVentana() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        excepcionDeVentana.setId(longCount.incrementAndGet());

        // Create the ExcepcionDeVentana
        ExcepcionDeVentanaDTO excepcionDeVentanaDTO = excepcionDeVentanaMapper.toDto(excepcionDeVentana);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restExcepcionDeVentanaMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(excepcionDeVentanaDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ExcepcionDeVentana in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateExcepcionDeVentanaWithPatch() throws Exception {
        // Initialize the database
        insertedExcepcionDeVentana = excepcionDeVentanaRepository.saveAndFlush(excepcionDeVentana);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the excepcionDeVentana using partial update
        ExcepcionDeVentana partialUpdatedExcepcionDeVentana = new ExcepcionDeVentana();
        partialUpdatedExcepcionDeVentana.setId(excepcionDeVentana.getId());

        partialUpdatedExcepcionDeVentana.motivo(UPDATED_MOTIVO);

        restExcepcionDeVentanaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedExcepcionDeVentana.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedExcepcionDeVentana))
            )
            .andExpect(status().isOk());

        // Validate the ExcepcionDeVentana in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertExcepcionDeVentanaUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedExcepcionDeVentana, excepcionDeVentana),
            getPersistedExcepcionDeVentana(excepcionDeVentana)
        );
    }

    @Test
    @Transactional
    void fullUpdateExcepcionDeVentanaWithPatch() throws Exception {
        // Initialize the database
        insertedExcepcionDeVentana = excepcionDeVentanaRepository.saveAndFlush(excepcionDeVentana);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the excepcionDeVentana using partial update
        ExcepcionDeVentana partialUpdatedExcepcionDeVentana = new ExcepcionDeVentana();
        partialUpdatedExcepcionDeVentana.setId(excepcionDeVentana.getId());

        partialUpdatedExcepcionDeVentana
            .fechaOcurrencia(UPDATED_FECHA_OCURRENCIA)
            .tipo(UPDATED_TIPO)
            .nuevoInicio(UPDATED_NUEVO_INICIO)
            .nuevoFin(UPDATED_NUEVO_FIN)
            .motivo(UPDATED_MOTIVO)
            .creadaEn(UPDATED_CREADA_EN);

        restExcepcionDeVentanaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedExcepcionDeVentana.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedExcepcionDeVentana))
            )
            .andExpect(status().isOk());

        // Validate the ExcepcionDeVentana in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertExcepcionDeVentanaUpdatableFieldsEquals(
            partialUpdatedExcepcionDeVentana,
            getPersistedExcepcionDeVentana(partialUpdatedExcepcionDeVentana)
        );
    }

    @Test
    @Transactional
    void patchNonExistingExcepcionDeVentana() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        excepcionDeVentana.setId(longCount.incrementAndGet());

        // Create the ExcepcionDeVentana
        ExcepcionDeVentanaDTO excepcionDeVentanaDTO = excepcionDeVentanaMapper.toDto(excepcionDeVentana);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restExcepcionDeVentanaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, excepcionDeVentanaDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(excepcionDeVentanaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ExcepcionDeVentana in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchExcepcionDeVentana() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        excepcionDeVentana.setId(longCount.incrementAndGet());

        // Create the ExcepcionDeVentana
        ExcepcionDeVentanaDTO excepcionDeVentanaDTO = excepcionDeVentanaMapper.toDto(excepcionDeVentana);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restExcepcionDeVentanaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(excepcionDeVentanaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ExcepcionDeVentana in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamExcepcionDeVentana() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        excepcionDeVentana.setId(longCount.incrementAndGet());

        // Create the ExcepcionDeVentana
        ExcepcionDeVentanaDTO excepcionDeVentanaDTO = excepcionDeVentanaMapper.toDto(excepcionDeVentana);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restExcepcionDeVentanaMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(excepcionDeVentanaDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ExcepcionDeVentana in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteExcepcionDeVentana() throws Exception {
        // Initialize the database
        insertedExcepcionDeVentana = excepcionDeVentanaRepository.saveAndFlush(excepcionDeVentana);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the excepcionDeVentana
        restExcepcionDeVentanaMockMvc
            .perform(delete(ENTITY_API_URL_ID, excepcionDeVentana.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return excepcionDeVentanaRepository.count();
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

    protected ExcepcionDeVentana getPersistedExcepcionDeVentana(ExcepcionDeVentana excepcionDeVentana) {
        return excepcionDeVentanaRepository.findById(excepcionDeVentana.getId()).orElseThrow();
    }

    protected void assertPersistedExcepcionDeVentanaToMatchAllProperties(ExcepcionDeVentana expectedExcepcionDeVentana) {
        assertExcepcionDeVentanaAllPropertiesEquals(expectedExcepcionDeVentana, getPersistedExcepcionDeVentana(expectedExcepcionDeVentana));
    }

    protected void assertPersistedExcepcionDeVentanaToMatchUpdatableProperties(ExcepcionDeVentana expectedExcepcionDeVentana) {
        assertExcepcionDeVentanaAllUpdatablePropertiesEquals(
            expectedExcepcionDeVentana,
            getPersistedExcepcionDeVentana(expectedExcepcionDeVentana)
        );
    }
}
