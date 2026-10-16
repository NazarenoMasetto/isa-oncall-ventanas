import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';

import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IVentanaDeMantenimiento, NewVentanaDeMantenimiento } from '../ventana-de-mantenimiento.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IVentanaDeMantenimiento for edit and NewVentanaDeMantenimientoFormGroupInput for create.
 */
type VentanaDeMantenimientoFormGroupInput = IVentanaDeMantenimiento | PartialWithRequiredKeyOf<NewVentanaDeMantenimiento>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IVentanaDeMantenimiento | NewVentanaDeMantenimiento> = Omit<T, 'inicio' | 'fin'> & {
  inicio?: string | null;
  fin?: string | null;
};

type VentanaDeMantenimientoFormRawValue = FormValueOf<IVentanaDeMantenimiento>;

type NewVentanaDeMantenimientoFormRawValue = FormValueOf<NewVentanaDeMantenimiento>;

type VentanaDeMantenimientoFormDefaults = Pick<NewVentanaDeMantenimiento, 'id' | 'inicio' | 'fin' | 'activa'>;

type VentanaDeMantenimientoFormGroupContent = {
  id: FormControl<VentanaDeMantenimientoFormRawValue['id'] | NewVentanaDeMantenimiento['id']>;
  nombre: FormControl<VentanaDeMantenimientoFormRawValue['nombre']>;
  descripcion: FormControl<VentanaDeMantenimientoFormRawValue['descripcion']>;
  inicio: FormControl<VentanaDeMantenimientoFormRawValue['inicio']>;
  fin: FormControl<VentanaDeMantenimientoFormRawValue['fin']>;
  zonaHoraria: FormControl<VentanaDeMantenimientoFormRawValue['zonaHoraria']>;
  recurrencia: FormControl<VentanaDeMantenimientoFormRawValue['recurrencia']>;
  diasSemana: FormControl<VentanaDeMantenimientoFormRawValue['diasSemana']>;
  recurrenciaHasta: FormControl<VentanaDeMantenimientoFormRawValue['recurrenciaHasta']>;
  activa: FormControl<VentanaDeMantenimientoFormRawValue['activa']>;
  equipo: FormControl<VentanaDeMantenimientoFormRawValue['equipo']>;
  creadaPor: FormControl<VentanaDeMantenimientoFormRawValue['creadaPor']>;
};

export type VentanaDeMantenimientoFormGroup = FormGroup<VentanaDeMantenimientoFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class VentanaDeMantenimientoFormService {
  createVentanaDeMantenimientoFormGroup(ventanaDeMantenimiento?: VentanaDeMantenimientoFormGroupInput): VentanaDeMantenimientoFormGroup {
    const ventanaDeMantenimientoRawValue = this.convertVentanaDeMantenimientoToVentanaDeMantenimientoRawValue({
      ...this.getFormDefaults(),
      ...(ventanaDeMantenimiento ?? { id: null }),
    });

    return new FormGroup<VentanaDeMantenimientoFormGroupContent>({
      id: new FormControl(
        { value: ventanaDeMantenimientoRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      nombre: new FormControl(ventanaDeMantenimientoRawValue.nombre, {
        validators: [Validators.required, Validators.maxLength(60)],
      }),
      descripcion: new FormControl(ventanaDeMantenimientoRawValue.descripcion, {
        validators: [Validators.maxLength(500)],
      }),
      inicio: new FormControl(ventanaDeMantenimientoRawValue.inicio, {
        validators: [Validators.required],
      }),
      fin: new FormControl(ventanaDeMantenimientoRawValue.fin, {
        validators: [Validators.required],
      }),
      zonaHoraria: new FormControl(ventanaDeMantenimientoRawValue.zonaHoraria, {
        validators: [Validators.required, Validators.maxLength(50)],
      }),
      recurrencia: new FormControl(ventanaDeMantenimientoRawValue.recurrencia, {
        validators: [Validators.required],
      }),
      diasSemana: new FormControl(ventanaDeMantenimientoRawValue.diasSemana, {
        validators: [Validators.maxLength(30)],
      }),
      recurrenciaHasta: new FormControl(ventanaDeMantenimientoRawValue.recurrenciaHasta),
      activa: new FormControl(ventanaDeMantenimientoRawValue.activa, {
        validators: [Validators.required],
      }),
      equipo: new FormControl(ventanaDeMantenimientoRawValue.equipo, {
        validators: [Validators.required],
      }),
      creadaPor: new FormControl(ventanaDeMantenimientoRawValue.creadaPor, {
        validators: [Validators.required],
      }),
    });
  }

  getVentanaDeMantenimiento(form: VentanaDeMantenimientoFormGroup): IVentanaDeMantenimiento | NewVentanaDeMantenimiento {
    return this.convertVentanaDeMantenimientoRawValueToVentanaDeMantenimiento(form.getRawValue());
  }

  resetForm(form: VentanaDeMantenimientoFormGroup, ventanaDeMantenimiento: VentanaDeMantenimientoFormGroupInput): void {
    const ventanaDeMantenimientoRawValue = this.convertVentanaDeMantenimientoToVentanaDeMantenimientoRawValue({
      ...this.getFormDefaults(),
      ...ventanaDeMantenimiento,
    });
    form.reset({
      ...ventanaDeMantenimientoRawValue,
      id: { value: ventanaDeMantenimientoRawValue.id, disabled: true },
    });
  }

  private getFormDefaults(): VentanaDeMantenimientoFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      inicio: currentTime,
      fin: currentTime,
      activa: false,
    };
  }

  private convertVentanaDeMantenimientoRawValueToVentanaDeMantenimiento(
    rawVentanaDeMantenimiento: VentanaDeMantenimientoFormRawValue | NewVentanaDeMantenimientoFormRawValue,
  ): IVentanaDeMantenimiento | NewVentanaDeMantenimiento {
    return {
      ...rawVentanaDeMantenimiento,
      inicio: dayjs(rawVentanaDeMantenimiento.inicio, DATE_TIME_FORMAT),
      fin: dayjs(rawVentanaDeMantenimiento.fin, DATE_TIME_FORMAT),
    };
  }

  private convertVentanaDeMantenimientoToVentanaDeMantenimientoRawValue(
    ventanaDeMantenimiento: IVentanaDeMantenimiento | (Partial<NewVentanaDeMantenimiento> & VentanaDeMantenimientoFormDefaults),
  ): VentanaDeMantenimientoFormRawValue | PartialWithRequiredKeyOf<NewVentanaDeMantenimientoFormRawValue> {
    return {
      ...ventanaDeMantenimiento,
      inicio: ventanaDeMantenimiento.inicio ? ventanaDeMantenimiento.inicio.format(DATE_TIME_FORMAT) : undefined,
      fin: ventanaDeMantenimiento.fin ? ventanaDeMantenimiento.fin.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
