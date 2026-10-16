import { HttpResponse } from '@angular/common/http';
import { ChangeDetectionStrategy, Component, OnInit, inject, signal } from '@angular/core';
import { ReactiveFormsModule } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { NgbInputDatepicker } from '@ng-bootstrap/ng-bootstrap/datepicker';
import { TranslatePipe } from '@ngx-translate/core';
import { Observable, finalize, map } from 'rxjs';

import { TipoRecurrencia } from 'app/entities/enumerations/tipo-recurrencia.model';
import { IEquipo } from 'app/entities/equipo/equipo.model';
import { EquipoService } from 'app/entities/equipo/service/equipo.service';
import { UserService } from 'app/entities/user/service/user.service';
import { IUser } from 'app/entities/user/user.model';
import { AlertError } from 'app/shared/alert/alert-error';
import { TranslateDirective } from 'app/shared/language';

import { VentanaDeMantenimientoService } from '../service/ventana-de-mantenimiento.service';
import { IVentanaDeMantenimiento } from '../ventana-de-mantenimiento.model';

import { VentanaDeMantenimientoFormGroup, VentanaDeMantenimientoFormService } from './ventana-de-mantenimiento-form.service';

@Component({
  changeDetection: ChangeDetectionStrategy.OnPush,
  selector: 'jhi-ventana-de-mantenimiento-update',
  templateUrl: './ventana-de-mantenimiento-update.html',
  imports: [TranslateDirective, TranslatePipe, FontAwesomeModule, AlertError, ReactiveFormsModule, NgbInputDatepicker],
})
export class VentanaDeMantenimientoUpdate implements OnInit {
  readonly isSaving = signal(false);
  ventanaDeMantenimiento: IVentanaDeMantenimiento | null = null;
  tipoRecurrenciaValues = Object.keys(TipoRecurrencia);

  equiposSharedCollection = signal<IEquipo[]>([]);
  usersSharedCollection = signal<IUser[]>([]);

  protected ventanaDeMantenimientoService = inject(VentanaDeMantenimientoService);
  protected ventanaDeMantenimientoFormService = inject(VentanaDeMantenimientoFormService);
  protected equipoService = inject(EquipoService);
  protected userService = inject(UserService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: VentanaDeMantenimientoFormGroup = this.ventanaDeMantenimientoFormService.createVentanaDeMantenimientoFormGroup();

  compareEquipo = (o1: IEquipo | null, o2: IEquipo | null): boolean => this.equipoService.compareEquipo(o1, o2);

  compareUser = (o1: IUser | null, o2: IUser | null): boolean => this.userService.compareUser(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ ventanaDeMantenimiento }) => {
      this.ventanaDeMantenimiento = ventanaDeMantenimiento;
      if (ventanaDeMantenimiento) {
        this.updateForm(ventanaDeMantenimiento);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    globalThis.history.back();
  }

  save(): void {
    this.isSaving.set(true);
    const ventanaDeMantenimiento = this.ventanaDeMantenimientoFormService.getVentanaDeMantenimiento(this.editForm);
    if (ventanaDeMantenimiento.id === null) {
      this.subscribeToSaveResponse(this.ventanaDeMantenimientoService.create(ventanaDeMantenimiento));
    } else {
      this.subscribeToSaveResponse(this.ventanaDeMantenimientoService.update(ventanaDeMantenimiento));
    }
  }

  protected subscribeToSaveResponse(result: Observable<IVentanaDeMantenimiento | null>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving.set(false);
  }

  protected updateForm(ventanaDeMantenimiento: IVentanaDeMantenimiento): void {
    this.ventanaDeMantenimiento = ventanaDeMantenimiento;
    this.ventanaDeMantenimientoFormService.resetForm(this.editForm, ventanaDeMantenimiento);

    this.equiposSharedCollection.update(equipos =>
      this.equipoService.addEquipoToCollectionIfMissing<IEquipo>(equipos, ventanaDeMantenimiento.equipo),
    );
    this.usersSharedCollection.update(users =>
      this.userService.addUserToCollectionIfMissing<IUser>(users, ventanaDeMantenimiento.creadaPor),
    );
  }

  protected loadRelationshipsOptions(): void {
    this.equipoService
      .query()
      .pipe(map((res: HttpResponse<IEquipo[]>) => res.body ?? []))
      .pipe(
        map((equipos: IEquipo[]) =>
          this.equipoService.addEquipoToCollectionIfMissing<IEquipo>(equipos, this.ventanaDeMantenimiento?.equipo),
        ),
      )
      .subscribe((equipos: IEquipo[]) => this.equiposSharedCollection.set(equipos));

    this.userService
      .query()
      .pipe(map((res: HttpResponse<IUser[]>) => res.body ?? []))
      .pipe(map((users: IUser[]) => this.userService.addUserToCollectionIfMissing<IUser>(users, this.ventanaDeMantenimiento?.creadaPor)))
      .subscribe((users: IUser[]) => this.usersSharedCollection.set(users));
  }
}
