import dayjs from 'dayjs/esm';

import { IExcepcionDeVentana, NewExcepcionDeVentana } from './excepcion-de-ventana.model';

export const sampleWithRequiredData: IExcepcionDeVentana = {
  id: 25752,
  fechaOcurrencia: dayjs('2023-12-04'),
  tipo: 'EXTENSION',
  motivo: 'passport if hmph',
  creadaEn: dayjs('2023-12-04T13:58'),
};

export const sampleWithPartialData: IExcepcionDeVentana = {
  id: 17154,
  fechaOcurrencia: dayjs('2023-12-04'),
  tipo: 'EXTENSION',
  nuevoFin: dayjs('2023-12-04T00:43'),
  motivo: 'ascertain sizzle',
  creadaEn: dayjs('2023-12-04T02:08'),
};

export const sampleWithFullData: IExcepcionDeVentana = {
  id: 16446,
  fechaOcurrencia: dayjs('2023-12-03'),
  tipo: 'EXTENSION',
  nuevoInicio: dayjs('2023-12-04T00:09'),
  nuevoFin: dayjs('2023-12-04T02:11'),
  motivo: 'faithfully pfft',
  creadaEn: dayjs('2023-12-04T16:00'),
};

export const sampleWithNewData: NewExcepcionDeVentana = {
  fechaOcurrencia: dayjs('2023-12-04'),
  tipo: 'CANCELACION',
  motivo: 'fairly',
  creadaEn: dayjs('2023-12-04T03:02'),
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
