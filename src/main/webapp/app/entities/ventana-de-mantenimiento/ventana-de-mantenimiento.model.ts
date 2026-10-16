import dayjs from 'dayjs/esm';

import { TipoRecurrencia } from 'app/entities/enumerations/tipo-recurrencia.model';
import { IEquipo } from 'app/entities/equipo/equipo.model';
import { IUser } from 'app/entities/user/user.model';

export interface IVentanaDeMantenimiento {
  id: number;
  nombre?: string | null;
  descripcion?: string | null;
  inicio?: dayjs.Dayjs | null;
  fin?: dayjs.Dayjs | null;
  zonaHoraria?: string | null;
  recurrencia?: keyof typeof TipoRecurrencia | null;
  diasSemana?: string | null;
  recurrenciaHasta?: dayjs.Dayjs | null;
  activa?: boolean | null;
  equipo?: Pick<IEquipo, 'id' | 'nombre'> | null;
  creadaPor?: Pick<IUser, 'id' | 'login'> | null;
}

export type NewVentanaDeMantenimiento = Omit<IVentanaDeMantenimiento, 'id'> & { id: null };
