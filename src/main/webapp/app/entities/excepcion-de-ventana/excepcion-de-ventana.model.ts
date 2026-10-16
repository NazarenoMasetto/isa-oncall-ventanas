import dayjs from 'dayjs/esm';

import { TipoExcepcionVentana } from 'app/entities/enumerations/tipo-excepcion-ventana.model';
import { IUser } from 'app/entities/user/user.model';
import { IVentanaDeMantenimiento } from 'app/entities/ventana-de-mantenimiento/ventana-de-mantenimiento.model';

export interface IExcepcionDeVentana {
  id: number;
  fechaOcurrencia?: dayjs.Dayjs | null;
  tipo?: keyof typeof TipoExcepcionVentana | null;
  nuevoInicio?: dayjs.Dayjs | null;
  nuevoFin?: dayjs.Dayjs | null;
  motivo?: string | null;
  creadaEn?: dayjs.Dayjs | null;
  ventana?: Pick<IVentanaDeMantenimiento, 'id' | 'nombre'> | null;
  autorizadaPor?: Pick<IUser, 'id' | 'login'> | null;
}

export type NewExcepcionDeVentana = Omit<IExcepcionDeVentana, 'id'> & { id: null };
