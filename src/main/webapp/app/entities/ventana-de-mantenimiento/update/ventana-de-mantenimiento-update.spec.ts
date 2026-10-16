import { beforeEach, describe, expect, it, vitest } from 'vitest';
import { HttpResponse } from '@angular/common/http';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';

import { provideTranslateService } from '@ngx-translate/core';
import { Subject, from, of } from 'rxjs';

import { IEquipo } from 'app/entities/equipo/equipo.model';
import { EquipoService } from 'app/entities/equipo/service/equipo.service';
import { UserService } from 'app/entities/user/service/user.service';
import { IUser } from 'app/entities/user/user.model';
import { VentanaDeMantenimientoService } from '../service/ventana-de-mantenimiento.service';
import { IVentanaDeMantenimiento } from '../ventana-de-mantenimiento.model';

import { VentanaDeMantenimientoFormService } from './ventana-de-mantenimiento-form.service';
import { VentanaDeMantenimientoUpdate } from './ventana-de-mantenimiento-update';

describe('VentanaDeMantenimiento Management Update Component', () => {
  let comp: VentanaDeMantenimientoUpdate;
  let fixture: ComponentFixture<VentanaDeMantenimientoUpdate>;
  let activatedRoute: ActivatedRoute;
  let ventanaDeMantenimientoFormService: VentanaDeMantenimientoFormService;
  let ventanaDeMantenimientoService: VentanaDeMantenimientoService;
  let equipoService: EquipoService;
  let userService: UserService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [
        provideTranslateService(),
        provideHttpClientTesting(),
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    });

    fixture = TestBed.createComponent(VentanaDeMantenimientoUpdate);
    activatedRoute = TestBed.inject(ActivatedRoute);
    ventanaDeMantenimientoFormService = TestBed.inject(VentanaDeMantenimientoFormService);
    ventanaDeMantenimientoService = TestBed.inject(VentanaDeMantenimientoService);
    equipoService = TestBed.inject(EquipoService);
    userService = TestBed.inject(UserService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should call Equipo query and add missing value', () => {
      const ventanaDeMantenimiento: IVentanaDeMantenimiento = { id: 27219 };
      const equipo: IEquipo = { id: 20906 };
      ventanaDeMantenimiento.equipo = equipo;

      const equipoCollection: IEquipo[] = [{ id: 20906 }];
      vitest.spyOn(equipoService, 'query').mockReturnValue(of(new HttpResponse({ body: equipoCollection })));
      const additionalEquipos = [equipo];
      const expectedCollection: IEquipo[] = [...additionalEquipos, ...equipoCollection];
      vitest.spyOn(equipoService, 'addEquipoToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ ventanaDeMantenimiento });
      comp.ngOnInit();

      expect(equipoService.query).toHaveBeenCalled();
      expect(equipoService.addEquipoToCollectionIfMissing).toHaveBeenCalledWith(
        equipoCollection,
        ...additionalEquipos.map(i => expect.objectContaining(i) as typeof i),
      );
      expect(comp.equiposSharedCollection()).toEqual(expectedCollection);
    });

    it('should call User query and add missing value', () => {
      const ventanaDeMantenimiento: IVentanaDeMantenimiento = { id: 27219 };
      const creadaPor: IUser = { id: 3944 };
      ventanaDeMantenimiento.creadaPor = creadaPor;

      const userCollection: IUser[] = [{ id: 3944 }];
      vitest.spyOn(userService, 'query').mockReturnValue(of(new HttpResponse({ body: userCollection })));
      const additionalUsers = [creadaPor];
      const expectedCollection: IUser[] = [...additionalUsers, ...userCollection];
      vitest.spyOn(userService, 'addUserToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ ventanaDeMantenimiento });
      comp.ngOnInit();

      expect(userService.query).toHaveBeenCalled();
      expect(userService.addUserToCollectionIfMissing).toHaveBeenCalledWith(
        userCollection,
        ...additionalUsers.map(i => expect.objectContaining(i) as typeof i),
      );
      expect(comp.usersSharedCollection()).toEqual(expectedCollection);
    });

    it('should update editForm', () => {
      const ventanaDeMantenimiento: IVentanaDeMantenimiento = { id: 27219 };
      const equipo: IEquipo = { id: 20906 };
      ventanaDeMantenimiento.equipo = equipo;
      const creadaPor: IUser = { id: 3944 };
      ventanaDeMantenimiento.creadaPor = creadaPor;

      activatedRoute.data = of({ ventanaDeMantenimiento });
      comp.ngOnInit();

      expect(comp.equiposSharedCollection()).toContainEqual(equipo);
      expect(comp.usersSharedCollection()).toContainEqual(creadaPor);
      expect(comp.ventanaDeMantenimiento).toEqual(ventanaDeMantenimiento);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<IVentanaDeMantenimiento>();
      const ventanaDeMantenimiento = { id: 31419 };
      vitest.spyOn(ventanaDeMantenimientoFormService, 'getVentanaDeMantenimiento').mockReturnValue(ventanaDeMantenimiento);
      vitest.spyOn(ventanaDeMantenimientoService, 'update').mockReturnValue(saveSubject);
      vitest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ ventanaDeMantenimiento });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(ventanaDeMantenimiento);
      saveSubject.complete();

      // THEN
      expect(ventanaDeMantenimientoFormService.getVentanaDeMantenimiento).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(ventanaDeMantenimientoService.update).toHaveBeenCalledWith(expect.objectContaining(ventanaDeMantenimiento));
      expect(comp.isSaving()).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<IVentanaDeMantenimiento>();
      const ventanaDeMantenimiento = { id: 31419 };
      vitest.spyOn(ventanaDeMantenimientoFormService, 'getVentanaDeMantenimiento').mockReturnValue({ id: null });
      vitest.spyOn(ventanaDeMantenimientoService, 'create').mockReturnValue(saveSubject);
      vitest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ ventanaDeMantenimiento: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(ventanaDeMantenimiento);
      saveSubject.complete();

      // THEN
      expect(ventanaDeMantenimientoFormService.getVentanaDeMantenimiento).toHaveBeenCalled();
      expect(ventanaDeMantenimientoService.create).toHaveBeenCalled();
      expect(comp.isSaving()).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<IVentanaDeMantenimiento>();
      const ventanaDeMantenimiento = { id: 31419 };
      vitest.spyOn(ventanaDeMantenimientoService, 'update').mockReturnValue(saveSubject);
      vitest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ ventanaDeMantenimiento });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(ventanaDeMantenimientoService.update).toHaveBeenCalled();
      expect(comp.isSaving()).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareEquipo', () => {
      it('should forward to equipoService', () => {
        const entity = { id: 20906 };
        const entity2 = { id: 21995 };
        vitest.spyOn(equipoService, 'compareEquipo');
        comp.compareEquipo(entity, entity2);
        expect(equipoService.compareEquipo).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareUser', () => {
      it('should forward to userService', () => {
        const entity = { id: 3944 };
        const entity2 = { id: 6275 };
        vitest.spyOn(userService, 'compareUser');
        comp.compareUser(entity, entity2);
        expect(userService.compareUser).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
