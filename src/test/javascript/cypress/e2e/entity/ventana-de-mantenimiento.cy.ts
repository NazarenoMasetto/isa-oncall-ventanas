import {
  entityConfirmDeleteButtonSelector,
  entityCreateButtonSelector,
  entityCreateCancelButtonSelector,
  entityCreateSaveButtonSelector,
  entityDeleteButtonSelector,
  entityDetailsBackButtonSelector,
  entityDetailsButtonSelector,
  entityEditButtonSelector,
  entityTableSelector,
} from '../../support/entity';

describe('VentanaDeMantenimiento e2e test', () => {
  const ventanaDeMantenimientoPageUrl = '/ventana-de-mantenimiento';
  let username: string;
  let password: string;
  // const ventanaDeMantenimientoSample = {"nombre":"far-off","inicio":"2023-12-04T14:58:22.159Z","fin":"2023-12-04T02:26:08.784Z","zonaHoraria":"skyline whenever","recurrencia":"DIARIA","activa":true};

  let ventanaDeMantenimiento;
  // let equipo;
  // let user;

  before(() => {
    cy.credentials().then(credentials => {
      ({ username, password } = credentials);
    });
  });

  beforeEach(() => {
    cy.login(username, password);
  });

  /* Disabled due to incompatibility
  beforeEach(() => {
    // create an instance at the required relationship entity:
    cy.authenticatedRequest({
      method: 'POST',
      url: '/api/equipos',
      body: {"nombre":"instead rarely","emailContacto":"sans as gah","canalChat":"awesome"},
    }).then(({ body }) => {
      equipo = body;
    });
    // create an instance at the required relationship entity:
    cy.authenticatedRequest({
      method: 'POST',
      url: '/api/users',
      body: {"login":"Ramiro38","firstName":"César","lastName":"Padrón Moreno","email":"Fernando.SanchezRangel29@hotmail.com","langKey":"officially","imageUrl":"gah"},
    }).then(({ body }) => {
      user = body;
    });
  });
   */

  beforeEach(() => {
    cy.intercept('GET', '/api/ventana-de-mantenimientos+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/ventana-de-mantenimientos').as('postEntityRequest');
    cy.intercept('DELETE', '/api/ventana-de-mantenimientos/*').as('deleteEntityRequest');
  });

  /* Disabled due to incompatibility
  beforeEach(() => {
    // Simulate relationships api for better performance and reproducibility.
    cy.intercept('GET', '/api/equipos', {
      statusCode: 200,
      body: [equipo],
    });

    cy.intercept('GET', '/api/users', {
      statusCode: 200,
      body: [user],
    });

    cy.intercept('GET', '/api/servicio-en-ventanas', {
      statusCode: 200,
      body: [],
    });

    cy.intercept('GET', '/api/excepcion-de-ventanas', {
      statusCode: 200,
      body: [],
    });

    cy.intercept('GET', '/api/alertas', {
      statusCode: 200,
      body: [],
    });

  });
   */

  afterEach(() => {
    if (ventanaDeMantenimiento) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/ventana-de-mantenimientos/${ventanaDeMantenimiento.id}`,
      }).then(() => {
        ventanaDeMantenimiento = undefined;
      });
    }
  });

  /* Disabled due to incompatibility
  afterEach(() => {
    if (equipo) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/equipos/${equipo.id}`,
      }).then(() => {
        equipo = undefined;
      });
    }
    if (user) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/users/${user.id}`,
      }).then(() => {
        user = undefined;
      });
    }
  });
   */

  it('VentanaDeMantenimientos menu should load VentanaDeMantenimientos page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('ventana-de-mantenimiento');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('VentanaDeMantenimiento').should('exist');
    cy.location('pathname').should('eq', ventanaDeMantenimientoPageUrl);
  });

  describe('VentanaDeMantenimiento page', () => {
    it('should have translated page title', () => {
      cy.visit(ventanaDeMantenimientoPageUrl);
      cy.getEntityHeading('VentanaDeMantenimiento').should('not.contain', 'oncallApp.ventanaDeMantenimiento.home.title');
    });

    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(ventanaDeMantenimientoPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create VentanaDeMantenimiento page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.location('pathname').should('eq', `${ventanaDeMantenimientoPageUrl}/new`);
        cy.getEntityCreateUpdateHeading('VentanaDeMantenimiento');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', ventanaDeMantenimientoPageUrl);
      });
    });

    describe('with existing value', () => {
      /* Disabled due to incompatibility
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/ventana-de-mantenimientos',
          body: {
            ...ventanaDeMantenimientoSample,
            equipo: equipo,
            creadaPor: user,
          },
        }).then(({ body }) => {
          ventanaDeMantenimiento = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/ventana-de-mantenimientos+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/ventana-de-mantenimientos?page=0&size=20>; rel="last",<http://localhost/api/ventana-de-mantenimientos?page=0&size=20>; rel="first"',
              },
              body: [ventanaDeMantenimiento],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(ventanaDeMantenimientoPageUrl);

        cy.wait('@entitiesRequestInternal');
      });
       */

      beforeEach(function () {
        cy.visit(ventanaDeMantenimientoPageUrl);

        cy.wait('@entitiesRequest').then(({ response }) => {
          if (response?.body.length === 0) {
            this.skip();
          }
        });
      });

      it('detail button click should load details VentanaDeMantenimiento page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('ventanaDeMantenimiento');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', ventanaDeMantenimientoPageUrl);
      });

      it('edit button click should load edit VentanaDeMantenimiento page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('VentanaDeMantenimiento');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', ventanaDeMantenimientoPageUrl);
      });

      it('edit button click should load edit VentanaDeMantenimiento page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('VentanaDeMantenimiento');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', ventanaDeMantenimientoPageUrl);
      });

      // Reason: cannot create a required entity with relationship with required relationships.
      it.skip('last delete button click should delete instance of VentanaDeMantenimiento', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('ventanaDeMantenimiento').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', ventanaDeMantenimientoPageUrl);

        ventanaDeMantenimiento = undefined;
      });
    });
  });

  describe('new VentanaDeMantenimiento page', () => {
    beforeEach(() => {
      cy.visit(ventanaDeMantenimientoPageUrl);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('VentanaDeMantenimiento');
    });

    // Reason: cannot create a required entity with relationship with required relationships.
    it.skip('should create an instance of VentanaDeMantenimiento', () => {
      cy.get(`[data-cy="nombre"]`).type('loyally wide although');
      cy.get(`[data-cy="nombre"]`).should('have.value', 'loyally wide although');

      cy.get(`[data-cy="descripcion"]`).type('distinction large than');
      cy.get(`[data-cy="descripcion"]`).should('have.value', 'distinction large than');

      cy.get(`[data-cy="inicio"]`).type('2023-12-04T05:34');
      cy.get(`[data-cy="inicio"]`).blur();
      cy.get(`[data-cy="inicio"]`).should('have.value', '2023-12-04T05:34');

      cy.get(`[data-cy="fin"]`).type('2023-12-04T12:45');
      cy.get(`[data-cy="fin"]`).blur();
      cy.get(`[data-cy="fin"]`).should('have.value', '2023-12-04T12:45');

      cy.get(`[data-cy="zonaHoraria"]`).type('director colorfully');
      cy.get(`[data-cy="zonaHoraria"]`).should('have.value', 'director colorfully');

      cy.get(`[data-cy="recurrencia"]`).select('SEMANAL');

      cy.get(`[data-cy="diasSemana"]`).type('repeatedly');
      cy.get(`[data-cy="diasSemana"]`).should('have.value', 'repeatedly');

      cy.get(`[data-cy="recurrenciaHasta"]`).type('2023-12-04');
      cy.get(`[data-cy="recurrenciaHasta"]`).blur();
      cy.get(`[data-cy="recurrenciaHasta"]`).should('have.value', '2023-12-04');

      cy.get(`[data-cy="activa"]`).should('not.be.checked');
      cy.get(`[data-cy="activa"]`).click();
      cy.get(`[data-cy="activa"]`).should('be.checked');

      cy.get(`[data-cy="equipo"]`).select(1);
      cy.get(`[data-cy="creadaPor"]`).select(1);

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        ventanaDeMantenimiento = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.location('pathname').should('eq', ventanaDeMantenimientoPageUrl);
    });
  });
});
