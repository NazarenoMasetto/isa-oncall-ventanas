import dayjs from 'dayjs/esm';

import { IVentanaDeMantenimiento, NewVentanaDeMantenimiento } from './ventana-de-mantenimiento.model';

export const sampleWithRequiredData: IVentanaDeMantenimiento = {
  id: 27630,
  nombre: 'yet debit',
  inicio: dayjs('2023-12-04T20:36'),
  fin: dayjs('2023-12-04T04:22'),
  zonaHoraria: 'sleepily',
  recurrencia: 'MENSUAL',
  activa: true,
};

export const sampleWithPartialData: IVentanaDeMantenimiento = {
  id: 22680,
  nombre: 'notwithstanding flint brr',
  inicio: dayjs('2023-12-04T20:45'),
  fin: dayjs('2023-12-04T05:35'),
  zonaHoraria: 'in',
  recurrencia: 'MENSUAL',
  recurrenciaHasta: dayjs('2023-12-04'),
  activa: true,
};

export const sampleWithFullData: IVentanaDeMantenimiento = {
  id: 21275,
  nombre: 'ha while',
  descripcion: 'where aha engage',
  inicio: dayjs('2023-12-04T19:19'),
  fin: dayjs('2023-12-04T10:34'),
  zonaHoraria: 'provided reporter',
  recurrencia: 'NINGUNA',
  diasSemana: 'cannibalise',
  recurrenciaHasta: dayjs('2023-12-04'),
  activa: false,
};

export const sampleWithNewData: NewVentanaDeMantenimiento = {
  nombre: 'as yawningly excluding',
  inicio: dayjs('2023-12-04T14:09'),
  fin: dayjs('2023-12-04T10:27'),
  zonaHoraria: 'right',
  recurrencia: 'MENSUAL',
  activa: false,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
