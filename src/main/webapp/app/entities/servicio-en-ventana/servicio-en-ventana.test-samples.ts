import { IServicioEnVentana, NewServicioEnVentana } from './servicio-en-ventana.model';

export const sampleWithRequiredData: IServicioEnVentana = {
  id: 21331,
  suprimirAlertas: false,
  suprimirEscalamiento: false,
};

export const sampleWithPartialData: IServicioEnVentana = {
  id: 27649,
  suprimirAlertas: false,
  suprimirEscalamiento: false,
  notas: 'um wearily',
};

export const sampleWithFullData: IServicioEnVentana = {
  id: 6395,
  suprimirAlertas: false,
  suprimirEscalamiento: true,
  notas: 'jovially',
};

export const sampleWithNewData: NewServicioEnVentana = {
  suprimirAlertas: true,
  suprimirEscalamiento: true,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
