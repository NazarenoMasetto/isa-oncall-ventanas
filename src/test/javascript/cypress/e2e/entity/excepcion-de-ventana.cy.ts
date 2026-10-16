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

describe('ExcepcionDeVentana e2e test', () => {
  const excepcionDeVentanaPageUrl = '/excepcion-de-ventana';
  let username: string;
  let password: string;
  // const excepcionDeVentanaSample = {"fechaOcurrencia":"2023-12-04","tipo":"CANCELACION","motivo":"incidentally wherever","creadaEn":"2023-12-04T15:32:55.284Z"};

  let excepcionDeVentana;
  // let ventanaDeMantenimiento;

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
      url: '/api/ventana-de-mantenimientos',
      body: {"nombre":"um steel","descripcion":"wonderfully whether which","inicio":"2023-12-04T02:22:42.908Z","fin":"2023-12-04T06:48:41.469Z","zonaHoraria":"proud brr","recurrencia":"DIARIA","diasSemana":"gust","recurrenciaHasta":"2023-12-04","activa":true},
    }).then(({ body }) => {
      ventanaDeMantenimiento = body;
    });
  });
   */

  beforeEach(() => {
    cy.intercept('GET', '/api/excepcion-de-ventanas+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/excepcion-de-ventanas').as('postEntityRequest');
    cy.intercept('DELETE', '/api/excepcion-de-ventanas/*').as('deleteEntityRequest');
  });

  /* Disabled due to incompatibility
  beforeEach(() => {
    // Simulate relationships api for better performance and reproducibility.
    cy.intercept('GET', '/api/ventana-de-mantenimientos', {
      statusCode: 200,
      body: [ventanaDeMantenimiento],
    });

    cy.intercept('GET', '/api/users', {
      statusCode: 200,
      body: [],
    });

  });
   */

  afterEach(() => {
    if (excepcionDeVentana) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/excepcion-de-ventanas/${excepcionDeVentana.id}`,
      }).then(() => {
        excepcionDeVentana = undefined;
      });
    }
  });

  /* Disabled due to incompatibility
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
   */

  it('ExcepcionDeVentanas menu should load ExcepcionDeVentanas page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('excepcion-de-ventana');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('ExcepcionDeVentana').should('exist');
    cy.location('pathname').should('eq', excepcionDeVentanaPageUrl);
  });

  describe('ExcepcionDeVentana page', () => {
    it('should have translated page title', () => {
      cy.visit(excepcionDeVentanaPageUrl);
      cy.getEntityHeading('ExcepcionDeVentana').should('not.contain', 'oncallApp.excepcionDeVentana.home.title');
    });

    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(excepcionDeVentanaPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create ExcepcionDeVentana page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.location('pathname').should('eq', `${excepcionDeVentanaPageUrl}/new`);
        cy.getEntityCreateUpdateHeading('ExcepcionDeVentana');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', excepcionDeVentanaPageUrl);
      });
    });

    describe('with existing value', () => {
      /* Disabled due to incompatibility
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/excepcion-de-ventanas',
          body: {
            ...excepcionDeVentanaSample,
            ventana: ventanaDeMantenimiento,
          },
        }).then(({ body }) => {
          excepcionDeVentana = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/excepcion-de-ventanas+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [excepcionDeVentana],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(excepcionDeVentanaPageUrl);

        cy.wait('@entitiesRequestInternal');
      });
       */

      beforeEach(function () {
        cy.visit(excepcionDeVentanaPageUrl);

        cy.wait('@entitiesRequest').then(({ response }) => {
          if (response?.body.length === 0) {
            this.skip();
          }
        });
      });

      it('detail button click should load details ExcepcionDeVentana page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('excepcionDeVentana');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', excepcionDeVentanaPageUrl);
      });

      it('edit button click should load edit ExcepcionDeVentana page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('ExcepcionDeVentana');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', excepcionDeVentanaPageUrl);
      });

      it('edit button click should load edit ExcepcionDeVentana page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('ExcepcionDeVentana');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', excepcionDeVentanaPageUrl);
      });

      // Reason: cannot create a required entity with relationship with required relationships.
      it.skip('last delete button click should delete instance of ExcepcionDeVentana', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('excepcionDeVentana').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', excepcionDeVentanaPageUrl);

        excepcionDeVentana = undefined;
      });
    });
  });

  describe('new ExcepcionDeVentana page', () => {
    beforeEach(() => {
      cy.visit(excepcionDeVentanaPageUrl);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('ExcepcionDeVentana');
    });

    // Reason: cannot create a required entity with relationship with required relationships.
    it.skip('should create an instance of ExcepcionDeVentana', () => {
      cy.get(`[data-cy="fechaOcurrencia"]`).type('2023-12-04');
      cy.get(`[data-cy="fechaOcurrencia"]`).blur();
      cy.get(`[data-cy="fechaOcurrencia"]`).should('have.value', '2023-12-04');

      cy.get(`[data-cy="tipo"]`).select('EXTENSION');

      cy.get(`[data-cy="nuevoInicio"]`).type('2023-12-04T12:05');
      cy.get(`[data-cy="nuevoInicio"]`).blur();
      cy.get(`[data-cy="nuevoInicio"]`).should('have.value', '2023-12-04T12:05');

      cy.get(`[data-cy="nuevoFin"]`).type('2023-12-04T17:40');
      cy.get(`[data-cy="nuevoFin"]`).blur();
      cy.get(`[data-cy="nuevoFin"]`).should('have.value', '2023-12-04T17:40');

      cy.get(`[data-cy="motivo"]`).type('sheathe analyse edge');
      cy.get(`[data-cy="motivo"]`).should('have.value', 'sheathe analyse edge');

      cy.get(`[data-cy="creadaEn"]`).type('2023-12-04T21:29');
      cy.get(`[data-cy="creadaEn"]`).blur();
      cy.get(`[data-cy="creadaEn"]`).should('have.value', '2023-12-04T21:29');

      cy.get(`[data-cy="ventana"]`).select(1);

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        excepcionDeVentana = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.location('pathname').should('eq', excepcionDeVentanaPageUrl);
    });
  });
});
