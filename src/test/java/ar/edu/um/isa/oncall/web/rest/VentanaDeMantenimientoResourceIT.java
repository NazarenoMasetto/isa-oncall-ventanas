package ar.edu.um.isa.oncall.web.rest;

import static ar.edu.um.isa.oncall.domain.VentanaDeMantenimientoAsserts.*;
import static ar.edu.um.isa.oncall.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import ar.edu.um.isa.oncall.IntegrationTest;
import ar.edu.um.isa.oncall.domain.Equipo;
import ar.edu.um.isa.oncall.domain.User;
import ar.edu.um.isa.oncall.domain.VentanaDeMantenimiento;
import ar.edu.um.isa.oncall.domain.enumeration.TipoRecurrencia;
import ar.edu.um.isa.oncall.repository.UserRepository;
import ar.edu.um.isa.oncall.repository.VentanaDeMantenimientoRepository;
import ar.edu.um.isa.oncall.service.VentanaDeMantenimientoService;
import ar.edu.um.isa.oncall.service.dto.VentanaDeMantenimientoDTO;
import ar.edu.um.isa.oncall.service.mapper.VentanaDeMantenimientoMapper;
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
 * Integration tests for the {@link VentanaDeMantenimientoResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class VentanaDeMantenimientoResourceIT {

    private static final String DEFAULT_NOMBRE = "AAAAAAAAAA";
    private static final String UPDATED_NOMBRE = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPCION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPCION = "BBBBBBBBBB";

    private static final Instant DEFAULT_INICIO = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_INICIO = Instant.ofEpochMilli(1701729143509L);

    private static final Instant DEFAULT_FIN = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_FIN = Instant.ofEpochMilli(1701729143509L);

    private static final String DEFAULT_ZONA_HORARIA = "AAAAAAAAAA";
    private static final String UPDATED_ZONA_HORARIA = "BBBBBBBBBB";

    private static final TipoRecurrencia DEFAULT_RECURRENCIA = TipoRecurrencia.NINGUNA;
    private static final TipoRecurrencia UPDATED_RECURRENCIA = TipoRecurrencia.DIARIA;

    private static final String DEFAULT_DIAS_SEMANA = "AAAAAAAAAA";
    private static final String UPDATED_DIAS_SEMANA = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_RECURRENCIA_HASTA = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_RECURRENCIA_HASTA = LocalDate.parse("2023-12-04");
    private static final LocalDate SMALLER_RECURRENCIA_HASTA = LocalDate.ofEpochDay(-1L);

    private static final Boolean DEFAULT_ACTIVA = false;
    private static final Boolean UPDATED_ACTIVA = true;

    private static final String ENTITY_API_URL = "/api/ventana-de-mantenimientos";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);

    @Autowired
    private ObjectMapper om;

    @Autowired
    private VentanaDeMantenimientoRepository ventanaDeMantenimientoRepository;

    @Autowired
    private UserRepository userRepository;

    @Mock
    private VentanaDeMantenimientoRepository ventanaDeMantenimientoRepositoryMock;

    @Autowired
    private VentanaDeMantenimientoMapper ventanaDeMantenimientoMapper;

    @Mock
    private VentanaDeMantenimientoService ventanaDeMantenimientoServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restVentanaDeMantenimientoMockMvc;

    private VentanaDeMantenimiento ventanaDeMantenimiento;

    private VentanaDeMantenimiento insertedVentanaDeMantenimiento;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static VentanaDeMantenimiento createEntity(EntityManager em) {
        VentanaDeMantenimiento ventanaDeMantenimiento = new VentanaDeMantenimiento()
            .nombre(DEFAULT_NOMBRE)
            .descripcion(DEFAULT_DESCRIPCION)
            .inicio(DEFAULT_INICIO)
            .fin(DEFAULT_FIN)
            .zonaHoraria(DEFAULT_ZONA_HORARIA)
            .recurrencia(DEFAULT_RECURRENCIA)
            .diasSemana(DEFAULT_DIAS_SEMANA)
            .recurrenciaHasta(DEFAULT_RECURRENCIA_HASTA)
            .activa(DEFAULT_ACTIVA);
        // Add required entity
        Equipo equipo;
        if (TestUtil.findAll(em, Equipo.class).isEmpty()) {
            equipo = EquipoResourceIT.createEntity();
            em.persist(equipo);
            em.flush();
        } else {
            equipo = TestUtil.findAll(em, Equipo.class).get(0);
        }
        ventanaDeMantenimiento.setEquipo(equipo);
        // Add required entity
        User user = UserResourceIT.createEntity();
        em.persist(user);
        em.flush();
        ventanaDeMantenimiento.setCreadaPor(user);
        return ventanaDeMantenimiento;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static VentanaDeMantenimiento createUpdatedEntity(EntityManager em) {
        VentanaDeMantenimiento updatedVentanaDeMantenimiento = new VentanaDeMantenimiento()
            .nombre(UPDATED_NOMBRE)
            .descripcion(UPDATED_DESCRIPCION)
            .inicio(UPDATED_INICIO)
            .fin(UPDATED_FIN)
            .zonaHoraria(UPDATED_ZONA_HORARIA)
            .recurrencia(UPDATED_RECURRENCIA)
            .diasSemana(UPDATED_DIAS_SEMANA)
            .recurrenciaHasta(UPDATED_RECURRENCIA_HASTA)
            .activa(UPDATED_ACTIVA);
        // Add required entity
        Equipo equipo;
        if (TestUtil.findAll(em, Equipo.class).isEmpty()) {
            equipo = EquipoResourceIT.createUpdatedEntity();
            em.persist(equipo);
            em.flush();
        } else {
            equipo = TestUtil.findAll(em, Equipo.class).get(0);
        }
        updatedVentanaDeMantenimiento.setEquipo(equipo);
        // Add required entity
        User user = UserResourceIT.createEntity();
        em.persist(user);
        em.flush();
        updatedVentanaDeMantenimiento.setCreadaPor(user);
        return updatedVentanaDeMantenimiento;
    }

    @BeforeEach
    void initTest() {
        ventanaDeMantenimiento = createEntity(em);
    }

    @AfterEach
    void cleanup() {
        if (insertedVentanaDeMantenimiento != null) {
            ventanaDeMantenimientoRepository.delete(insertedVentanaDeMantenimiento);
            insertedVentanaDeMantenimiento = null;
        }
    }

    @Test
    @Transactional
    void createVentanaDeMantenimiento() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the VentanaDeMantenimiento
        VentanaDeMantenimientoDTO ventanaDeMantenimientoDTO = ventanaDeMantenimientoMapper.toDto(ventanaDeMantenimiento);
        var returnedVentanaDeMantenimientoDTO = om.readValue(
            restVentanaDeMantenimientoMockMvc
                .perform(
                    post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(ventanaDeMantenimientoDTO))
                )
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            VentanaDeMantenimientoDTO.class
        );

        // Validate the VentanaDeMantenimiento in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedVentanaDeMantenimiento = ventanaDeMantenimientoMapper.toEntity(returnedVentanaDeMantenimientoDTO);
        assertVentanaDeMantenimientoUpdatableFieldsEquals(
            returnedVentanaDeMantenimiento,
            getPersistedVentanaDeMantenimiento(returnedVentanaDeMantenimiento)
        );

        insertedVentanaDeMantenimiento = returnedVentanaDeMantenimiento;
    }

    @Test
    @Transactional
    void createVentanaDeMantenimientoWithExistingId() throws Exception {
        // Create the VentanaDeMantenimiento with an existing ID
        ventanaDeMantenimiento.setId(1L);
        VentanaDeMantenimientoDTO ventanaDeMantenimientoDTO = ventanaDeMantenimientoMapper.toDto(ventanaDeMantenimiento);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restVentanaDeMantenimientoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(ventanaDeMantenimientoDTO)))
            .andExpect(status().isBadRequest());

        // Validate the VentanaDeMantenimiento in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNombreIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        ventanaDeMantenimiento.setNombre(null);

        // Create the VentanaDeMantenimiento, which fails.
        VentanaDeMantenimientoDTO ventanaDeMantenimientoDTO = ventanaDeMantenimientoMapper.toDto(ventanaDeMantenimiento);

        restVentanaDeMantenimientoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(ventanaDeMantenimientoDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkInicioIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        ventanaDeMantenimiento.setInicio(null);

        // Create the VentanaDeMantenimiento, which fails.
        VentanaDeMantenimientoDTO ventanaDeMantenimientoDTO = ventanaDeMantenimientoMapper.toDto(ventanaDeMantenimiento);

        restVentanaDeMantenimientoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(ventanaDeMantenimientoDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkFinIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        ventanaDeMantenimiento.setFin(null);

        // Create the VentanaDeMantenimiento, which fails.
        VentanaDeMantenimientoDTO ventanaDeMantenimientoDTO = ventanaDeMantenimientoMapper.toDto(ventanaDeMantenimiento);

        restVentanaDeMantenimientoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(ventanaDeMantenimientoDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkZonaHorariaIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        ventanaDeMantenimiento.setZonaHoraria(null);

        // Create the VentanaDeMantenimiento, which fails.
        VentanaDeMantenimientoDTO ventanaDeMantenimientoDTO = ventanaDeMantenimientoMapper.toDto(ventanaDeMantenimiento);

        restVentanaDeMantenimientoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(ventanaDeMantenimientoDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkRecurrenciaIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        ventanaDeMantenimiento.setRecurrencia(null);

        // Create the VentanaDeMantenimiento, which fails.
        VentanaDeMantenimientoDTO ventanaDeMantenimientoDTO = ventanaDeMantenimientoMapper.toDto(ventanaDeMantenimiento);

        restVentanaDeMantenimientoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(ventanaDeMantenimientoDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkActivaIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        ventanaDeMantenimiento.setActiva(null);

        // Create the VentanaDeMantenimiento, which fails.
        VentanaDeMantenimientoDTO ventanaDeMantenimientoDTO = ventanaDeMantenimientoMapper.toDto(ventanaDeMantenimiento);

        restVentanaDeMantenimientoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(ventanaDeMantenimientoDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllVentanaDeMantenimientos() throws Exception {
        // Initialize the database
        insertedVentanaDeMantenimiento = ventanaDeMantenimientoRepository.saveAndFlush(ventanaDeMantenimiento);

        // Get all the ventanaDeMantenimientoList
        restVentanaDeMantenimientoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(ventanaDeMantenimiento.getId().intValue())))
            .andExpect(jsonPath("$.[*].nombre").value(hasItem(DEFAULT_NOMBRE)))
            .andExpect(jsonPath("$.[*].descripcion").value(hasItem(DEFAULT_DESCRIPCION)))
            .andExpect(jsonPath("$.[*].inicio").value(hasItem(DEFAULT_INICIO.toString())))
            .andExpect(jsonPath("$.[*].fin").value(hasItem(DEFAULT_FIN.toString())))
            .andExpect(jsonPath("$.[*].zonaHoraria").value(hasItem(DEFAULT_ZONA_HORARIA)))
            .andExpect(jsonPath("$.[*].recurrencia").value(hasItem(DEFAULT_RECURRENCIA.toString())))
            .andExpect(jsonPath("$.[*].diasSemana").value(hasItem(DEFAULT_DIAS_SEMANA)))
            .andExpect(jsonPath("$.[*].recurrenciaHasta").value(hasItem(DEFAULT_RECURRENCIA_HASTA.toString())))
            .andExpect(jsonPath("$.[*].activa").value(hasItem(DEFAULT_ACTIVA)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllVentanaDeMantenimientosWithEagerRelationshipsIsEnabled() throws Exception {
        when(ventanaDeMantenimientoServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restVentanaDeMantenimientoMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(ventanaDeMantenimientoServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllVentanaDeMantenimientosWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(ventanaDeMantenimientoServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restVentanaDeMantenimientoMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(ventanaDeMantenimientoRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getVentanaDeMantenimiento() throws Exception {
        // Initialize the database
        insertedVentanaDeMantenimiento = ventanaDeMantenimientoRepository.saveAndFlush(ventanaDeMantenimiento);

        // Get the ventanaDeMantenimiento
        restVentanaDeMantenimientoMockMvc
            .perform(get(ENTITY_API_URL_ID, ventanaDeMantenimiento.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(ventanaDeMantenimiento.getId().intValue()))
            .andExpect(jsonPath("$.nombre").value(DEFAULT_NOMBRE))
            .andExpect(jsonPath("$.descripcion").value(DEFAULT_DESCRIPCION))
            .andExpect(jsonPath("$.inicio").value(DEFAULT_INICIO.toString()))
            .andExpect(jsonPath("$.fin").value(DEFAULT_FIN.toString()))
            .andExpect(jsonPath("$.zonaHoraria").value(DEFAULT_ZONA_HORARIA))
            .andExpect(jsonPath("$.recurrencia").value(DEFAULT_RECURRENCIA.toString()))
            .andExpect(jsonPath("$.diasSemana").value(DEFAULT_DIAS_SEMANA))
            .andExpect(jsonPath("$.recurrenciaHasta").value(DEFAULT_RECURRENCIA_HASTA.toString()))
            .andExpect(jsonPath("$.activa").value(DEFAULT_ACTIVA));
    }

    @Test
    @Transactional
    void getVentanaDeMantenimientosByIdFiltering() throws Exception {
        // Initialize the database
        insertedVentanaDeMantenimiento = ventanaDeMantenimientoRepository.saveAndFlush(ventanaDeMantenimiento);

        Long id = ventanaDeMantenimiento.getId();

        defaultVentanaDeMantenimientoFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultVentanaDeMantenimientoFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultVentanaDeMantenimientoFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllVentanaDeMantenimientosByNombreIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedVentanaDeMantenimiento = ventanaDeMantenimientoRepository.saveAndFlush(ventanaDeMantenimiento);

        // Get all the ventanaDeMantenimientoList where nombre equals to
        defaultVentanaDeMantenimientoFiltering("nombre.equals=" + DEFAULT_NOMBRE, "nombre.equals=" + UPDATED_NOMBRE);
    }

    @Test
    @Transactional
    void getAllVentanaDeMantenimientosByNombreIsInShouldWork() throws Exception {
        // Initialize the database
        insertedVentanaDeMantenimiento = ventanaDeMantenimientoRepository.saveAndFlush(ventanaDeMantenimiento);

        // Get all the ventanaDeMantenimientoList where nombre in
        defaultVentanaDeMantenimientoFiltering("nombre.in=" + DEFAULT_NOMBRE + "," + UPDATED_NOMBRE, "nombre.in=" + UPDATED_NOMBRE);
    }

    @Test
    @Transactional
    void getAllVentanaDeMantenimientosByNombreIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedVentanaDeMantenimiento = ventanaDeMantenimientoRepository.saveAndFlush(ventanaDeMantenimiento);

        // Get all the ventanaDeMantenimientoList where nombre is not null
        defaultVentanaDeMantenimientoFiltering("nombre.specified=true", "nombre.specified=false");
    }

    @Test
    @Transactional
    void getAllVentanaDeMantenimientosByNombreContainsSomething() throws Exception {
        // Initialize the database
        insertedVentanaDeMantenimiento = ventanaDeMantenimientoRepository.saveAndFlush(ventanaDeMantenimiento);

        // Get all the ventanaDeMantenimientoList where nombre contains
        defaultVentanaDeMantenimientoFiltering("nombre.contains=" + DEFAULT_NOMBRE, "nombre.contains=" + UPDATED_NOMBRE);
    }

    @Test
    @Transactional
    void getAllVentanaDeMantenimientosByNombreNotContainsSomething() throws Exception {
        // Initialize the database
        insertedVentanaDeMantenimiento = ventanaDeMantenimientoRepository.saveAndFlush(ventanaDeMantenimiento);

        // Get all the ventanaDeMantenimientoList where nombre does not contain
        defaultVentanaDeMantenimientoFiltering("nombre.doesNotContain=" + UPDATED_NOMBRE, "nombre.doesNotContain=" + DEFAULT_NOMBRE);
    }

    @Test
    @Transactional
    void getAllVentanaDeMantenimientosByDescripcionIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedVentanaDeMantenimiento = ventanaDeMantenimientoRepository.saveAndFlush(ventanaDeMantenimiento);

        // Get all the ventanaDeMantenimientoList where descripcion equals to
        defaultVentanaDeMantenimientoFiltering("descripcion.equals=" + DEFAULT_DESCRIPCION, "descripcion.equals=" + UPDATED_DESCRIPCION);
    }

    @Test
    @Transactional
    void getAllVentanaDeMantenimientosByDescripcionIsInShouldWork() throws Exception {
        // Initialize the database
        insertedVentanaDeMantenimiento = ventanaDeMantenimientoRepository.saveAndFlush(ventanaDeMantenimiento);

        // Get all the ventanaDeMantenimientoList where descripcion in
        defaultVentanaDeMantenimientoFiltering(
            "descripcion.in=" + DEFAULT_DESCRIPCION + "," + UPDATED_DESCRIPCION,
            "descripcion.in=" + UPDATED_DESCRIPCION
        );
    }

    @Test
    @Transactional
    void getAllVentanaDeMantenimientosByDescripcionIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedVentanaDeMantenimiento = ventanaDeMantenimientoRepository.saveAndFlush(ventanaDeMantenimiento);

        // Get all the ventanaDeMantenimientoList where descripcion is not null
        defaultVentanaDeMantenimientoFiltering("descripcion.specified=true", "descripcion.specified=false");
    }

    @Test
    @Transactional
    void getAllVentanaDeMantenimientosByDescripcionContainsSomething() throws Exception {
        // Initialize the database
        insertedVentanaDeMantenimiento = ventanaDeMantenimientoRepository.saveAndFlush(ventanaDeMantenimiento);

        // Get all the ventanaDeMantenimientoList where descripcion contains
        defaultVentanaDeMantenimientoFiltering(
            "descripcion.contains=" + DEFAULT_DESCRIPCION,
            "descripcion.contains=" + UPDATED_DESCRIPCION
        );
    }

    @Test
    @Transactional
    void getAllVentanaDeMantenimientosByDescripcionNotContainsSomething() throws Exception {
        // Initialize the database
        insertedVentanaDeMantenimiento = ventanaDeMantenimientoRepository.saveAndFlush(ventanaDeMantenimiento);

        // Get all the ventanaDeMantenimientoList where descripcion does not contain
        defaultVentanaDeMantenimientoFiltering(
            "descripcion.doesNotContain=" + UPDATED_DESCRIPCION,
            "descripcion.doesNotContain=" + DEFAULT_DESCRIPCION
        );
    }

    @Test
    @Transactional
    void getAllVentanaDeMantenimientosByInicioIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedVentanaDeMantenimiento = ventanaDeMantenimientoRepository.saveAndFlush(ventanaDeMantenimiento);

        // Get all the ventanaDeMantenimientoList where inicio equals to
        defaultVentanaDeMantenimientoFiltering("inicio.equals=" + DEFAULT_INICIO, "inicio.equals=" + UPDATED_INICIO);
    }

    @Test
    @Transactional
    void getAllVentanaDeMantenimientosByInicioIsInShouldWork() throws Exception {
        // Initialize the database
        insertedVentanaDeMantenimiento = ventanaDeMantenimientoRepository.saveAndFlush(ventanaDeMantenimiento);

        // Get all the ventanaDeMantenimientoList where inicio in
        defaultVentanaDeMantenimientoFiltering("inicio.in=" + DEFAULT_INICIO + "," + UPDATED_INICIO, "inicio.in=" + UPDATED_INICIO);
    }

    @Test
    @Transactional
    void getAllVentanaDeMantenimientosByInicioIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedVentanaDeMantenimiento = ventanaDeMantenimientoRepository.saveAndFlush(ventanaDeMantenimiento);

        // Get all the ventanaDeMantenimientoList where inicio is not null
        defaultVentanaDeMantenimientoFiltering("inicio.specified=true", "inicio.specified=false");
    }

    @Test
    @Transactional
    void getAllVentanaDeMantenimientosByFinIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedVentanaDeMantenimiento = ventanaDeMantenimientoRepository.saveAndFlush(ventanaDeMantenimiento);

        // Get all the ventanaDeMantenimientoList where fin equals to
        defaultVentanaDeMantenimientoFiltering("fin.equals=" + DEFAULT_FIN, "fin.equals=" + UPDATED_FIN);
    }

    @Test
    @Transactional
    void getAllVentanaDeMantenimientosByFinIsInShouldWork() throws Exception {
        // Initialize the database
        insertedVentanaDeMantenimiento = ventanaDeMantenimientoRepository.saveAndFlush(ventanaDeMantenimiento);

        // Get all the ventanaDeMantenimientoList where fin in
        defaultVentanaDeMantenimientoFiltering("fin.in=" + DEFAULT_FIN + "," + UPDATED_FIN, "fin.in=" + UPDATED_FIN);
    }

    @Test
    @Transactional
    void getAllVentanaDeMantenimientosByFinIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedVentanaDeMantenimiento = ventanaDeMantenimientoRepository.saveAndFlush(ventanaDeMantenimiento);

        // Get all the ventanaDeMantenimientoList where fin is not null
        defaultVentanaDeMantenimientoFiltering("fin.specified=true", "fin.specified=false");
    }

    @Test
    @Transactional
    void getAllVentanaDeMantenimientosByZonaHorariaIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedVentanaDeMantenimiento = ventanaDeMantenimientoRepository.saveAndFlush(ventanaDeMantenimiento);

        // Get all the ventanaDeMantenimientoList where zonaHoraria equals to
        defaultVentanaDeMantenimientoFiltering("zonaHoraria.equals=" + DEFAULT_ZONA_HORARIA, "zonaHoraria.equals=" + UPDATED_ZONA_HORARIA);
    }

    @Test
    @Transactional
    void getAllVentanaDeMantenimientosByZonaHorariaIsInShouldWork() throws Exception {
        // Initialize the database
        insertedVentanaDeMantenimiento = ventanaDeMantenimientoRepository.saveAndFlush(ventanaDeMantenimiento);

        // Get all the ventanaDeMantenimientoList where zonaHoraria in
        defaultVentanaDeMantenimientoFiltering(
            "zonaHoraria.in=" + DEFAULT_ZONA_HORARIA + "," + UPDATED_ZONA_HORARIA,
            "zonaHoraria.in=" + UPDATED_ZONA_HORARIA
        );
    }

    @Test
    @Transactional
    void getAllVentanaDeMantenimientosByZonaHorariaIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedVentanaDeMantenimiento = ventanaDeMantenimientoRepository.saveAndFlush(ventanaDeMantenimiento);

        // Get all the ventanaDeMantenimientoList where zonaHoraria is not null
        defaultVentanaDeMantenimientoFiltering("zonaHoraria.specified=true", "zonaHoraria.specified=false");
    }

    @Test
    @Transactional
    void getAllVentanaDeMantenimientosByZonaHorariaContainsSomething() throws Exception {
        // Initialize the database
        insertedVentanaDeMantenimiento = ventanaDeMantenimientoRepository.saveAndFlush(ventanaDeMantenimiento);

        // Get all the ventanaDeMantenimientoList where zonaHoraria contains
        defaultVentanaDeMantenimientoFiltering(
            "zonaHoraria.contains=" + DEFAULT_ZONA_HORARIA,
            "zonaHoraria.contains=" + UPDATED_ZONA_HORARIA
        );
    }

    @Test
    @Transactional
    void getAllVentanaDeMantenimientosByZonaHorariaNotContainsSomething() throws Exception {
        // Initialize the database
        insertedVentanaDeMantenimiento = ventanaDeMantenimientoRepository.saveAndFlush(ventanaDeMantenimiento);

        // Get all the ventanaDeMantenimientoList where zonaHoraria does not contain
        defaultVentanaDeMantenimientoFiltering(
            "zonaHoraria.doesNotContain=" + UPDATED_ZONA_HORARIA,
            "zonaHoraria.doesNotContain=" + DEFAULT_ZONA_HORARIA
        );
    }

    @Test
    @Transactional
    void getAllVentanaDeMantenimientosByRecurrenciaIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedVentanaDeMantenimiento = ventanaDeMantenimientoRepository.saveAndFlush(ventanaDeMantenimiento);

        // Get all the ventanaDeMantenimientoList where recurrencia equals to
        defaultVentanaDeMantenimientoFiltering("recurrencia.equals=" + DEFAULT_RECURRENCIA, "recurrencia.equals=" + UPDATED_RECURRENCIA);
    }

    @Test
    @Transactional
    void getAllVentanaDeMantenimientosByRecurrenciaIsInShouldWork() throws Exception {
        // Initialize the database
        insertedVentanaDeMantenimiento = ventanaDeMantenimientoRepository.saveAndFlush(ventanaDeMantenimiento);

        // Get all the ventanaDeMantenimientoList where recurrencia in
        defaultVentanaDeMantenimientoFiltering(
            "recurrencia.in=" + DEFAULT_RECURRENCIA + "," + UPDATED_RECURRENCIA,
            "recurrencia.in=" + UPDATED_RECURRENCIA
        );
    }

    @Test
    @Transactional
    void getAllVentanaDeMantenimientosByRecurrenciaIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedVentanaDeMantenimiento = ventanaDeMantenimientoRepository.saveAndFlush(ventanaDeMantenimiento);

        // Get all the ventanaDeMantenimientoList where recurrencia is not null
        defaultVentanaDeMantenimientoFiltering("recurrencia.specified=true", "recurrencia.specified=false");
    }

    @Test
    @Transactional
    void getAllVentanaDeMantenimientosByDiasSemanaIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedVentanaDeMantenimiento = ventanaDeMantenimientoRepository.saveAndFlush(ventanaDeMantenimiento);

        // Get all the ventanaDeMantenimientoList where diasSemana equals to
        defaultVentanaDeMantenimientoFiltering("diasSemana.equals=" + DEFAULT_DIAS_SEMANA, "diasSemana.equals=" + UPDATED_DIAS_SEMANA);
    }

    @Test
    @Transactional
    void getAllVentanaDeMantenimientosByDiasSemanaIsInShouldWork() throws Exception {
        // Initialize the database
        insertedVentanaDeMantenimiento = ventanaDeMantenimientoRepository.saveAndFlush(ventanaDeMantenimiento);

        // Get all the ventanaDeMantenimientoList where diasSemana in
        defaultVentanaDeMantenimientoFiltering(
            "diasSemana.in=" + DEFAULT_DIAS_SEMANA + "," + UPDATED_DIAS_SEMANA,
            "diasSemana.in=" + UPDATED_DIAS_SEMANA
        );
    }

    @Test
    @Transactional
    void getAllVentanaDeMantenimientosByDiasSemanaIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedVentanaDeMantenimiento = ventanaDeMantenimientoRepository.saveAndFlush(ventanaDeMantenimiento);

        // Get all the ventanaDeMantenimientoList where diasSemana is not null
        defaultVentanaDeMantenimientoFiltering("diasSemana.specified=true", "diasSemana.specified=false");
    }

    @Test
    @Transactional
    void getAllVentanaDeMantenimientosByDiasSemanaContainsSomething() throws Exception {
        // Initialize the database
        insertedVentanaDeMantenimiento = ventanaDeMantenimientoRepository.saveAndFlush(ventanaDeMantenimiento);

        // Get all the ventanaDeMantenimientoList where diasSemana contains
        defaultVentanaDeMantenimientoFiltering("diasSemana.contains=" + DEFAULT_DIAS_SEMANA, "diasSemana.contains=" + UPDATED_DIAS_SEMANA);
    }

    @Test
    @Transactional
    void getAllVentanaDeMantenimientosByDiasSemanaNotContainsSomething() throws Exception {
        // Initialize the database
        insertedVentanaDeMantenimiento = ventanaDeMantenimientoRepository.saveAndFlush(ventanaDeMantenimiento);

        // Get all the ventanaDeMantenimientoList where diasSemana does not contain
        defaultVentanaDeMantenimientoFiltering(
            "diasSemana.doesNotContain=" + UPDATED_DIAS_SEMANA,
            "diasSemana.doesNotContain=" + DEFAULT_DIAS_SEMANA
        );
    }

    @Test
    @Transactional
    void getAllVentanaDeMantenimientosByRecurrenciaHastaIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedVentanaDeMantenimiento = ventanaDeMantenimientoRepository.saveAndFlush(ventanaDeMantenimiento);

        // Get all the ventanaDeMantenimientoList where recurrenciaHasta equals to
        defaultVentanaDeMantenimientoFiltering(
            "recurrenciaHasta.equals=" + DEFAULT_RECURRENCIA_HASTA,
            "recurrenciaHasta.equals=" + UPDATED_RECURRENCIA_HASTA
        );
    }

    @Test
    @Transactional
    void getAllVentanaDeMantenimientosByRecurrenciaHastaIsInShouldWork() throws Exception {
        // Initialize the database
        insertedVentanaDeMantenimiento = ventanaDeMantenimientoRepository.saveAndFlush(ventanaDeMantenimiento);

        // Get all the ventanaDeMantenimientoList where recurrenciaHasta in
        defaultVentanaDeMantenimientoFiltering(
            "recurrenciaHasta.in=" + DEFAULT_RECURRENCIA_HASTA + "," + UPDATED_RECURRENCIA_HASTA,
            "recurrenciaHasta.in=" + UPDATED_RECURRENCIA_HASTA
        );
    }

    @Test
    @Transactional
    void getAllVentanaDeMantenimientosByRecurrenciaHastaIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedVentanaDeMantenimiento = ventanaDeMantenimientoRepository.saveAndFlush(ventanaDeMantenimiento);

        // Get all the ventanaDeMantenimientoList where recurrenciaHasta is not null
        defaultVentanaDeMantenimientoFiltering("recurrenciaHasta.specified=true", "recurrenciaHasta.specified=false");
    }

    @Test
    @Transactional
    void getAllVentanaDeMantenimientosByRecurrenciaHastaIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedVentanaDeMantenimiento = ventanaDeMantenimientoRepository.saveAndFlush(ventanaDeMantenimiento);

        // Get all the ventanaDeMantenimientoList where recurrenciaHasta is greater than or equal to
        defaultVentanaDeMantenimientoFiltering(
            "recurrenciaHasta.greaterThanOrEqual=" + DEFAULT_RECURRENCIA_HASTA,
            "recurrenciaHasta.greaterThanOrEqual=" + UPDATED_RECURRENCIA_HASTA
        );
    }

    @Test
    @Transactional
    void getAllVentanaDeMantenimientosByRecurrenciaHastaIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedVentanaDeMantenimiento = ventanaDeMantenimientoRepository.saveAndFlush(ventanaDeMantenimiento);

        // Get all the ventanaDeMantenimientoList where recurrenciaHasta is less than or equal to
        defaultVentanaDeMantenimientoFiltering(
            "recurrenciaHasta.lessThanOrEqual=" + DEFAULT_RECURRENCIA_HASTA,
            "recurrenciaHasta.lessThanOrEqual=" + SMALLER_RECURRENCIA_HASTA
        );
    }

    @Test
    @Transactional
    void getAllVentanaDeMantenimientosByRecurrenciaHastaIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedVentanaDeMantenimiento = ventanaDeMantenimientoRepository.saveAndFlush(ventanaDeMantenimiento);

        // Get all the ventanaDeMantenimientoList where recurrenciaHasta is less than
        defaultVentanaDeMantenimientoFiltering(
            "recurrenciaHasta.lessThan=" + UPDATED_RECURRENCIA_HASTA,
            "recurrenciaHasta.lessThan=" + DEFAULT_RECURRENCIA_HASTA
        );
    }

    @Test
    @Transactional
    void getAllVentanaDeMantenimientosByRecurrenciaHastaIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedVentanaDeMantenimiento = ventanaDeMantenimientoRepository.saveAndFlush(ventanaDeMantenimiento);

        // Get all the ventanaDeMantenimientoList where recurrenciaHasta is greater than
        defaultVentanaDeMantenimientoFiltering(
            "recurrenciaHasta.greaterThan=" + SMALLER_RECURRENCIA_HASTA,
            "recurrenciaHasta.greaterThan=" + DEFAULT_RECURRENCIA_HASTA
        );
    }

    @Test
    @Transactional
    void getAllVentanaDeMantenimientosByActivaIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedVentanaDeMantenimiento = ventanaDeMantenimientoRepository.saveAndFlush(ventanaDeMantenimiento);

        // Get all the ventanaDeMantenimientoList where activa equals to
        defaultVentanaDeMantenimientoFiltering("activa.equals=" + DEFAULT_ACTIVA, "activa.equals=" + UPDATED_ACTIVA);
    }

    @Test
    @Transactional
    void getAllVentanaDeMantenimientosByActivaIsInShouldWork() throws Exception {
        // Initialize the database
        insertedVentanaDeMantenimiento = ventanaDeMantenimientoRepository.saveAndFlush(ventanaDeMantenimiento);

        // Get all the ventanaDeMantenimientoList where activa in
        defaultVentanaDeMantenimientoFiltering("activa.in=" + DEFAULT_ACTIVA + "," + UPDATED_ACTIVA, "activa.in=" + UPDATED_ACTIVA);
    }

    @Test
    @Transactional
    void getAllVentanaDeMantenimientosByActivaIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedVentanaDeMantenimiento = ventanaDeMantenimientoRepository.saveAndFlush(ventanaDeMantenimiento);

        // Get all the ventanaDeMantenimientoList where activa is not null
        defaultVentanaDeMantenimientoFiltering("activa.specified=true", "activa.specified=false");
    }

    @Test
    @Transactional
    void getAllVentanaDeMantenimientosByEquipoIsEqualToSomething() throws Exception {
        Equipo equipo;
        if (TestUtil.findAll(em, Equipo.class).isEmpty()) {
            ventanaDeMantenimientoRepository.saveAndFlush(ventanaDeMantenimiento);
            equipo = EquipoResourceIT.createEntity();
        } else {
            equipo = TestUtil.findAll(em, Equipo.class).get(0);
        }
        em.persist(equipo);
        em.flush();
        ventanaDeMantenimiento.setEquipo(equipo);
        ventanaDeMantenimientoRepository.saveAndFlush(ventanaDeMantenimiento);
        Long equipoId = equipo.getId();
        // Get all the ventanaDeMantenimientoList where equipo equals to equipoId
        defaultVentanaDeMantenimientoShouldBeFound("equipoId.equals=" + equipoId);

        // Get all the ventanaDeMantenimientoList where equipo equals to (equipoId + 1)
        defaultVentanaDeMantenimientoShouldNotBeFound("equipoId.equals=" + (equipoId + 1));
    }

    @Test
    @Transactional
    void getAllVentanaDeMantenimientosByCreadaPorIsEqualToSomething() throws Exception {
        User creadaPor;
        if (TestUtil.findAll(em, User.class).isEmpty()) {
            ventanaDeMantenimientoRepository.saveAndFlush(ventanaDeMantenimiento);
            creadaPor = UserResourceIT.createEntity();
        } else {
            creadaPor = TestUtil.findAll(em, User.class).get(0);
        }
        em.persist(creadaPor);
        em.flush();
        ventanaDeMantenimiento.setCreadaPor(creadaPor);
        ventanaDeMantenimientoRepository.saveAndFlush(ventanaDeMantenimiento);
        Long creadaPorId = creadaPor.getId();
        // Get all the ventanaDeMantenimientoList where creadaPor equals to creadaPorId
        defaultVentanaDeMantenimientoShouldBeFound("creadaPorId.equals=" + creadaPorId);

        // Get all the ventanaDeMantenimientoList where creadaPor equals to (creadaPorId + 1)
        defaultVentanaDeMantenimientoShouldNotBeFound("creadaPorId.equals=" + (creadaPorId + 1));
    }

    private void defaultVentanaDeMantenimientoFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultVentanaDeMantenimientoShouldBeFound(shouldBeFound);
        defaultVentanaDeMantenimientoShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultVentanaDeMantenimientoShouldBeFound(String filter) throws Exception {
        restVentanaDeMantenimientoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(ventanaDeMantenimiento.getId().intValue())))
            .andExpect(jsonPath("$.[*].nombre").value(hasItem(DEFAULT_NOMBRE)))
            .andExpect(jsonPath("$.[*].descripcion").value(hasItem(DEFAULT_DESCRIPCION)))
            .andExpect(jsonPath("$.[*].inicio").value(hasItem(DEFAULT_INICIO.toString())))
            .andExpect(jsonPath("$.[*].fin").value(hasItem(DEFAULT_FIN.toString())))
            .andExpect(jsonPath("$.[*].zonaHoraria").value(hasItem(DEFAULT_ZONA_HORARIA)))
            .andExpect(jsonPath("$.[*].recurrencia").value(hasItem(DEFAULT_RECURRENCIA.toString())))
            .andExpect(jsonPath("$.[*].diasSemana").value(hasItem(DEFAULT_DIAS_SEMANA)))
            .andExpect(jsonPath("$.[*].recurrenciaHasta").value(hasItem(DEFAULT_RECURRENCIA_HASTA.toString())))
            .andExpect(jsonPath("$.[*].activa").value(hasItem(DEFAULT_ACTIVA)));

        // Check, that the count call also returns 1
        restVentanaDeMantenimientoMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultVentanaDeMantenimientoShouldNotBeFound(String filter) throws Exception {
        restVentanaDeMantenimientoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restVentanaDeMantenimientoMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingVentanaDeMantenimiento() throws Exception {
        // Get the ventanaDeMantenimiento
        restVentanaDeMantenimientoMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingVentanaDeMantenimiento() throws Exception {
        // Initialize the database
        insertedVentanaDeMantenimiento = ventanaDeMantenimientoRepository.saveAndFlush(ventanaDeMantenimiento);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the ventanaDeMantenimiento
        VentanaDeMantenimiento updatedVentanaDeMantenimiento = ventanaDeMantenimientoRepository
            .findById(ventanaDeMantenimiento.getId())
            .orElseThrow();
        // Disconnect from session so that the updates on updatedVentanaDeMantenimiento are not directly saved in db
        em.detach(updatedVentanaDeMantenimiento);
        updatedVentanaDeMantenimiento
            .nombre(UPDATED_NOMBRE)
            .descripcion(UPDATED_DESCRIPCION)
            .inicio(UPDATED_INICIO)
            .fin(UPDATED_FIN)
            .zonaHoraria(UPDATED_ZONA_HORARIA)
            .recurrencia(UPDATED_RECURRENCIA)
            .diasSemana(UPDATED_DIAS_SEMANA)
            .recurrenciaHasta(UPDATED_RECURRENCIA_HASTA)
            .activa(UPDATED_ACTIVA);
        VentanaDeMantenimientoDTO ventanaDeMantenimientoDTO = ventanaDeMantenimientoMapper.toDto(updatedVentanaDeMantenimiento);

        restVentanaDeMantenimientoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, ventanaDeMantenimientoDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(ventanaDeMantenimientoDTO))
            )
            .andExpect(status().isOk());

        // Validate the VentanaDeMantenimiento in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedVentanaDeMantenimientoToMatchAllProperties(updatedVentanaDeMantenimiento);
    }

    @Test
    @Transactional
    void putNonExistingVentanaDeMantenimiento() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        ventanaDeMantenimiento.setId(longCount.incrementAndGet());

        // Create the VentanaDeMantenimiento
        VentanaDeMantenimientoDTO ventanaDeMantenimientoDTO = ventanaDeMantenimientoMapper.toDto(ventanaDeMantenimiento);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restVentanaDeMantenimientoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, ventanaDeMantenimientoDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(ventanaDeMantenimientoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the VentanaDeMantenimiento in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchVentanaDeMantenimiento() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        ventanaDeMantenimiento.setId(longCount.incrementAndGet());

        // Create the VentanaDeMantenimiento
        VentanaDeMantenimientoDTO ventanaDeMantenimientoDTO = ventanaDeMantenimientoMapper.toDto(ventanaDeMantenimiento);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVentanaDeMantenimientoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(ventanaDeMantenimientoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the VentanaDeMantenimiento in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamVentanaDeMantenimiento() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        ventanaDeMantenimiento.setId(longCount.incrementAndGet());

        // Create the VentanaDeMantenimiento
        VentanaDeMantenimientoDTO ventanaDeMantenimientoDTO = ventanaDeMantenimientoMapper.toDto(ventanaDeMantenimiento);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVentanaDeMantenimientoMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(ventanaDeMantenimientoDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the VentanaDeMantenimiento in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateVentanaDeMantenimientoWithPatch() throws Exception {
        // Initialize the database
        insertedVentanaDeMantenimiento = ventanaDeMantenimientoRepository.saveAndFlush(ventanaDeMantenimiento);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the ventanaDeMantenimiento using partial update
        VentanaDeMantenimiento partialUpdatedVentanaDeMantenimiento = new VentanaDeMantenimiento();
        partialUpdatedVentanaDeMantenimiento.setId(ventanaDeMantenimiento.getId());

        partialUpdatedVentanaDeMantenimiento
            .nombre(UPDATED_NOMBRE)
            .descripcion(UPDATED_DESCRIPCION)
            .fin(UPDATED_FIN)
            .zonaHoraria(UPDATED_ZONA_HORARIA)
            .recurrencia(UPDATED_RECURRENCIA)
            .activa(UPDATED_ACTIVA);

        restVentanaDeMantenimientoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedVentanaDeMantenimiento.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedVentanaDeMantenimiento))
            )
            .andExpect(status().isOk());

        // Validate the VentanaDeMantenimiento in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertVentanaDeMantenimientoUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedVentanaDeMantenimiento, ventanaDeMantenimiento),
            getPersistedVentanaDeMantenimiento(ventanaDeMantenimiento)
        );
    }

    @Test
    @Transactional
    void fullUpdateVentanaDeMantenimientoWithPatch() throws Exception {
        // Initialize the database
        insertedVentanaDeMantenimiento = ventanaDeMantenimientoRepository.saveAndFlush(ventanaDeMantenimiento);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the ventanaDeMantenimiento using partial update
        VentanaDeMantenimiento partialUpdatedVentanaDeMantenimiento = new VentanaDeMantenimiento();
        partialUpdatedVentanaDeMantenimiento.setId(ventanaDeMantenimiento.getId());

        partialUpdatedVentanaDeMantenimiento
            .nombre(UPDATED_NOMBRE)
            .descripcion(UPDATED_DESCRIPCION)
            .inicio(UPDATED_INICIO)
            .fin(UPDATED_FIN)
            .zonaHoraria(UPDATED_ZONA_HORARIA)
            .recurrencia(UPDATED_RECURRENCIA)
            .diasSemana(UPDATED_DIAS_SEMANA)
            .recurrenciaHasta(UPDATED_RECURRENCIA_HASTA)
            .activa(UPDATED_ACTIVA);

        restVentanaDeMantenimientoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedVentanaDeMantenimiento.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedVentanaDeMantenimiento))
            )
            .andExpect(status().isOk());

        // Validate the VentanaDeMantenimiento in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertVentanaDeMantenimientoUpdatableFieldsEquals(
            partialUpdatedVentanaDeMantenimiento,
            getPersistedVentanaDeMantenimiento(partialUpdatedVentanaDeMantenimiento)
        );
    }

    @Test
    @Transactional
    void patchNonExistingVentanaDeMantenimiento() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        ventanaDeMantenimiento.setId(longCount.incrementAndGet());

        // Create the VentanaDeMantenimiento
        VentanaDeMantenimientoDTO ventanaDeMantenimientoDTO = ventanaDeMantenimientoMapper.toDto(ventanaDeMantenimiento);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restVentanaDeMantenimientoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, ventanaDeMantenimientoDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(ventanaDeMantenimientoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the VentanaDeMantenimiento in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchVentanaDeMantenimiento() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        ventanaDeMantenimiento.setId(longCount.incrementAndGet());

        // Create the VentanaDeMantenimiento
        VentanaDeMantenimientoDTO ventanaDeMantenimientoDTO = ventanaDeMantenimientoMapper.toDto(ventanaDeMantenimiento);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVentanaDeMantenimientoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(ventanaDeMantenimientoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the VentanaDeMantenimiento in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamVentanaDeMantenimiento() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        ventanaDeMantenimiento.setId(longCount.incrementAndGet());

        // Create the VentanaDeMantenimiento
        VentanaDeMantenimientoDTO ventanaDeMantenimientoDTO = ventanaDeMantenimientoMapper.toDto(ventanaDeMantenimiento);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVentanaDeMantenimientoMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(ventanaDeMantenimientoDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the VentanaDeMantenimiento in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteVentanaDeMantenimiento() throws Exception {
        // Initialize the database
        insertedVentanaDeMantenimiento = ventanaDeMantenimientoRepository.saveAndFlush(ventanaDeMantenimiento);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the ventanaDeMantenimiento
        restVentanaDeMantenimientoMockMvc
            .perform(delete(ENTITY_API_URL_ID, ventanaDeMantenimiento.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return ventanaDeMantenimientoRepository.count();
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

    protected VentanaDeMantenimiento getPersistedVentanaDeMantenimiento(VentanaDeMantenimiento ventanaDeMantenimiento) {
        return ventanaDeMantenimientoRepository.findById(ventanaDeMantenimiento.getId()).orElseThrow();
    }

    protected void assertPersistedVentanaDeMantenimientoToMatchAllProperties(VentanaDeMantenimiento expectedVentanaDeMantenimiento) {
        assertVentanaDeMantenimientoAllPropertiesEquals(
            expectedVentanaDeMantenimiento,
            getPersistedVentanaDeMantenimiento(expectedVentanaDeMantenimiento)
        );
    }

    protected void assertPersistedVentanaDeMantenimientoToMatchUpdatableProperties(VentanaDeMantenimiento expectedVentanaDeMantenimiento) {
        assertVentanaDeMantenimientoAllUpdatablePropertiesEquals(
            expectedVentanaDeMantenimiento,
            getPersistedVentanaDeMantenimiento(expectedVentanaDeMantenimiento)
        );
    }
}
