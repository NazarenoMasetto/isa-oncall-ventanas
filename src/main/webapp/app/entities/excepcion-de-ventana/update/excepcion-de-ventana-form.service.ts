import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';

import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IExcepcionDeVentana, NewExcepcionDeVentana } from '../excepcion-de-ventana.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IExcepcionDeVentana for edit and NewExcepcionDeVentanaFormGroupInput for create.
 */
type ExcepcionDeVentanaFormGroupInput = IExcepcionDeVentana | PartialWithRequiredKeyOf<NewExcepcionDeVentana>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IExcepcionDeVentana | NewExcepcionDeVentana> = Omit<T, 'nuevoInicio' | 'nuevoFin' | 'creadaEn'> & {
  nuevoInicio?: string | null;
  nuevoFin?: string | null;
  creadaEn?: string | null;
};

type ExcepcionDeVentanaFormRawValue = FormValueOf<IExcepcionDeVentana>;

type NewExcepcionDeVentanaFormRawValue = FormValueOf<NewExcepcionDeVentana>;

type ExcepcionDeVentanaFormDefaults = Pick<NewExcepcionDeVentana, 'id' | 'nuevoInicio' | 'nuevoFin' | 'creadaEn'>;

type ExcepcionDeVentanaFormGroupContent = {
  id: FormControl<ExcepcionDeVentanaFormRawValue['id'] | NewExcepcionDeVentana['id']>;
  fechaOcurrencia: FormControl<ExcepcionDeVentanaFormRawValue['fechaOcurrencia']>;
  tipo: FormControl<ExcepcionDeVentanaFormRawValue['tipo']>;
  nuevoInicio: FormControl<ExcepcionDeVentanaFormRawValue['nuevoInicio']>;
  nuevoFin: FormControl<ExcepcionDeVentanaFormRawValue['nuevoFin']>;
  motivo: FormControl<ExcepcionDeVentanaFormRawValue['motivo']>;
  creadaEn: FormControl<ExcepcionDeVentanaFormRawValue['creadaEn']>;
  ventana: FormControl<ExcepcionDeVentanaFormRawValue['ventana']>;
  autorizadaPor: FormControl<ExcepcionDeVentanaFormRawValue['autorizadaPor']>;
};

export type ExcepcionDeVentanaFormGroup = FormGroup<ExcepcionDeVentanaFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class ExcepcionDeVentanaFormService {
  createExcepcionDeVentanaFormGroup(excepcionDeVentana?: ExcepcionDeVentanaFormGroupInput): ExcepcionDeVentanaFormGroup {
    const excepcionDeVentanaRawValue = this.convertExcepcionDeVentanaToExcepcionDeVentanaRawValue({
      ...this.getFormDefaults(),
      ...(excepcionDeVentana ?? { id: null }),
    });

    return new FormGroup<ExcepcionDeVentanaFormGroupContent>({
      id: new FormControl(
        { value: excepcionDeVentanaRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      fechaOcurrencia: new FormControl(excepcionDeVentanaRawValue.fechaOcurrencia, {
        validators: [Validators.required],
      }),
      tipo: new FormControl(excepcionDeVentanaRawValue.tipo, {
        validators: [Validators.required],
      }),
      nuevoInicio: new FormControl(excepcionDeVentanaRawValue.nuevoInicio),
      nuevoFin: new FormControl(excepcionDeVentanaRawValue.nuevoFin),
      motivo: new FormControl(excepcionDeVentanaRawValue.motivo, {
        validators: [Validators.required, Validators.maxLength(255)],
      }),
      creadaEn: new FormControl(excepcionDeVentanaRawValue.creadaEn, {
        validators: [Validators.required],
      }),
      ventana: new FormControl(excepcionDeVentanaRawValue.ventana, {
        validators: [Validators.required],
      }),
      autorizadaPor: new FormControl(excepcionDeVentanaRawValue.autorizadaPor),
    });
  }

  getExcepcionDeVentana(form: ExcepcionDeVentanaFormGroup): IExcepcionDeVentana | NewExcepcionDeVentana {
    return this.convertExcepcionDeVentanaRawValueToExcepcionDeVentana(form.getRawValue());
  }

  resetForm(form: ExcepcionDeVentanaFormGroup, excepcionDeVentana: ExcepcionDeVentanaFormGroupInput): void {
    const excepcionDeVentanaRawValue = this.convertExcepcionDeVentanaToExcepcionDeVentanaRawValue({
      ...this.getFormDefaults(),
      ...excepcionDeVentana,
    });
    form.reset({
      ...excepcionDeVentanaRawValue,
      id: { value: excepcionDeVentanaRawValue.id, disabled: true },
    });
  }

  private getFormDefaults(): ExcepcionDeVentanaFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      nuevoInicio: currentTime,
      nuevoFin: currentTime,
      creadaEn: currentTime,
    };
  }

  private convertExcepcionDeVentanaRawValueToExcepcionDeVentana(
    rawExcepcionDeVentana: ExcepcionDeVentanaFormRawValue | NewExcepcionDeVentanaFormRawValue,
  ): IExcepcionDeVentana | NewExcepcionDeVentana {
    return {
      ...rawExcepcionDeVentana,
      nuevoInicio: dayjs(rawExcepcionDeVentana.nuevoInicio, DATE_TIME_FORMAT),
      nuevoFin: dayjs(rawExcepcionDeVentana.nuevoFin, DATE_TIME_FORMAT),
      creadaEn: dayjs(rawExcepcionDeVentana.creadaEn, DATE_TIME_FORMAT),
    };
  }

  private convertExcepcionDeVentanaToExcepcionDeVentanaRawValue(
    excepcionDeVentana: IExcepcionDeVentana | (Partial<NewExcepcionDeVentana> & ExcepcionDeVentanaFormDefaults),
  ): ExcepcionDeVentanaFormRawValue | PartialWithRequiredKeyOf<NewExcepcionDeVentanaFormRawValue> {
    return {
      ...excepcionDeVentana,
      nuevoInicio: excepcionDeVentana.nuevoInicio ? excepcionDeVentana.nuevoInicio.format(DATE_TIME_FORMAT) : undefined,
      nuevoFin: excepcionDeVentana.nuevoFin ? excepcionDeVentana.nuevoFin.format(DATE_TIME_FORMAT) : undefined,
      creadaEn: excepcionDeVentana.creadaEn ? excepcionDeVentana.creadaEn.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
